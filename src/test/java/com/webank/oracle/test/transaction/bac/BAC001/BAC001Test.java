package com.webank.oracle.test.transaction.bac.BAC001;


import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class BAC001Test extends BaseTest {

    @Test
    public void testBAC001() throws Exception {

        BigInteger gasPrice = new BigInteger("1");
        BigInteger gasLimit = new BigInteger("2100000000");
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);

        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId, groupId);


        // 部署合约 即发行资产 资产描述：fisco bcos car asset; 资产简称 TTT; 最小转账单位 1 ;发行总量 10000000;
        BAC001 bac001 = BAC001.deploy(web3j, credentials, contractGasProvider, "GDX car asset", "TTT", BigInteger.valueOf(1), BigInteger.valueOf(1000000)).send();
        String contractAddress = bac001.getContractAddress();
        //增加 发行者
        bac001.addIssuer(Alice).send();

        // 增发资产
        bac001.issue(Alice, new BigInteger("10000"), "increase 10000 asset  ".getBytes()).send();
        // 销毁资产, owner销毁自己的资产
        bac001.destroy(new BigInteger("10000"), "destroy 10000 asset".getBytes()).send();
        //转账 以及转账备注 Owner -> Alice
        bac001.send(Alice, new BigInteger("10000"), "dinner money".getBytes()).send();
        //Owner批准Bob可以从自己账户转走1000TTT
        bac001.approve(Bob, new BigInteger("10000")).send();
        //Bob开始转走Owner 1000TTT ，需要先根据credentialsBob 重新load合约；
        BAC001 bac001Bob = BAC001.load(contractAddress, web3j, credentialsBob, contractGasProvider);
        bac001Bob.sendFrom(Owner, Alice, new BigInteger("10000"), "dddd".getBytes()).send();
        //查询余额
        assertEquals(bac001.balance(Alice).send().toString(), "30000");
        assertEquals(bac001.balance(Owner).send().toString(), "9970000");

    }

}
