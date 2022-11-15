package com.webank.truora.test.bcos3;

import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos3runner.*;
import com.webank.truora.contract.bcos3.GeneralOracle;
import com.webank.truora.crawler.BaseUrl;
import com.webank.truora.crawler.HashUrlCrawler;
import com.webank.truora.dapps.GeneralOracleConfig;
import com.webank.truora.dapps.GeneralOracleSource;
import com.webank.truora.test.LocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class GeneralOracleTest extends LocalTestBase {


    //@Autowired
    //DappsConfig dappsConfig;
    @Autowired
    GeneralOracleConfig generalOracleConfig;
    @Autowired
    Bcos3SdkFactory sdkFactory;
    @Autowired
    Bcos3ClientConfig clientConfig;

    @Autowired
    Bcos3EventRegisterFactory eventRegisterFactory;

    ContractCodec contractCodec;

    public String remove0xprefix(String address) {
        if (address.startsWith("0x")) {
            return address.substring(2);
        }
        return address;
    }


    public void reqeustSource(GeneralOracle generalOracle,GeneralOracleSource source) throws Exception{
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
        BigInteger ret = BigInteger.valueOf(0);
        while (i<8) {
            Thread.sleep(1000);
            ret = generalOracle.get();
            log.info("Get ret : {}", ret);
            if (ret.compareTo(BigInteger.valueOf(0)) > 0) {
                break;
            }
        }
        Assertions.assertNotEquals(ret,BigInteger.valueOf(0));
        log.info("---> REQUEST DONE! OK ! {}",targetUrl);

    }

    @Test
    public void testGeneralOracle() throws Exception {


        String chainId = generalOracleConfig.getChainId();
        String groupId = generalOracleConfig.getGroupId();

        Bcos3EventRegister eventRegister = eventRegisterFactory.get(chainId, groupId);
        //Client client  = sdkFactory.getClientByChainIdAndGroupIdNotNull(chainId,groupId);
        Client client = eventRegister.getBcos3client();
        CryptoKeyPair keyPair = Bcos3KeyTools.getKeyPairByFile(client, clientConfig.getDefaultKeyfile());
        contractCodec = new ContractCodec(client.getCryptoSuite(), false);

        String oracleCoreAddress = eventRegister.getConfig().getOracleCoreAddress();

        //deploy the oracleCore direct for TEST
        //OracleCore oracleCore = OracleCore.deploy(client,keyPair,chainId,groupId);
        //oracleCoreAddress = oracleCore.getContractAddress();

        oracleCoreAddress = remove0xprefix(oracleCoreAddress);
        GeneralOracle generalOracle;
        log.info("OraceCoreAddress :{}", oracleCoreAddress);
        String dappAddress = remove0xprefix(generalOracleConfig.getContractAddress());
        if (!dappAddress.isEmpty()) {

            generalOracle = GeneralOracle.load(dappAddress, client, keyPair);

        } else {
            log.info("--> Deploy contract APISampleOracle,set oracleCore: {}", oracleCoreAddress);
            generalOracle = GeneralOracle.deploy(client, keyPair, oracleCoreAddress);
            dappAddress = generalOracle.getContractAddress();
        }
        log.info("APISample contract address: {}", dappAddress);
        String urlonchain = "";
        String targetUrl = generalOracleConfig.getUrl();
        //urlonchain = apiSampleOracle.getUrl();
        BaseUrl hashUrl = new BaseUrl(HashUrlCrawler.NAME,"http://www.qq.com/");

        ArrayList<GeneralOracleSource> targetSourcceList = new ArrayList<>();
        String targetUrl0 = JsonUtils.toJSONString(hashUrl);
        targetSourcceList.add(new GeneralOracleSource
                (targetUrl0,BigInteger.valueOf(1), BigInteger.valueOf(0)));

        String targetUrl1 = "plain(https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new)";
        targetSourcceList.add(new GeneralOracleSource(targetUrl1,BigInteger.valueOf(1), BigInteger.valueOf(0)));

        String targetUrl2 = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
        targetSourcceList.add(new GeneralOracleSource(targetUrl2,BigInteger.valueOf(100), BigInteger.valueOf(0)));

        //targetSourcceList.clear();
        String targetUrl3 = generalOracle.getUrl();
        targetSourcceList.add(new GeneralOracleSource(targetUrl3,BigInteger.valueOf(1), BigInteger.valueOf(0)));
        for (GeneralOracleSource source : targetSourcceList){
            reqeustSource(generalOracle, source);
        }
    }

}
