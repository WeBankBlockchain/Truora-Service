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

package com.webank.truora.bcos3runner.vrf;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.bcos3runner.AbstractContractClientV3;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.simplevrf.VRFCore;
import com.webank.truora.base.exception.FullFillException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.truora.base.enums.ReqStatusEnum.VRF_CONTRACT_ADDRESS_ERROR;

/**
 * VRFService.
 */
@Slf4j
@Service
public class VRFClientV3 extends AbstractContractClientV3 {

    @Override
    public ContractEnum getContractType() {
        return ContractEnum.VRF;
    }

    @Override
    public boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress) {

        try {
            VRFCore vrfCore =  VRFCore.load(contractAddress, eventRegister.getBcos3client(),
                    eventRegister.getKeyPair());
            return vrfCore==null;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }


    @Override
    protected String deployContract(Bcos3EventRegister eventRegister) {

        VRFCore vrfCore;
        try {
            vrfCore = VRFCore.deploy(eventRegister.getBcos3client(),
                    eventRegister.getKeyPair(),
                    eventRegister.getConfig().getChainId(),
                    eventRegister.getConfig().getGroupId()
                    );
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return vrfCore.getContractAddress();

    }



    public String getResultAndUpToChain(Bcos3EventRegister eventRegister, VRFCore.RandomnessRequestEventResponse eventResponse) throws Exception {
        /*
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
        String proof = LibVRFK1.InstanceHolder.getInstance().prove(servicePrivateKey, actualSeed);
        log.info("Generate proof:[{}] for request:[{}]", proof, requestId);

        this.fulfill(chainId, groupId, sender, baseLogResult, proof);
        return proof;*/
        return "";
    }

    /**
     * 将数据上链.
     */

    public void fulfill(Bcos3EventRegister eventRegister, String contractAddress, VRFCore.RandomnessRequestEventResponse eventResponse, Object result) throws Exception {


        String proof = String.valueOf(result);
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        BigInteger blockNumber = eventResponse.blockNumber;

        String vrfCoreAddress = eventResponse.coreAddress;
        String chainId = eventRegister.getConfig().getChainId();
        String groupId = eventRegister.getConfig().getGroupId();
        if (StringUtils.isBlank(vrfCoreAddress)) {
            throw new FullFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }

        String sender = eventResponse.sender;
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] data:[{}] requestId:[{}]", vrfCoreAddress, sender, proof, requestId);
        try {
            Client client = eventRegister.getBcos3client();
            CryptoKeyPair key = eventRegister.getKeyPair();
            VRFCore vrfCore = VRFCore.load(vrfCoreAddress, client, key);

            TransactionReceipt receipt = vrfCore.fulfillRandomnessRequest(
                    //credentials.getEcKeyPair().getPrivateKey().toString(16)
                    CredentialUtils.calculateThePK(key.getHexPrivateKey()),
                    ByteUtil.hexStringToBytes(proof),
                    eventResponse.seed, blockNumber);
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());
            if(receipt.getStatus() == 0)
            {
                //调用成功
                List<VRFCore.RandomnessRequestFulfilledEventResponse> randomnessRequestFulfilledEvents = vrfCore.getRandomnessRequestFulfilledEvents(receipt);
                if (CollectionUtils.isNotEmpty(randomnessRequestFulfilledEvents)) {
                    VRFCore.RandomnessRequestFulfilledEventResponse response = randomnessRequestFulfilledEvents.get(0);
                    String randomness = response.output == null ? "" : response.output.toString(16);
                    ThreadLocalHolder.setRandomness(randomness);
                    log.info("requestId:[{}], randomness: [{}]", requestId, randomness);
                    log.info("upBlockChain success chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId);
                }
            }else {
                //失败
                dealWithReceipt(receipt);
            }

        } catch (OracleException oe) {
            log.error("upBlockChain exception chainId: {}  groupId: {}. sender:{} data:{} requestId:{}", chainId, groupId, sender, proof, requestId, oe);
            throw new FullFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
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
    public Pair<String, String> decodeProof(String chainId, String group, String proof) throws Exception {
        return Pair.of("0", proof);
    }
}

