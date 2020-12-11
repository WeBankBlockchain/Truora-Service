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

package com.webank.oracle.transaction.vrf;

import static com.webank.oracle.base.enums.ReqStatusEnum.REQ_ALREADY_EXISTS;
import static com.webank.oracle.transaction.vrf.VRFCoordinator.RANDOMNESSREQUEST_EVENT;

import org.fisco.bcos.web3j.tx.txdecode.LogResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.webank.oracle.base.enums.OracleVersionEnum;
import com.webank.oracle.base.enums.SourceTypeEnum;
import com.webank.oracle.event.exception.PushEventLogException;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.event.callback.AbstractEventCallback;

import lombok.extern.slf4j.Slf4j;

/**
 * 从callback中获取事件推送过来的请求地址，再请求该地址获取数据上链。
 */
@Component
@Scope("prototype")
@Slf4j
public class VRFContractEventCallback extends AbstractEventCallback {

    @Autowired private VRFService vrfService;

    /**
     * @param chainId
     * @param groupId
     */
    public VRFContractEventCallback(int chainId, int groupId) {
        super(VRFCoordinator.ABI, RANDOMNESSREQUEST_EVENT, chainId, groupId);
    }


    @Override
    public String loadOrDeployContract(int chainId, int group) {
        return vrfService.loadContractAddress(chainId, group);
    }

    @Override
    public String processLog(int status, LogResult logResult) throws Exception {
        VRFLogResult vrfLogResult = new VRFLogResult(logResult);

        log.info("Process log event:[{}]", vrfLogResult);

        if (this.reqHistoryRepository.findByReqId(vrfLogResult.getRequestId()).isPresent()) {
            log.error("Request already exists:[{}:{}:{}].",
                    vrfLogResult.getRequestId(), vrfLogResult.getSender(), vrfLogResult.getSeedAndBlockNum());
            throw new PushEventLogException(REQ_ALREADY_EXISTS, vrfLogResult.getRequestId());
        }

        this.reqHistoryRepository.save(vrfLogResult.convert(chainId, groupId, OracleVersionEnum.VRF_4000, SourceTypeEnum.VRF));

        // save request to db
        log.info("Save request:[{}:{}:{}] to db.", vrfLogResult.getRequestId(), vrfLogResult.getSender(), vrfLogResult.getSeedAndBlockNum());

        //get data from url and update blockChain
        return vrfService.getResultAndUpToChain(chainId, groupId, vrfLogResult);
    }

    @Override
    public void setContractAddress(EventRegister eventRegister, String contractAddress) {
        eventRegister.setVrfContractAddress(contractAddress);
    }

    @Override
    public String getContractAddress(EventRegister eventRegister) {
        return eventRegister.getVrfContractAddress();
    }
}
