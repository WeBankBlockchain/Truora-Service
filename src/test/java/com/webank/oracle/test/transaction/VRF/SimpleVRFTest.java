//package com.webank.oracle.test.transaction.VRF;
//
//import com.webank.oracle.base.properties.ConstantProperties;
//import com.webank.oracle.base.utils.CryptoUtil;
//import com.webank.oracle.test.base.BaseTest;
//import com.webank.oracle.transaction.vrf.VRFCore;
//import com.webank.oracle.transaction.vrf.LibVRFK1;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.fisco.bcos.web3j.abi.datatypes.Address;
//import org.fisco.bcos.web3j.crypto.Credentials;
//import org.fisco.bcos.web3j.protocol.Web3j;
//import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
//import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
//import org.fisco.bcos.web3j.utils.ByteUtil;
//import org.fisco.bcos.web3j.utils.Numeric;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.webank.oracle.event.service.AbstractCoreService.dealWithReceipt;
//
//@Slf4j
//public class SimpleVRFTest extends BaseTest {
//
//
//    @Test
//    public void testVRFRandomNumberConsumer() throws Exception {
//
//        credentials = Credentials.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
//
//        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
//        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
//
//        log.info("deploy vrf core");
//        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(), 1);
//
//        VRFCore vrfCore = VRFCore.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, BigInteger.valueOf(chainId), BigInteger.valueOf(groupId)).send();
//        log.info("vrf core address : " + vrfCore.getContractAddress());
//
//        byte[] keyhashbyte = calculateTheHashOfPK("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
//        log.info("deploy consumer  contract");
//
//        List ilist = calculateThePK("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
//
//        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, vrfCore.getContractAddress(), keyhashbyte).send();
//        log.info("consumer address: " + randomNumberConsumer.getContractAddress());
//
//        log.info("consumer start a query ....... ");
//        TransactionReceipt randomT = randomNumberConsumer.getRandomNumber(new BigInteger("1")).send();
//        log.info(randomT.getStatus());
//        //  log.info(randomT.getOutput());
//        log.info("consumer query reqId: " + randomT.getOutput());
//
//        log.info("core listen to the event .........");
//        VRFCore.RandomnessRequestEventResponse randomevent = vrfCore.getRandomnessRequestEvents(randomT).get(0);
//
//        log.info("hash:" + bytesToHex(randomevent.keyHash));
//        log.info("preseed " + randomevent.seed);
//        log.info("blocknumber: " + randomevent.blockNumber);
//
//        log.info("sender: " + randomevent.sender);
//        log.info("requestId: " + bytesToHex(randomevent.requestId));
//        log.info("seedAndBlock:        " + bytesToHex(randomevent.seedAndBlockNum));
//
//        log.info("vrf core generate the proof .........");
//        BigInteger preseed = randomevent.seed;
//        BigInteger blockNumber = randomevent.blockNumber;
//        String blockhash = web3j.getBlockHashByNumber(DefaultBlockParameter.valueOf(blockNumber)).send().getBlockHashByNumber();
//        log.info("blockhash : {}", blockhash);
//        byte[] bnbytes = Numeric.toBytesPadded(blockNumber, 32);
//
//        String actualSeed = bytesToHex(CryptoUtil.soliditySha3(randomevent.seed, CryptoUtil.solidityBytes(hexStringtoBytes(blockhash.substring(2)))));
//        String actualSeedonchain = bytesToHex(randomevent.seedAndBlockNum);
//        log.info("actualseed: {} ", actualSeed);
//        log.info("actualSeedonchain: {} ", actualSeedonchain);
//
//        String proof = LibVRFK1.InstanceHolder.getInstance().prove(credentials.getEcKeyPair().getPrivateKey().toString(16), actualSeed);
//        log.info("Generate proof :" + proof);
//
//        Thread.sleep(10);
//        byte[] proofbyte = ByteUtil.hexStringToBytes(proof);
//
//        log.info("vrf core fulfill the request .........");
//        TransactionReceipt tt = (TransactionReceipt) vrfCore.fulfillRandomnessRequest(ilist, proofbyte, preseed, blockNumber).send();
//        dealWithReceipt(tt);
//        log.info(tt.getStatus());
//        log.info(tt.getOutput());
//
//        VRFCore.RandomnessRequestFulfilledEventResponse res = vrfCore.getRandomnessRequestFulfilledEvents(tt).get(0);
//        log.info("ramdom result: " + res.output);
//        log.info("requestId: " + bytesToHex(res.requestId));
//
//        log.info(" consumer query the ramdom result");
//        BigInteger ram = randomNumberConsumer.randomResult().send();
//        log.info(" ram: " + ram);
//        //  log.info(DecodeOutputUtils.decodeOutputReturnString0x16(t.getOutput()));
//    }
//
//
//    @Test
//    public void testSha() {
//        String keyhash = "6c3699283bda56ad74f6b855546325b68d482e983852a7a82979cc4807b641f4";
//        log.info("credential: " + credentialsBob.getEcKeyPair().getPublicKey().toString());
//
//        String requestId = "0x3B8588274FD75969C29B0DB8C63D7E5716DFDEA31F8FDF229E041896C5FAF745";
//        String keyhash1 = "0x3B8588274FD75969C29B0DB8C63D7E5716DFDEA31F8FDF229E041896C5FAF745";
//        String vrfInput = "";
//        byte[] vrfseed = CryptoUtil.soliditySha3(hexStringtoBytes(keyhash), new BigInteger("1"), new Address("0xe5fd2eb6f001ea1d2675103f3047563bb4c0ab48"), new BigInteger("0"));
//        log.info("vrf seed :{}", new BigInteger(bytesToHex(vrfseed), 16));
//    }
//
//    public static String bytesToHex(byte[] bytes) {
//        final char[] hexArray = "0123456789ABCDEF".toCharArray();
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        String finalHex = new String(hexChars);
//        return finalHex;
//    }
//
//    public byte[] hexStringtoBytes(String s) {
//        byte[] val = new byte[s.length() / 2];
//        for (int i = 0; i < val.length; i++) {
//            int index = i * 2;
//            int j = Integer.parseInt(s.substring(index, index + 2), 16);
//            val[i] = (byte) j;
//        }
//        return val;
//    }
//
//
//    public static byte[] calculateTheHashOfPK(String skhex) {
//        List ilist = calculateThePK(skhex);
//
//        return CryptoUtil.soliditySha3(ilist.get(0), ilist.get(1));
//    }
//
//
//}
