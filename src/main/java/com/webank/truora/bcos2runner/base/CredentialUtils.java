/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.truora.bcos2runner.base;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.crypto.gm.sm2.crypto.asymmetric.SM2KeyGenerator;
import org.fisco.bcos.web3j.crypto.gm.sm2.crypto.asymmetric.SM2PrivateKey;
import org.fisco.bcos.web3j.crypto.gm.sm2.crypto.asymmetric.SM2PublicKey;
import org.fisco.bcos.web3j.crypto.gm.sm2.util.encoders.Hex;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fisco.bcos.web3j.crypto.Keys.PUBLIC_KEY_LENGTH_IN_HEX;

@Log4j2
public class CredentialUtils extends GenCredential {

    public static ECKeyPair createKeyPair(String privKey) {
        if (EncryptType.encryptType == 1) return createGuomiKeyPair(privKey);
        return createECDSAKeyPair(privKey);
    }

    public static ECKeyPair createKeyPair() {
        // use guomi
        if (EncryptType.encryptType == 1) {
            return createGuomiKeyPair();
        }
        // default use ECDSA
        return createECDSAKeyPair();
    }

    public static List<BigInteger> calculatePubkeyFromPrivkey(String skhex) {
        Credentials user = Credentials.create(skhex);
        // gm address  0x1f609497612656e806512fb90972d720e2e508b5
        //   address   0xc950b511a1a6a1241fc53d5692fdcbed4f766c65
        String pk = user.getEcKeyPair().getPublicKey().toString(16);

        return calculatFromPubkey(pk);
    }


    public static List<BigInteger> calculatFromPubkey(String pk) {

        int len = pk.length();
        String pkx = pk.substring(0, len / 2);
        String pky = pk.substring(len / 2);
        BigInteger Bx = new BigInteger(pkx, 16);
        BigInteger By = new BigInteger(pky, 16);
        List<BigInteger> ilist = new ArrayList<>();
        ilist.add(Bx);
        ilist.add(By);
        log.info("calculatFromPubkey {},List:{}",pk,ilist);
        return ilist;
    }



    public static List<BigInteger> getPublicKeyList(String publicKey) {
        if (StringUtils.startsWith(publicKey,"0x")){
            publicKey = publicKey.substring(2);
        }
        if (StringUtils.length(publicKey) != PUBLIC_KEY_LENGTH_IN_HEX) {
            return null;
        }

        String prePublicKeyStr = publicKey.substring(0, 64);
        String postPublicKeyStr = publicKey.substring(64);

        return Arrays.asList(new BigInteger[]{
            new BigInteger(prePublicKeyStr, 16),
            new BigInteger(postPublicKeyStr, 16)
        });
    }


    private static ECKeyPair createECDSAKeyPair() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            return keyPair;
        } catch (Exception e) {
            log.error("create keypair of ECDSA failed, error msg:" + e.getMessage());
            return null;
        }
    }

    private static ECKeyPair createGuomiKeyPair(String privKey) {
        SM2KeyGenerator generator = new SM2KeyGenerator();
        final KeyPair keyPairData = generator.generateKeyPair(privKey);
        if (keyPairData != null) {
            return genEcPairFromKeyPair(keyPairData);
        }
        return null;
    }

    private static ECKeyPair genEcPairFromKeyPair(KeyPair keyPairData) {
        try {
            SM2PrivateKey vk = (SM2PrivateKey) keyPairData.getPrivate();
            SM2PublicKey pk = (SM2PublicKey) keyPairData.getPublic();
            final byte[] publicKey = pk.getEncoded();
            final byte[] privateKey = vk.getEncoded();

            // System.out.println("===public:" + Hex.toHexString(publicKey));
            // System.out.println("===private:" + Hex.toHexString(privateKey));
            BigInteger biPublic = new BigInteger(Hex.toHexString(publicKey), 16);
            BigInteger biPrivate = new BigInteger(Hex.toHexString(privateKey), 16);

            // System.out.println("---public:" + biPublic.toString(16));
            // System.out.println("---private:" + biPrivate.toString(16));

            ECKeyPair keyPair = new ECKeyPair(biPrivate, biPublic);
            return keyPair;
        } catch (Exception e) {
            log.error("GmUtils create ec_keypair of guomi failed, error msg:" + e.getMessage());
            return null;
        }
    }

    private static ECKeyPair createECDSAKeyPair(String privKey) {
        try {
            BigInteger bigPrivKey = new BigInteger(privKey, 16);
            ECKeyPair keyPair = ECKeyPair.create(bigPrivKey);
            return keyPair;
        } catch (Exception e) {
            log.error("GmUtils create keypair of ECDSA failed, error msg:" + e.getMessage());
            return null;
        }
    }
}
