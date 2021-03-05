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

        credentials = GenCredential.create();

        Web3j web3j = getWeb3j(eventRegisterProperties.getEventRegisters().get(0).getChainId(), 1);
        //  System.out.println(Credentials.create("1").getAddress());
        //fist  secretRegistty
        TestHelperVRF vrf = TestHelperVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER).send();
        log.info("address: " + vrf.getContractAddress());

        String sk= new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494335").toString(16);
        String message= "sample";
        String result =  LibVRFK1.InstanceHolder.getInstance().prove(sk,message);
        String pi = "0x03359425334b14173856433b4e695f1d19c7c0cb4eb9b5c72b0b00afe170ce7fd738334a976a8be4582b05a480cdecf8a4f4dd9d0694ae1dcb384429a1c99082bdb2845e2c3010054071489f41fc4b65a5";
        System.out.println(result);

       // byte[] encodedData = ByteUtil.hexStringToBytes(message);
        byte[] encodedData = message.getBytes();

        System.out.println("****" +bytesToHex(encodedData));
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
        TransactionReceipt t=  vrf.decodeProof(ByteUtil.hexStringToBytes(result)).send();
        List prooflist = vrf.getDecodeProofOutput(t).getValue1();

        TransactionReceipt t1 = (TransactionReceipt) vrf.verify(publist, prooflist, encodedData).send();
        System.out.println(t1.getOutput());

    }
}
