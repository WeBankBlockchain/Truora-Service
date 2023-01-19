package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.contract.bcos3.simplevrf.RandomNumberSampleVRF;
import com.webank.truora.test.LocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.vrf.Curve25519VRF;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFInterface;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.web3j.crypto.Hash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Slf4j
public class VRF25519WorkerTest extends LocalTestBase {
    @Autowired
    Bcos3ClientConfig bcos3sdkconfig;

    @Autowired
    Bcos3EventRegisterFactory registerFactory;

    String groupId = "group0";
    String chainId = "chain0";

    VRFInterface vrfInterface = new Curve25519VRF();
    String vrfPrivkey = "";//""945dbf86c04f14363f13e2bd2c4ad1fdfab04c38f3f1b07953c0b1e35ae76fab";
    @Test
    public void testVRF25519Worker() throws Exception {
        log.info("deploy consumer  contract");
        Bcos3EventRegister register = registerFactory.get(chainId,groupId);
        Client client  = register.getBcos3client();


        this.vrfPrivkey = register.getKeyPair().getHexPrivateKey();

        String vrfPublicKey = vrfInterface.getPublicKeyFromPrivateKey(vrfPrivkey);
        log.info("TEST using privkey: {}",vrfPrivkey);
        log.info("VRFPublickey : {}",vrfPublicKey);
        byte[] keyHashByte = Hash.sha3(Hex.decodeHex(vrfPublicKey));
        String vrfCoreAddress  =register.getConfig().getVrf25519CoreAddress();
        log.info("vrf core address " + vrfCoreAddress);
        //--------------------------ready to go------------------------------------

        RandomNumberSampleVRF randomNumberConsumer = RandomNumberSampleVRF.deploy(client,register.getKeyPair(),
                vrfCoreAddress, keyHashByte);
        log.info("consumer address: " + randomNumberConsumer.getContractAddress());
        log.info("consumer start a query ....... ");
        int seed = (int) (Math.random() *100);
        TransactionReceipt randomT = randomNumberConsumer.requestRandomNumber(BigInteger.valueOf(seed));
        log.info("randomNumberConsumer.getRandomNumber status: {}",randomT.getStatus());
        AbstractContractWorker.dealWithReceipt(randomT);
        log.info("consumer query reqId: " + randomT.getOutput());
        log.info("randomNumberConsumer.getRandomNumbe status:",randomT.getStatus());
        log.info("RandomNumberSampleVRF reqId: " + randomT.getOutput());

        Thread.sleep(5000);
        BigInteger random = randomNumberConsumer.get();
        log.info("Random:[{}]", random);
        Assertions.assertTrue(random.compareTo(BigInteger.ZERO) != 0);


    }

}
