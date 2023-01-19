package com.webank.truora.test.bcos2.VRF;

import com.webank.truora.base.enums.ContractEnum;
import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.database.DBContractDeploy;
import com.webank.truora.test.base.BaseTest;
import com.webank.truora.contract.bcos2.LotteryOracleUseVrf;
import com.webank.truora.contract.bcos2.RandomNumberSampleVRF;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.webank.truora.bcos2runner.AbstractCoreService.dealWithReceipt;

@Slf4j
public class LotteryOracleWithVRFTest extends BaseTest {

    @Test
    public void testLotteryOracleWithVRF() {
        credentials = this.keyStoreService.getCredentials();
        try {
            EventRegisterConfig eventRegister = eventRegisterProperties.getEventRegisters().get(0);

            String chainId = eventRegister.getChainId();
            String groupId = eventRegister.getGroupId();
            Web3j web3j = getWeb3j(chainId, groupId);

            Optional<DBContractDeploy> deployOptional =
                    this.contractDeployRepository.findByPlatformAndChainIdAndGroupIdAndContractTypeAndVersion(
                            "fiscobcos",
                            chainId, groupId,
                        ContractEnum.VRF_K1_CORE.getType(), this.contractVersion.getOracleCoreVersion() );

            if (!deployOptional.isPresent()) {
                Assertions.fail();
                return;
            }

            String vrfCoreAddress = deployOptional.get().getContractAddress();
            log.info("vrf core address " + vrfCoreAddress);
            byte[] keyHashByte = calculateTheHashFromPrivkey(credentials.getEcKeyPair().getPrivateKey().toString(16));
            // asset
            RandomNumberSampleVRF randomNumberSampleVRF = RandomNumberSampleVRF.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, vrfCoreAddress, keyHashByte).send();

            String sampleVrfAddress = randomNumberSampleVRF.getContractAddress();
            log.info("Deploy Vrf sample Address contract:[{}]", sampleVrfAddress);

            LotteryOracleUseVrf lotteryOracle = LotteryOracleUseVrf.deploy(web3j, credentials, ConstantProperties.GAS_PROVIDER, sampleVrfAddress).send();


            String[] array = {"0x2b5ad5c4795c026514f8317c7a215e218dccd6cf","0x2b5ad5c4795c026514f8317c7a215e218dccd6c1","0x2b5ad5c4795c026514f8317c7a215e218dccd6c2","0x2b5ad5c4795c026514f8317c7a215e218dccd6c3","0x2b5ad5c4795c026514f8317c7a215e218dccd6c4"};
            List<String> playerAddressList = Arrays.asList(array);

            TransactionReceipt t1 = lotteryOracle.start_new_lottery(playerAddressList).send();
            dealWithReceipt(t1);
            log.info( "status: {}" ,t1.getStatus());
            log.info( "output: {}" ,t1.getOutput());

            Thread.sleep(5000);

            TransactionReceipt randomTrans = lotteryOracle.pickWinner().send();
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
