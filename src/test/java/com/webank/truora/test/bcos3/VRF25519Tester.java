package com.webank.truora.test.bcos3;

import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.base.utils.CryptoUtil;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.contract.bcos3.TestVRF25519;
import com.webank.truora.contract.bcos3.simplevrf.RandomNumberSampleVRF;
import com.webank.truora.contract.bcos3.simplevrf.VRF25519Core;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.crypto.vrf.Curve25519VRF;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFInterface;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFKeyPair;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.net.URL;
import java.util.List;

@Slf4j
public class VRF25519Tester {

    BcosSDK sdk;

    Client client;
    String sampleKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
    String samplePubKey = "";
    CryptoKeyPair sampleKeyPair;
    String chainId="chain0";
    String groupId="group0";
    String vrfCoreAddress;

    ContractCodec codec;
    public void init() {
        try {
            String configFileName = "bcos3sdk_config.toml";
            URL configUrl = Bcos3Client.class.getClassLoader().getResource(configFileName);
            log.info("The configFile is: " + configUrl);
            if (configUrl == null) {
                System.out.println("The configFile " + configFileName + " doesn't exist!");
                return;
            }
            String groupId = "group0";
            String chainId = "chain0";
            String configFile = configUrl.getPath();
            System.out.println("configFile : " + configFile);
            sdk = BcosSDK.build(configFile);
            client = sdk.getClient(groupId);
            String sampleKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
            //sampleKeyPair = Bcos3KeyTools.getKeyPairByHexkey(client.getCryptoSuite(),sampleKey);
            sampleKeyPair = client.getCryptoSuite().getCryptoKeyPair();
            this.samplePubKey = sampleKeyPair.getHexPublicKey();
            codec = new ContractCodec(client.getCryptoSuite(),false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChain() throws Exception{
        init();
        BlockNumber number = client.getBlockNumber();
        log.info("blocknumber is {}",number.getBlockNumber());
    }

    @Test void test25519local() throws Exception{
        String message = "3EFE057156D33244CF7370D2062BA667BBED2276CA666CB83623ED05CFC5DFC0";//"vrf-test";
        VRFInterface vrfInterface = new Curve25519VRF();
        VRFKeyPair vrfKeyPair = vrfInterface.createKeyPair();
        String byteMessage = new String(Hex.decodeHex(message),"UTF-16BE" );
        String hexcheck = Hex.encodeHexString(byteMessage.getBytes());
        log.info("hexcheck {}",hexcheck);
        String vrfProof =
                vrfInterface.generateVRFProof(
                        vrfKeyPair.getVrfPrivateKey(), byteMessage);

        log.info("message: {}", byteMessage);
        log.info("publickey: {}",vrfKeyPair.getVrfPublicKey());
        log.info("proof: {}",vrfProof);


        boolean res = vrfInterface.verify(vrfKeyPair.getVrfPublicKey(),byteMessage,vrfProof);
        log.info("vrf verify result {}",res);

    }

    @Test void test25519onchain() throws Exception{
        init();
        CryptoKeyPair clientKeypair = client.getCryptoSuite().getCryptoKeyPair();
        String privatekey = clientKeypair.getHexPrivateKey();
        log.info("sdk public key is {}",clientKeypair.getHexPublicKey());

        String message = "09B2BF6EB5EF35AE0B9A7F06A7BFC0EFB86BE57C8116469E5DA338C56EB7CA0E";// "vrf-test";
        VRFInterface vrfInterface = new Curve25519VRF();
        String vrfpublickey = vrfInterface.getPublicKeyFromPrivateKey(privatekey);
        String vrfProof =
                vrfInterface.generateVRFProof(
                        privatekey, message);

        log.info("message: {}", Hex.encodeHexString(message.getBytes()));
        log.info("publickey: {}",vrfpublickey);
        log.info("proof: {}",vrfProof);

        TestVRF25519 tester = TestVRF25519.load("0xf222a6d35cd04a520f5a18f97e2168b6bfbe0432",client,clientKeypair);

        Tuple2<Boolean, BigInteger> result =  tester.verify(
                message.getBytes(),
                Hex.decodeHex(vrfpublickey),
                Hex.decodeHex(vrfProof)
        );
        log.info("result :{}",result.getValue1());
        log.info("randomValue: {}",result.getValue2());

    }




    @Test
    public void testVRFWithDeployManually() throws Exception {


        init();
        log.info("deploy vrf core");

        //VRFCore vrfCore = VRFK1Core.deploy(client,sampleKeyPair,chainId,groupId);
        VRF25519Core vrfCore = VRF25519Core.deploy(client,client.getCryptoSuite().getCryptoKeyPair(),chainId,groupId);
        log.info("vrf core address : " + vrfCore.getContractAddress());
        this.vrfCoreAddress = vrfCore.getContractAddress();
        VRFInterface vrfInterface = new Curve25519VRF();
        samplePubKey = vrfInterface.getPublicKeyFromPrivateKey(sampleKeyPair.getHexPrivateKey());
        byte[] keyHashByte = client.getCryptoSuite().hash(Hex.decodeHex(samplePubKey));
        log.info("keyHashByte hex is: {}", Hex.encodeHexString(keyHashByte));
        log.info("deploy consumer  contract");
        //List<BigInteger> ilist = CredentialUtils.calculatFromPubkey(samplePubKey);
        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(client,sampleKeyPair,
                vrfCore.getContractAddress(), keyHashByte);
        log.info("consumer address: " + randomNumberConsumer.getContractAddress());

        log.info("consumer start a query ....... ");
        TransactionReceipt randomT = randomNumberConsumer.requestRandomNumber(new BigInteger("1"));
        log.info("randomNumberConsumer.getRandomNumber status: {}",randomT.getStatus());
        AbstractContractWorker.dealWithReceipt(randomT);
        log.info("consumer query reqId: " + randomT.getOutput());


        log.info("core listen to the event .........");
        VRF25519Core.RandomnessRequestEventResponse randomevent = vrfCore.getRandomnessRequestEvents(randomT).get(0);

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
        String actualSeed = CommonUtils.bytesToHex(CryptoUtil.solidityCommonHash(client.getCryptoSuite().getHashImpl(),
                seed,blockhash_trim));
        log.info("actualseed: {} ", actualSeed);
        try {
            String proof ="";

            String resstr =
                    vrfInterface.generateVRFProof(
                            sampleKeyPair.getHexPrivateKey(), actualSeed);

            log.info("proof from vrfk1 {}",resstr);
            proof = resstr;
            log.info("Generate proof len:{}:{}" ,proof.length(), proof);
            log.info("pubkey:{}",samplePubKey);



            boolean  verifyres = vrfInterface.verify(samplePubKey,actualSeed,proof);
            log.info("verify result {}",verifyres);



            Thread.sleep(10);
            byte[] proofbyte = ByteUtil.hexStringToBytes(proof);
            log.info("Generate proof bytes len:{}" ,proofbyte.length);


            log.info("vrf core fulfill blockNumber:{},proofbyte:{},preseed:{}",blockNumber,
                    Hex.encodeHexString(proofbyte),
                    preseed
            );

            byte[] blockhash_bin_no0x = Hex.decodeHex(blockhash.substring(2));

            // 结果上链
            TransactionReceipt tt =  vrfCore.fulfillRandomnessRequest(Hex.decodeHex(samplePubKey), proofbyte,
                    preseed,blockNumber);
            log.info("fulfill transaction on block:{},txhash: {}",tt.getBlockNumber(),tt.getTransactionHash());


            if (tt.getStatus()!=0){
                AbstractContractWorker.dealWithReceipt(tt);
                return;
            }

            List<TransactionReceipt.Logs> logs = tt.getLogEntries() ;

            for(int i=0;i<logs.size();i++){
                TransactionReceipt.Logs eventlog = logs.get(i);
                String abi = VRF25519Core.getABI();
                EventLog elog = eventlog.toEventLog();
                String topic = eventlog.getTopics().get(0);
                try {
                    List<String> decodeRes = codec.decodeEventToString(abi, "doVerify", elog);
                    log.info("{} )Event -----------",i);
                    String temps ="";
                    for (String s : decodeRes) {
                        temps = temps+s+"\n";
                    }
                    log.info(temps);
                    log.info("actualSeed {}",actualSeed);
                    log.info("pubkey : {}",samplePubKey);
                    log.info("proof: {}",proof);

                    boolean res = vrfInterface.verify(samplePubKey,actualSeed,proof);
                    log.info("after tx,verify res is: {}",res);

                }catch(Exception e){
                    log.info("parselog skip {}",i);
                }
            }


            log.info("fulfillRandomnessRequest status: {}",tt.getStatus());
            log.info("fulfillRandomnessRequest output: {}",tt.getOutput());

            VRF25519Core.RandomnessRequestFulfilledEventResponse res = vrfCore.getRandomnessRequestFulfilledEvents(tt).get(0);
            log.info("ramdom result: " + res.output);
            log.info("requestId: " + CommonUtils.bytesToHex(res.requestId));

            log.info(" consumer query the ramdom result");
            BigInteger ram = randomNumberConsumer.get();
            log.info("randomResult from chain: " + ram);
            log.info("-----------------VRF end---------------------------");

        } catch (Exception e) {
            log.error("TEST VRF exception {}", e);
        }
    }

}
