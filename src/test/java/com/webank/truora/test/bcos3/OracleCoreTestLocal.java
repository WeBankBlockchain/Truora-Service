package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.bcos3runner.Bcos3EventCallback;
import com.webank.truora.bcos3runner.oracle.OracleCoreWorker;
import com.webank.truora.contract.bcos3.OracleCore;
import com.webank.truora.test.LocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
//import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.math.BigInteger;
import java.util.Random;

@Slf4j
public class OracleCoreTestLocal extends LocalTestBase {
    @Autowired
    Bcos3EventRegisterFactory eventRegisterFactory;
    @Autowired private ApplicationContext ctx;
    @Autowired
    OracleCoreWorker oracleExcutor;


    @Test
    public void testOracleCoreLocal() throws Exception
    {
        String chainId = "chain0";
        String groupId = "group0";
        Bcos3EventRegister eventRegister = eventRegisterFactory.get("chain0","group0");
        log.info("register config chainId {},groupId {}",eventRegister.getConfig().getChainId(),eventRegister.getConfig().getGroupId());
        log.info("register config  {}",eventRegister.getConfig().getKeyfile());

        Bcos3EventCallback oracleCoreEventCallback = ctx.getBean(Bcos3EventCallback.class);
        oracleExcutor.init(eventRegister);
        String oracleCoreAddress = oracleExcutor.loadOrDeployContract();
        log.info("oracleCoreAddress = {}",oracleCoreAddress);
        // 这里只是强行触发一下oracle的事件，fulfill结果一定是false的，因为下面这个callAddress一定不存在
        //逻辑完整的测试，参见APISampleTest
        String _callAddress = "0x5c3348fb91f6c238f18dd982999d4e88edb9f400";
        BigInteger _nonce = BigInteger.valueOf(new Random().nextInt(100000));
        String _url = "plain(https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new)";
        BigInteger _timesAmount = BigInteger.valueOf(10);
        BigInteger _expiryTime = BigInteger.valueOf(10 * 60 * 1000);
        Boolean _needProof = false;
        BigInteger _returnType = BigInteger.valueOf(1);
        OracleCore oracleCore = OracleCore.load(oracleCoreAddress,eventRegister.getBcos3client(),eventRegister.getKeyPair());
        TransactionReceipt receipt = oracleCore.query(_callAddress,_nonce,_url,_timesAmount,_expiryTime,_needProof,_returnType);
        log.info("send oracle query on block {} ,status :{}",receipt.getBlockNumber(),receipt.getStatus());
        int i=0;
        while(true){
            Thread.sleep(1000);
            if(i++ > 5){
                break;
            }
            log.info("ticking");
        }


    }
}
