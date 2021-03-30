package com.webank.oracle.test.transaction.VRF;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.webank.oracle.base.enums.ContractTypeEnum;
import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.base.utils.CommonUtils;
import com.webank.oracle.base.utils.CredentialUtils;
import com.webank.oracle.base.utils.CryptoUtil;
import com.webank.oracle.contract.ContractDeploy;
import com.webank.oracle.event.service.AbstractCoreService;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.transaction.vrf.LibVRFK1;
import com.webank.oracle.transaction.vrf.VRFCore;
import com.webank.oracle.trial.contract.RandomNumberSampleVRF;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class RandomNumberConsumerTest extends BaseTest {

    @Test
    public void testVRFWithDeployManually() throws Exception {

        credentials = Credentials.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");

        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();

        log.info("deploy vrf core");
        Web3j web3j = getWeb3j(chainId, groupId);

        VRFCore vrfCore = VRFCore.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, BigInteger.valueOf(chainId), BigInteger.valueOf(groupId)).send();
        log.info("vrf core address : " + vrfCore.getContractAddress());

        byte[] keyHashByte = calculateTheHashOfPK("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
        log.info("deploy consumer  contract");

        List<BigInteger> ilist = CredentialUtils.calculateThePK("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");


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

        String proof = LibVRFK1.InstanceHolder.getInstance().prove(credentials.getEcKeyPair().getPrivateKey().toString(16), actualSeed);
        log.info("Generate proof :" + proof);

        Thread.sleep(10);
        byte[] proofbyte = ByteUtil.hexStringToBytes(proof);

        log.info("vrf core fulfill the request .........");
        TransactionReceipt tt = (TransactionReceipt) vrfCore.fulfillRandomnessRequest(ilist, proofbyte, preseed, blockNumber).send();
        AbstractCoreService.dealWithReceipt(tt);
        log.info(tt.getStatus());
        log.info(tt.getOutput());

        VRFCore.RandomnessRequestFulfilledEventResponse res = vrfCore.getRandomnessRequestFulfilledEvents(tt).get(0);
        log.info("ramdom result: " + res.output);
        log.info("requestId: " + CommonUtils.bytesToHex(res.requestId));

        log.info(" consumer query the ramdom result");
        BigInteger ram = randomNumberConsumer.randomResult().send();
        log.info(" ram: " + ram);
        //  log.info(DecodeOutputUtils.decodeOutputReturnString0x16(t.getOutput()));
    }

    @Test
    public void testVRFWithAutoDeploy() {

        try {
            credentials = this.keyStoreService.getCredentials();

            EventRegister eventRegister = eventRegisterProperties.getEventRegisters().get(0);

            int chainId = eventRegister.getChainId();
            int groupId = eventRegister.getGroup();
            Web3j web3j = getWeb3j(chainId, groupId);

            Optional<ContractDeploy> deployOptional =
                    this.contractDeployRepository.findByChainIdAndGroupIdAndContractTypeAndVersion( chainId, groupId,
                    ContractTypeEnum.VRF.getId(), this.contractVersion.getVrfCoreVersion() );
            if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String vrfCoreAddress = deployOptional.get().getContractAddress();
            log.info("vrf core address " + vrfCoreAddress);

            // asset
            byte[] keyHashByte = calculateTheHashOfPK(credentials.getEcKeyPair().getPrivateKey().toString(16));
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