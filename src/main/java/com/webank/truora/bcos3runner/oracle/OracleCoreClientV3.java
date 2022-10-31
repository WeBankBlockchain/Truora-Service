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
import com.webank.truora.base.exception.FullFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.bcos3runner.AbstractContractClientV3;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.OracleCore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.utils.Numeric;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.*;
import static com.webank.truora.base.utils.JsonUtils.toJSONString;

/**
 * OracleService.
 */
@Slf4j
@Service
public class OracleCoreClientV3 extends AbstractContractClientV3 {

    @Override
    public ContractEnum getContractType() {
        return ContractEnum.ORACLE_CORE;
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


    @Async
    public String handleOracleRequestAndFulfill(Bcos3EventRegister eventRegister, OracleCore.OracleRequestEventResponse eventResponse) throws Exception {
        log.info("handleOracleRequestAndFulfill: START-> url {} ", eventResponse.url);
        String finalResult  = this.doGetHttpResut(eventResponse.url);
        log.info("handleOracleRequestAndFulfill: url {} https result: {} ", eventResponse.url, toJSONString(finalResult));
        this.fulfill(eventRegister,
                eventResponse.callbackAddr, eventResponse, finalResult);
        return toJSONString(finalResult);
    }

    public String doGetHttpResut(String url) throws Exception {
        // Samples:
        // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";

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

    public void fulfill(Bcos3EventRegister eventRegister, String contractAddress, OracleCore.OracleRequestEventResponse eventResponse, Object result) throws Exception {
        //send transaction

        String requestId = Hex.encodeHexString(eventResponse.requestId);
        log.info("Start to write data to chain, event:reqid:{},callbackAddr:{} data:{}", requestId,eventResponse.callbackAddr, result);

        try {
            Client client = eventRegister.getBcos3client();

            String oracleCoreAddress = eventResponse.coreAddress;
            if (StringUtils.isBlank(oracleCoreAddress)) {
                throw new FullFillException(ORACLE_CORE_CONTRACT_ADDRESS_ERROR);
            }

            OracleCore oracleCore = OracleCore.load(oracleCoreAddress, client, eventRegister.getKeyPair());
            TransactionReceipt receipt = null;
            switch (ReturnTypeEnum.get(eventResponse.returnType)) {
                case INT256:
                    BigInteger afterTimesAmount = new BigDecimal(String.valueOf(result))
                            .multiply(new BigDecimal(eventResponse.timesAmount))
                            .toBigInteger();
                    log.info("After times amount:[{}]", Hex.encodeHexString(afterTimesAmount.toByteArray()));

                    receipt = oracleCore.fulfillRequest(Numeric.hexStringToByteArray(requestId),
                            eventResponse.callbackAddr, eventResponse.expiration, CryptoUtil.toBytes(afterTimesAmount), new byte[0]);
                    break;
                case STRING:
                    log.info("Full fill string value:[{}:{}]", requestId, String.valueOf(result));
                    byte[] bytesValueOfString = String.valueOf(result).getBytes();
                    receipt = oracleCore.fulfillRequest(Numeric.hexStringToByteArray(requestId),
                            eventResponse.callbackAddr, eventResponse.expiration, bytesValueOfString, new byte[0]);
                    break;
                case BYTES:
                    byte[] bytesValue = CryptoUtil.toBytes(result);
                    receipt = oracleCore.fulfillRequest(Numeric.hexStringToByteArray(requestId),
                            eventResponse.callbackAddr, eventResponse.expiration, bytesValue, new byte[0]);
                    break;

                default:
                    throw new FullFillException(UNSUPPORTED_RETURN_TYPE_ERROR, eventResponse.returnType);
            }
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
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }

}
