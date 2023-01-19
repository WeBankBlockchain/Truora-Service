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

package com.webank.truora.bcos2runner.vrf;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.exception.FulFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.bcos2runner.AbstractCoreService;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.contract.bcos2.VRFCore;
import com.webank.truora.vrfutils.VRFUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.truora.base.enums.ReqStatusEnum.VRF_CONTRACT_ADDRESS_ERROR;

/**
 * VRFService.
 */
@Slf4j
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@Service
public class VRFService extends AbstractCoreService {

    @Override
    public ContractEnum getContractType() {
        return ContractEnum.VRF_K1_CORE;
    }


    @Override
    public boolean isContractAddressValid(String chainId, String groupId, String contractAddress) {
        try {
            return VRFCore.load(contractAddress, web3jMapService.getNotNullWeb3j(chainId, groupId),
                    keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER).isValid();
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }

    /**
     * @param chainId
     * @param groupId
     * @return
     */
    @Override
    protected String deployContract(String chainId, String groupId) {
        Credentials credentials = keyStoreService.getCredentials();
        VRFCore vrfCore;
        try {
            vrfCore = VRFCore.deploy(web3jMapService.getNotNullWeb3j(chainId, groupId),
                    credentials, ConstantProperties.GAS_PROVIDER,
                    BigInteger.valueOf(Integer.parseInt(chainId)),
                            BigInteger.valueOf(Integer.parseInt(groupId))).send();
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return vrfCore.getContractAddress();
    }

    @Override
    public String getResultAndUpToChain(String chainId, String groupId, BaseLogResult baseLogResult) throws Exception {
        VRFLogResult vrfLogResult = (VRFLogResult) baseLogResult;

        String requestId = vrfLogResult.getRequestId();
        String keyHash = vrfLogResult.getKeyHash();
        BigInteger seed = vrfLogResult.getSeed();
        BigInteger blockNumber = vrfLogResult.getBlockNumber();
        String sender = vrfLogResult.getSender();
        String seedAndBlockNum = vrfLogResult.getSeedAndBlockNum();
        String blockHash = web3jMapService.getNotNullWeb3j(chainId, groupId)
                .getBlockHashByNumber(DefaultBlockParameter.valueOf(blockNumber)).send().getBlockHashByNumber();

        String actualSeed = CommonUtils.bytesToHex(CryptoUtil.soliditySha3(seed,
                CryptoUtil.solidityBytes(ByteUtil.hexStringToBytes(blockHash.substring(2)))));

        ThreadLocalHolder.setActualSeed(actualSeed);

        Credentials credentials = keyStoreService.getCredentials();
        String servicePrivateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        log.info("Call vrf lib:[{}], actualSeed:[{}].", requestId, actualSeed);
        String proof = VRFUtils.prove(servicePrivateKey, actualSeed);
        log.info("Generate proof:[{}] for request:[{}]", proof, requestId);

        this.fulfill(chainId, groupId, sender, baseLogResult, proof);
        return proof;
    }

    /**
     * 将数据上链.
     */
    @Override
    public void fulfill(String chainId, String groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception {
        VRFLogResult vrfLogResult = (VRFLogResult) baseLogResult;

        String proof = String.valueOf(result);
        String requestId = vrfLogResult.getRequestId();
        BigInteger blockNumber = vrfLogResult.getBlockNumber();

        String vrfCoreAddress = baseLogResult.getCoreContractAddress();

        if (StringUtils.isBlank(vrfCoreAddress)) {
            throw new FulFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }

        String sender = vrfLogResult.getSender();
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] data:[{}] requestId:[{}]", vrfCoreAddress, sender, proof, requestId);
        try {
            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            Credentials credentials = keyStoreService.getCredentials();

            VRFCore vrfCore = VRFCore.load(vrfCoreAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);

            TransactionReceipt receipt = vrfCore.fulfillRandomnessRequest(
                    CredentialUtils.calculatePubkeyFromPrivkey(credentials.getEcKeyPair().getPrivateKey().toString(16)),
                    ByteUtil.hexStringToBytes(proof),
                    vrfLogResult.getSeed(), blockNumber).send();
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());

            List<VRFCore.RandomnessRequestFulfilledEventResponse> randomnessRequestFulfilledEvents = vrfCore.getRandomnessRequestFulfilledEvents(receipt);
            if (CollectionUtils.isNotEmpty(randomnessRequestFulfilledEvents)) {
                VRFCore.RandomnessRequestFulfilledEventResponse response = randomnessRequestFulfilledEvents.get(0);
                String randomness = response.output == null ? "" : response.output.toString(16);
                ThreadLocalHolder.setRandomness(randomness);
                log.info("requestId:[{}], randomness: [{}]", requestId, randomness);
            }
            dealWithReceipt(receipt);
            log.info("upBlockChain success chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId);
        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId, oe);
            throw new FulFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
        }

    }

    /**
     * TODO. Use go lib or java implementation
     *
     * @param chainId
     * @param group
     * @param proof
     * @return
     */

}

