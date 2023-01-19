package com.webank.truora.test.bcos2.VRF;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.bcos2runner.AbstractCoreService;
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.contract.bcos2.RandomNumberSampleVRF;
import com.webank.truora.contract.bcos2.VRFCore;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.test.base.BaseTest;
import com.webank.truora.vrfutils.VRFUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 *
 */

@Slf4j
public class RandomNumberConsumerTest extends BaseTest {

    @Test
    public void testVRFWithDeployManually() throws Exception {

        String sampleKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
        credentials = Credentials.create(sampleKey);

        String chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        String groupId = eventRegisterProperties.getEventRegisters().get(0).getGroupId();

        log.info("deploy vrf core");
        Web3j web3j = getWeb3j(chainId, groupId);

        VRFCore vrfCore = VRFCore.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER,
                BigInteger.valueOf(Integer.parseInt(chainId)), BigInteger.valueOf(Integer.parseInt(groupId))).send();
        log.info("vrf core address : " + vrfCore.getContractAddress());

        byte[] keyHashByte = calculateTheHashFromPrivkey(sampleKey);
        log.info("keyHashByte hex is: {}", Hex.encodeHexString(keyHashByte));
        log.info("deploy consumer  contract");

        List<BigInteger> ilist = CredentialUtils.calculatePubkeyFromPrivkey(sampleKey);


        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, vrfCore.getContractAddress(), keyHashByte).send();
        log.info("consumer address: " + randomNumberConsumer.getContractAddress());

        log.info("consumer start a query ....... ");
        TransactionReceipt randomT = randomNumberConsumer.getRandomNumber(new BigInteger("1")).send();
        log.info(randomT.getStatus());
        log.info("consumer query reqId: " + randomT.getOutput());


        log.info("core listen to the event .........");
        VRFCore.RandomnessRequestEventResponse randomevent = vrfCore.getRandomnessRequestEvents(randomT).get(0);

        log.info("hash:" + CommonUtils.bytesToHex(randomevent.keyHash));
        log.info("preseed " + randomevent.seed);
        log.info("blocknumber: " + randomevent.blockNumber);

        log.info("sender: " + randomevent.sender);
        log.info("requestId: " + CommonUtils.bytesToHex(randomevent.requestId));
        log.info("seedAndBlock:        " + CommonUtils.bytesToHex(randomevent.seedAndBlockNum));

        log.info("vrf core generate the proof .........");
        BigInteger preseed = randomevent.seed;
        BigInteger blockNumber = randomevent.blockNumber;
        String blockhash = web3j.getBlockHashByNumber(DefaultBlockParameter.valueOf(blockNumber)).send().getBlockHashByNumber();
        log.info("blockhash : {}", blockhash);
//        byte[] bnbytes = Numeric.toBytesPadded(blockNumber, 32);

        String actualSeed = CommonUtils.bytesToHex(CryptoUtil.soliditySha3(randomevent.seed, CryptoUtil.solidityBytes(ByteUtil.hexStringToBytes(blockhash.substring(2)))));
        String actualSeedonchain = CommonUtils.bytesToHex(randomevent.seedAndBlockNum);
        log.info("actualseed: {} ", actualSeed);
        log.info("actualSeedonchain: {} ", actualSeedonchain);
        try {
            String proof ="";
            String hexkey = credentials.getEcKeyPair().getPrivateKey().toString(16);
            //VRFUtils.debuglevel = 1;
            String resstr = VRFUtils.prove(hexkey,actualSeed);
            proof = resstr;
            log.info("proof from vrfk1 {}",proof);


            String vrfpubkey = VRFUtils.derive_public_key(hexkey);
            String  verifyres = VRFUtils.verify(vrfpubkey,proof,actualSeed);
            log.info("verify result {}",verifyres);

            log.info("Generate proof len:{}:{}" ,proof.length(), proof);

            Thread.sleep(10);
            byte[] proofbyte = ByteUtil.hexStringToBytes(proof);
            log.info("Generate proof bytes len:{}" ,proofbyte.length);

            log.info("vrf core fulfill the request ......{}",ilist);
            log.info("vrf core fulfill blockNumber:{},proofbyte:{},preseed:{}",blockNumber,
                    Hex.encodeHexString(proofbyte),
                    preseed
            );
            TransactionReceipt tt = (TransactionReceipt) vrfCore.fulfillRandomnessRequest(ilist, proofbyte, preseed, blockNumber).send();

            log.info("gasUsed: {}",tt.getGasUsed());
            AbstractCoreService.dealWithReceipt(tt);
            log.info(tt.getStatus());
            log.info(tt.getOutput());

            VRFCore.RandomnessRequestFulfilledEventResponse res = vrfCore.getRandomnessRequestFulfilledEvents(tt).get(0);
            log.info("ramdom result: " + res.output);
            log.info("requestId: " + CommonUtils.bytesToHex(res.requestId));

            log.info(" consumer query the ramdom result");
            BigInteger ram = randomNumberConsumer.randomResult().send();
            log.info("randomResult from chain: " + ram);
            String proofhash = VRFUtils.proof_to_hash_hex(proof);
            log.info("proof_to_hash_hex VRF "+proofhash);
            //  log.info(DecodeOutputUtils.decodeOutputReturnString0x16(t.getOutput()));
        } catch (Exception e) {
            log.error("TEST VRF exception {}", e);
        }
    }

    @Test
    public void testVRFWithAutoDeploy() {

        try {
            credentials = this.keyStoreService.getCredentials();

            EventRegisterConfig eventRegister = eventRegisterProperties.getEventRegisters().get(0);

            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();
            Web3j web3j = getWeb3j(chainId, groupId);

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos",
                            chainId, groupId,
                            ContractEnum.VRF_K1_CORE.getType(), this.contractVersion.getVrfCoreVersion());
            if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String vrfCoreAddress = deployOptional.get().getContractAddress();
            log.info("vrf core address " + vrfCoreAddress);

            // asset
            byte[] keyHashByte = calculateTheHashFromPrivkey(credentials.getEcKeyPair().getPrivateKey().toString(16));
            RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, vrfCoreAddress, keyHashByte).send();
            String randomConsumerAddress = randomNumberConsumer.getContractAddress();
            log.info("Deploy RandomNumberSampleVRF contract:[{}]", randomConsumerAddress);

            TransactionReceipt randomT = randomNumberConsumer.getRandomNumber(new BigInteger("1")).send();
            log.info(randomT.getStatus());
            log.info("RandomNumberSampleVRF reqId: " + randomT.getOutput());

            Thread.sleep(5000);

            BigInteger random = randomNumberConsumer.randomResult().send();
            log.info("Random:[{}]", random);

            Assertions.assertTrue(random.compareTo(BigInteger.ZERO) != 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}