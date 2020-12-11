/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.oracle.keystore;

import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.keystore.KeyStoreInfo;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.utils.CommonUtils;
import com.webank.oracle.base.utils.CredentialUtils;
import com.webank.oracle.base.utils.OracleFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.crypto.Sign;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.utils.ByteUtil;
import org.fisco.bcos.web3j.utils.Numeric;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.webank.oracle.base.utils.JsonUtils.stringToObj;
import static com.webank.oracle.base.utils.JsonUtils.toJSONString;


/**
 * KeyStoreService.
 * 2019/11/26 support guomi： create keyPair, useAes=>aes or sm4 encrypt
 */
@Slf4j
@Service
public class KeyStoreService {

    private static final int PUBLIC_KEY_LENGTH_IN_HEX = 128;
    private static KeyStoreInfo keyStoreInfo = null;

    /**
     * get getKeyStoreInfo.
     *
     * @return
     */
    public KeyStoreInfo getKeyStoreInfo() {
        return keyStoreInfo;
    }

    /**
     * createKeyStore.
     */
    public void createKeyStore(File keyStoreFile) {
        log.info("start createKeyStore....");

        // create keyPair(support guomi)
        try {
            // TODO upgrade in web3sdk 2.1.3+
            ECKeyPair keyPair = CredentialUtils.createKeyPair();
            KeyStoreInfo keyStoreInfo = keyStoreInfoFromKeyPair(keyPair);

            //write to file

            OracleFileUtils.writeConstantToFile(keyStoreFile, toJSONString(keyStoreInfo));
        } catch (Exception ex) {
            throw new OracleException(ConstantCode.NEW_KEY_STORE_FAIL, ex);
        }
    }


    /**
     * read keyStore file.
     */
    public void readKeyStoreFile(File keyStoreFile) {
        String keyStoreInfoJSON = OracleFileUtils.readFileToString(keyStoreFile);
        keyStoreInfo = stringToObj(keyStoreInfoJSON, KeyStoreInfo.class);
    }


    /**
     * convert ECKeyPair to KeyStoreInfo.
     */
    private KeyStoreInfo keyStoreInfoFromKeyPair(ECKeyPair keyPair) {
        KeyStoreInfo keyStoreInfo = new KeyStoreInfo()
                .setAddress("0x" + Keys.getAddress(keyPair.getPublicKey()))
                .setPrivateKey(Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()))
                .setPublicKey(Numeric.toHexStringWithPrefixZeroPadded(keyPair.getPublicKey(), PUBLIC_KEY_LENGTH_IN_HEX));
        log.debug("keyPair2KeyStoreInfo result:{} ", toJSONString(keyStoreInfo));
        return keyStoreInfo;
    }


    /**
     * get credential to send transaction
     * 2019/11/26 support guomi
     */
    public Credentials getCredentials() throws OracleException {
        return GenCredential.create(keyStoreInfo.getPrivateKey());
    }


    public String sign(String constantStr) throws BaseException {
        log.info("start sign....");
        // signature
        Credentials credentials = getCredentials();
        byte[] encodedData = ByteUtil.hexStringToBytes(constantStr);
        Sign.SignatureData signatureData = Sign.getSignInterface().signMessage(
                encodedData, credentials.getEcKeyPair());
        String signDataStr = CommonUtils.signatureDataToString(signatureData);
        log.info("finish sign. signDataStr:{}", signDataStr);
        return signDataStr;
    }
}

