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

package com.webank.truora.bcos3runner.vrf;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.bcos3runner.AbstractEventCallbackV3;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.simplevrf.VRFCore;
import com.webank.truora.database.DBReqHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * 从callback中获取事件推送过来的请求地址，再请求该地址获取数据上链。
 */
@Component
@Scope("prototype")
@Slf4j
public class VRFContractEventCallbackV3 extends AbstractEventCallbackV3 {

    @Autowired private VRFClientV3 vrfService;

    /**
     * @param chainId
     * @param groupId
     */
    public VRFContractEventCallbackV3(String chainId, String groupId, Bcos3EventRegister eventRegister) {
        super(VRFCore.ABI, VRFCore.RANDOMNESSREQUEST_EVENT,  SourceTypeEnum.VRF, eventRegister);
    }

    @Override
    public String getRequestId(EventLog eventLog) throws ContractCodecException, DecoderException {
        List<String> decodeResult = contractCodec.decodeEventToString(abi,VRFCore.RANDOMNESSREQUEST_EVENT.getName(),eventLog);
        VRFCore.RandomnessRequestEventResponse response = fromDecordResult(decodeResult);
        return Hex.encodeHexString(response.requestId);
    }
    public static VRFCore.RandomnessRequestEventResponse fromDecordResult(List<String> decodeResult) throws DecoderException {
        VRFCore.RandomnessRequestEventResponse response = new VRFCore.RandomnessRequestEventResponse();
        response.coreAddress = decodeResult.get(0);
        response.keyHash = Hex.decodeHex(decodeResult.get(1).substring("hex://".length()));
        response.seed = new BigInteger(decodeResult.get(2));
        response.blockNumber = new BigInteger(decodeResult.get(3));
        response.sender = decodeResult.get(4);
        response.requestId = Hex.decodeHex(decodeResult.get(5).substring("hex://".length()));
        response.seedAndBlockNum =Hex.decodeHex(decodeResult.get(6).substring("hex://".length()));
        response.consumerSeed = new BigInteger(decodeResult.get(7));
        return response;
    }

    @Override
    public String loadOrDeployContract(Bcos3EventRegister eventRegister) {
        return vrfService.loadOrDeployContract(eventRegister,eventRegister.getConfig().getVrfCoreVersion());
    }

    @Override
    public String processLog(EventLog logEvent) throws Exception {
        /*
        VRFLogResult vrfLogResult = new VRFLogResult(logResult);

        log.info("Process log event:[{}]", vrfLogResult);

        if (this.reqHistoryRepository.findByReqId(vrfLogResult.getRequestId()).isPresent()) {
            log.error("Request already exists:[{}:{}:{}].",
                    vrfLogResult.getRequestId(), vrfLogResult.getSender(), vrfLogResult.getSeedAndBlockNum());
            throw new PushEventLogException(REQ_ALREADY_EXISTS, vrfLogResult.getRequestId());
        }

        this.reqHistoryRepository.save(vrfLogResult.convert(chainId, groupId, logResult.getLog().getBlockNumber(),
                ChainGroupMapUtil.getVersionWithDefault(chainId, groupId, vrfLogResult.getCoreContractAddress(),
                        eventRegister.getConfig().getVrfCoreVersion()),
                SourceTypeEnum.VRF));

        // save request to db
        log.info("Save request:[{}:{}:{}] to db.", vrfLogResult.getRequestId(), vrfLogResult.getSender(), vrfLogResult.getSeedAndBlockNum());

        //get data from url and update blockChain
        return vrfService.getResultAndUpToChain(chainId, groupId, vrfLogResult);*/
        return "";
    }



    @Override
    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        eventRegister.getConfig().setVrfCoreAddress(contractAddress);
    }

    @Override
    public String getContractAddress(Bcos3EventRegister eventRegister) {

        return eventRegister.getConfig().getVrfCoreAddress();
    }

    public DBReqHistory convert(String requestId,String chainId, String groupId, BigInteger blockNumber,
                                String vrfCoreVersion,String sender, SourceTypeEnum sourceTypeEnum,BigInteger consumerSeed) {
        return DBReqHistory.build(chainId, groupId, blockNumber,requestId, sender, vrfCoreVersion,
                sourceTypeEnum,"", null, null, consumerSeed.toString(16));
    }
}
