package com.webank.truora.test.bcos3;

import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos3runner.*;
import com.webank.truora.crawler.BaseUrl;
import com.webank.truora.crawler.HashUrlCrawler;
import com.webank.truora.dapps.GeneralOracleClient;
import com.webank.truora.dapps.GeneralOracleConfig;
import com.webank.truora.dapps.GeneralOracleSource;
import com.webank.truora.dapps.GeneralResult;
import com.webank.truora.test.LocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;


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

    @Test
    public void testGeneralOracle() throws Exception {


        String chainId = "chain0";
        String groupId = "group0";

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

        log.info("OraceCoreAddress :{}", oracleCoreAddress);
        String dappAddress = remove0xprefix(generalOracleConfig.getContractAddress());

        GeneralOracleClient generalOracleClient = new GeneralOracleClient(oracleCoreAddress,client,keyPair);

        if (!dappAddress.isEmpty()&&false) {

            generalOracleClient.loadContract(dappAddress);

        } else {
            log.info("--> Deploy contract APISampleOracle,set oracleCore: {}", oracleCoreAddress);
            generalOracleClient.deployContract();
        }
        log.info("APISample contract address: {}", dappAddress);
        String urlonchain = "";
        String targetUrl = generalOracleConfig.getUrl();
        //urlonchain = apiSampleOracle.getUrl();
        //仅供测试，传入任何一个有效的url均可，示例为http://www.xxxx.com/
        BaseUrl hashUrl = new BaseUrl(HashUrlCrawler.NAME,"http://www.qq.com/");

        ArrayList<GeneralOracleSource> targetSourcceList = new ArrayList<>();
        String targetUrl0 = JsonUtils.toJSONString(hashUrl);
        targetSourcceList.add(new GeneralOracleSource
              (targetUrl0,BigInteger.valueOf(1), ReturnTypeEnum.STRING));


        //BaseUrl simpleUrl = new BaseUrl("URLCrawler","https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new");
        //targetSourcceList.add(new GeneralOracleSource
          //      (simpleUrl.toJSONString(),BigInteger.valueOf(1),BigInteger.valueOf(1)));


        String targetUrl1 = "plain(https://www.random.org/integers/?num=30&min=1&max=1000&col=1&base=10&format=plain&rnd=new)";
        targetSourcceList.add(new GeneralOracleSource(targetUrl1,BigInteger.valueOf(1), ReturnTypeEnum.INT256));

        String targetUrl2 = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
        targetSourcceList.add(new GeneralOracleSource(targetUrl2,BigInteger.valueOf(100),ReturnTypeEnum.INT256));





        for (GeneralOracleSource source : targetSourcceList){
           GeneralResult result  =  generalOracleClient.reqeustSource(source);
           //Assertions.assertNotEquals(res.intValue(),0);
        }
    }

}
