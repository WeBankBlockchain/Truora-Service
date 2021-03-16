package com.webank.oracle.test.transaction.multi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.webank.oracle.base.enums.ContractTypeEnum;
import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.contract.ContractDeploy;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.trial.contract.APISampleOracle;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
public class MultiChainTest extends BaseTest {

    @Ignore
    @Test
    public void testMultipleGroupApiConsumer() {
        credentials = GenCredential.create();

        List<String> requestIds = new ArrayList<>();
        List<String> contractAdds = new ArrayList<>();
        for (int i = 0; i < eventRegisterProperties.getEventRegisters().size(); i++) {
            try {

                EventRegister eventRegister = eventRegisterProperties.getEventRegisters().get(i);
                int chainId = eventRegister.getChainId();
                int groupId = eventRegister.getGroup();
                Web3j web3j = getWeb3j(chainId, groupId);

                Optional<ContractDeploy> deployOptional =
                        this.contractDeployRepository.findByChainIdAndGroupIdAndContractTypeAndVersion(
                                chainId, groupId, ContractTypeEnum.ORACLE_CORE.getId(), this.contractVersion.getOracleCoreVersion() );
                if (!deployOptional.isPresent()) {
                    Assertions.fail();
                    return;
                }
                String oracleCoreAddress = deployOptional.get().getContractAddress();
                log.info("oracle core address " + oracleCoreAddress);
                contractAdds.add(oracleCoreAddress);
                // asset
                APISampleOracle apiConsumer = APISampleOracle.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCoreAddress).send();
                String apiConsumerAddress = apiConsumer.getContractAddress();
                log.info("Deploy APIConsumer contract:[{}]", apiConsumerAddress);

                TransactionReceipt t1 = apiConsumer.request().send();
                log.info("Generate random receipt: [{}:{}]", t1.getStatus(), t1.getOutput());
                requestIds.add(t1.getOutput());
                Thread.sleep(5000);

                BigInteger random = apiConsumer.result().send();
                Assertions.assertTrue(random.compareTo(BigInteger.ZERO) != 0);
            } catch (Exception e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
        Assertions.assertEquals(contractAdds.get(0), contractAdds.get(1));
        log.info("group1 request id: {}", requestIds.get(0));
        log.info("group2 request id: {}", requestIds.get(1));
        Assertions.assertNotEquals(requestIds.get(0),requestIds.get(1));
    }
}