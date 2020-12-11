package com.webank.oracle.test.transaction.VRF;

import static com.webank.oracle.event.service.AbstractCoreService.dealWithReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.Numeric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.utils.CryptoUtil;
import com.webank.oracle.base.utils.DecodeOutputUtils;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.transaction.vrf.LibVRF;
import com.webank.oracle.transaction.vrf.VRF;
import com.webank.oracle.transaction.vrf.VRFCoordinator;
import com.webank.oracle.trial.contract.RandomNumberSampleOracle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VRFTest extends BaseTest {

    @Test
    public void testVRF() throws Exception {

        credentials = GenCredential.create();

        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(),1);
      //  System.out.println(Credentials.create("1").getAddress());
        //fist  secretRegistty
       VRF vrf = VRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
        System.out.println("address: " + vrf.getContractAddress());
       String s ="0xb015f8044f5fcbdcf21ca26d6c34fb8197829205c7b7d2a7cb66418c157b112cab8c1e086d04e813744a655b2df8d5f83b3cdc6faa3088c1d3aea1454e3a1d5f0c7e532826ca771636f9430be430ff0905a97cdc5378aec06854e1bcc4cec42e65089603d0c65e5d84a295169b664ae2fff258220ca0a2c716ea05744792dee7ecf04e000751e54cee9372b7676bd5e3cdf38eb326ca556ebb241b672087a8f6594901f956764cfadbcc9712deda529710dd8195971f70a185a2d5f3124e498c0000000000000000000000000000000000000000000000000000000000c831f60000000000000000000000001cc0c65ca5dd6b767338946f2c44c02040744ef52bc36732922c89b38dc35f684e632c52174097c158d05a4dc28aa3dec591f4cb0cb6ec42358ee1b0f9d3c4a92eb74a5dffcabeccb7ed5d628ebbb57bb3876722ef94966c20b32ef87cbf1f50a767016dbbaa5cf234350738dfa9700ebe3fce19367866cf7241ee0ea1362578c01578ec2a9dcfbc0e96dbcf45945eda665ca9cc592cb7b588f4c2268024b8f1524768198d5e0904e45c9e5bb8e1718bb85b18f7";

        byte[] i= Numeric.hexStringToByteArray(s);
        TransactionReceipt t = vrf.testRandomValueFromVRFProof(i).send();

        log.info(t.getStatus());
        log.info(t.getOutput());
        VRF.TestRandomEventResponse log =  vrf.getTestRandomEvents(t).get(0);

        BigInteger result = vrf.randomValueFromVRFProof(i).send();
        Assertions.assertNotNull(result);

    }


    @Test
    public void testVRFCoordinator() throws Exception {

        credentials = GenCredential.create();

        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(),1);
        log.info(Credentials.create("1").getAddress());
        log.info(GenCredential.create("1").getAddress());
        VRFCoordinator vrfCoordinator = VRFCoordinator.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
      //  String s ="0x79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8220932710bb12fef86739686044ec7659904ab9992085b5f08eac3d80e610b7ec662bf88aca6461cecb8e49b221e087f5fb6a6cbcda0296ae99dc8c3b5cdb9d69b3c9054e20e8dd14e82c7d37de1b3a9aad5449854825d3cce6475d2148aced764c36fab1df1722eb17d382c821e4c550fd9984e5ac642fef16de8babbab726b00000000000000000000000000000000000000000000000000000000000000020000000000000000000000007e5f4552091a69125d5dfcb7b8c2659029395bdfaa9881ee471e42fc5327067e85bf914f4384aa36954c831dc48b96b6b38a7d07c38f5bfc7eccbb81f71ff0dfab91a09fa4725d4f26f60e7ff206c6383aad575b6cea027120ad41374b9720edf0e9bc8a8e09a9ffddda6d925054ae71dc6468ff4bcf24d770067e334e2c7f9722bbe09ea6031aa63be8311557296986dd4dccf03fbe9e7d30509914af77cffa148ee040c85ace282707527eb07f8db31ced4b0a";
        String s ="0xb015f8044f5fcbdcf21ca26d6c34fb8197829205c7b7d2a7cb66418c157b112cab8c1e086d04e813744a655b2df8d5f83b3cdc6faa3088c1d3aea1454e3a1d5f0c7e532826ca771636f9430be430ff0905a97cdc5378aec06854e1bcc4cec42e65089603d0c65e5d84a295169b664ae2fff258220ca0a2c716ea05744792dee7ecf04e000751e54cee9372b7676bd5e3cdf38eb326ca556ebb241b672087a8f6594901f956764cfadbcc9712deda529710dd8195971f70a185a2d5f3124e498c0000000000000000000000000000000000000000000000000000000000c831f60000000000000000000000001cc0c65ca5dd6b767338946f2c44c02040744ef52bc36732922c89b38dc35f684e632c52174097c158d05a4dc28aa3dec591f4cb0cb6ec42358ee1b0f9d3c4a92eb74a5dffcabeccb7ed5d628ebbb57bb3876722ef94966c20b32ef87cbf1f50a767016dbbaa5cf234350738dfa9700ebe3fce19367866cf7241ee0ea1362578c01578ec2a9dcfbc0e96dbcf45945eda665ca9cc592cb7b588f4c2268024b8f1524768198d5e0904e45c9e5bb8e1718bb85b18f7";
        String blockNumber = "0000000000000000000000000000000000000000000000000000000000000005";
        byte[] i= Numeric.hexStringToByteArray(s+blockNumber);
        TransactionReceipt t  = vrfCoordinator.fulfillRandomnessRequest(i).send();
        log.info("address:" + vrfCoordinator.getContractAddress());
        log.info(t.getStatus());
        log.info(t.getOutput());
        log.info(DecodeOutputUtils.decodeOutputReturnString0x16(t.getOutput()));
    }

    @Test
    public void testCalculateTheKeyHash() throws Exception {
        credentials = GenCredential.create();
        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(),1);
        Credentials user = Credentials.create("2");
        log.info(user.getAddress());
        log.info("pk : {} ",user.getEcKeyPair().getPublicKey());
        String pk = user.getEcKeyPair().getPublicKey().toString(16);
        log.info(pk);
        int len = pk.length();
        String pkx = pk.substring(0,len/2);
        String pky = pk.substring(len/2);
        BigInteger Bx = new BigInteger(pkx,16);
        BigInteger By = new BigInteger(pky,16);
        log.info("*** "+ Bx);
        log.info("*** " +By);
        log.info("offline: " +bytesToHex(CryptoUtil.soliditySha3(Bx,By)));

        VRFCoordinator vrfCoordinator = VRFCoordinator.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();

         List ilist = new ArrayList<BigInteger>();
         ilist.add(Bx);
         ilist.add(By);

        TransactionReceipt r1 = (TransactionReceipt) vrfCoordinator.hashOfKey(ilist).send();
        log.info("online hash of key: "+ r1.getOutput());
    }


    @Test
    public void testRandomNumberConsumer() throws Exception {

         credentials = GenCredential.create();

        log.info("deploy vrf coordinator");
        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(),1);

        VRFCoordinator vrfCoordinator = VRFCoordinator.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
      // interface VRFCoordinator vrfCoordinator = VRFCoordinator.load("0xcfdaa4a02061d0ccb4357dcf1a607b8fac9b57c0",web3j, credentials, contractGasProvider);
        log.info("coordinate address : " + vrfCoordinator.getContractAddress());
//        String keyhash = "1398E8BAD6043BA497B9679E148947262EC5E21739FE3A931C29E8B84EE34A0F";
//        byte[] keyhashbyte = hexStringtoBytes(keyhash);
        byte[] keyhashbyte  = calculateTheHashOfPK("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
        log.info("deploy consumer  contract");

        RandomNumberSampleOracle randomNumberConsumer = RandomNumberSampleOracle.deploy(web3j,credentials,ConstantProperties.GAS_PROVIDER,vrfCoordinator.getContractAddress(),keyhashbyte).send();
      // RandomNumberConsumer randomNumberConsumer = RandomNumberConsumer.load("0x0382f73a5924aa2dbf51429421275a8541eb53c3", web3j,credentials,contractGasProvider);
        log.info("consumer address: " + randomNumberConsumer.getContractAddress() );


        log.info("consumer start a query ....... "  );
        TransactionReceipt randomT =  randomNumberConsumer.getRandomNumber(new BigInteger("1")).send();
        log.info(randomT.getStatus());
      //  log.info(randomT.getOutput());
        log.info("consumer query reqId: "+randomT.getOutput());

        log.info("coordinate listen to the event .........");
        VRFCoordinator.RandomnessRequestEventResponse randomevent =vrfCoordinator.getRandomnessRequestEvents(randomT).get(0);
       // vrfCoordinator.callbacks(randomevent.requestId);

        log.info("hash:" + bytesToHex(randomevent.keyHash));
        log.info("preseed " + randomevent.seed);
        log.info("blocknumber: " + randomevent.blockNumber);

        log.info("sender: "+ randomevent.sender);
        log.info("requestId: " +bytesToHex(randomevent.requestId));
        log.info("seedAndBlock:        " +bytesToHex(randomevent.seedAndBlockNum));

//        Tuple2<String, byte[]> callback =  vrfCoordinator.callbacks(randomevent.requestId).send();
//        log.info("callback add: "+ callback.getValue1());
//        log.info("callback seedandblock: "+ bytesToHex(callback.getValue2()));
//        byte[] seedAndBlock  = CryptoUtil.soliditySha3( randomevent.seed, randomevent.blockNumber);
//        log.info("seedAndBlockoffline: " + Numeric.toHexString(seedAndBlock));

//        byte[] vrfseed  = CryptoUtil.soliditySha3( randomevent.keyHash, new BigInteger("1"),new Address(randomNumberConsumer.getContractAddress()),new BigInteger("0"));
//        log.info("vrfseed: " + Numeric.toHexString(vrfseed));
//        byte[] requestId1 =  CryptoUtil.soliditySha3(randomevent.keyHash,new BigInteger(Numeric.toHexString(vrfseed).substring(2),16));
//        log.info("requestId1: "+ bytesToHex(requestId1));

        log.info("coordinate generate the proof .........");
        BigInteger preseed = randomevent.seed;
        BigInteger blockNumber = randomevent.blockNumber;
        byte[] bnbytes =Numeric.toBytesPadded(blockNumber, 32);
//        String blockHash = bytesToHex(CryptoUtil.soliditySha3(blockNumber));

//         String actualSeed = bytesToHex(CryptoUtil.soliditySha3(randomevent.seed, CryptoUtil.solidityBytes(blockNumber)));

     //   log.info("credential: " + credentialsBob.getEcKeyPair().getPrivateKey().toString());

        String proof =  LibVRF.InstanceHolder.getInstance().VRFProoFGenerate(credentials.getEcKeyPair().getPrivateKey().toString(16),preseed.toString(16));
        log.info("Generate proof :" + proof);

        Thread.sleep(10);
        byte[] i= Numeric.hexStringToByteArray(proof);
        byte[] destination = new byte[i.length + 32];
        System.arraycopy(i, 0, destination, 0, i.length);
        System.arraycopy(bnbytes, 0, destination, i.length, 32);


        log.info("coordinate fulfill the request .........");
        TransactionReceipt t  = vrfCoordinator.fulfillRandomnessRequest(destination).send();
        dealWithReceipt(t);
        log.info(t.getStatus());
        log.info(t.getOutput());


        VRFCoordinator.RandomnessRequestFulfilledEventResponse res = vrfCoordinator.getRandomnessRequestFulfilledEvents(t).get(0);

        log.info("ramdom result: " +res.output);
        log.info("requestId: " +bytesToHex(res.requestId));

        log.info(" consumer query the ramdom result");


        BigInteger ram = randomNumberConsumer.randomResult().send();
        log.info(" ram: "+  ram.toString(16));
     //   log.info(DecodeOutputUtils.decodeOutputReturnString0x16(t.getOutput()));
    }



    @Test
    public void testJNA() {

        String sk= "1a";
        String seed= "1b";
       String result =  LibVRF.InstanceHolder.getInstance().VRFProoFGenerate(sk,seed);
        log.info(result);
    }



    @Test
    public void testSha() {
        String keyhash = "6c3699283bda56ad74f6b855546325b68d482e983852a7a82979cc4807b641f4";
        log.info("credential: " + credentialsBob.getEcKeyPair().getPublicKey().toString());

         String requestId = "0x3B8588274FD75969C29B0DB8C63D7E5716DFDEA31F8FDF229E041896C5FAF745";
         String keyhash1 = "0x3B8588274FD75969C29B0DB8C63D7E5716DFDEA31F8FDF229E041896C5FAF745";
         String vrfInput = "";
         byte[] vrfseed  = CryptoUtil.soliditySha3( hexStringtoBytes(keyhash), new BigInteger("1"),new Address("0xe5fd2eb6f001ea1d2675103f3047563bb4c0ab48"),new BigInteger("0"));
        log.info("vrf seed :{}",new BigInteger(bytesToHex(vrfseed),16));
    }

    public static String bytesToHex(byte[] bytes)
    {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String finalHex = new String(hexChars);
        return finalHex;
    }

    public byte[]  hexStringtoBytes(String s) {
        byte[] val = new byte[s.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(s.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }


    public static byte[] calculateTheHashOfPK(String skhex) {
        Credentials user = Credentials.create(skhex);
        // gm address  0x1f609497612656e806512fb90972d720e2e508b5
        //   address   0xc950b511a1a6a1241fc53d5692fdcbed4f766c65
        String pk = user.getEcKeyPair().getPublicKey().toString(16);

        int len = pk.length();
        String pkx = pk.substring(0,len/2);
        String pky = pk.substring(len/2);
        BigInteger Bx = new BigInteger(pkx,16);
        BigInteger By = new BigInteger(pky,16);

        return CryptoUtil.soliditySha3(Bx,By);
    }

}
