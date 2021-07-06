package com.webank.oracle.test.base;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.webank.oracle.Application;
import com.webank.oracle.base.properties.ContractVersion;
import com.webank.oracle.base.properties.EventRegisterProperties;
import com.webank.oracle.base.service.Web3jMapService;
import com.webank.oracle.base.utils.CryptoUtil;
import com.webank.oracle.contract.ContractDeployRepository;
import com.webank.oracle.history.ReqHistoryRepository;
import com.webank.oracle.keystore.KeyStoreService;
import com.webank.oracle.transaction.register.OracleRegisterCenterService;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 *
 */
@Slf4j
@SpringBootTest(classes = Application.class)
public class BaseTest {
    @Autowired protected Web3jMapService web3jMapService;
    @Autowired protected EventRegisterProperties eventRegisterProperties;
    @Autowired protected KeyStoreService keyStoreService;
    @Autowired protected ContractDeployRepository contractDeployRepository;
    @Autowired protected ReqHistoryRepository reqHistoryRepository;
    @Autowired protected OracleRegisterCenterService oracleRegisterCenterService;
    @Autowired protected ContractVersion contractVersion;



    BigInteger gasPrice = new BigInteger("1");
    BigInteger gasLimit = new BigInteger("2100000000");


    protected ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);


    //随机生成
    protected Credentials credentials;

    //根据私钥导入账户
    protected Credentials credentialsDaMing = Credentials.create("1");
    protected Credentials credentialsBob = Credentials.create("2");
    protected Credentials credentialsAlice = Credentials.create("3");

    // 生成随机私钥使用下面方法；
    // Credentials credentialsBob =Credentials.create(Keys.createEcKeyPair());

    protected String DaMing = credentialsDaMing.getAddress();//
    protected String Bob = credentialsBob.getAddress();//
    protected String Alice = credentialsAlice.getAddress();
    protected String Owner;




    @PostConstruct
    private void  init() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        credentials =Credentials.create(Keys.createEcKeyPair());
        Owner = credentials.getAddress();
    }


//    @Autowired
//    private Flyway flyway;

//    @BeforeEach
//    public void setUp()   {
//
//      //  flyway.setBaselineOnMigrate(true);
//        flyway.clean();
//        flyway.migrate();
//    }
    
    protected Web3j getWeb3j(int chainId, int groupId){
         return web3jMapService.getNotNullWeb3j(chainId,groupId);
    }


    public static byte[] calculateTheHashOfPK(String skhex) {
        Credentials user = Credentials.create(skhex);
        // gm address  0x1f609497612656e806512fb90972d720e2e508b5
        //   address   0xc950b511a1a6a1241fc53d5692fdcbed4f766c65
        String pk = user.getEcKeyPair().getPublicKey().toString(16);

        int len = pk.length();
        String pkx = pk.substring(0,len/2);
        String pky = pk.substring(len/2);
        BigInteger Bx = new BigInteger(pkx,16);
        BigInteger By = new BigInteger(pky,16);

        return CryptoUtil.soliditySha3(Bx,By);
    }


}