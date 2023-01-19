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
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.exception.FulFillException;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.base.utils.ThreadLocalHolder;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3EventContext;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.simplevrf.VRF25519Core;
import com.webank.truora.database.DBReqHistory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodecException;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.crypto.vrf.Curve25519VRF;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFInterface;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import static com.webank.truora.base.enums.ReqStatusEnum.UPLOAD_RESULT_TO_CHAIN_ERROR;
import static com.webank.truora.base.enums.ReqStatusEnum.VRF_CONTRACT_ADDRESS_ERROR;

/**
 * VRFService.
 */
@Slf4j
@ConditionalOnProperty(name = "runner.fiscobcos3",havingValue = "true")
@Service
@Scope("prototype")
public class VRF25519CoreWorker extends AbstractContractWorker {

    @Autowired
    Bcos3ClientConfig bcos3ClientConfig;

    public void init(Bcos3EventRegister eventRegister){
        super.init(eventRegister);
    }
    @Override
    public ContractEnum getContractType() {
        return ContractEnum.VRF_25519_CORE;
    }

    @Override
    public Event getEvent() {
        return VRF25519Core.RANDOMNESSREQUEST_EVENT;
    }
    @Override
    public String getAbi(){return VRF25519Core.getABI();}
    @Override
    public SourceTypeEnum getSourceType(){return SourceTypeEnum.VRF;}

    protected VRFInterface vrf25519interface = new Curve25519VRF() ;
    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        eventRegister.getConfig().setVrf25519CoreAddress(contractAddress);
    }


    public String getContractAddress(Bcos3EventRegister eventRegister) {

        return eventRegister.getConfig().getVrf25519CoreAddress();
    }



    @Override
    public String getRequestId(Object objEventlog) throws DecoderException, UnsupportedEncodingException, ContractCodecException {
        VRF25519Core.RandomnessRequestEventResponse response = (VRF25519Core.RandomnessRequestEventResponse)objEventlog;
        return Hex.encodeHexString(response.requestId);
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

    @Override
    public Object parseEventLog(EventLog eventLog) throws Exception {
        List<String> decodeResult = contractCodec.decodeEventToString(getAbi(),
                VRF25519Core.RANDOMNESSREQUEST_EVENT.getName(),eventLog);
        VRF25519Core.RandomnessRequestEventResponse response = fromDecordResult(decodeResult);
        return response;
    }

    @Override
    public boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress) {

        try {
            VRF25519Core vrfCore =  VRF25519Core.load(contractAddress, eventRegister.getBcos3client(),
                    eventRegister.getKeyPair());
            return vrfCore!=null;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }


    @Override
    protected String deployContract(Bcos3EventRegister eventRegister) {

        VRF25519Core vrfCore;
        try {
            vrfCore = VRF25519Core.deploy(eventRegister.getBcos3client(),
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





    public static VRF25519Core.RandomnessRequestEventResponse fromDecordResult(List<String> decodeResult) throws DecoderException {
        VRF25519Core.RandomnessRequestEventResponse response = new VRF25519Core.RandomnessRequestEventResponse();
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
    public String processLog(Bcos3EventContext eventContext) throws Exception {
        VRF25519Core.RandomnessRequestEventResponse eventResponse = (VRF25519Core.RandomnessRequestEventResponse)eventContext.getEventResponse();
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        log.info("Process log event:[{}]", eventResponse);

        Client client = eventRegister.getBcos3client();
        BigInteger seed = eventResponse.seed;
        BigInteger blockNumber = eventResponse.blockNumber;

        // v3合约新增，合约事件里返回了blocknumber,通过blocknumber获取blockhash
        String blockHash = client.getBlockHashByNumber(blockNumber).getBlockHashByNumber();
        if(blockHash.startsWith("0x")){
            blockHash = blockHash.substring(2);
        }
        /*构建输入数据，生成vrf proof*/
        byte[] blockhash_bytes = CryptoUtil.solidityBytes(ByteUtil.hexStringToBytes(blockHash));
        String actualSeed = CommonUtils.bytesToHex(CryptoUtil.soliditySha3(seed,blockhash_bytes));
        ThreadLocalHolder.setActualSeed(actualSeed);

        CryptoKeyPair keyPair = eventRegister.getKeyPair();
        String vrfPrivateKey = keyPair.getHexPrivateKey();

        //String vrfpublickey = vrfInterface.getPublicKeyFromPrivateKey(vrfPrivateKey);
        String vrfProof =
                vrf25519interface.generateVRFProof(
                        vrfPrivateKey,
                        actualSeed);

        log.info("Call vrf lib:[{}], actualSeed:[{}],privkey: {}", requestId, actualSeed,vrfPrivateKey);
        log.info("Generate proof:[{}] for request:[{}]", vrfProof, requestId);
        /*将结果写回链上*/
        fulfill(eventRegister, eventResponse, vrfProof,actualSeed,blockhash_bytes);
        return vrfProof;
    }

    /**
     * 将数据上链.
     */

    public void fulfill(Bcos3EventRegister eventRegister,
                        VRF25519Core.RandomnessRequestEventResponse eventResponse,

                        String result,String actualSeed,byte[] blockhash_bytes) throws Exception {

        String proof = String.valueOf(result);
        byte[] proofbytes = ByteUtil.hexStringToBytes(proof);
        log.info("proof string len {}, byteslen {}",proof.length(),proofbytes.length);
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        BigInteger blockNumber = eventResponse.blockNumber;

        String vrfCoreAddress = eventResponse.coreAddress;
        if (StringUtils.isBlank(vrfCoreAddress)) {
            throw new FulFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }


        String sender = eventResponse.sender;
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] proof:[{}] requestId:[{}]", vrfCoreAddress, sender, proof, requestId);
        try {
            Client client = eventRegister.getBcos3client();
            CryptoKeyPair keyPair = eventRegister.getKeyPair();
            String vrfPublicKey = vrf25519interface.getPublicKeyFromPrivateKey(keyPair.getHexPrivateKey());

            boolean vrfverifyResult = vrf25519interface.verify(vrfPublicKey,actualSeed,proof);
            log.info("before fulfill,verify vrf proof result {},publickey:{}",vrfverifyResult,vrfPublicKey);
            if(vrfverifyResult == false){
                throw new OracleException(ConstantCode.VERIFY_VRF_PROOF_ERROR);
            }

            VRF25519Core vrfCore = VRF25519Core.load(vrfCoreAddress, client, keyPair);
            TransactionReceipt receipt = vrfCore.fulfillRandomnessRequest(
                    Hex.decodeHex(vrfPublicKey),
                    proofbytes,
                    eventResponse.seed,
                    blockNumber
                    );
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());
            if(receipt.getStatus() == 0)
            {

                //调用成功
                List<VRF25519Core.RandomnessRequestFulfilledEventResponse> randomnessRequestFulfilledEvents = vrfCore.getRandomnessRequestFulfilledEvents(receipt);
                if (CollectionUtils.isNotEmpty(randomnessRequestFulfilledEvents)) {
                    VRF25519Core.RandomnessRequestFulfilledEventResponse response = randomnessRequestFulfilledEvents.get(0);
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
            throw new FulFillException(UPLOAD_RESULT_TO_CHAIN_ERROR, oe.getCodeAndMsg().getMessage());
        }

    }


    @Override
    public DBReqHistory makeDBReqHistory(Bcos3EventRegister register, EventLog eventLog, Object eventResponseObject) {
        VRF25519Core.RandomnessRequestEventResponse eventResponse = (VRF25519Core.RandomnessRequestEventResponse )eventResponseObject;
        DBReqHistory reqHistory = new DBReqHistory();
        reqHistory.setPlatform(this.eventRegister.getPlatform());
        reqHistory.setChainId(chainId);
        reqHistory.setGroupId(groupId);
        reqHistory.setReqQuery("VRF");
        reqHistory.setSourceType(this.getSourceType().getId());
        reqHistory.setBlockNumber(eventLog.getBlockNumber());
        reqHistory.setReqId(Hex.encodeHexString(eventResponse.requestId));
        reqHistory.setOracleVersion(register.getConfig().getOracleCoreVersion());
        reqHistory.setSourceType(SourceTypeEnum.URL.getId());
        reqHistory.setInputSeed(eventResponse.consumerSeed.toString(16));
        reqHistory.setUserContract(eventResponse.sender);
        return reqHistory;
    }

}

