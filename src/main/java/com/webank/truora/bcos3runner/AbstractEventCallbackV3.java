package com.webank.truora.bcos3runner;

import com.webank.truora.base.enums.ProofTypeEnum;
import com.webank.truora.base.enums.ReqStatusEnum;
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.HexUtil;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.database.DBReqHistoryRepository;
import com.webank.truora.database.DBReqHistoryService;
import com.webank.truora.base.exception.EventBaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.codec.EventEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.eventsub.EventSubParams;
import org.fisco.bcos.sdk.v3.eventsub.EventSubscribe;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.webank.truora.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

/**
 *
 */

@Slf4j
public abstract class AbstractEventCallbackV3 implements EventSubCallback {


    @Autowired protected DBReqHistoryRepository reqHistoryRepository;
    @Autowired protected DBReqHistoryService reqHistoryService;
    @Autowired protected Bcos3SdkFactory sdkFactory;
    // from constructor
    protected SourceTypeEnum sourceType;

    // from contract
    protected String abi;
    protected Event event;
    protected EventEncoder eventEncoder;

    // from config
    protected String chainId;
    protected String groupId;
    protected int blockNumber;

    protected Bcos3EventRegister eventRegister;

    protected EventSubscribe subcriber;
    protected ContractCodec contractCodec;
    /**
     * @param abi
     * @param event
     */
    public AbstractEventCallbackV3(String abi, Event event, SourceTypeEnum sourceType, Bcos3EventRegister eventRegister) {
        super();
        this.abi = abi;
        this.event = event;
        this.contractCodec =  new ContractCodec(eventRegister.getBcos3client().getCryptoSuite(),false);
        this.chainId = eventRegister.getConfig().getChainId();
        this.groupId = eventRegister.getConfig().getGroupId();
        this.sourceType = sourceType;
        this.eventRegister = eventRegister;
        this.eventEncoder = new EventEncoder(eventRegister.getBcos3client().getCryptoSuite());

    }

    /**
     * 部署合约
     *
     */
    public abstract String loadOrDeployContract(Bcos3EventRegister eventRegister);

    /**
     * 解析日志
     */
    public abstract String processLog(EventLog eventLog) throws Exception;
    public abstract String getRequestId(EventLog eventLog) throws DecoderException, UnsupportedEncodingException, ContractCodecException;
    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress);

    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract String getContractAddress(Bcos3EventRegister eventRegister);


    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public DBReqHistory getLatestRecord(String chainId, String groupId) {
        if (sourceType == null) {
            throw new OracleException(ConstantCode.UNSET_SOURCE_TYPE_ERROR);
        }
        return reqHistoryService.getLatestRecord(chainId, groupId, sourceType.getId());
    }

    /**
     * 事件回调主入口，一次可能有一到多个事件回调
     * 根据Log对象中的 requestId 进行去重，已经处理过的requestId，就不要再处理了
     */
    @Override
    public void onReceiveLog(String eventSubId,int status, List<EventLog> logs)
    {


        for (EventLog eventLog : logs) {
            long start = ThreadLocalHolder.setStartTime();
            log.info("ContractEventCallback onPushEventLog  address: {}, status: {}, logs: {}, start:{}",
                    eventLog.getAddress() ,status, logs, start);

            // request and result
            String result = "";
            int reqStatus = ReqStatusEnum.SUCCESS.getStatus();
            String error = "";
            String requestId = "";
            try {
                requestId = getRequestId(eventLog);
                result = this.processLog(eventLog);
            }
            //关键处理流程就上面两行，以下都是异常处理加一个final,即处理单个事件异常时，只是打个错误日志，继续处理其他
            catch (ContractCodecException e) {
                log.error("onReceiveLog: ContractCodecException error {}",e);
            }
            catch (OracleException oe) {
                // oracle exception
                reqStatus = oe.getCodeAndMsg().getCode();
                error = String.format("%s,%s", oe.getCodeAndMsg().getMessage(), ExceptionUtils.getRootCauseMessage(oe));
                log.error("onReceiveLog: OracleException: requestId:[{}], error:[{}]", requestId, error, oe);
            } catch (EventBaseException be) {
                // response error
                reqStatus = be.getStatus();
                error = be.getDetailMessage();
                log.error("onReceiveLog: BaseException: type:[{}],requestId:[{}], error:[{}]", be.getClass().getSimpleName(), requestId, error, be);
            } catch (Exception e) {
                // other exception (db)
                reqStatus = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.getStatus();
                error = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.format(ExceptionUtils.getRootCauseMessage(e));
                log.error("onReceiveLog: Exception: requestId:[{}], error:[{}]", "", error, e);
            } finally {
                // Avoid updating the req history when this request is a duplicated one !!!
                if (reqStatus != ReqStatusEnum.REQ_ALREADY_EXISTS.getStatus()) {
                    // if requestId already exists, return directly.
                    this.updateReqHistory(requestId, reqStatus, error, result);
                }
            }
        }
    }




    /**
     * @param eventRegister
     */
    public void init(Bcos3EventRegister eventRegister) throws OracleException {
        String contractAddress = this.loadOrDeployContract(eventRegister);
        if (StringUtils.isBlank(contractAddress)) {
            String msg = String.format("{}:loadOrDeployContract error {}",this.getClass().getSimpleName());
            log.error(msg);
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR, msg);
        }
        log.info("LoadOrDeploy contract on group of chain:[{}:{}:{}],", eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), contractAddress);
        this.setCoreContractAddress(eventRegister, contractAddress);

        // init EventLogUserParams for register
        DBReqHistory reqHistory = getLatestRecord(chainId, groupId);
        String from = "latest";
        if (reqHistory != null) {
            from = reqHistory.getBlockNumber().add(new BigInteger("1")).toString();
        }
        log.info("*** chainId: {} ,groupId: {}, blockNumber: {}", chainId, groupId, from);

        /*实现注册合约事件*/
        try {
            subcriber = EventSubscribe.build(eventRegister.getBcos3client());
            EventSubParams eventSubParams = initEventSubParams("","",contractAddress);
            subcriber.subscribeEvent(eventSubParams,this);
            log.info("[{}]: subscribe_Event ,address:{} , topics: {},from :{},to: {}",event.getName(),eventSubParams.getAddresses(),
                    eventSubParams.getTopics(),
                    eventSubParams.getFromBlock(),eventSubParams.getToBlock());
        }catch(Exception e){
            log.error("Subcribe event error ! address "+contractAddress);

        }
    }

    public BigInteger parseBlockStr(String blockStr){
        try{
             BigInteger block = new BigInteger(blockStr);
             return block;
        }catch (Exception e){
            return BigInteger.valueOf(-1);
        }
    }
    /**
     * 设置监听
     *
     * @return
     */
    public EventSubParams initEventSubParams(String fromBlock, String toBlock, String contractAddress) {
        EventSubParams params = new EventSubParams();
        BigInteger ifromBlock =parseBlockStr(fromBlock);
        BigInteger iToBlock = parseBlockStr(toBlock);
        params.setFromBlock(ifromBlock);
        params.setToBlock(iToBlock);
        params.addAddress(contractAddress);
        String topic  = eventEncoder.encode(this.event);
        params.addTopic(0,topic);
        return params;
    }

    /**
     * 处理完成后，更新 ReqHistory 请求状态和结果。
     *
     * @param requestId
     * @param reqStatus
     * @param error
     * @param result
     */
    private void updateReqHistory(String requestId, int reqStatus, String error, String result) {
        try {
            // check req history exists
            Optional<DBReqHistory> reqHistoryOptional = this.reqHistoryRepository.findByReqId(requestId);
            if (reqHistoryOptional.isPresent()) {
                // update req history record
                DBReqHistory reqHistory = reqHistoryOptional.get();
                reqHistory.setReqStatus(reqStatus);
                if (StringUtils.isNotBlank(error)) {
                    reqHistory.setError(StringUtils.length(error) > MAX_ERROR_LENGTH ?
                            StringUtils.substring(error, 0, MAX_ERROR_LENGTH) : error);
                }
                long startTime = ThreadLocalHolder.getStartTime();
                startTime = startTime > 0 ? startTime
                        // get start from db when startTime le 0
                        : reqHistory.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                reqHistory.setProcessTime(System.currentTimeMillis() - startTime);
                reqHistory.setResult(result);
                reqHistory.setProof(result);
                reqHistory.setProofType(ProofTypeEnum.DEFAULT.getId());
                // VRF request
                if (SourceTypeEnum.isVrf(reqHistory.getSourceType())) {
                    reqHistory.setActualSeed(HexUtil.add0xPrefix(ThreadLocalHolder.getActualSeed()));
                    reqHistory.setResult(HexUtil.add0xPrefix(ThreadLocalHolder.getRandomness()));
                    reqHistory.setProof(HexUtil.add0xPrefix(result));
                    reqHistory.setProofType(ProofTypeEnum.VRF.getId());
                    // add 0x for input seed
                    reqHistory.setInputSeed(HexUtil.add0xPrefix(reqHistory.getInputSeed()));
                }

                // save
                this.reqHistoryRepository.save(reqHistory);
            } else {
                log.error("No request history:[{}] inserted!!!", requestId);
            }
        } catch (Exception e) {
            log.error("Update req history error:[{}]", requestId, e);
        } finally {
            ThreadLocalHolder.removeStartTime();
        }
    }
}