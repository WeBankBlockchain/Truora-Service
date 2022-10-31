/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.truora.bcos3runner.oracle;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.bcos3runner.AbstractEventCallbackV3;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.OracleCore;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.base.exception.PushEventLogException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.REQ_ALREADY_EXISTS;


/**
 * 从callback中获取事件推送过来的请求地址，再请求该地址获取数据上链。
 */
@Component
@Scope("prototype")
@Slf4j
public class OracleCoreEventCallbackV3 extends AbstractEventCallbackV3 {

    @Autowired public OracleCoreClientV3 oracleCoreClient;


    /**
     * @param chainId
     * @param groupId
     */
    public OracleCoreEventCallbackV3(String chainId, String groupId, Bcos3EventRegister eventRegister) {
         super(OracleCore.ABI, OracleCore.ORACLEREQUEST_EVENT,  SourceTypeEnum.URL, eventRegister);
    }

    @Override
    public String loadOrDeployContract(Bcos3EventRegister eventRegister) {
        return oracleCoreClient.loadOrDeployContract(eventRegister, eventRegister.getConfig().getOracleCoreVersion());
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

    @Override
    public String getRequestId(EventLog eventLog) throws ContractCodecException, DecoderException, UnsupportedEncodingException {
        List<String> decodeResult = this.contractCodec.decodeEventToString(this.abi,this.event.getName(),eventLog);
        OracleCore.OracleRequestEventResponse response = fromDecodeString(decodeResult);
        return Hex.encodeHexString(response.requestId);
    }
    @Override
    public String processLog(EventLog eventLog) throws Exception {

        log.info("---------------[{}] : processLog----------------------------",this.getClass().getName());
        log.info("---> eventLog: blocknumber: {},address: {},txhash: {}",
                eventLog.getBlockNumber(),eventLog.getAddress(),eventLog.getTransactionHash());
        List<String> decodeResult = this.contractCodec.decodeEventToString(this.abi,this.event.getName(),eventLog);
        OracleCore.OracleRequestEventResponse eventResponse = fromDecodeString(decodeResult);
        String requestIdStr = Hex.encodeHexString(eventResponse.requestId);

        log.info("Process log event:[{}]", eventLog);
        if (this.reqHistoryRepository.findByReqId(requestIdStr).isPresent()) {
            log.error("Request already exists:[{}:{}:{}].", eventResponse.callbackAddr,
                    requestIdStr, eventResponse.url);
            throw new PushEventLogException(REQ_ALREADY_EXISTS, requestIdStr);
        }

        // TODO. get version
        this.reqHistoryRepository.save(this.makeDBReqHistory(this.eventRegister,eventLog,eventResponse));
        log.info("Save request:[{}:{}:{}:{}] to db.",requestIdStr, eventResponse.callbackAddr,
                requestIdStr, eventResponse.url);

        //get data from url and update blockChain
        String result =  oracleCoreClient.handleOracleRequestAndFulfill(eventRegister, eventResponse);
        return result;

    }

    @Override
    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        eventRegister.getConfig().setOracleCoreAddress(contractAddress);
    }

    @Override
    public String getContractAddress(Bcos3EventRegister eventRegister) {
        return eventRegister.getConfig().getOracleCoreAddress();
    }


    /*
    将流程性数据转成数据库表对象存盘
    * */
    public DBReqHistory makeDBReqHistory(Bcos3EventRegister register, EventLog eventLog, OracleCore.OracleRequestEventResponse eventResponse) {
        DBReqHistory reqHistory = new DBReqHistory();
        reqHistory.setPlatform(this.eventRegister.getPlatform());
        reqHistory.setChainId(chainId);
        reqHistory.setGroupId(groupId);
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
