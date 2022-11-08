package com.webank.truora.bcos3runner;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.keystore.KeyStoreInfo;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.OracleFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static com.webank.truora.base.utils.JsonUtils.stringToObj;

/**
 * init a private key.
 */

@Slf4j
public class Bcos3KeyTools  {

    public static KeyStoreInfo readKeyStoreFile(String keyStoreFilePath) throws Exception{
        File keyfile = FileUtils.getFile(keyStoreFilePath);
        if(!keyfile.exists()){
            Resource r =  new  ClassPathResource(keyStoreFilePath);
            keyfile = r.getFile();
        }
        if(!keyfile.exists()){
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION,"key file no exist : "+keyStoreFilePath);
        }
        KeyStoreInfo keyStoreInfo = readKeyStoreFile(keyfile);
        return keyStoreInfo;
    }

    public static KeyStoreInfo readKeyStoreFile(File keyStoreFile) {
        String keyStoreInfoJSON = OracleFileUtils.readFileToString(keyStoreFile);
        KeyStoreInfo keyStoreInfo = stringToObj(keyStoreInfoJSON, KeyStoreInfo.class);
        return keyStoreInfo;
    }

    public static CryptoKeyPair getKeyPairByFile(Client client,String keyfile) throws Exception {
        KeyStoreInfo keyStoreInfo =  Bcos3KeyTools.readKeyStoreFile(keyfile);
        CryptoKeyPair keyPair = client.getCryptoSuite().getKeyPairFactory().createKeyPair(keyStoreInfo.getPrivateKey());
        return keyPair;
    }

    public static CryptoKeyPair getKeyPairByFile(CryptoSuite suite,String keyfile) throws Exception{
        KeyStoreInfo keyStoreInfo =  Bcos3KeyTools.readKeyStoreFile(keyfile);
        CryptoKeyPair keyPair = suite.getKeyPairFactory().createKeyPair(keyStoreInfo.getPrivateKey());
        return keyPair;
    }

    public static CryptoKeyPair getKeyPairByHexkey(CryptoSuite suite,String hexkey) throws Exception{
        CryptoKeyPair keyPair = suite.getKeyPairFactory().createKeyPair(hexkey);
        return keyPair;
    }

}
