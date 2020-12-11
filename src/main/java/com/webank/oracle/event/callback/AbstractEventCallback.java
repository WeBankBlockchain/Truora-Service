package com.webank.oracle.event.callback;

import static com.webank.oracle.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.webank.oracle.base.enums.ProofTypeEnum;
import com.webank.oracle.base.enums.ReqStatusEnum;
import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.base.utils.CommonUtils;
import com.webank.oracle.base.utils.ThreadLocalHolder;
import com.webank.oracle.event.exception.EventBaseException;
import com.webank.oracle.event.vo.BaseLogResult;
import com.webank.oracle.history.ReqHistory;
import com.webank.oracle.history.ReqHistoryRepository;
import com.webank.oracle.keystore.KeyStoreService;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public abstract class AbstractEventCallback extends EventLogPushWithDecodeCallback {

    @Autowired protected Map<Integer, Map<Integer, Service>> serviceMapWithChainId;
    @Autowired protected ReqHistoryRepository reqHistoryRepository;
    @Autowired protected KeyStoreService keyStoreService;

    // from contract
    protected String abi;
    protected Event event;

    // from config
    protected int chainId;
    protected int groupId;

    /**
     * @param abi
     * @param event
     * @param chainId
     * @param groupId
     */
    public AbstractEventCallback(String abi, Event event, int chainId, int groupId) {
        super();
        this.abi = abi;
        this.event = event;
        this.chainId = chainId;
        this.groupId = groupId;
    }

    /**
     * 部署合约
     *
     * @param chainId
     * @param group
     */
    public abstract String loadOrDeployContract(int chainId, int group);

    /**
     * 解析日志
     */
    public abstract String processLog(int status, LogResult logResult) throws Exception;

    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract void setContractAddress(EventRegister eventRegister, String contractAddress);

    /**
     * @param eventRegister
     * @return
     * @throws Exception
     */
    public abstract String getContractAddress(EventRegister eventRegister);

    /**
     * 根据Log对象中的 requestId 进行去重
     *
     * @param status
     * @param logs
     */
    @Override
    public void onPushEventLog(int status, List<LogResult> logs) {
        long start = ThreadLocalHolder.setStartTime();
        log.info("ContractEventCallback onPushEventLog  params: {}, status: {}, logs: {}, start:{}",
                getFilter().getParams(), status, logs, start);

        //get(0)
        for (LogResult logResult : logs) {
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
                this.updateReqHistory(requestId,reqStatus,error,result);
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
    public void init(EventRegister eventRegister) {
        // set decoder
        TransactionDecoder decoder = new TransactionDecoder(abi);
        setDecoder(decoder);

        String contractAddress = this.loadOrDeployContract(eventRegister.getChainId(), eventRegister.getGroup());
        if (StringUtils.isBlank(contractAddress)) {
            log.error("Deploy contract error");
            return;
        }
        // todo  add service name
        log.info("Deploy contract on group of chain:[{}:{}:{}],", eventRegister.getChainId(), eventRegister.getGroup(), contractAddress);
        this.setContractAddress(eventRegister, contractAddress);

        // init EventLogUserParams for register
        EventLogUserParams params = this.initSingleEventLogUserParams(eventRegister.getFromBlock(),
                eventRegister.getToBlock(), getContractAddress(eventRegister));
        log.info("RegisterContractEvent chainId: {} groupId:{},abi:{},params:{}", eventRegister.getChainId(), eventRegister.getGroup(), abi, params);
        org.fisco.bcos.channel.client.Service service = serviceMapWithChainId.get(eventRegister.getChainId()).get(eventRegister.getGroup());
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
     *  处理完成后，更新 ReqHistory 请求状态和结果。
     *
     * @param requestId
     * @param reqStatus
     * @param error
     * @param result
     */
    private void updateReqHistory(String requestId, int reqStatus, String error, String result) {
        try {
            // check req history exists
            Optional<ReqHistory> reqHistoryOptional = this.reqHistoryRepository.findByReqId(requestId);
            if (reqHistoryOptional.isPresent()) {
                // update req history record
                ReqHistory reqHistory = reqHistoryOptional.get();
                reqHistory.setReqStatus(reqStatus);
                if (StringUtils.isNotBlank(error)) {
                    reqHistory.setError(StringUtils.length(error) > MAX_ERROR_LENGTH ?
                            StringUtils.substring(error, 0, MAX_ERROR_LENGTH) : error);
                }
                reqHistory.setProcessTime(System.currentTimeMillis() - ThreadLocalHolder.getStartTime());
                reqHistory.setResult(result);
                reqHistory.setProof(result);
                reqHistory.setProofType(ProofTypeEnum.SIGN.getId());

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