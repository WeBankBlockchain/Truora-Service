package com.webank.truora.test.bcos2.multi;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.contract.bcos2.APISampleOracle;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.test.base.BaseTest;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

                EventRegisterConfig eventRegister = eventRegisterProperties.getEventRegisters().get(i);
                String chainId = eventRegister.getChainId();
                String groupId = eventRegister.getGroupId();
                Web3j web3j = getWeb3j(chainId, groupId);

                Optional<DBContractDeploy> deployOptional =
                        this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                                "fiscobcos",
                                chainId, groupId, ContractEnum.ORACLE_CORE.getType(), this.contractVersion.getOracleCoreVersion() );
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