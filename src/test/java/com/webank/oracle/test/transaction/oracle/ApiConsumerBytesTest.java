package com.webank.oracle.test.transaction.oracle;

import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.utils.CryptoUtil;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.test.temp.APISampleOracle;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

@Slf4j
public class ApiConsumerBytesTest extends BaseTest {


    @Test
    public void test() throws Exception {

        credentials = GenCredential.create();

        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId,groupId);


        APISampleOracle apiConsumer = APISampleOracle.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, "0xeafd7b4160cd6f6869542e87421831d63bca3b69").send();
        String apiConsumerAddress = apiConsumer.getContractAddress();
        log.info("apiConsumerAddress:{} ", apiConsumerAddress);

//       TransactionReceipt t =  apiConsumer.request().send();
//       log.info("**** {}" ,t.getStatus());
//       log.info("**** {}", t.getOutput());
      BigInteger result = new BigInteger("1590000000000000");
       byte[] i = CryptoUtil.toBytes(result);

      TransactionReceipt tt = apiConsumer.__callback( i, i).send();
        log.info("**** {}" ,tt.getStatus());
        log.info("**** {}", tt.getOutput());

        BigInteger integer = apiConsumer.get().send();
        log.info("{}", integer);


    }




}
