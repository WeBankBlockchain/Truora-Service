package com.webank.truora.bcos3runner;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.database.DBReqHistoryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.codec.EventEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.eventsub.EventSubParams;
import org.fisco.bcos.sdk.v3.eventsub.EventSubscribe;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 *事件注册和响应类
 * 和链节点直接对接，提交注册请求，并实现响应入口（实现EventSubCallback接口）
 * 此类保持尽量简单，只包含最小的方法和成员变量，因继承接口，不能修改其接口定义，如改为Async都是不行的，会破坏反射机制。
 * 响应接口中，不应做耗时和同步的操作，所以收到event后，立刻交给AbstractContractWorkerV3去处理，worker是可以异步的。
 *
 */

@Slf4j
@Data
@Service
@Scope("prototype")
public class Bcos3EventCallback implements EventSubCallback {


    @Autowired protected DBReqHistoryService reqHistoryService;

    // from config
    protected String chainId;
    protected String groupId;

    protected Bcos3EventRegister eventRegister;
    protected String contractAddress;

    AbstractContractWorker eventWorker = null;

    protected EventSubscribe subcriber;
    protected ContractCodec contractCodec;

    public Bcos3EventCallback( ) {
        super();


    }


    public void init(AbstractContractWorker eventWorker) throws OracleException {
        this.eventWorker = eventWorker;
        this.eventRegister = eventWorker.getEventRegister();
        this.chainId = eventRegister.getConfig().getChainId();
        this.groupId = eventRegister.getConfig().getGroupId();
        registerEventListener();
    }


    /**
     * 事件回调主入口，一次可能有一到多个事件回调
     * 根据Log对象中的 requestId 进行去重，已经处理过的requestId，就不要再处理了
     */
    public void registerEventListener()
    {
        SourceTypeEnum sourceType = eventWorker.getSourceType();
        DBReqHistory reqHistory = getLatestRecord(chainId, groupId,sourceType);
        String from = "latest";
        if (reqHistory != null) {
            from = reqHistory.getBlockNumber().add(new BigInteger("1")).toString();
        }
        log.info("*** chainId: {} ,groupId: {},from blockNumber: {}", chainId, groupId, from);

        /*实现注册合约事件*/
        try {
            Event event = eventWorker.getEvent();
            contractAddress = eventWorker.getContractAddress();
            subcriber = EventSubscribe.build(eventRegister.getBcos3client());
            EventSubParams eventSubParams = makeEventSubParams(event,from,"latest",contractAddress);
            subcriber.subscribeEvent(eventSubParams,this);
            log.info("[{}]: subscribe_Event ,address:{} , topics: {},from :{},to: {}", event.getName(),eventSubParams.getAddresses(),
                    eventSubParams.getTopics(),
                    eventSubParams.getFromBlock(),eventSubParams.getToBlock());
        }catch(Exception e){
            log.error("Subcribe event error ! address "+contractAddress,e);

        }

    }


    public DBReqHistory getLatestRecord(String chainId, String groupId,SourceTypeEnum sourceType) {
        if (sourceType == null) {
            throw new OracleException(ConstantCode.UNSET_SOURCE_TYPE_ERROR);
        }
        return reqHistoryService.getLatestRecord(chainId, groupId, sourceType.getId());
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
     * 设置监听参数， bcos3sdk的标准做法，目前只监听事件，不监听事件里的indexed字段
     *     * @return
     */
    public EventSubParams makeEventSubParams(Event event, String fromBlock, String toBlock, String contractAddress) {
        EventEncoder eventEncoder = new EventEncoder(eventRegister.getBcos3client().getCryptoSuite());
        EventSubParams params = new EventSubParams();
        BigInteger ifromBlock =parseBlockStr(fromBlock);
        BigInteger iToBlock = parseBlockStr(toBlock);
        params.setFromBlock(ifromBlock);
        params.setToBlock(iToBlock);
        params.addAddress(contractAddress);
        String topic  = eventEncoder.encode(event);
        params.addTopic(0,topic);
        return params;
    }

    /*接收并处理回调事件的总入口，收到后立刻丢给worker去处理，让worker去异步*/
    @Override
    public void onReceiveLog(String eventSubId,int status, List<EventLog> logs)
    {
        if (logs==null||logs.size()==0){
            log.info("ReceivLog but Logs is Empty.eventSubId {}, status:{}",eventSubId,status);
            return;
        }
        eventWorker.processBatchLogs(eventSubId,status,logs);
    }
}