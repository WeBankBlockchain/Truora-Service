    package com.webank.truora.test.bcos3;

    import com.webank.truora.base.utils.JsonUtils;
    import com.webank.truora.bcos3runner.*;
    import com.webank.truora.contract.bcos3.APISampleOracle;
    import com.webank.truora.crawler.HashUrl;
    import com.webank.truora.dapps.ApiSampleConfig;
    import com.webank.truora.test.LocalTestBase;
    import lombok.extern.slf4j.Slf4j;
    import org.fisco.bcos.sdk.v3.client.Client;
    import org.fisco.bcos.sdk.v3.codec.ContractCodec;
    import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
    import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
    import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
    import org.fisco.bcos.sdk.v3.model.EventLog;
    import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
    import org.fisco.bcos.sdk.v3.transaction.codec.decode.RevertMessageParser;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;

    import java.math.BigInteger;
    import java.util.List;


@Slf4j
public class APISampleTestLocal  extends LocalTestBase {

    @Autowired
    ApiSampleConfig apiSampleConfig;
    //@Autowired
    //DappsConfig dappsConfig;
    @Autowired
    Bcos3SdkFactory sdkFactory;
    @Autowired
    Bcos3ClientConfig clientConfig;

    @Autowired
    Bcos3EventRegisterFactory eventRegisterFactory;


    public String remove0xprefix(String address){
        if (address.startsWith("0x")){
            return address.substring(2);
        }
        return address;
    }

    @Test
    public void testAPISampleLocal() throws Exception {


        String chainId = apiSampleConfig.getChainId();
        String groupId = apiSampleConfig.getGroupId();
        //log.info("AllDappsConfig {}",dappsConfig.getDapps().toString());
        log.info("testAPISampleLocal,chainid:{},groupId:{},address: {} ,url: {}",chainId,groupId,apiSampleConfig.getContractAddress(),apiSampleConfig.getUrl());
        Bcos3EventRegister eventRegister = eventRegisterFactory.get(chainId,groupId);
        //Client client  = sdkFactory.getClientByChainIdAndGroupIdNotNull(chainId,groupId);
        Client client = eventRegister.getBcos3client();
        CryptoKeyPair keyPair = Bcos3KeyTools.getKeyPairByFile(client,clientConfig.getDefaultKeyfile());
        ContractCodec codec = new ContractCodec(client.getCryptoSuite(),false);

        String oracleCoreAddress = eventRegister.getConfig().getOracleCoreAddress();

        //deploy the oracleCore direct for TEST
        //OracleCore oracleCore = OracleCore.deploy(client,keyPair,chainId,groupId);
        //oracleCoreAddress = oracleCore.getContractAddress();

        oracleCoreAddress = remove0xprefix(oracleCoreAddress);
        APISampleOracle apiSampleOracle;
        log.info("OraceCoreAddress :{}",oracleCoreAddress);
        String apiSampleAddress = remove0xprefix(apiSampleConfig.getContractAddress());
        if (!apiSampleAddress.isEmpty()) {

            apiSampleOracle = APISampleOracle.load(apiSampleAddress, client, keyPair);

        }else{
            log.info("--> Deploy contract APISampleOracle,set oracleCore: {}",oracleCoreAddress);
            apiSampleOracle = APISampleOracle.deploy(client,keyPair,oracleCoreAddress);
            apiSampleAddress = apiSampleOracle.getContractAddress();

        }
        log.info("APISample contract address: {}",apiSampleAddress);
        String urlonchain = "";
        String targetUrl = apiSampleConfig.getUrl();
        //urlonchain = apiSampleOracle.getUrl();
        HashUrl hashUrl = new HashUrl("http://www.qq.com/");
        targetUrl = JsonUtils.toJSONString(hashUrl);
        //targetUrl = apiSampleConfig.getUrl();
        log.info("urlonchain is : {}",urlonchain);
        log.info("targetUrl is : {}",targetUrl);
        if (urlonchain.compareTo(targetUrl)!=0) {

            TransactionReceipt receipt = apiSampleOracle.setUrl(targetUrl);
            log.info("setUrl result: status: {},on block: {}",receipt.getStatus(),receipt.getBlockNumber());
            if(receipt.getStatus()!=0){
                List<Type> listoutput = codec.decodeMethod(APISampleOracle.ABI,"setUrl",receipt.getOutput());
                log.info("output : {}",listoutput);



                Tuple2 err = RevertMessageParser.tryResolveRevertMessage(receipt);
                log.error(String.format("%d,%s",err.getValue1(),err.getValue2()));
            }
        }
        TransactionReceipt receipt = apiSampleOracle.request();

        log.info("apiSampleOracle request receipt status {}",receipt.getStatus());

        if (receipt.getStatus()!=0){
            AbstractContractWorker.dealWithReceipt(receipt);
        }


        //logs
        List<TransactionReceipt.Logs> logs =  receipt.getLogEntries();
        log.info("quest on Block {}, logs: {}",receipt.getBlockNumber(),logs.toString());

        EventLog eventLog = Bcos3ModelTools.logToEventLog(logs.get(0));
        List<String> decodelogs = codec.decodeEventToString(APISampleOracle.ABI,APISampleOracle.REQUESTED_EVENT.getName(), eventLog);
        log.info("DecodeLogs is : {}",decodelogs.toString());

        //try call OracleRequest direct
        BigInteger requestCount =new BigInteger( decodelogs.get(2));
        //因为上面已经调用过一次了，已经有一个request在OracleCore里，再调用的话，有可能冲突，需要修改一下合约
        requestCount = BigInteger.valueOf(requestCount.intValue()-1);


        //oracleCore = OracleCore.load(oracleCoreAddress,client,keyPair);
        BigInteger _timesAmount = BigInteger.valueOf(100);
        BigInteger _expiryTime = BigInteger.valueOf(10 * 60 * 1000);
        Boolean _needProof = false;
        BigInteger _returnType = BigInteger.valueOf(0);
        /*
        //这是主动调用OracleCore合约触发事件，仅供测试验证
        TransactionReceipt recepitQuery = oracleCore.query(apiSampleAddress,requestCount,apiSampleConfig.getUrl(),_timesAmount,_expiryTime,_needProof,_returnType);
        log.info("OracleQuery status: {}",recepitQuery.getStatus());
        int statusQuery =recepitQuery.getStatus();
        if(statusQuery==0) {
            EventLog eventLogQuery = Bcos3ModelTools.logToEventLog(recepitQuery.getLogEntries().get(0));
            List<String> eventstrs = codec.decodeEventToString(OracleCore.ABI, OracleCore.ORACLEREQUEST_EVENT.getName(), eventLogQuery);
            OracleCore.OracleRequestEventResponse eventresponse = OracleCoreEventCallbackV3.fromDecodeString(eventstrs);
            String strReqId = Hex.encodeHexString(eventresponse.requestId);
            log.info("reqId in response is :{}", strReqId);
        }else if(statusQuery==0x16){
            //codec.decode
            Tuple2<Boolean, String> res0x16 =  RevertMessageParser.tryResolveRevertMessage(receipt);
            log.error("oracleQuery 0x16 {}:{}",res0x16.getValue1(),res0x16.getValue2());
        }*/
        while(true) {
            BigInteger ret = apiSampleOracle.get();

            log.info("apiSampleOracle get ret : {}",ret);
            if (ret.compareTo(BigInteger.valueOf(0)) > 0){
                log.info("---> TEST DONE! OK !");
                break;
            }
            Thread.sleep(2000);

        }
    }
}
