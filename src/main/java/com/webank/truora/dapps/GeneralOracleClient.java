package com.webank.truora.dapps;

import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos3runner.AbstractContractWorker;
import com.webank.truora.contract.bcos3.GeneralOracle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

import java.math.BigInteger;

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
        log.info("deploy contract done {},address {}",generalOracle,generalOracle.getContractAddress());
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
    public GeneralResult reqeustSource(GeneralOracleSource source) throws Exception{
        String targetUrl = source.getUrl();
        BigInteger timesAmount = source.getTimesAmount();
        ReturnTypeEnum returnType =source.getReturnType();
        log.info("---------------START REQUEST SOURCE-----------------");
        log.info("timesAmount:[{}],returnType:[{}],url:[{}]",timesAmount,returnType,targetUrl);
        TransactionReceipt receipt = generalOracle.requestSource(targetUrl,timesAmount,BigInteger.valueOf(returnType.getId()));

        log.info("request receipt status {}", receipt.getStatus());

        if (receipt.getStatus() != 0) {
            AbstractContractWorker.dealWithReceipt(receipt);
        }
        Tuple1<byte[]> requestOutput = generalOracle.getRequestSourceOutput(receipt);
        byte[] requestIdBytes = requestOutput.getValue1();
        /*
        //get request id from logs,just demo
        List<TransactionReceipt.Logs> logs = receipt.getLogEntries();
        log.info("quest on Block {}, logs: {}", receipt.getBlockNumber(), logs.toString());
        EventLog eventLog = Bcos3ModelTools.logToEventLog(logs.get(0));
        List<String> decodelogs = contractCodec.decodeEventToString(GeneralOracle.ABI, GeneralOracle.REQUESTED_EVENT.getName(), eventLog);
        log.info("DecodeLogs is : {}", decodelogs.toString());
        // 从回执中获取requestId，对应事件event Requested(bytes32 indexed id,address oracleAddress,uint256 requestCount,string url);
        String requestId = decodelogs.get(0);
        byte[] requestIdBytes = Hex.decodeHex(requestId.substring(2));
        log.info("requestId is {},bytes lens {}",requestId,requestIdBytes.length);*/

        int i=0;
        GeneralResult retValue = new GeneralResult(returnType);
        BigInteger rtype =  generalOracle.getReqType(requestIdBytes);
        //等10秒，可以修改为：链上合约被回写时生成事件，客户端监听事件
        while (i<10) {
            Thread.sleep(1000);
            i++;
            //BigInteger v = generalOracle.get();
            try {
                BigInteger fullfil = generalOracle.checkIdFulfilled(requestIdBytes);

                log.info("{}) returnType is  {}, Fullfill status is {}",i,rtype,fullfil);
                if(fullfil.intValue()!=2){ //还没有准备好
                    continue;
                }


                if(rtype.intValue() == ReturnTypeEnum.INT256.getId()) {
                    BigInteger v = generalOracle.getIntById(requestIdBytes);
                    log.info("Get ret : {}", v);
                    if (v.compareTo(BigInteger.valueOf(0)) > 0) {
                        retValue.put(v);
                        break;
                    }
                }else{
                    byte[] res = generalOracle.getById(requestIdBytes);
                    if(res.length > 0) {
                        log.info("-->Return bytes len:{} , {}",res.length, res);
                        if(rtype.intValue() == ReturnTypeEnum.STRING.getId()) {
                            String s = new String(res);
                            log.info("-->Return String len:{},  {}",s.length(), s);
                            retValue.put(s);
                        }else{
                            retValue.put(res);
                        }
                        break;
                    }
                }

            }catch(Exception e){
                log.info("call exception ",e.getMessage());
                break;
            }

        }
        log.info("After request ,result is {}",retValue);
        if(!retValue.checkValid()  ){
            throw new OracleException(ConstantCode.ORACLE_TIMEOUT);
        }
        log.info("---> REQUEST DONE! OK ! {}",targetUrl);
        return retValue;

    }

}
