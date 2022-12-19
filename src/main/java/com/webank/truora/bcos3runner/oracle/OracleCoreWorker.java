/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.webank.truora.bcos3runner.oracle;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.FulFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3EventContext;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.OracleCore;
import com.webank.truora.crawler.SourceCrawlerFactory;
import com.webank.truora.database.DBReqHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.ORACLE_CORE_CONTRACT_ADDRESS_ERROR;
import static com.webank.truora.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.truora.base.utils.JsonUtils.toJSONString;

/**
 *  * Event处理的实现类
 *  * 面向具体的合约，如OracleCore，VRFCore，如有更多的预言机合约，参照此类再做一个单独的实现
 * OracleService.
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "runner.fiscobcos3",havingValue = "true")
@Scope("prototype")
public class OracleCoreWorker extends AbstractContractWorker {


    @Autowired
    SourceCrawlerFactory sourceCrawlerFactory;


    public void init(Bcos3EventRegister eventRegister){

        super.init(eventRegister);

    }

    //必须实现的几个继承与抽象类的方法，返回具体合约的信息，如event定义，abi等


    @Override
    public Event getEvent() {
        return OracleCore.ORACLEREQUEST_EVENT;
    }
    @Override
    public  String getAbi(){
        return OracleCore.getABI();
    }
    /*目前定义的reqid是bytes32类型的，转成hex输出。*/
    @Override
    public String getRequestId(Object objEventlog) throws Exception  {
        OracleCore.OracleRequestEventResponse response =(OracleCore.OracleRequestEventResponse)objEventlog;
        return Hex.encodeHexString(response.requestId);
    }

    /*目前定义了URL类和VRF类的预言机合约，可以继续扩展
    * 这个sourcetype会用于存数据库历史表和检索，如某个链和群组的最新的一个相同类型的事件监听到哪个区块高度了
    * */
    @Override
    public SourceTypeEnum getSourceType()
    {
        return SourceTypeEnum.URL;
    }

    @Override
    /* 合约类型，用于合约的部署表检索 *？是否可以跟SourceType归并*/
    public ContractEnum getContractType() {
        return ContractEnum.ORACLE_CORE;
    }

    @Override
    /* 部署后，将新合约地址设置回配置数据区*/
    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        this.eventRegister.getConfig().setOracleCoreAddress(contractAddress);
    }

    @Override
    public boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress) {
        try {
            OracleCore oracleCore = OracleCore.load(contractAddress, eventRegister.getBcos3client(),
                   eventRegister.getKeyPair());
            if(oracleCore != null && !oracleCore.getContractAddress().isEmpty()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }

    /*顾名思义，部署具体的合约*/
    @Override
    protected String deployContract(Bcos3EventRegister eventRegister) {

        OracleCore oracleCore = null;
        try {
            oracleCore = OracleCore.deploy(eventRegister.getBcos3client(),
                    eventRegister.getKeyPair(),
                    eventRegister.getConfig().getChainId(),
                    eventRegister.getConfig().getGroupId());

        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return oracleCore.getContractAddress();

    }

    /*合约里实现的事件，如on_set(bytes32 reqid,string data,uint256 value),解析出来，符合合约java类的定义
    * 注意： eventLog是节点sdk定义的通用合约事件类，具体的合约事件在这里面是abi编码的，这个方法的功效就是做abi decode
    * 由于不同合约的事件对应不同的java类，所以返回的做成泛型，要做强行转换
    * */
    @Override
    public Object parseEventLog(EventLog eventLog) throws Exception
    {
        List<String> decodeResult = this.contractCodec.decodeEventToString(getAbi(),getEvent().getName(),eventLog);
        OracleCore.OracleRequestEventResponse response = fromDecodeString(decodeResult);
        return response;
    }


    public static OracleCore.OracleRequestEventResponse fromDecodeString(List<String>  decodeResult) throws UnsupportedEncodingException, DecoderException {
        OracleCore.OracleRequestEventResponse response = new OracleCore.OracleRequestEventResponse();
        response.coreAddress = (String)decodeResult.get(0);
        response.callbackAddr = (String)decodeResult.get(1);
        response.requestId = Hex.decodeHex((String) ((String) decodeResult.get(2)).substring("hex://".length()));
        response.url = (String)decodeResult.get(3);
        response.expiration = new BigInteger( decodeResult.get(4));
        response.timesAmount =new BigInteger(decodeResult.get(5));
        response.needProof = Boolean.valueOf(decodeResult.get(6));
        response.returnType = new BigInteger(decodeResult.get(7));
        return response;
    }

    /*实际上响应单个事件，去链下获取各种资源并回写链上的方法*/
    @Override
    public String processLog(Bcos3EventContext eventContext) throws Exception {

        EventLog eventLog = eventContext.getEventLog();

        log.info("---------------[{}] : processLog----------------------------",this.getClass().getName());
        log.info("---> eventLog: blocknumber: {},address: {},txhash: {}",
                eventLog.getBlockNumber(),eventLog.getAddress(),eventLog.getTransactionHash());
        OracleCore.OracleRequestEventResponse eventResponse = (OracleCore.OracleRequestEventResponse)eventContext.getEventResponse();
        //get data from url and update blockChain
        log.info("processLog : START-> url {} ", eventResponse.url);
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        String finalResult = "";
        if(eventResponse.url.isEmpty())
        {
            log.warn("processlog ,source URL is Empty,requestId {}, txhash: {}",requestId,eventLog.getTransactionHash());
            return "";
        }
        if(JsonUtils.isJson(eventResponse.url))
        {
            /*如果应用指示的source数据，是json定义的，则交给factory去处理
            * 一个隐含规则：json里必须包含name字段，指示factory实例化什么类去处理
            *  ***后续迭代重构成合约事件里直接明确的返回name
            * */
            String actuallyJsonStr = eventResponse.url; /*这个字段实际上包含的是json*/
            log.info("processlog :  json&hash {}",actuallyJsonStr);
            finalResult = sourceCrawlerFactory.handle(actuallyJsonStr);
            log.info("from json&hash finalResult is {}",finalResult);
        }else {
            /* 默认的最简单实现，当做url处理 （url有plain/json等前缀）
            * 名字是大小写敏感
            * */
            log.info("processlog : URLCrawler is {}",eventResponse.url);
            finalResult = sourceCrawlerFactory.handle("URL",eventResponse.url);
            log.info("from URL finalResult len is {}",finalResult.length());
        }
        log.info("processLog: url {} https result: {} ", eventResponse.url, toJSONString(finalResult));
        fulfill(eventRegister,
                eventResponse.callbackAddr, eventResponse, finalResult);
        /*返回一个结果字符串，供写库*/
        String result =  toJSONString(finalResult);
        return result;

    }

    /**
     * 将数据上链.
     */

    public void fulfill(Bcos3EventRegister eventRegister, String contractAddress, OracleCore.OracleRequestEventResponse eventResponse, Object result) throws Exception {
        //send transaction

        String requestId = Hex.encodeHexString(eventResponse.requestId);
        log.info("Start to write data to chain, event:reqid:{},callbackAddr:{} ", requestId,eventResponse.callbackAddr);

        try {
            Client client = eventRegister.getBcos3client();

            String oracleCoreAddress = eventResponse.coreAddress;
            if (StringUtils.isBlank(oracleCoreAddress)) {
                throw new FulFillException(ORACLE_CORE_CONTRACT_ADDRESS_ERROR);
            }

            OracleCore oracleCore = OracleCore.load(oracleCoreAddress, client, eventRegister.getKeyPair());
            TransactionReceipt receipt = null;
            /*convert result to bytes according the return type*/
            byte[] returnbytes = ReturnTypeEnum.convert2Bytes(
                    ReturnTypeEnum.get(eventResponse.returnType),result,eventResponse.timesAmount);
            byte[] requestIdBytes = Numeric.hexStringToByteArray(requestId);
            Tuple2<Boolean, String> checkResult =  oracleCore.checkRequestId(requestIdBytes);
            if(!checkResult.getValue1()){
                //检测request id不为true，不要发起fulfill交易，省一个错误的交易进块。
                log.error("Before fulfill checkRequestIdError. chainId: {}  groupId: {} ," +
                                "contractAddress {},requestId: {},message:{},url:[{}],data [{}]",
                        eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(),
                        requestId,
                        contractAddress,checkResult.getValue2(),
                        eventResponse.url,
                        result

                );
                return ;
            }

            receipt = oracleCore.fulfillRequest(requestIdBytes,
                    eventResponse.callbackAddr, eventResponse.expiration, returnbytes, new byte[0]);


            log.info("Write data to chain status: [{}], output:[{}]", receipt.getStatus(),receipt.getOutput());

            if(receipt.getStatus() == 0)
            {
                Tuple1<Boolean> fulfilresult =  oracleCore.getFulfillRequestOutput(receipt);
                log.info("Fulfile result is {}",fulfilresult.getValue1());
                log.info("upBlockChain success chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}",
                        eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(),
                        contractAddress, result, requestId);
            }else {
                dealWithReceipt(receipt);
            }
        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}",
                    eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), contractAddress, result, requestId, oe);
            throw new FulFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
        }
    }






    /*
    将流程性数据转成数据库表对象存盘
    * */
    @Override
    public DBReqHistory makeDBReqHistory(Bcos3EventRegister register, EventLog eventLog, Object eventResponseObject) {
        OracleCore.OracleRequestEventResponse eventResponse = (OracleCore.OracleRequestEventResponse)eventResponseObject;
        DBReqHistory reqHistory = new DBReqHistory();
        reqHistory.setPlatform(this.eventRegister.getPlatform());
        reqHistory.setChainId(chainId);
        reqHistory.setGroupId(groupId);
        reqHistory.setSourceType(this.getSourceType().getId());
        reqHistory.setBlockNumber(eventLog.getBlockNumber());
        reqHistory.setReqId(Hex.encodeHexString(eventResponse.requestId));
        reqHistory.setUserContract(eventResponse.callbackAddr);
        reqHistory.setOracleVersion(register.getConfig().getOracleCoreVersion());
        reqHistory.setSourceType(SourceTypeEnum.URL.getId());
        reqHistory.setReqQuery(eventResponse.url);
        reqHistory.setTimesAmount(eventResponse.timesAmount.toString(10));
        return reqHistory;
    }


}
