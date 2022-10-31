package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.Bcos3EventRegister;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import com.webank.truora.bcos3runner.oracle.OracleCoreEventCallbackV3;
import com.webank.truora.contract.bcos3.OracleCore;
import com.webank.truora.test.LocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
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


    @Test
    public void testOracleCoreLocal() throws Exception
    {
        String chainId = "chain0";
        String groupId = "group0";
        Bcos3EventRegister eventRegister = eventRegisterFactory.get("chain0","group0");
        log.info("register config chainId {},groupId {}",eventRegister.getConfig().getChainId(),eventRegister.getConfig().getGroupId());
        log.info("register config  {}",eventRegister.getConfig().getKeyfile());

        OracleCoreEventCallbackV3 oracleCoreEventCallback = ctx.getBean(OracleCoreEventCallbackV3.class,
                eventRegister.getConfig().getChainId(), eventRegister.getConfig().getGroupId(), eventRegister);
        String oracleCoreAddress = oracleCoreEventCallback.loadOrDeployContract(eventRegister);
        log.info("oracleCoreAddress = {}",oracleCoreAddress);

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

        while(true){
            Thread.sleep(3000);
            log.info("ticking");
        }


    }
}
