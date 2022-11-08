package com.webank.truora.bcos2runner;

import com.webank.truora.base.enums.ProofTypeEnum;
import com.webank.truora.base.enums.ReqStatusEnum;
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.HexUtil;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.database.DBReqHistoryRepository;
import com.webank.truora.database.DBReqHistoryService;
import com.webank.truora.base.exception.EventBaseException;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import com.webank.truora.keystore.KeyStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.channel.event.filter.EventLogUserParams;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigInteger;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.webank.truora.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

/**
 *
 */

@Slf4j
public abstract class AbstractEventCallback extends EventLogPushWithDecodeCallback {

    @Autowired
    @Qualifier("serviceMapWithChainId")
    protected Map<String, Map<String, Service>> serviceMapWithChainId;
    @Autowired protected DBReqHistoryRepository reqHistoryRepository;
    @Autowired protected DBReqHistoryService reqHistoryService;
    @Autowired protected KeyStoreService keyStoreService;

    // from constructor
    protected SourceTypeEnum sourceType;

    // from contract
    protected String abi;
    protected Event event;

    // from config
    protected String chainId;
    protected String groupId;
    protected int blockNumber;

    protected EventRegisterConfig eventRegister;

    /**
     * @param abi
     * @param event
     * @param chainId
     * @param groupId
     */
    public AbstractEventCallback(String abi, Event event, String chainId, String groupId, SourceTypeEnum sourceType, EventRegisterConfig eventRegister) {
        super();
        this.abi = abi;
        this.event = event;
        this.chainId = chainId;
        this.groupId = groupId;
        this.sourceType = sourceType;
        this.eventRegister = eventRegister;
    }

    /**
     * 部署合约
     *
     * @param chainId
     * @param group
     */
    public abstract String loadOrDeployContract(String chainId, String group);

    /**
     * 解析日志
     */
    public abstract String processLog(int status, LogResult logResult) throws Exception;

    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract void setContractAddress(EventRegisterConfig eventRegister, String contractAddress);

    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract String getContractAddress(EventRegisterConfig eventRegister);


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
     * 根据Log对象中的 requestId 进行去重
     *
     * @param status
     * @param logs
     */
    @Override
    public void onPushEventLog(int status, List<LogResult> logs) {
        for (LogResult logResult : logs) {

            long start = ThreadLocalHolder.setStartTime();
            log.info("ContractEventCallback onPushEventLog  params: {}, status: {}, logs: {}, start:{}",
                    getFilter().getParams(), status, logs, start);

            // request and result
            String requestId = "";
            String result = "";

            // re
            int reqStatus = ReqStatusEnum.SUCCESS.getStatus();
            String error = "";

            try {
                requestId = CommonUtils.byte32LogToString(logResult.getLogParams(), BaseLogResult.LOG_REQUEST_ID);

                // call implementation
                result = this.processLog(status, logResult);
            } catch (OracleException oe) {
                // oracle exception
                reqStatus = oe.getCodeAndMsg().getCode();
                error = String.format("%s,%s", oe.getCodeAndMsg().getMessage(), ExceptionUtils.getRootCauseMessage(oe));
                log.error("OracleException: requestId:[{}], error:[{}]", requestId, error, oe);
            } catch (EventBaseException be) {
                // response error
                reqStatus = be.getStatus();
                error = be.getDetailMessage();
                log.error("BaseException: type:[{}],requestId:[{}], error:[{}]", be.getClass().getSimpleName(), requestId, error, be);
            } catch (Exception e) {
                // other exception (db)
                reqStatus = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.getStatus();
                error = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.format(ExceptionUtils.getRootCauseMessage(e));
                log.error("Exception: requestId:[{}], error:[{}]", requestId, error, e);
            } finally {
                // Avoid updating the req history when this request is a duplicated one !!!
                if (reqStatus != ReqStatusEnum.REQ_ALREADY_EXISTS.getStatus()) {
                    // if requestId already exists, return directly.
                    this.updateReqHistory(requestId, reqStatus, error, result);
                }
            }
        }
    }


    @Override
    public LogResult transferLogToLogResult(Log logInfo) {
        try {
            LogResult logResult = getDecoder().decodeEventLogReturnObject(logInfo);
            return logResult;
        } catch (BaseException e) {
            log.error("Event log decode failed, log: {}", logInfo, e);
            return null;
        }
    }

    /**
     * @param eventRegister
     */
    public void init(EventRegisterConfig eventRegister) {
        // set decoder
        TransactionDecoder decoder = new TransactionDecoder(abi);
        setDecoder(decoder);

        String contractAddress = this.loadOrDeployContract(eventRegister.getChainId(), eventRegister.getGroupId());
        if (StringUtils.isBlank(contractAddress)) {
            log.error("Deploy contract error");
            return;
        }
        // todo  add service name
        log.info("Deploy contract on group of chain:[{}:{}:{}],", eventRegister.getChainId(), eventRegister.getGroupId(), contractAddress);
        this.setContractAddress(eventRegister, contractAddress);

        // init EventLogUserParams for register

        DBReqHistory reqHistory = getLatestRecord(chainId, groupId);
        String from = "latest";
        if (reqHistory != null) {
            from = reqHistory.getBlockNumber().add(new BigInteger("1")).toString();
        }
        log.info("*** chainId: {} ,groupId: {}, blockNumber: {}", chainId, groupId, from);

        EventLogUserParams params = this.initSingleEventLogUserParams(from, eventRegister.getToBlock(), getContractAddress(eventRegister));
        log.info("RegisterContractEvent chainId: {} groupId:{},abi:{},params:{}", eventRegister.getChainId(), eventRegister.getGroupId(), abi.length(), params);
        org.fisco.bcos.channel.client.Service service = serviceMapWithChainId.get(eventRegister.getChainId()).get(eventRegister.getGroupId());
        service.registerEventLogFilter(params, this);
    }

    /**
     * 设置监听
     *
     * @return
     */
    public EventLogUserParams initSingleEventLogUserParams(String fromBlock, String toBlock, String contractAddress) {
        EventLogUserParams params = new EventLogUserParams();
        params.setFromBlock(fromBlock);
        params.setToBlock(toBlock);

        // addresses，设置为Java合约对象的地址
        List<String> addresses = new ArrayList<>();
        addresses.add(contractAddress);
        params.setAddresses(addresses);
        List<Object> topics = new ArrayList<>();
        topics.add(EventEncoder.encode(event));
        params.setTopics(topics);

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