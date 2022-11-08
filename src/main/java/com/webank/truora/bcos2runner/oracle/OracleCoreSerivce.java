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

package com.webank.truora.bcos2runner.oracle;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.exception.FullFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos2runner.AbstractCoreService;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import com.webank.truora.contract.bcos2.OracleCore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.Numeric;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.ORACLE_CORE_CONTRACT_ADDRESS_ERROR;
import static com.webank.truora.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.truora.base.utils.JsonUtils.toJSONString;

/**
 * OracleService.
 */
@Slf4j
@Service
public class OracleCoreSerivce extends AbstractCoreService {

    @Override
    public ContractEnum getContractType() {
        return ContractEnum.ORACLE_CORE;
    }


    @Override
    public boolean isContractAddressValid(String chainId, String groupId, String contractAddress) {
        try {
            return OracleCore.load(contractAddress, web3jMapService.getNotNullWeb3j(chainId, groupId),
                    keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER).isValid();
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }

    @Override
    protected String deployContract(String chainId, String groupId) {
        Credentials credentials = keyStoreService.getCredentials();
        OracleCore oraliceCore = null;
        try {
            oraliceCore = OracleCore.deploy(web3jMapService.getNotNullWeb3j(chainId, groupId),
                    credentials, ConstantProperties.GAS_PROVIDER,
                    BigInteger.valueOf(Integer.parseInt(chainId)),
                    BigInteger.valueOf(Integer.parseInt(groupId)))
                    .send();
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return oraliceCore.getContractAddress();
    }

    @Override
    public String getResultAndUpToChain(String chainId, String groupId, BaseLogResult baseLogResult) throws Exception {
        OracleCoreLogResult oracleCoreLogResult = (OracleCoreLogResult) baseLogResult;
        String finalResult  = this.parseUrlFromEventAndGetHttpResut(oracleCoreLogResult);
        log.info("url {} https result: {} ", oracleCoreLogResult.getUrl(), toJSONString(finalResult));

        this.fulfill(chainId, groupId, oracleCoreLogResult.getCallbackAddress(), oracleCoreLogResult, finalResult);
        return toJSONString(finalResult);
    }

    public String parseUrlFromEventAndGetHttpResut(OracleCoreLogResult oracleCoreLogResult) throws Exception {
        String url = oracleCoreLogResult.getUrl();
        int len = url.length();
        if (url.startsWith("\"")) {
            url = url.substring(1, len - 1);
            len = len-2;
        }
        int left = url.indexOf("(");
        int right = url.indexOf(")");
        String format = url.substring(0, left);
        String httpUrl = url.substring(left + 1, right);
        String path = "";
        if(url.length() > right + 1) {
            path =  url.substring(right+1,len);
        }
        log.info("***parse event url resut: {}, formate: {}, path: {}", url,format,path);
        //get data
        String finalResult =  httpService.getHttpResultAndParse(httpUrl, format, path);
        return  finalResult;
    }


    /**
     * 将数据上链.
     */
    @Override
    public void fulfill(String chainId, String groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception {
        //send transaction
        OracleCoreLogResult oracleCoreLogResult = (OracleCoreLogResult) baseLogResult;
        String requestId = oracleCoreLogResult.getRequestId();
        log.info("Start to write data to chain, contractAddress:{} data:{}", JsonUtils.toJSONString(baseLogResult), result);

        try {
            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            Credentials credentials = keyStoreService.getCredentials();
            String oracleCoreAddress = oracleCoreLogResult.getCoreContractAddress();
            if (StringUtils.isBlank(oracleCoreAddress)) {
                throw new FullFillException(ORACLE_CORE_CONTRACT_ADDRESS_ERROR);
            }

            OracleCore oracleCore = OracleCore.load(oracleCoreAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);
            TransactionReceipt receipt = null;
            /*convert result to bytes according the return type*/
            byte[] returnbytes = ReturnTypeEnum.convert2Bytes(((OracleCoreLogResult) baseLogResult).getReturnType(),result,
                        ((OracleCoreLogResult) baseLogResult).getTimesAmount()
                    );

            receipt = oracleCore.fulfillRequest(Numeric.hexStringToByteArray(requestId),
                    oracleCoreLogResult.getCallbackAddress(), oracleCoreLogResult.getExpiration(), returnbytes, new byte[0]).send();

            log.info("Write data to chain status: [{}], output:[{}]", receipt.getStatus(),receipt.getOutput());

            dealWithReceipt(receipt);
            log.info("upBlockChain success chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}", chainId, groupId, contractAddress, result, requestId);
        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}", chainId, groupId, contractAddress, result, requestId, oe);
            throw new FullFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
        }
    }


    /**
     * @param argValue
     * @return
     */
    private List<String> subFiledValueForHttpResultIndex(String argValue) {
        if (StringUtils.isBlank(argValue) || argValue.endsWith(")")) {
            log.warn("argValue is:{} ,return empty list", argValue);
            return Collections.EMPTY_LIST;
        }

        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");//.replaceAll("\\.", ",").split(",")
        List<String> resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }

}
