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
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3EventContext;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.contract.bcos3.simplevrf.VRFK1CoreWithBlockHash;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.vrfutils.VRFK1Utils;
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
public class VRFK1CoreWorker extends AbstractContractWorker {

    @Autowired
    Bcos3ClientConfig bcos3ClientConfig;

    public void init(Bcos3EventRegister eventRegister){
        super.init(eventRegister);
    }
    @Override
    public ContractEnum getContractType() {
        return ContractEnum.VRF_K1_CORE;
    }

    @Override
    public Event getEvent() {
        return VRFK1CoreWithBlockHash.RANDOMNESSREQUEST_EVENT;
    }
    @Override
    public String getAbi(){return VRFK1CoreWithBlockHash.getABI();}
    @Override
    public SourceTypeEnum getSourceType(){return SourceTypeEnum.VRF;}

    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        eventRegister.getConfig().setVrfK1CoreAddress(contractAddress);
    }


    public String getContractAddress(Bcos3EventRegister eventRegister) {

        return eventRegister.getConfig().getVrfK1CoreAddress();
    }



    @Override
    public String getRequestId(Object objEventlog) throws DecoderException, UnsupportedEncodingException, ContractCodecException {
        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse response = (VRFK1CoreWithBlockHash.RandomnessRequestEventResponse)objEventlog;
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
                VRFK1CoreWithBlockHash.RANDOMNESSREQUEST_EVENT.getName(),eventLog);
        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse response = fromDecordResult(decodeResult);
        return response;
    }

    @Override
    public boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress) {

        try {
            VRFK1CoreWithBlockHash VRFK1Core =  VRFK1CoreWithBlockHash.load(contractAddress, eventRegister.getBcos3client(),
                    eventRegister.getKeyPair());
            return VRFK1Core!=null;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }


    @Override
    protected String deployContract(Bcos3EventRegister eventRegister) {

        VRFK1CoreWithBlockHash VRFK1Core;
        try {
            VRFK1Core = VRFK1CoreWithBlockHash.deploy(eventRegister.getBcos3client(),
                    eventRegister.getKeyPair(),
                    eventRegister.getConfig().getChainId(),
                    eventRegister.getConfig().getGroupId()
                    );
        } catch (OracleException e) {
            throw e;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.DEPLOY_FAILED);
        }
        return VRFK1Core.getContractAddress();

    }





    public static VRFK1CoreWithBlockHash.RandomnessRequestEventResponse fromDecordResult(List<String> decodeResult) throws DecoderException {
        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse response = new VRFK1CoreWithBlockHash.RandomnessRequestEventResponse();
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
        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse eventResponse = (VRFK1CoreWithBlockHash.RandomnessRequestEventResponse)eventContext.getEventResponse();
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        log.info("Process log event:[{}]", eventResponse);

        Client client = eventRegister.getBcos3client();
        BigInteger seed = eventResponse.seed;
        BigInteger blockNumber = eventResponse.blockNumber;
        //String sender = eventResponse.sender;
        //String seedAndBlockNum = Hex.encodeHexString(eventResponse.seedAndBlockNum);

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

        log.info("Call vrf lib:[{}], actualSeed:[{}].", requestId, actualSeed);
        String proof = VRFK1Utils.prove(vrfPrivateKey, actualSeed);
        log.info("Generate proof:[{}] for request:[{}]", proof, requestId);
        /*将结果写回链上*/
        fulfill(eventRegister, eventResponse, proof,blockhash_bytes);
        return proof;
    }

    /**
     * 将数据上链.
     */

    public void fulfill(Bcos3EventRegister eventRegister,
                        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse eventResponse,
                        String result,byte[] blockhash_bytes) throws Exception {

        String proof = String.valueOf(result);
        byte[] proofbytes = ByteUtil.hexStringToBytes(proof);
        log.info("proof string len {}, byteslen {}",proof.length(),proofbytes.length);
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        BigInteger blockNumber = eventResponse.blockNumber;

        String VRFK1CoreAddress = eventResponse.coreAddress;
        if (StringUtils.isBlank(VRFK1CoreAddress)) {
            throw new FulFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }


        String sender = eventResponse.sender;
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] proof:[{}] requestId:[{}]", VRFK1CoreAddress, sender, proof, requestId);
        try {
            Client client = eventRegister.getBcos3client();
            CryptoKeyPair keyPair = eventRegister.getKeyPair();

            VRFK1CoreWithBlockHash VRFK1Core = VRFK1CoreWithBlockHash.load(VRFK1CoreAddress, client, keyPair);

            List<BigInteger> pubkeylist = CredentialUtils.calculatFromPubkey(keyPair.getHexPublicKey());
            TransactionReceipt receipt = VRFK1Core.fulfillRandomnessRequest(
                    pubkeylist,
                    proofbytes,
                    eventResponse.seed,
                    blockNumber,
                    blockhash_bytes);
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());
            if(receipt.getStatus() == 0)
            {
                //调用成功
                List<VRFK1CoreWithBlockHash.RandomnessRequestFulfilledEventResponse> randomnessRequestFulfilledEvents = VRFK1Core.getRandomnessRequestFulfilledEvents(receipt);
                if (CollectionUtils.isNotEmpty(randomnessRequestFulfilledEvents)) {
                    VRFK1CoreWithBlockHash.RandomnessRequestFulfilledEventResponse response = randomnessRequestFulfilledEvents.get(0);
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
        VRFK1CoreWithBlockHash.RandomnessRequestEventResponse eventResponse = (VRFK1CoreWithBlockHash.RandomnessRequestEventResponse )eventResponseObject;
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

