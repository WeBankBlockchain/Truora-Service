package com.webank.oracle.test.transaction.bac.BAC002;

import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class BAC002Test extends BaseTest {

    @Test
    public void testBAC002() throws Exception {

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId, groupId);


        // 部署合约 即发行资产 需要传入 资产描述和资产简称
        BAC002 bac002 = BAC002.deploy(web3j, credentials, contractGasProvider, "fisco bcos car asset", "TTT").send();
        String contractAddress = bac002.getContractAddress();
        //增加 发行者
        bac002.addIssuer(Alice).send();
        // 新建资产
        bac002.issueWithAssetURI(Alice, new BigInteger("1"), "this is a car information url is www.XXX.com", "add for alice".getBytes()).send();
        bac002.issueWithAssetURI(Owner, new BigInteger("2"), "this is a house information url is www.XXX.com", "add for Owner".getBytes()).send();
        bac002.issueWithAssetURI(Owner, new BigInteger("3"), "this is a house information url is www.XXX.com", "add for Owner".getBytes()).send();

        assertEquals(bac002.ownerOf(new BigInteger("1")).send(), Alice);

        // 销毁资产, owner销毁自己的资产
        bac002.destroy(new BigInteger("2"), "destroy the house asset".getBytes()).send();

        bac002.approve(Bob, new BigInteger("3")).send();

        BAC002 bac002Bob = BAC002.load(contractAddress, web3j, credentialsBob, contractGasProvider);

        //转账 以及转账备注 Bob发交易Owner -> Alice
        bac002Bob.sendFrom(Owner, Alice, new BigInteger("3"), "sell house".getBytes()).send();

        //查询余额
        assertEquals(bac002.balance(Alice).send().toString(), "2");
        assertEquals(bac002.ownerOf(new BigInteger("3")).send(), Alice);

    }
}
