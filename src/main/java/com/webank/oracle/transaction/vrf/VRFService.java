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

package com.webank.oracle.transaction.vrf;

import static com.webank.oracle.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.oracle.base.enums.ReqStatusEnum.VRF_CONTRACT_ADDRESS_ERROR;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import com.webank.oracle.event.exception.FullFillException;
import com.webank.oracle.event.service.AbstractCoreService;
import com.webank.oracle.event.vo.BaseLogResult;

import lombok.extern.slf4j.Slf4j;

/**
 * VRFService.
 */
@Slf4j
@Service
public class VRFService extends AbstractCoreService {

    @Override
    public ContractTypeEnum getContractType() {
        return ContractTypeEnum.VRF;
    }

    @Override
    public boolean isContractAddressValid(int chainId, int groupId, String contractAddress) {
        try {
            return VRFCoordinator.load(contractAddress, web3jMapService.getNotNullWeb3j(chainId, groupId),
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
    protected String deployContract(int chainId, int groupId) {
        Credentials credentials = keyStoreService.getCredentials();
        VRFCoordinator vrfCoordinator = null;
        try {
            vrfCoordinator = VRFCoordinator.deploy(web3jMapService.getNotNullWeb3j(chainId, groupId),
                    credentials, ConstantProperties.GAS_PROVIDER).send();
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return vrfCoordinator.getContractAddress();
    }

    @Override
    public String getResultAndUpToChain(int chainId, int groupId, BaseLogResult baseLogResult) throws Exception {
        VRFLogResult vrfLogResult = (VRFLogResult) baseLogResult;

        String requestId = vrfLogResult.getRequestId();
        String keyHash = vrfLogResult.getKeyHash();
        BigInteger seed = vrfLogResult.getSeed();
        BigInteger blockNumber = vrfLogResult.getBlockNumber();
        String sender = vrfLogResult.getSender();
        String seedAndBlockNum = vrfLogResult.getSeedAndBlockNum();

        Credentials credentials = keyStoreService.getCredentials();
        String servicePrivateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

        log.info("Call vrf lib:[{}].", requestId);
        String proof = LibVRF.InstanceHolder.getInstance().VRFProoFGenerate(servicePrivateKey, seed.toString(16));
        log.info("Generate proof:[{}] for request:[{}]", proof, requestId);

        this.fulfill(chainId, groupId, sender, baseLogResult, proof);
        return proof;
    }

    /**
     * 将数据上链.
     */
    @Override
    public void fulfill(int chainId, int groupId, String contractAddress, BaseLogResult baseLogResult, Object result) throws Exception {
        VRFLogResult vrfLogResult = (VRFLogResult) baseLogResult;

        String proof = String.valueOf(result);
        String requestId = vrfLogResult.getRequestId();
        BigInteger blockNumber = vrfLogResult.getBlockNumber();

        String vrfCoordinatorAddress = contractAddressMap.get(ChainGroupMapKeyUtil.getKey(chainId, groupId));
        if (StringUtils.isBlank(vrfCoordinatorAddress)) {
            throw new FullFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }

        String sender = vrfLogResult.getSender();
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] data:[{}] requestId:[{}]",vrfCoordinatorAddress, sender, proof, requestId);
        try {
            Web3j web3j = web3jMapService.getNotNullWeb3j(chainId, groupId);
            Credentials credentials = keyStoreService.getCredentials();

            VRFCoordinator vrfCoordinator = VRFCoordinator.load(vrfCoordinatorAddress, web3j, credentials, ConstantProperties.GAS_PROVIDER);

            byte[] bnbytes = Numeric.toBytesPadded(blockNumber, 32);
            byte[] i = Numeric.hexStringToByteArray(proof);
            byte[] destination = new byte[i.length + 32];
            System.arraycopy(i, 0, destination, 0, i.length);
            System.arraycopy(bnbytes, 0, destination, i.length, 32);

            TransactionReceipt receipt = vrfCoordinator.fulfillRandomnessRequest(destination).send();
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());
            dealWithReceipt(receipt);
            log.info("upBlockChain success chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId);
        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId, oe);
            throw new FullFillException(UPLOAD_RESULT_TO_CHAIN_ERROR,oe.getCodeAndMsg().getMessage());
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
    public Pair<String, String> decodeProof(int chainId, int group, String proof) throws Exception {
        return Pair.of("0", proof);
    }
}

