package com.webank.oracle.test.transaction.bac.blindBox;

import com.webank.oracle.base.utils.CommonUtils;
import com.webank.oracle.base.utils.JsonUtils;
import com.webank.oracle.event.vo.BaseLogResult;
import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple6;
import org.fisco.bcos.web3j.tx.txdecode.*;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.webank.oracle.event.service.AbstractCoreService.dealWithReceipt;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CatBlindBoxTest extends BaseTest {

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
        RandomNumberSampleVRF randomNumberSampleVRF = RandomNumberSampleVRF.deploy(web3j, credentials, contractGasProvider, vrfCoreAddress, ByteUtil.hexStringToBytes(_keyHash)).send();
        randomNumberSampleVrfAddress = randomNumberSampleVRF.getContractAddress();
        //部署CatBlindbox，用于盲盒猫抽奖
        CatBlindbox catBlindbox = CatBlindbox.deploy(web3j, credentials, contractGasProvider, randomNumberSampleVrfAddress).send();


        //默认循环抽奖一次，最多七次（本合约只有7个猫）
        for(int i=0;i<1;i++){
            //--------------------------------
            //步骤二：盲盒抽奖-----------------
            //--------------------------------
            TransactionReceipt reqResult = catBlindbox.requestNewBlindboxCat(BigInteger.valueOf(new Random().nextInt(10000)), "cat" + new Random().nextInt(10000)).send();
            dealWithReceipt(reqResult);
            String requestId = getByKeyNameFromTransactionReceipt(reqResult,CatBlindbox.ABI, CatBlindbox.RESULTOFNEWBLINDBOXCAT_EVENT,BaseLogResult.LOG_REQUEST_ID);

            //--------------------------------
            //步骤三：查看结果-----------------
            //--------------------------------
            //获取抽奖结果
            Thread.sleep(3*1000);
            TransactionReceipt genResult = catBlindbox.generateBlindBoxCat(ByteUtil.hexStringToBytes(requestId)).send();
            dealWithReceipt(genResult);
            String requestIdOfGen = getByKeyNameFromTransactionReceipt(genResult,CatBlindbox.ABI, CatBlindbox.SURPRISECAT_EVENT,BaseLogResult.LOG_REQUEST_ID);
            assertEquals(requestId, requestIdOfGen);
            //根据nftId查询猫信息
            Tuple6<BigInteger, String, String, BigInteger, String, String> queryResult = catBlindbox.getCatInfo(BigInteger.valueOf(i)).send();
            System.out.println(JsonUtils.objToString(""+queryResult));


        }



    }

    /**
     * 从交
     * @param reqResult
     * @param abi
     * @param event
     * @param keName
     * @return
     */
    public String getByKeyNameFromTransactionReceipt(TransactionReceipt reqResult, String abi, Event event, String keName) {
        TransactionDecoder decoder = TransactionDecoderFactory.buildTransactionDecoder(abi, "");
        Map<String, List<List<EventResultEntity>>> resultEntityListMap =null;
        try {
            resultEntityListMap =  decoder.decodeEventReturnObject(reqResult.getLogs());
        } catch (Exception e) {
           return  null;
        }

        for(String key:resultEntityListMap.keySet()){
            if(key.contains(event.getName())){
                List<List<EventResultEntity>> entityList = resultEntityListMap.get(key);
             return CommonUtils.byte32LogToString(entityList.get(0), keName);
            }
        }
        return null;
    }

}
