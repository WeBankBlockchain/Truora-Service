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
import com.webank.truora.base.exception.FullFillException;
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
import com.webank.truora.contract.bcos3.simplevrf.VRFCoreWithBlockHash;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.vrfutils.VRFUtils;
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
@Service
@Scope("prototype")
public class VRFCoreWorker extends AbstractContractWorker {

    @Autowired
    Bcos3ClientConfig bcos3ClientConfig;

    public void init(Bcos3EventRegister eventRegister){
        super.init(eventRegister);
    }
    @Override
    public ContractEnum getContractType() {
        return ContractEnum.VRF;
    }

    @Override
    public Event getEvent() {
        return VRFCoreWithBlockHash.RANDOMNESSREQUEST_EVENT;
    }
    @Override
    public String getAbi(){return VRFCoreWithBlockHash.getABI();}
    @Override
    public SourceTypeEnum getSourceType(){return SourceTypeEnum.VRF;}

    public void setCoreContractAddress(Bcos3EventRegister eventRegister, String contractAddress) {
        eventRegister.getConfig().setVrfCoreAddress(contractAddress);
    }


    public String getContractAddress(Bcos3EventRegister eventRegister) {

        return eventRegister.getConfig().getVrfCoreAddress();
    }



    @Override
    public String getRequestId(Object objEventlog) throws DecoderException, UnsupportedEncodingException, ContractCodecException {
        VRFCoreWithBlockHash.RandomnessRequestEventResponse response = (VRFCoreWithBlockHash.RandomnessRequestEventResponse)objEventlog;
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
                VRFCoreWithBlockHash.RANDOMNESSREQUEST_EVENT.getName(),eventLog);
        VRFCoreWithBlockHash.RandomnessRequestEventResponse response = fromDecordResult(decodeResult);
        return response;
    }

    @Override
    public boolean isContractAddressValid(Bcos3EventRegister eventRegister, String contractAddress) {

        try {
            VRFCoreWithBlockHash vrfCore =  VRFCoreWithBlockHash.load(contractAddress, eventRegister.getBcos3client(),
                    eventRegister.getKeyPair());
            return vrfCore!=null;
        } catch (Exception e) {
            throw new OracleException(ConstantCode.CHECK_CONTRACT_VALID_ERROR);
        }
    }


    @Override
    protected String deployContract(Bcos3EventRegister eventRegister) {

        VRFCoreWithBlockHash vrfCore;
        try {
            vrfCore = VRFCoreWithBlockHash.deploy(eventRegister.getBcos3client(),
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





    public static VRFCoreWithBlockHash.RandomnessRequestEventResponse fromDecordResult(List<String> decodeResult) throws DecoderException {
        VRFCoreWithBlockHash.RandomnessRequestEventResponse response = new VRFCoreWithBlockHash.RandomnessRequestEventResponse();
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
        VRFCoreWithBlockHash.RandomnessRequestEventResponse eventResponse = (VRFCoreWithBlockHash.RandomnessRequestEventResponse)eventContext.getEventResponse();
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
        String proof = VRFUtils.prove(vrfPrivateKey, actualSeed);
        log.info("Generate proof:[{}] for request:[{}]", proof, requestId);
        /*将结果写回链上*/
        fulfill(eventRegister, eventResponse, proof,blockhash_bytes);
        return proof;
    }

    /**
     * 将数据上链.
     */

    public void fulfill(Bcos3EventRegister eventRegister,
                        VRFCoreWithBlockHash.RandomnessRequestEventResponse eventResponse,
                        String result,byte[] blockhash_bytes) throws Exception {

        String proof = String.valueOf(result);
        byte[] proofbytes = ByteUtil.hexStringToBytes(proof);
        log.info("proof string len {}, byteslen {}",proof.length(),proofbytes.length);
        String requestId = Hex.encodeHexString(eventResponse.requestId);
        BigInteger blockNumber = eventResponse.blockNumber;

        String vrfCoreAddress = eventResponse.coreAddress;
        if (StringUtils.isBlank(vrfCoreAddress)) {
            throw new FullFillException(VRF_CONTRACT_ADDRESS_ERROR);
        }


        String sender = eventResponse.sender;
        log.info("upBlockChain start. CoordinatorAddress:[{}] sender:[{}] proof:[{}] requestId:[{}]", vrfCoreAddress, sender, proof, requestId);
        try {
            Client client = eventRegister.getBcos3client();
            CryptoKeyPair keyPair = eventRegister.getKeyPair();

            VRFCoreWithBlockHash vrfCore = VRFCoreWithBlockHash.load(vrfCoreAddress, client, keyPair);

            List<BigInteger> pubkeylist = CredentialUtils.calculatFromPubkey(keyPair.getHexPublicKey());
            TransactionReceipt receipt = vrfCore.fulfillRandomnessRequest(
                    pubkeylist,
                    proofbytes,
                    eventResponse.seed,
                    blockNumber,
                    blockhash_bytes);
            log.info("requestId:[{}], receipt status:[{}]", requestId, receipt.getStatus());
            if(receipt.getStatus() == 0)
            {
                //调用成功
                List<VRFCoreWithBlockHash.RandomnessRequestFulfilledEventResponse> randomnessRequestFulfilledEvents = vrfCore.getRandomnessRequestFulfilledEvents(receipt);
                if (CollectionUtils.isNotEmpty(randomnessRequestFulfilledEvents)) {
                    VRFCoreWithBlockHash.RandomnessRequestFulfilledEventResponse response = randomnessRequestFulfilledEvents.get(0);
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


    @Override
    public DBReqHistory makeDBReqHistory(Bcos3EventRegister register, EventLog eventLog, Object eventResponseObject) {
        VRFCoreWithBlockHash.RandomnessRequestEventResponse eventResponse = (VRFCoreWithBlockHash.RandomnessRequestEventResponse )eventResponseObject;
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

