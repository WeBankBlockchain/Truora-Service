package com.webank.truora.test.bcos3;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3KeyTools;
import com.webank.truora.bcos3runner.Bcos3SdkFactory;
import com.webank.truora.contract.bcos3.APISampleOracle;
import com.webank.truora.contract.bcos3.LotteryOracle;
import com.webank.truora.crawler.BaseUrl;
import com.webank.truora.crawler.LocalRandomCrawler;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.database.DBContractDeployRepository;
import com.webank.truora.test.base.BaseTest;
import com.webank.truora.vrfutils.VRFUtilsConfig;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.webank.truora.bcos3runner.AbstractContractWorker.dealWithReceipt;


@Slf4j
public class LotteryTestV3 extends BaseTest {

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


    //String userPrivkey;
    //String vrfCoreAddress;
    //Bcos3EventRegister eventRegister;
    @Autowired
    DBContractDeployRepository contractDeployRepository;

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
    public void testLotteryOracle() throws Exception{
        init_test();
        try {

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos3",
                            chainId, groupId,
                        ContractEnum.ORACLE_CORE.getType(), this.contractVersion.getOracleCoreVersion() );

            if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String oracleCoreAddress = deployOptional.get().getContractAddress();
            log.info("oracle core address " + oracleCoreAddress);

            // asset
            APISampleOracle apiConsumer = APISampleOracle.deploy(client, keyPair, oracleCoreAddress);
            BaseUrl baseSource = new BaseUrl(LocalRandomCrawler.NAME,null);
            String url = baseSource.toJSONString();
            apiConsumer.setUrl(url);
            //apiConsumer.setUrl("plain(https://www.random.org/integers/?num=3&min=1&max=100&col=1&base=10&format=plain&rnd=new)");

            String apiConsumerAddress = apiConsumer.getContractAddress();
            log.info("Deploy APIConsumer contract:[{}]", apiConsumerAddress);

            LotteryOracle lotteryOracle = LotteryOracle.deploy(client, keyPair,  apiConsumerAddress);


            String[] array = {"0x2b5ad5c4795c026514f8317c7a215e218dccd6cf","0x2b5ad5c4795c026514f8317c7a215e218dccd6c1","0x2b5ad5c4795c026514f8317c7a215e218dccd6c2","0x2b5ad5c4795c026514f8317c7a215e218dccd6c3","0x2b5ad5c4795c026514f8317c7a215e218dccd6c4"};
            List<String> playerAddressList = Arrays.asList(array);

            TransactionReceipt t1 = lotteryOracle.start_new_lottery(playerAddressList);
            dealWithReceipt(t1);
            log.info( "status: {}" ,t1.getStatus());
            log.info( "output: {}" ,t1.getOutput());

            Thread.sleep(3000);

            TransactionReceipt randomTrans = lotteryOracle.pickWinner();
            dealWithReceipt(randomTrans);
           log.info( "status: {}" ,randomTrans.getStatus());

           log.info("winner: {}", lotteryOracle.getWinnerEvents(randomTrans).get(0).winner);
           log.info( "id: {}" ,lotteryOracle.getWinnerEvents(randomTrans).get(0).lotteryId);
           log.info( "randomness: {}", lotteryOracle.getWinnerEvents(randomTrans).get(0).ramdomness);
           log.info( "output: {}", randomTrans.getOutput());

           Assertions.assertTrue(randomTrans.getOutput().contains("2b5ad5c4795c026514f8317c7a215e218dccd6c"));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }


}
