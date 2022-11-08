package com.webank.truora.bcos3runner;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.enums.ProofTypeEnum;
import com.webank.truora.base.enums.ReqStatusEnum;
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.EventBaseException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.exception.PushEventLogException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.ChainGroupMapUtil;
import com.webank.truora.base.utils.HexUtil;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.database.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.codec.decode.RevertMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.webank.truora.base.enums.ReqStatusEnum.REQ_ALREADY_EXISTS;
import static com.webank.truora.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

/**
 *  合约功能抽象类
 *  在这一层不关心具体合约，只关心合约的共同能力，包括合约部署，合约事件处理等。
 *  公共对象包括数据库表， ABI，Event，合约地址，合约和事件编解码
 */

@Slf4j
public abstract class AbstractContractWorker {



    @Autowired
    protected DBReqHistoryRepository reqHistoryRepository;
    @Autowired
    protected DBReqHistoryService reqHistoryService;
    SourceTypeEnum sourceType;

    protected String platform = "fiscobcos3";

    @Autowired
    protected DBContractDeployRepository contractDeployRepository;
    protected String chainId = null;
    protected String groupId = null;
    protected Bcos3EventRegister eventRegister;
    protected ContractCodec contractCodec;
    protected String contractAddress = "";


    public void init(Bcos3EventRegister eventRegister) {
        chainId = eventRegister.getConfig().getChainId();
        groupId = eventRegister.getConfig().getGroupId();
        this.eventRegister = eventRegister;
        this.contractCodec = new ContractCodec(eventRegister.getBcos3client().getCryptoSuite(), false);
        initContract();
    }
    public Bcos3EventRegister getEventRegister(){
        return this.eventRegister;
    }
    public String getContractAddress(){
        return this.contractAddress;
    }

    public abstract Event getEvent();
    public abstract String getAbi();
    public abstract SourceTypeEnum getSourceType();

    public void initContract() {

        this.contractAddress = loadOrDeployContract(eventRegister);
        if (StringUtils.isBlank(contractAddress)) {
            String msg = String.format("{}:loadOrDeployContract error {}", this.getClass().getSimpleName());
            log.error(msg);
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR, msg);
        }
        log.info("LoadOrDeploy contract on group of chain:[{}:{}:{}],", eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), contractAddress);
        this.setCoreContractAddress(eventRegister, contractAddress);
    }

    public abstract void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress);

    /**
     * 返回当前合约类型
     *
     * @return
     */
    public abstract ContractEnum getContractType();


    public abstract DBReqHistory makeDBReqHistory(Bcos3EventRegister register, EventLog eventLog, Object eventResponseObject);

    /**
     * 检查合约地址是否有效
     *
     * @return
     */
    public abstract boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress);

    /**
     * 部署合约
     *
     * @return
     */
    protected abstract String deployContract(Bcos3EventRegister eventRegister);


    /**
     * 加载合约地址，如数据库里无记录，则部署一个，并入库
     *
     * @return
     */
    public String loadOrDeployContract(Bcos3EventRegister eventRegister) {
        String currentVersion = this.eventRegister.getConfig().getOracleCoreVersion();
        String chainId = eventRegister.getConfig().getChainId();
        String groupId = eventRegister.getConfig().getGroupId();
        ContractEnum contractType = getContractType();

        // load from db
        Optional<DBContractDeploy> deployOptional = this.contractDeployRepository
                .findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                        platform,
                        chainId, groupId, contractType.getType(), currentVersion);

        DBContractDeploy contractDeploy = null;
        if (deployOptional.isPresent()) {
            contractDeploy = deployOptional.get();
            String contractAddress = contractDeploy.getContractAddress();
            if (StringUtils.isNotBlank(contractAddress)) {
                // contract address valid
                if (this.isContractAddressValid(eventRegister, contractAddress)) {
                    // oracle core already deployed
                    ChainGroupMapUtil.put(chainId, groupId, contractAddress, contractDeploy.getVersion());
                    return contractAddress;
                }

                // delete this dirty contract address
                log.warn("Contract address:[{}] and version:[{}] exists, but not valid on chain:[{}] and group:[{}]. " +
                                "Maybe dirty data, try to re-deploy this contract:[{}].",
                        contractAddress, currentVersion, chainId, groupId, this.getContractType().getName());

                // delete this contract address
                log.warn("Delete contract address:[{}:{}]", contractDeploy.getChainId(), contractAddress);
                this.contractDeployRepository.deleteById(contractDeploy.getId());
            }
        }

        // not found in database , deploy contract
        log.info("Deploy contract:[{}] of version:[{}] on chain:[{}:{}]", this.getContractType(), currentVersion, chainId, groupId);
        String deployedContractAddress = this.deployContract(eventRegister);
        if (StringUtils.isNotBlank(deployedContractAddress)) {
            contractDeploy = DBContractDeploy.build(platform,
                    chainId, groupId, contractType, getContractType().getName(), currentVersion);
            contractDeploy.setContractAddress(deployedContractAddress);
            //保存合约地址到数据库
            contractDeployRepository.save(contractDeploy);
            setCoreContractAddress(eventRegister,deployedContractAddress);
            this.contractAddress = deployedContractAddress;
        }
        return deployedContractAddress;
    }

    /**
     * @param receipt
     *
     */
    public static void dealWithReceipt(TransactionReceipt receipt) {
        if(receipt.getStatus() ==0){
            log.info("dealWithReceipt status OK");
            return ;
        }

        if (receipt.getStatus() == 0x16) {
            Tuple2<Boolean, String> res0x16 = RevertMessageParser.tryResolveRevertMessage(receipt);
            log.error("dealWithReceipt 0x16 {}:{}", res0x16.getValue1(), res0x16.getValue2());
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(),
                    String.format("status:%d,code:%s,msg:%s", receipt.getStatus(), res0x16.getValue1(), res0x16.getValue2()));
        }
        if (receipt.getStatus() != 0) {

            log.error("transaction error, status:{} output:{}", receipt.getStatus(), receipt.getOutput());
            Tuple2<Boolean, String> res = RevertMessageParser.tryResolveRevertMessage(receipt);
            log.error("dealWithReceipt  {}:{}", res.getValue1(), res.getValue2());
            ;
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(),
                    String.format("status:%d,res:%s,msg:%s", receipt.getStatus(), res.getValue1(), res.getValue2()));
        }
    }

    /*按合约类型解析eventlog*/
    protected abstract Object parseEventLog(EventLog eventLog) throws Exception;

    /*从不同的事件内容里解出reqid,要求每个供监听的合约事件都包含一个reqID * ?是否可以用transactionHash代替*/
    protected abstract String getRequestId(Object responseObj) throws Exception;

    /*抽象类的派生实现类实际上要处理的合约入口*/
    abstract public String processLog(Bcos3EventContext eventContext) throws Exception;

    /*处理单个从事件监听得到的eventLog,解析成不同的事件具体的字段，
    通过数据库记录判断是否重复处理，如是新事件则封装成一个context，继续处理*/
    public void processSingleLog(EventLog eventLog) {
        String requestId = "";
        int reqStatus = 0;
        String result = "";
        String errorMsg = "";
        try {
            Object eventResposneObj = parseEventLog(eventLog);
            requestId = getRequestId(eventResposneObj);
            log.info("Process log event:[{}]", eventLog);
            //先确认是否已经接受并记录过同一个reqid
            if (this.reqHistoryRepository.findByReqId(requestId).isPresent()) {
                log.error("Request already exists:[{}].", requestId);
                throw new PushEventLogException(REQ_ALREADY_EXISTS, requestId);
            }
            //立刻保存当前req记录
            reqHistoryRepository.save(this.makeDBReqHistory(this.eventRegister, eventLog, eventResposneObj));
            log.info("Save request:[{}],result:[{}] to db.", requestId, result);
            Bcos3EventContext eventContext = new Bcos3EventContext();
            eventContext.setEventRegister(eventRegister);
            eventContext.setEventLog(eventLog);
            eventContext.setEventResponse(eventResposneObj);
            //调用具体的实现类去处理事件和解析过的object
            result = processLog(eventContext);
        }
        //关键处理流程就上面两行，以下都是异常处理加一个final,即处理单个事件异常时，只是打个错误日志，继续处理其他
        catch (OracleException oe) {
            // oracle exception
            reqStatus = oe.getCodeAndMsg().getCode();
            errorMsg = String.format("%s,%s", oe.getCodeAndMsg().getMessage(), ExceptionUtils.getRootCauseMessage(oe));
            log.error("OracleException: requestId:[{}], error:[{}]", requestId, errorMsg, oe);
        } catch (EventBaseException be) {
            // response error
            reqStatus = be.getStatus();
            errorMsg = be.getDetailMessage();
            log.error("BaseException: type:[{}],requestId:[{}], error:[{}]", be.getClass().getSimpleName(),
                    requestId, errorMsg, be);
        }

        catch (Exception e) {
            // other exception (db)
            reqStatus = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.getStatus();
            errorMsg = ReqStatusEnum.UNEXPECTED_EXCEPTION_ERROR.format(ExceptionUtils.getRootCauseMessage(e));
            log.error("Exception: requestId:[{}], error:[{}]", requestId, errorMsg, e);
        } finally {
            //最后完成，更新状态
            if (reqStatus != ReqStatusEnum.REQ_ALREADY_EXISTS.getStatus()) {
                // if requestId already exists, return directly.
                this.updateReqHistory(requestId, reqStatus, errorMsg, result);
            }
        }
    }

    /*异步入口*/
    @Async("eventAsync")
    public void processBatchLogs(String eventSubId, int status, List<EventLog> logs) {
        log.info("onReceiveLog id,{},status {},sizeof logs {}", eventSubId, status, logs.size());
        for (EventLog eventLog : logs) {
            long start = ThreadLocalHolder.setStartTime();
            log.info("ContractEventCallback onPushEventLog  address: {}, status: {}, logs: {}, start:{}",
                    eventLog.getAddress(), status, logs, start);
            processSingleLog(eventLog);
        }
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