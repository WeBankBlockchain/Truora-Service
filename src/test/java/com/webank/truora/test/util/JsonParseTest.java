package com.webank.truora.test.util;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.contract.bcos2.APISampleOracle;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 */

@Slf4j
public class JsonParseTest extends BaseTest {

    public static final String[] URL_ARRAY = new String[]{
         "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)",
         "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)[5]",
         "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY",
         "json(https://api.kraken.com/0/public/Ticker?pair=ETHXBT).result.XETHXXBT.c[0]",
         "json(https://devapi.qweather.com/v7/weather/3d?location=101280601&key=90d8a8ee98ff495694dce72e96f53a18).daily[1].tempMax"
    };

    @Test
    public void testApiConsumer() {
        credentials = GenCredential.create();

            EventRegisterConfig eventRegister = eventRegisterProperties.getEventRegisters().get(0);

            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();
            Web3j web3j = getWeb3j(chainId, groupId);

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos",
                            chainId, groupId,
                            ContractEnum.VRF.getType(), this.contractVersion.getVrfCoreVersion() );
        if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String oracleCoreAddress = deployOptional.get().getContractAddress();
            log.info("oracle core address " + oracleCoreAddress);

            try {
                APISampleOracle apiConsumer = APISampleOracle.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCoreAddress).send();

                // asset
                for (String url : URL_ARRAY) {
                    TransactionReceipt t = apiConsumer.setUrl(url).send();

                    String apiConsumerAddress = apiConsumer.getContractAddress();
                    log.info("Deploy APIConsumer contract:[{}]", apiConsumerAddress);

                    TransactionReceipt t1 = apiConsumer.request().send();
                    log.info("Generate api receipt: [{}:{}]", t1.getStatus(), t1.getOutput());

                    Thread.sleep(3000);

                    BigInteger result = apiConsumer.result().send();
                    log.info("httpresult:[{}]", result);

                    Assertions.assertTrue(result.compareTo(BigInteger.ZERO) != 0);
                    Assertions.assertNull(reqHistoryRepository.findByReqId(t1.getOutput()).get().getError());
                    }


                String wrongUrl= "json(https://api.kraken.com/0/public/Ticker?pair=ETHXBT).result.XETHXXBT.c.0";
                apiConsumer.setUrl(wrongUrl).send();
                TransactionReceipt transactionReceipt = apiConsumer.request().send();
                Thread.sleep(3000);
                assertNotNull(reqHistoryRepository.findByReqId(transactionReceipt.getOutput()).get().getError());

                } catch (Exception e) {
                e.printStackTrace();
                Assertions.fail();
            }

    }



}

