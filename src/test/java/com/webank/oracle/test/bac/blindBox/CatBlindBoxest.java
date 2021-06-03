package com.webank.oracle.test.bac.blindBox;

import com.webank.oracle.test.base.BaseTest;
import com.webank.oracle.test.temp.RandomNumberSampleVRF;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CatBlindBoxest extends BaseTest {

    @Test
    public void testBlindBox() throws Exception {
        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId, groupId);


        //--------------------------
        //部分公共参-----------------
        //--------------------------
        String vrfCoreAddress = "0xc026a332a69b609c82505b2526098597a22ca46e";//VRFCore合约地址
        String _keyHash = "0x880bc68ae2e08e4ab4ff68939837558ec1b0ada0994e2be44fdd5c9c1a4a3aba";//VRFCore的_keyHash
        String randomNumberSampleVrfAddress;


        //------------------------------------
        //步骤一：部署相关合约-----------------
        //------------------------------------
        //部署RandomNumberSampleVRF，用于生成随机数
        RandomNumberSampleVRF randomNumberSampleVRF = RandomNumberSampleVRF.deploy(web3j, credentials, contractGasProvider, vrfCoreAddress, _keyHash.getBytes()).send();
        randomNumberSampleVrfAddress = randomNumberSampleVRF.getContractAddress();
        //部署CatBlindbox，用于盲盒猫抽奖
        CatBlindbox catBlindbox = CatBlindbox.deploy(web3j, credentials, contractGasProvider, randomNumberSampleVrfAddress).send();


        //--------------------------------
        //步骤二：盲盒抽奖-----------------
        //--------------------------------
        TransactionReceipt reqResult = catBlindbox.requestNewBlindboxCat(BigInteger.valueOf(new Random().nextInt(10000)), "cat" + new Random().nextInt(10000)).send();
        assertEquals(reqResult.getStatus(), "0");

        String outPutString = reqResult.getOutput();

        //--------------------------------
        //步骤三：查看结果-----------------
        //--------------------------------
    }
}
