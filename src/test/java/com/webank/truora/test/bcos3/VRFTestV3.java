package com.webank.truora.test.bcos3;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.bcos2runner.base.CredentialUtils;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3KeyTools;
import com.webank.truora.bcos3runner.Bcos3SdkFactory;
import com.webank.truora.contract.bcos3.simplevrf.RandomNumberSampleVRF;
import com.webank.truora.contract.bcos3.simplevrf.VRFCore;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.database.DBContractDeployRepository;
import com.webank.truora.test.LocalTestBase;
import com.webank.truora.test.base.BaseTest;
import com.webank.truora.vrfutils.VRFUtils;
import com.webank.truora.vrfutils.VRFUtilsConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 *
 */

@Slf4j
public class VRFTestV3 extends LocalTestBase {
    @Autowired
    VRFUtilsConfig vrfUtilsConfig;
    @Autowired
    Bcos3ClientConfig bcos3sdkconfig;
    @Autowired
    Bcos3SdkFactory sdkFactory;
    String chainId;
    String groupId;
    String sampleKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
    String samplePubKey = "";
    CryptoKeyPair sampleKeyPair;
    Client client;
    CryptoKeyPair keyPair;


    String userPrivkey;
    String vrfCoreAddress;
    public void init_test() throws Exception{
        this.chainId = "chain0";
        this.groupId = "group0";
        this.client = sdkFactory.getClientByChainIdAndGroupIdNotNull(chainId,groupId);
         this.keyPair = Bcos3KeyTools.getKeyPairByFile(client,bcos3sdkconfig.getDefaultKeyfile());
       // this.userPrivkey = keyPair.getHexPrivateKey();
        sampleKeyPair = Bcos3KeyTools.getKeyPairByHexkey(client.getCryptoSuite(),sampleKey);
        sampleKeyPair = keyPair;
        this.samplePubKey = sampleKeyPair.getHexPublicKey();
        log.info("calc sampleKey privkey: {},pubkey:{}",sampleKey,samplePubKey);

    }

    @Test
    public void testVRFWithDeployManually() throws Exception {


        init_test();
        log.info("deploy vrf core");

        //VRFCore vrfCore = VRFCore.deploy(client,sampleKeyPair,chainId,groupId);
        VRFCore vrfCore = VRFCore.deploy(client,sampleKeyPair,chainId,groupId);
        log.info("vrf core address : " + vrfCore.getContractAddress());
        this.vrfCoreAddress = vrfCore.getContractAddress();
        //this.vrfCoreAddress = "0x49bc3af839b3c083d136b30118a9b8acc7d035e7";
        //VRFCore vrfCore = VRFCore.load(this.vrfCoreAddress,client,sampleKeyPair);
        byte[] keyHashByte = BaseTest.calculateTheHashOfPK(samplePubKey);
        log.info("keyHashByte hex is: {}", Hex.encodeHexString(keyHashByte));
        log.info("deploy consumer  contract");
        List<BigInteger> ilist = CredentialUtils.calculatFromPubkey(samplePubKey);
        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(client,sampleKeyPair,
                vrfCore.getContractAddress(), keyHashByte);
        log.info("consumer address: " + randomNumberConsumer.getContractAddress());

        log.info("consumer start a query ....... ");
        TransactionReceipt randomT = randomNumberConsumer.getRandomNumber(new BigInteger("1"));
        log.info("randomNumberConsumer.getRandomNumber status: {}",randomT.getStatus());
        AbstractContractWorker.dealWithReceipt(randomT);
        log.info("consumer query reqId: " + randomT.getOutput());


        log.info("core listen to the event .........");
        VRFCore.RandomnessRequestEventResponse randomevent = vrfCore.getRandomnessRequestEvents(randomT).get(0);

        log.info("hash:" + CommonUtils.bytesToHex(randomevent.keyHash));
        log.info("preseed " + randomevent.seed);
        log.info("blocknumber: " + randomevent.blockNumber);

        log.info("sender: " + randomevent.sender);
        log.info("requestId: " + CommonUtils.bytesToHex(randomevent.requestId));
        log.info("seedAndBlock:  " + CommonUtils.bytesToHex(randomevent.seedAndBlockNum));

        log.info("vrf core generate the proof .........");
        BigInteger preseed = randomevent.seed;
        BigInteger blockNumber = randomevent.blockNumber;
        String blockhash = client.getBlockHashByNumber(blockNumber).getBlockHashByNumber();
        log.info("blockhash : {}", blockhash);
//        byte[] bnbytes = Numeric.toBytesPadded(blockNumber, 32);
        BigInteger seed =randomevent.seed;
        byte[] blockhash_trim =  CryptoUtil.solidityBytes(ByteUtil.hexStringToBytes(blockhash.substring(2)));
        String actualSeed = CommonUtils.bytesToHex(CryptoUtil.soliditySha3(seed,blockhash_trim));
        String actualSeedonchain = CommonUtils.bytesToHex(randomevent.seedAndBlockNum);
        log.info("actualseed: {} ", actualSeed);
        log.info("actualSeedonchain: {} ", actualSeedonchain);
        try {
            VRFUtils.debuglevel = vrfUtilsConfig.getDebuglevel();
            String proof ="";
            String hexkey = sampleKeyPair.getHexPrivateKey();
            String resstr = VRFUtils.prove(hexkey,actualSeed);
            log.info("proof from vrfk1 {}",resstr);
            proof = resstr;
            log.info("Generate proof len:{}:{}" ,proof.length(), proof);


            String vrfpubkey = VRFUtils.derive_public_key(hexkey);
            String  verifyres = VRFUtils.verify(vrfpubkey,proof,actualSeed);
            log.info("verify result {}",verifyres);



            Thread.sleep(10);
            byte[] proofbyte = ByteUtil.hexStringToBytes(proof);
            log.info("Generate proof bytes len:{}" ,proofbyte.length);

            log.info("vrf core fulfill the request ......{}",ilist);
            log.info("vrf core fulfill blockNumber:{},proofbyte:{},preseed:{}",blockNumber,
                    Hex.encodeHexString(proofbyte),
                    preseed
                    );

            byte[] blockhash_bin_no0x = Hex.decodeHex(blockhash.substring(2));

            // 结果上链
            TransactionReceipt tt =  vrfCore.fulfillRandomnessRequest(ilist, proofbyte,
                    preseed,blockNumber);
            log.info("fulfill transaction on block:{},txhash: {}",tt.getBlockNumber(),tt.getTransactionHash());

            if (tt.getStatus()==12){
                String gasUsed  = tt.getGasUsed();

                log.info("status {},out of gas,gasUsed :{}",tt.getStatus(),gasUsed);
            }
            if (tt.getStatus()!=0){
                AbstractContractWorker.dealWithReceipt(tt);
                return;
            }

            List<TransactionReceipt.Logs> logs = tt.getLogEntries() ;

            log.info("vrfCore.fulfillRandomnessRequest status: {}",tt.getStatus());
            log.info("vrfCore.fulfillRandomnessRequest output: {}",tt.getOutput());

            VRFCore.RandomnessRequestFulfilledEventResponse res = vrfCore.getRandomnessRequestFulfilledEvents(tt).get(0);
            log.info("ramdom result: " + res.output);
            log.info("requestId: " + CommonUtils.bytesToHex(res.requestId));

            log.info(" consumer query the ramdom result");
            BigInteger ram = randomNumberConsumer.randomResult();
            log.info("randomResult from chain: " + ram);
            log.info("-----------------VRF end---------------------------");
            //String ss = "no resource found for suffixes";
            //String reg=".*no resource.*";
            //System.out.println("REG RES: "+ss.matches(reg));
        } catch (Exception e) {
            log.error("TEST VRF exception {}", e);
        }
    }
    @Autowired
    DBContractDeployRepository contractDeployRepository;
    @Test
    public void testVRFWithAutoDeploy() {

        try {
            init_test();

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos3",
                            chainId, groupId,
                            ContractEnum.VRF.getType(), bcos3sdkconfig.getContractVersion().getVrfCoreVersion());
            if (!deployOptional.isPresent()) {
                Assertions.fail();
                  return;
            }


            this.vrfCoreAddress = deployOptional.get().getContractAddress();


            log.info("vrf core address " + vrfCoreAddress);


             // asset
            byte[] keyHashByte = BaseTest.calculateTheHashOfPK(samplePubKey);
            RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(client, sampleKeyPair,vrfCoreAddress, keyHashByte);
            String randomConsumerAddress = randomNumberConsumer.getContractAddress();
            log.info("Deploy RandomNumberSampleVRF contract:[{}]", randomConsumerAddress);
            int seed = (int) (Math.random() *100);
            log.info("userseed {}",seed);
            TransactionReceipt randomT = randomNumberConsumer.getRandomNumber(BigInteger.valueOf(seed));
            log.info("randomNumberConsumer.getRandomNumbe status:",randomT.getStatus());
            log.info("RandomNumberSampleVRF reqId: " + randomT.getOutput());

            Thread.sleep(5000);

            BigInteger random = randomNumberConsumer.randomResult();
            log.info("Random:[{}]", random);

            Assertions.assertTrue(random.compareTo(BigInteger.ZERO) != 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }


}