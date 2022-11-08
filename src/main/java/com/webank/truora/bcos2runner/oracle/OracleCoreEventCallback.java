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

package com.webank.truora.bcos2runner.oracle;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.base.utils.ChainGroupMapUtil;
import com.webank.truora.contract.bcos2.OracleCore;
import com.webank.truora.bcos2runner.AbstractEventCallback;
import com.webank.truora.base.exception.PushEventLogException;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.webank.truora.base.enums.ReqStatusEnum.REQ_ALREADY_EXISTS;


/**

 *
 * 从callback中获取事件推送过来的请求地址，再请求该地址获取数据上链。
 */
@Component
@Scope("prototype")
@Slf4j
public class OracleCoreEventCallback extends AbstractEventCallback {

    @Autowired private OracleCoreSerivce oracleService;


    /**
     * @param chainId
     * @param groupId
     */
    public OracleCoreEventCallback(String chainId, String groupId, EventRegisterConfig eventRegister) {
        super(OracleCore.ABI, OracleCore.ORACLEREQUEST_EVENT, chainId, groupId, SourceTypeEnum.URL, eventRegister);
    }

    @Override
    public String loadOrDeployContract(String chainId, String groupId) {
        return oracleService.loadContractAddress(chainId, groupId, eventRegister.getOracleCoreVersion());
    }

    @Override
    public String processLog(int status, LogResult logResult) throws Exception {
        OracleCoreLogResult oracleCoreLogResult = new OracleCoreLogResult(logResult);

        log.info("Process log event:[{}]", oracleCoreLogResult);
        if (this.reqHistoryRepository.findByReqId(oracleCoreLogResult.getRequestId()).isPresent()) {
            log.error("Request already exists:[{}:{}:{}].", oracleCoreLogResult.getCallbackAddress(),
                    oracleCoreLogResult.getRequestId(), oracleCoreLogResult.getUrl());
            throw new PushEventLogException(REQ_ALREADY_EXISTS, oracleCoreLogResult.getRequestId());
        }

        // TODO. get version
        this.reqHistoryRepository.save(oracleCoreLogResult.convert(chainId, groupId, logResult.getLog().getBlockNumber(),
                ChainGroupMapUtil.getVersionWithDefault(chainId, groupId, oracleCoreLogResult.getCoreContractAddress(),
                        eventRegister.getOracleCoreVersion()),
                SourceTypeEnum.URL));
        log.info("Save request:[{}:{}:{}] to db.", oracleCoreLogResult.getCallbackAddress(),
                oracleCoreLogResult.getRequestId(), oracleCoreLogResult.getUrl());

        //get data from url and update blockChain
        return oracleService.getResultAndUpToChain(chainId, groupId, oracleCoreLogResult);
    }

    @Override
    public void setContractAddress(EventRegisterConfig eventRegister, String contractAddress) {
        eventRegister.setOracleCoreAddress(contractAddress);
    }

    @Override
    public String getContractAddress(EventRegisterConfig eventRegister) {
        return eventRegister.getOracleCoreAddress();
    }
}
