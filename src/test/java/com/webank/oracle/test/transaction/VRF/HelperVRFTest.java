package com.webank.oracle.test.transaction.VRF;

import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.test.temp.TestHelperVRF;
import com.webank.oracle.transaction.vrf.LibVRFK1;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HelperVRFTest extends BaseTest {

    @Test
    public void testVRFK1() throws Exception {

        credentials = GenCredential.create();

        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(), 1);
        //  System.out.println(Credentials.create("1").getAddress());
        //fist  secretRegistty
        TestHelperVRF vrf = TestHelperVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
        log.info("address: " + vrf.getContractAddress());
        String pi = "0x03359425334b14173856433b4e695f1d19c7c0cb4eb9b5c72b0b00afe170ce7fd738334a976a8be4582b05a480cdecf8a4f4dd9d0694ae1dcb384429a1c99082bdb2845e2c3010054071489f41fc4b65a5";


        String message = "0x73616d706c65";
        byte[] encodedData = ByteUtil.hexStringToBytes(message);
        credentials = GenCredential.create(new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494335").toString(16));
        String pk = credentials.getEcKeyPair().getPublicKey().toString(16);
        int len = pk.length();
        String pkx = pk.substring(0, len / 2);
        String pky = pk.substring(len / 2);
        BigInteger Bx = new BigInteger(pkx, 16);
        BigInteger By = new BigInteger(pky, 16);
        List publist = new ArrayList();
        publist.add(Bx);
        publist.add(By);
        TransactionReceipt t=  vrf.decodeProof(ByteUtil.hexStringToBytes(pi)).send();
        List prooflist = vrf.getDecodeProofOutput(t).getValue1();

        TransactionReceipt result = (TransactionReceipt) vrf.verify(publist, prooflist, encodedData).send();
        System.out.println(result.getOutput());

    }


    @Test
    public void testVRFK1WithJNA() throws Exception {

        credentials = GenCredential.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");

        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(), 1);
        TestHelperVRF vrf = TestHelperVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
        log.info("address: " + vrf.getContractAddress());

//        String sk= new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494335").toString(16);
        String message= "1A8D4805836F6F6C922335173CF93076850F391DED0B519D80776AE232309B9D";

        String result =  LibVRFK1.InstanceHolder.getInstance().prove(credentials.getEcKeyPair().getPrivateKey().toString(16),message);
     //   String pi = "0x03359425334b14173856433b4e695f1d19c7c0cb4eb9b5c72b0b00afe170ce7fd738334a976a8be4582b05a480cdecf8a4f4dd9d0694ae1dcb384429a1c99082bdb2845e2c3010054071489f41fc4b65a5";
        String pi = "0294b97694dedccc562d885b4a612240ff148bb0625d8810f22d2e797b780f8dc4b67b40c27055a26ee610ec3115ec87d79a0c3b3a9a62cc8f92f3acd62be62df3888c783328aabd41455f88fd99f5df29";

        log.info(result);

       // byte[] encodedData = ByteUtil.hexStringToBytes(message);
        byte[] encodedData = message.getBytes();

        log.info("****" +bytesToHex(encodedData));
        String pk = credentials.getEcKeyPair().getPublicKey().toString(16);
        int len = pk.length();
        String pkx = pk.substring(0, len / 2);
        String pky = pk.substring(len / 2);
        BigInteger Bx = new BigInteger(pkx, 16);
        BigInteger By = new BigInteger(pky, 16);
        List publist = new ArrayList();
        publist.add(Bx);
        publist.add(By);
        TransactionReceipt t=  vrf.decodeProof(ByteUtil.hexStringToBytes(pi)).send();
        List prooflist = vrf.getDecodeProofOutput(t).getValue1();

        TransactionReceipt t1 = (TransactionReceipt) vrf.verify(publist, prooflist, encodedData).send();
        log.info(t1.getOutput());

    }
}
