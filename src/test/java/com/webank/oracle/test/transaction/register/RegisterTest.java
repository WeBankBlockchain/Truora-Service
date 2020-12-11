package com.webank.oracle.test.transaction.register;

import com.webank.oracle.base.properties.ConstantProperties;
import com.webank.oracle.base.utils.JsonUtils;
import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.transaction.register.OracleRegisterCenter;
import com.webank.oracle.transaction.register.OracleServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.webank.oracle.event.service.AbstractCoreService.dealWithReceipt;

/**
 *
 */

@Slf4j
public class RegisterTest extends BaseTest {

    @Test
    public void testRegisterDeploy() throws Exception {
        int chainId = 1;
        int groupId = 1;
        Web3j web3j = getWeb3j(chainId, groupId);
        OracleRegisterCenter registerCenter = OracleRegisterCenter.deploy( web3j, GenCredential.create(RandomUtils.nextInt() + ""), ConstantProperties.GAS_PROVIDER).send();
        String operator = "operator";
        String url = "http://www.baidu.com";
        List<BigInteger> publicKeyList =
                Arrays.asList(new BigInteger[]{BigInteger.valueOf(1L), BigInteger.valueOf(2L)});
        TransactionReceipt oracleRegisterReceipt = registerCenter.oracleRegister(operator, url, publicKeyList).send();
        log.info("Register service list on chain:[{}:{}], receipt status:[{}], output: [{}]", chainId, groupId, oracleRegisterReceipt.getStatus(), oracleRegisterReceipt.getOutput());
    //    dealWithReceipt(oracleRegisterReceipt);

        List<OracleServiceInfo> oracleServiceInfoList = registerCenter.getAllOracleServiceInfo().send();
        log.info("Oracle service info list:[{}]", JsonUtils.toJSONString(oracleServiceInfoList));
        Assertions.assertTrue(CollectionUtils.size(oracleServiceInfoList) > 0);
    }

    @Test
    public void testRegister() throws Exception {
        int chainId = 1;
        int groupId = 1;
        Web3j web3j = getWeb3j(chainId, groupId);
        String registerCenterAddress = this.oracleRegisterCenterService.getRegisterCenterAddress(chainId, groupId);
        log.info("Oracle register center address:[{}]", registerCenterAddress);

        OracleRegisterCenter registerCenter = OracleRegisterCenter.load(registerCenterAddress, web3j, GenCredential.create(RandomUtils.nextInt() + ""), ConstantProperties.GAS_PROVIDER);

        String operator = "operator";
        String url = "http://www.baidu.com";
        List<BigInteger> publicKeyList =
                Arrays.asList(new BigInteger[]{BigInteger.valueOf(1L), BigInteger.valueOf(2L)});
        TransactionReceipt oracleRegisterReceipt = registerCenter.oracleRegister(operator, url, publicKeyList).send();
        log.info("Register service list on chain:[{}:{}], receipt status:[{}], output: [{}]", chainId, groupId, oracleRegisterReceipt.getStatus(), oracleRegisterReceipt.getOutput());
        dealWithReceipt(oracleRegisterReceipt);

        List<OracleServiceInfo> oracleServiceInfoList = this.oracleRegisterCenterService.getOracleServiceList(chainId, groupId);
        log.info("Oracle service info list:[{}]", JsonUtils.toJSONString(oracleServiceInfoList));
        Assertions.assertTrue(CollectionUtils.size(oracleServiceInfoList) > 0);
    }


}