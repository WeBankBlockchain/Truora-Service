package com.webank.truora.test.bcos2.register;

import com.webank.truora.base.properties.ConstantProperties;
import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos2runner.oracle.OracleServiceInfo;
import com.webank.truora.contract.bcos2.OracleRegisterCenter;
import com.webank.truora.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple10;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.webank.truora.bcos2runner.AbstractCoreService.dealWithReceipt;

/**
 *
 */

@Slf4j
public class RegisterTest extends BaseTest {

    @Test
    public void testRegisterDeploy() throws Exception {
        String chainId = "1";
        String groupId = "1";
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
        String chainId = "1";
        String  groupId = "1";
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


    @Test
    public void testUpdateRegisterInfo() throws Exception {
        String chainId = "1";
        String groupId = "1";
        Web3j web3j = getWeb3j(chainId, groupId);

        String registerAddress = oracleRegisterCenterService.getRegisterCenterAddress(chainId,groupId);
        OracleRegisterCenter registerCenter = OracleRegisterCenter.load(registerAddress, web3j, keyStoreService.getCredentials(), ConstantProperties.GAS_PROVIDER);

        Tuple10 allServiceInfo = registerCenter.getOracleServiceInfo(keyStoreService.getCredentials().getAddress()).send();
        String oldOpreator = (String)allServiceInfo.getValue5();
        String oldUrl =(String) allServiceInfo.getValue6();
        log.info("old operator:  {}" , oldOpreator);
        log.info("old url:  {} , oldUrl", oldUrl);
        oracleRegisterCenterService.updateOracleInfo("fisco bcos oracle", "127.0.0.1:5020");

        Tuple10 allServiceInfo1 = registerCenter.getOracleServiceInfo(keyStoreService.getCredentials().getAddress()).send();
        String operator = (String)allServiceInfo1.getValue5();
        String url =(String) allServiceInfo1.getValue6();
        log.info("new operator:  {}" , operator);
        log.info("new url:  {} , ", url);
        Assertions.assertTrue(operator.equals("fisco bcos oracle"));
        Assertions.assertTrue(url.equals("127.0.0.1:5020"));

    }

}