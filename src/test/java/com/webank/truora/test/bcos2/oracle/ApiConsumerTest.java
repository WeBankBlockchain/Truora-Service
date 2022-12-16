package com.webank.truora.test.bcos2.oracle;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.contract.bcos2.APISampleOracle;
import com.webank.truora.contract.bcos2.APISampleOracleReturnString;
import com.webank.truora.contract.bcos2.OracleCore;
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

@Slf4j
public class ApiConsumerTest extends BaseTest {


    @Test
    public void testOracleChain() throws Exception {

        credentials = GenCredential.create();

        String chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        String groupId = eventRegisterProperties.getEventRegisters().get(0).getGroupId();
        Web3j web3j = getWeb3j(chainId,groupId);

        OracleCore oracleCore = OracleCore.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER,
                BigInteger.valueOf(Integer.parseInt(chainId)),  BigInteger.valueOf(Integer.parseInt(groupId))).send();
        String orcleAddress = oracleCore.getContractAddress();
        log.info("oracleAddress:{} ", orcleAddress);


        //******* test return type int256 *******
        APISampleOracle apiConsumer = APISampleOracle.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCore.getContractAddress()).send();
        String apiConsumerAddress = apiConsumer.getContractAddress();
        log.info("apiConsumerAddress:{} ", apiConsumerAddress);
        TransactionReceipt t = apiConsumer.request().send();

        OracleCore.OracleRequestEventResponse requestedEventResponse = oracleCore.getOracleRequestEvents(t).get(0);

        Assertions.assertNotNull(requestedEventResponse.url);
        Assertions.assertNotNull(requestedEventResponse.timesAmount);
        Assertions.assertNotNull(requestedEventResponse.callbackAddr);
        Assertions.assertNotNull(requestedEventResponse.returnType);
        Assertions.assertNotNull(CommonUtils.bytesToHex(requestedEventResponse.requestId));


        //******* test return type String *******
        APISampleOracleReturnString apiConsumerReturnString = APISampleOracleReturnString.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCore.getContractAddress()).send();
        String apiConsumerReturnStringAddress = apiConsumerReturnString.getContractAddress();
        log.info("APISampleOracleReturnString:{} ", apiConsumerReturnStringAddress);
        TransactionReceipt returnStringReceipt = apiConsumerReturnString.request().send();

        requestedEventResponse = oracleCore.getOracleRequestEvents(returnStringReceipt).get(0);

        Assertions.assertNotNull(requestedEventResponse.url);
        Assertions.assertNotNull(requestedEventResponse.timesAmount);
        Assertions.assertNotNull(requestedEventResponse.callbackAddr);
        Assertions.assertNotNull(requestedEventResponse.returnType);
        Assertions.assertNotNull(CommonUtils.bytesToHex(requestedEventResponse.requestId));

    }

    @Test
    public void testApiConsumer() {
        credentials = GenCredential.create();
        try {
            EventRegisterConfig eventRegister = eventRegisterProperties.getEventRegisters().get(0);

            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();
            Web3j web3j = getWeb3j(chainId, groupId);

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos",
                            chainId, groupId,
                            ContractEnum.ORACLE_CORE.getType(), this.contractVersion.getOracleCoreVersion() );

            if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String oracleCoreAddress = deployOptional.get().getContractAddress();
            log.info("oracle core address " + oracleCoreAddress);

            //******* test return type int256 *******
            APISampleOracle apiConsumer = APISampleOracle.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCoreAddress).send();
            String apiConsumerAddress = apiConsumer.getContractAddress();
            log.info("Deploy APIConsumer contract:[{}]", apiConsumerAddress);

            TransactionReceipt t1 = apiConsumer.request().send();
            log.info("Generate random receipt: [{}:{}]", t1.getStatus(), t1.getOutput());

            Thread.sleep(5000);

            BigInteger random = apiConsumer.result().send();
            log.info("apiConsumer result: Random:[{}]", random);

            Assertions.assertTrue(random.compareTo(BigInteger.ZERO) != 0);


            //******* test return type String *******
            APISampleOracleReturnString apiConsumerReturnString = APISampleOracleReturnString.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, oracleCoreAddress).send();
            String apiConsumerReturnStringAddress = apiConsumerReturnString.getContractAddress();
            log.info("Deploy APIConsumer return String contract:[{}]", apiConsumerReturnStringAddress);

            TransactionReceipt returnStringReceipt = apiConsumerReturnString.request().send();
            log.info("APIConsumer return String receipt: [{}:{}]", returnStringReceipt.getStatus(), returnStringReceipt.getOutput());

            Thread.sleep(5000);

            String stringValue = apiConsumerReturnString.result().send();
            log.info("apiConsumerReturnString String value:[{}]", stringValue);

            Assertions.assertTrue(stringValue.length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }




}
