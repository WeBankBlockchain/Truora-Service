package com.webank.truora.dapps;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.bcos3runner.Bcos3ModelTools;
import com.webank.truora.contract.bcos3.GeneralOracle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.List;

@Data
@Slf4j
public class GeneralOracleClient {

    String oracleCoreAddress;
    Client bcos3client;
    CryptoKeyPair keyPair;

    String dappContractAddress;
    ContractCodec contractCodec;

    GeneralOracle generalOracle;


    public GeneralOracleClient(String _oracleCoreAddress,Client _bcos3client,CryptoKeyPair _keyPair){
        this.oracleCoreAddress = _oracleCoreAddress;
        this.bcos3client = _bcos3client;
        this.keyPair = _keyPair;
        contractCodec = new ContractCodec(_bcos3client.getCryptoSuite(), false);
    }

    public void deployContract() throws ContractException {
        generalOracle = GeneralOracle.deploy(bcos3client, keyPair, oracleCoreAddress);
        this.dappContractAddress = generalOracle.getContractAddress();
    }

    public void loadContract(String _dappContractAddress) throws ContractException
    {
        generalOracle = GeneralOracle.load(_dappContractAddress,bcos3client,keyPair);
        this.dappContractAddress = _dappContractAddress;
    }


    /**
     * 这是一个相对通用的oracle应用客户端，原理是
     * 1： 输入一个目的url，即要获取的数据源地址，以及相应的数据系数（比如要乘以10），返回类型（0：大整形， 1：字符串 2： 字节）
     * 2： 对dapp合约发送交易，dapp合约再调用链上预置的OracleCore合约，生成预言机事件
     * 3： 链下预言机服务监听OracleCore合约的事件，获取外部数据源数据，并回写
     * 4:  客户端等待回写成功后，链上合约处理完成（轮询或接受事件），调用get接口获取结果
     *
     * 参照这些步骤，可以开发更加复杂的dapp合约，在回写后，实现更多的业务逻辑
     * 并开发相应的java客户端来触发链上交易，等待结果
     * */
    public BigInteger reqeustSource(GeneralOracleSource source) throws Exception{
        String targetUrl = source.getUrl();
        BigInteger timesAmount = source.getTimesAmount();
        BigInteger returnType =source.getReturnType();
        log.info("---------------START REQUEST SOURCE-----------------");
        log.info("timesAmount:[{}],returnType:[{}],url:[{}]",timesAmount,returnType,targetUrl);
        TransactionReceipt receipt = generalOracle.requestSource(targetUrl,timesAmount,returnType);

        log.info("request receipt status {}", receipt.getStatus());

        if (receipt.getStatus() != 0) {
            AbstractContractWorker.dealWithReceipt(receipt);
        }


        //logs
        List<TransactionReceipt.Logs> logs = receipt.getLogEntries();
        log.info("quest on Block {}, logs: {}", receipt.getBlockNumber(), logs.toString());
        EventLog eventLog = Bcos3ModelTools.logToEventLog(logs.get(0));
        List<String> decodelogs = contractCodec.decodeEventToString(GeneralOracle.ABI, GeneralOracle.REQUESTED_EVENT.getName(), eventLog);
        log.info("DecodeLogs is : {}", decodelogs.toString());
        int i=0;
        BigInteger retValue = BigInteger.valueOf(0);

        //等10秒，可以修改为：链上合约被回写时生成事件，客户端监听事件
        while (i<10) {
            Thread.sleep(1000);
            BigInteger v = generalOracle.get();
            log.info("Get ret : {}", v);
            if (v.compareTo(BigInteger.valueOf(0)) > 0) {
                retValue = v;
                break;
            }
        }
        if(retValue.intValue() == 0 ){
            throw new OracleException(ConstantCode.ORACLE_TIMEOUT);
        }
        log.info("---> REQUEST DONE! OK ! {}",targetUrl);
        return retValue;

    }

}
