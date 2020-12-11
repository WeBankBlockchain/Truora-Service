package com.webank.oracle.test.base;

import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.webank.oracle.Application;
import com.webank.oracle.base.properties.EventRegisterProperties;
import com.webank.oracle.base.service.Web3jMapService;
import com.webank.oracle.contract.ContractDeployRepository;
import com.webank.oracle.keystore.KeyStoreService;
import com.webank.oracle.transaction.register.OracleRegisterCenterService;

/**
 *
 */

@SpringBootTest(classes = Application.class)
public class BaseTest {
    @Autowired protected Web3jMapService web3jMapService;
    @Autowired protected EventRegisterProperties eventRegisterProperties;
    @Autowired protected KeyStoreService keyStoreService;
    @Autowired protected ContractDeployRepository contractDeployRepository;
    @Autowired protected OracleRegisterCenterService oracleRegisterCenterService;

    //根据私钥导入账户
    protected Credentials credentials;
    protected Credentials credentialsBob = Credentials.create("2");

    // 生成随机私钥使用下面方法；
    // Credentials credentialsBob =Credentials.create(Keys.createEcKeyPair());
    protected String Bob = "0x2b5ad5c4795c026514f8317c7a215e218dccd6cf";
    protected String Owner = "0x148947262ec5e21739fe3a931c29e8b84ee34a0f";

    protected String Alice = "0x1abc9fd9845cd5a0acefa72e4f40bcfd4136f864";


    protected Web3j getWeb3j(int chainId, int groupId){
         return web3jMapService.getNotNullWeb3j(chainId,groupId);
    }

    protected static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String finalHex = new String(hexChars);
        return finalHex;
    }

}