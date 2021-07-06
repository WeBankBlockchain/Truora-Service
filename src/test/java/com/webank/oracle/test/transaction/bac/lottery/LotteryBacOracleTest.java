package com.webank.oracle.test.transaction.bac.lottery;


import com.webank.oracle.test.transaction.bac.BAC001.BAC001;
import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class LotteryBacOracleTest extends BaseTest {

    @Test
    public void testLottery() throws Exception {

        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId, groupId);

        //OracleCore合约地址
        String oracleCoreAddress = "0xa60a49b75ee98a19939a786d57cd2a9802e4984b";
        //参与抽奖需要质押的资产额度
        BigInteger lottery_amount = BigInteger.valueOf(50);


        //------------------------------------
        //步骤一：部署相关合约------------------
        //------------------------------------
        //部署oracle客户端合约，用于获取随机数
        APISampleOracle apiSampleOracle = APISampleOracle.deploy(web3j, credentials, contractGasProvider, oracleCoreAddress).send();
        //部署合约 即发行资产 资产描述：fisco bcos car asset; 资产简称 TTT; 最小转账单位 1 ;发行总量 10000000;
        BAC001 bac001 = BAC001.deploy(web3j, credentials, contractGasProvider, "GDX car asset", "TTT", BigInteger.valueOf(1), BigInteger.valueOf(1000000)).send();
        //部署抽奖合约
        LotteryBacOracle lottery = LotteryBacOracle.deploy(web3j, credentials, contractGasProvider, apiSampleOracle.getContractAddress(), bac001.getContractAddress()).send();


        //--------------------------------------------------------
        //步骤二：管理员分别给bob和alice转一笔bac001----------------
        //--------------------------------------------------------
        bac001.send(Bob, BigInteger.valueOf(3000), "init player asset".getBytes()).send();
        bac001.send(Alice, BigInteger.valueOf(3000), "init player asset".getBytes()).send();


        //-----------------------------------
        //步骤三：主持人开启抽奖---------------
        //-----------------------------------
        List<String> playerList = Arrays.asList(Bob, Alice);
        lottery.start_new_lottery(playerList, lottery_amount).send();


        //---------------------------------------------------------------
        //步骤四：参与抽奖者根据抽奖要求，允许抽奖合约从自己账户转走部分bac001
        //---------------------------------------------------------------
        BAC001 bac001Bob = BAC001.load(bac001.getContractAddress(), web3j, credentialsBob, contractGasProvider);
        bac001Bob.approve(lottery.getContractAddress(), lottery_amount).send();
        BAC001 bac001Alice = BAC001.load(bac001.getContractAddress(), web3j, credentialsAlice, contractGasProvider);
        bac001Alice.approve(lottery.getContractAddress(), lottery_amount).send();


        //----------------------------
        //步骤五：参与抽奖者确认质押资产
        //----------------------------
        LotteryBacOracle lotteryBob = LotteryBacOracle.load(lottery.getContractAddress(), web3j, credentialsBob, contractGasProvider);
        lotteryBob.deposit().send();
        LotteryBacOracle lotteryAlice = LotteryBacOracle.load(lottery.getContractAddress(), web3j, credentialsAlice, contractGasProvider);
        lotteryAlice.deposit().send();


        //----------------------------
        //步骤六：主持人停止质押
        //----------------------------
        lottery.stop_deposit().send();


        //-----------------------------------------------
        //步骤七：主持人开奖，并将奖池的bac001全部转给胜出者
        //-----------------------------------------------
        Thread.sleep(3*1000);
        TransactionReceipt winnerAddresss = lottery.pickWinner().send();


        //-----------------------------------------------
        //步骤八：结果验证
        //-----------------------------------------------
        BigInteger id = lottery.lotteryId().send();
        String winnerAddress = lottery.winners(id).send();//查询本轮胜出者
        String loserAddress =  winnerAddress.equalsIgnoreCase(Bob) ? Alice: Bob;//失败者
        String wb = bac001.balance(winnerAddress).send().toString();
        String lb = bac001.balance(loserAddress).send().toString();
        assertEquals(bac001.balance(winnerAddress).send().toString(), "3050");//3000-50+50*2
        assertEquals(bac001.balance(loserAddress).send().toString(), "2950");//3000-50
    }

}
