package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.oracle.OracleCoreClientV3;
import com.webank.truora.bcos3runner.oracle.OracleCoreEventCallbackV3;
import com.webank.truora.contract.bcos3.OracleCore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.v3.codec.ContractCodec;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Random;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class OracleCoreTest {

    //@Autowired
    //Bcos3SdkFactory bcos3SdkFactory;


    @Test
    public void testOracleCore() throws Exception{
        log.info("Start OracleCore Test for BCOS3");
        String chainId = "Chain0";
        String groupId = "group0";
        String configFileName = "bcos3sdk_config.toml";
        URL configUrl = Bcos3Client.class.getClassLoader().getResource(configFileName);
        String configFile = configUrl.getPath();
        BcosSDK sdk = BcosSDK.build(configFile);

        Client client = sdk.getClient(groupId);
        BlockNumber number = client.getBlockNumber();
        log.info("Blocknumber :"+number.getBlockNumber());
        CryptoKeyPair keyPair  = client.getCryptoSuite().getCryptoKeyPair();

        OracleCore oracleCore = OracleCore.deploy(client,keyPair,chainId,groupId);
        log.info("Deploy oracleCore address: "+oracleCore.getContractAddress());
        /*    address _callbackAddress,
    uint256 _nonce,
    string calldata _url,
    uint256 _timesAmount,
    uint256 _expiryTime,
    bool _needProof,
    uint _returnType*/
        /*
        String _callbackAddress, BigInteger _nonce, String _url,
                java.math.BigInteger _timesAmount, java.math.BigInteger _expiryTime, Boolean _needProof,
                java.math.BigInteger _returnType*/
        String _callAddress = "0x0313495c42548f29ba594b30d5f4d327ce619c14";
        BigInteger _nonce = BigInteger.valueOf(new Random().nextInt(100000));
        String _url = "plain(https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new)";
        BigInteger _timesAmount = BigInteger.valueOf(30);
        BigInteger _expiryTime = BigInteger.valueOf(31);
        Boolean _needProof = false;
        BigInteger _returnType = BigInteger.valueOf(1);
        TransactionReceipt receipt = oracleCore.query(_callAddress,_nonce,_url,_timesAmount,_expiryTime,_needProof,_returnType);
        log.info("receipt static "+receipt.getStatus());
        log.info("receipt logs "+receipt.getLogEntries());
        List<TransactionReceipt.Logs> logs = receipt.getLogEntries();
        TransactionReceipt.Logs receiptlog = logs.get(0);

        ContractCodec codec = new ContractCodec(client.getCryptoSuite(),false);
        EventLog eventLog = new EventLog();
        eventLog.setAddress(receiptlog.getAddress());
        eventLog.setLogIndex("0");
        eventLog.setData(receiptlog.getData());
        eventLog.setTopics(receiptlog.getTopics());
        eventLog.setBlockNumber(receiptlog.getBlockNumber());
        eventLog.setTransactionHash(receipt.getTransactionHash());
        eventLog.setTransactionIndex("0");
        log.info("Topic is "+eventLog.getTopics().get(0));
        List<String>  decodeResult = codec.decodeEventToString(OracleCore.ABI,OracleCore.ORACLEREQUEST_EVENT.getName(),eventLog);
        //codec.decodeEventByTopic(OracleCore.ABI,eventLog.getTopics().get(0),eventLog);
        log.info("decode : "+decodeResult);
        OracleCore.OracleRequestEventResponse response = OracleCoreEventCallbackV3.fromDecodeString(decodeResult);

        log.info("response coreAddress:" +response.coreAddress);
        log.info("response callbackAddr:" +response.callbackAddr);

        log.info("response requestId len "+response.requestId.length);
        log.info("response requestId:" + Hex.encodeHexString(response.requestId));
        log.info("response url:" +response.url);
        log.info("response expiration:" +response.expiration);
        log.info("response needProof:" +response.needProof);
        log.info("response returnType:" +response.returnType);

        File f =     new File(".");
        log.info("getAbsolutePath"+f.getAbsolutePath());
        log.info("getCanonicalPath"+f.getCanonicalPath());
        OracleCoreClientV3 oracleClient = new OracleCoreClientV3();
        String url = response.url;
        String result = oracleClient.doGetHttpResut(url);
        log.info("Http Result: "+result);



    }
}
