package com.webank.truora.test.bcos3;

import com.webank.truora.bcos3runner.Bcos3SdkHelper;
import com.webank.truora.contract.bcos3.ECVerify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.crypto.signature.ECDSASignatureResult;
import org.fisco.bcos.sdk.v3.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

@Slf4j
public class ECVerifyTest  {

    String sampleKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
    @Test
    public void testVerify() throws Exception {
        Bcos3SdkHelper clientHelper = new Bcos3SdkHelper(null);
        Client client = clientHelper.getSdk().getClient("group0");

        //clientHelper.testClient();
        CryptoKeyPair keyPairFactory = client.getCryptoSuite().getKeyPairFactory();
        CryptoKeyPair keyPair = keyPairFactory.createKeyPair(sampleKey);
        ECVerify ecVerify = ECVerify.deploy(client,keyPair);
        String address = keyPair.getAddress();
        String publicKey = keyPair.getHexPublicKey();
        log.info("address : {}, publickey: {}",address,publicKey);

        String textmessage = "this is a test message";
        String hexHashMessage = client.getCryptoSuite().hash(textmessage);
        log.info("text message :[{}],hash is :[{}]",textmessage,hexHashMessage);
        SignatureResult sigRes = client.getCryptoSuite().sign(hexHashMessage,keyPair);
        log.info("sigRes: {}",sigRes.convertToString());
        log.info("R:[{}]\nS:[{}]",
                Hex.encodeHexString(sigRes.getR()),
                Hex.encodeHexString(sigRes.getS()));

        log.info("sig bytes len: {}",sigRes.getSignatureBytes().length);
        ECDSASignatureResult ecdsaSigRes = new ECDSASignatureResult(sigRes.convertToString());
        log.info("R:[{}]\nS:[{}]\nV:[{}]",
                Hex.encodeHexString(ecdsaSigRes.getR()),
                Hex.encodeHexString(ecdsaSigRes.getS()),
                ecdsaSigRes.getV()
                );
        log.info("address:{} ,hash:{}",address,hexHashMessage);
        log.info("call ECVerify latest Verify {} hex://{} {} hex://{} hex://{}",address,hexHashMessage,ecdsaSigRes.getV(),
                Hex.encodeHexString(ecdsaSigRes.getR()),
                Hex.encodeHexString(ecdsaSigRes.getS()));
        log.info("call ECVerify latest recoverSigner  hex://{} {} hex://{} hex://{}",   hexHashMessage,ecdsaSigRes.getV(),
                Hex.encodeHexString(ecdsaSigRes.getR()),
                Hex.encodeHexString(ecdsaSigRes.getS()));

        boolean res = client.getCryptoSuite().verify(publicKey,hexHashMessage,sigRes.convertToString());
        log.info("local verify result {}",res);

        TransactionReceipt receipt;

        receipt = ecVerify.recover_vrs(Hex.decodeHex(hexHashMessage),
                BigInteger.valueOf(ecdsaSigRes.getV()+27),
                ecdsaSigRes.getR(),
                ecdsaSigRes.getS());
        Tuple1<String> sRes  = ecVerify.getRecover_vrsOutput(receipt);
        log.info("contract recoverSigner result {}",sRes);


        receipt = ecVerify.recover_sig(Hex.decodeHex(hexHashMessage),ecdsaSigRes.getSignatureBytes());
        sRes = ecVerify.getRecover_sigOutput(receipt);
        log.info("contract recover_sig result {}",sRes);


        Tuple1<Boolean> vRes;

        receipt = ecVerify.verify_vrs(address,
                Hex.decodeHex(hexHashMessage),
                BigInteger.valueOf(ecdsaSigRes.getV()+27),
                ecdsaSigRes.getR(),
                ecdsaSigRes.getS());
        vRes = ecVerify.getVerify_vrsOutput(receipt);
        log.info("contract verify_vrs result {}",vRes);

        receipt = ecVerify.verify_sig(address,
                Hex.decodeHex(hexHashMessage),
                ecdsaSigRes.getSignatureBytes());
        vRes = ecVerify.getVerify_vrsOutput(receipt);
        log.info("contract verify_sig result {}",vRes);



    }


}
