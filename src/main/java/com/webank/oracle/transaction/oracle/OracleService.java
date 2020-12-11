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

package com.webank.oracle.transaction.oracle;

import static com.webank.oracle.base.enums.ReqStatusEnum.ORACLE_CORE_CONTRACT_ADDRESS_ERROR;
import static com.webank.oracle.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.oracle.base.utils.JsonUtils.toJSONString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.Numeric;
import org.springframework.stereotype.Service;

import com.webank.oracle.base.enums.ContractTypeEnum;
import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.utils.ChainGroupMapKeyUtil;
import com.webank.oracle.base.utils.JsonUtils;
import com.webank.oracle.event.exception.FullFillException;
import com.webank.oracle.event.service.AbstractCoreService;
import com.webank.oracle.event.vo.BaseLogResult;

import lombok.extern.slf4j.Slf4j;

/**
 * OracleService.
 */
@Slf4j
@Service
public class OracleService extends AbstractCoreService {

    @Override
    public ContractTypeEnum getContractType() {
        return ContractTypeEnum.ORACLE_CORE;
    }

    @Override
    public boolean isContractAddressValid(int chainId, int groupId, String contractAddress) {
        try {
            return OracleCore.load(contractAddress, web3jMapService.getNotNullWeb3j(chainId, groupId),
                    keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER).isValid();
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }

    @Override
    protected String deployContract(int chainId, int groupId) {
        Credentials credentials = keyStoreService.getCredentials();
        OracleCore oraliceCore = null;
        try {
            oraliceCore = OracleCore.deploy(web3jMapService.getNotNullWeb3j(chainId, groupId),
                    credentials, ConstantProperties.GAS_PROVIDER).send();
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return oraliceCore.getContractAddress();
    }

    @Override
    public String getResultAndUpToChain(int chainId, int groupId, BaseLogResult baseLogResult) throws Exception {
        OracleCoreLogResult oracleCoreLogResult = (OracleCoreLogResult) baseLogResult;

        // TODO. optimize
        String url = oracleCoreLogResult.getUrl();
        if (url.startsWith("\"")) {
            int len1 = url.length();
            url = url.substring(1, len1 - 1);
        }
        int left = url.indexOf("(");
        int right = url.indexOf(")");
        String format = url.substring(0, left);
        String httpUrl = url.substring(left + 1, right);
        List<String> httpResultIndexList = subFiledValueForHttpResultIndex(url.substring(right + 1));

        //get data
        BigDecimal httpResult = httpService.getObjectByUrlAndKeys(httpUrl,
                format, httpResultIndexList);
        log.info("url {} https result: {} ", oracleCoreLogResult.getUrl(), toJSONString(httpResult));

        this.fulfill(chainId, groupId, oracleCoreLogResult.getCallbackAddress(), oracleCoreLogResult, httpResult);
        return toJSONString(httpResult);
    }

    /**
     * 将数据上链.
     */
    @Override
    public void fulfill(int chainId, int groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception {
        //send transaction
        OracleCoreLogResult oracleCoreLogResult = (OracleCoreLogResult) baseLogResult;
        String requestId = oracleCoreLogResult.getRequestId();
        log.info("Start to write data to chain, contractAddress:{} data:{}", JsonUtils.toJSONString(baseLogResult), result);

        BigInteger afterTimesAmount = ((BigDecimal) result)
                .multiply(new BigDecimal(oracleCoreLogResult.getTimesAmount()))
                .toBigInteger();
        log.info("After times amount:[{}]", Hex.encodeHexString(afterTimesAmount.toByteArray()));
        try {

            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            Credentials credentials = keyStoreService.getCredentials();
            String oracleCoreAddress = contractAddressMap.get(ChainGroupMapKeyUtil.getKey(chainId, groupId));
            if (StringUtils.isBlank(oracleCoreAddress)) {
                throw new FullFillException(ORACLE_CORE_CONTRACT_ADDRESS_ERROR);
            }
            OracleCore oracleCore = OracleCore.load(oracleCoreAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);
            TransactionReceipt receipt = oracleCore.fulfillRequest(Numeric.hexStringToByteArray(requestId),
                    oracleCoreLogResult.getCallbackAddress(), oracleCoreLogResult.getExpiration(), afterTimesAmount,new byte[0]).send();
            log.info("Write data to chain status: [{}], output:[{}]", receipt.getStatus(),receipt.getOutput());

            dealWithReceipt(receipt);
            log.info("upBlockChain success chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}", chainId, groupId, contractAddress, afterTimesAmount, requestId);
        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {} . contractAddress:{} data:{} requestId:{}", chainId, groupId, contractAddress, afterTimesAmount, requestId, oe);
            throw new FullFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
        }
    }

    /**
     * @param argValue
     * @return
     */
    private String subFiledValueForUrl(String argValue) {
        if (StringUtils.isBlank(argValue)) {
            log.warn("argValue is empty");
            return argValue;
        }
        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0, left);
        String url = argValue.substring(left, right);
        return url;
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
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }

}
