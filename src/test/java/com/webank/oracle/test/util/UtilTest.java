package com.webank.oracle.test.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.webank.oracle.base.utils.CredentialUtils;
import com.webank.oracle.base.utils.CryptoUtil;
import com.webank.oracle.base.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.webank.oracle.base.utils.JsonUtils.stringToJsonNode;
import static com.webank.oracle.base.utils.JsonUtils.toList;
import static com.webank.oracle.test.transaction.VRF.VRFTest.bytesToHex;

@Slf4j
public class UtilTest {


    @Test
    public void numberTest() {
        BigInteger x = new BigInteger("c6047f9441ed7d6d3045406e95c07cd85c778e4b8cef3ca7abac09b95c709ee5", 16);
        BigInteger y = new BigInteger("1ae168fea63dc339a3c58419466ceaeef7f632653266d0e1236431a950cfe52a", 16);
        log.info("x {}" , x);
        log.info("y {}" , y);
        BigInteger z = new BigInteger("22108724849966695483138513023527230203911536283199945193882728796528949515038");
        log.info("z {}" , z.toString(16));

        Credentials user = Credentials.create("b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6");
        // gm address  0x1f609497612656e806512fb90972d720e2e508b5
        //   address   0xc950b511a1a6a1241fc53d5692fdcbed4f766c65
        log.info(user.getAddress());
        log.info("pub: " ,user.getEcKeyPair().getPublicKey());
        String pk = user.getEcKeyPair().getPublicKey().toString(16);
        int len = pk.length();
        String pkx = pk.substring(0, len / 2);
        String pky = pk.substring(len / 2);
        BigInteger Bx = new BigInteger(pkx, 16);
        BigInteger By = new BigInteger(pky, 16);
        log.info(bytesToHex(CryptoUtil.soliditySha3(Bx, By)));

    }


    @Test
    public void stringTest() {

        String argValue = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0, left);
        String url = argValue.substring(left + 1, right);
        log.info(header);
        log.info(url);

        String argValue1 = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        if (StringUtils.isBlank(argValue1) || argValue1.endsWith(")")) {
            log.info("*******");

        }
        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        // List<Object> httpResultIndexList = subFiledValueForHttpResultIndex(argValue);

    }

    @Test
    public void httpsTest() throws Exception, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // String argValue = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";

        String argValue = "json(https://api.kraken.com/0/public/Ticker?pair=ETHXBT).result.XETHXXBT.c.0";
        //      String argValue = "json(https://api.apiopen.top/EmailSearch?number=1012002).result";

        //   String argValue = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";

        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0, left);
        String url = argValue.substring(left + 1, right);
        List<String> httpResultIndexList = subFiledValueForHttpResultIndex(argValue);
        log.info("********* begin");
//            String result = HttpsUtil.get(url);
//            log.info("*********" + result);
//            Object o = getValueByKeys(result, httpResultIndexList);
//            log.info(o);
        // Object o =  HttpService.getObjectByUrlAndKeys(url,header,httpResultIndexList);
        String result = HttpUtil.get(url);
        log.info(result);
    }

    @Test
    public void testGetPublicKeyListFromString() {
        String publicKey = "0x3e5a96d3f70e010d7e850e500d7a370b2f06a52ca993ddbebfa5018721ab04c1fccf0c9582798ef5f61f612cbb49ca07381b8fcc4e692c2d65b9aeb12480f029";
        List<BigInteger> publicKeyList = CredentialUtils.getPublicKeyList(publicKey);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(publicKeyList));
        for (BigInteger key : publicKeyList) {
            log.info("pk : {}",Hex.encodeHex(key.toByteArray()));
        }
    }


    private List<String> subFiledValueForHttpResultIndex(String argValue) {
        if (StringUtils.isBlank(argValue) || argValue.endsWith(")")) {
            return Collections.EMPTY_LIST;
        }

        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }

    private Object getValueByKeys(String resultString, List<String> keyList) {
        if (resultString == null || keyList == null || keyList.size() == 0) return resultString;

        Object finalResult = stringToJsonNode(resultString);
        for (String key : keyList) {
            finalResult = getValueByKey(finalResult, key);
        }
        return finalResult;
    }

    private Object getValueByKey(Object jsonNode, String key) {
        if (jsonNode instanceof ArrayNode) {
            List<Object> jsonArray = toList(jsonNode);
            return jsonArray.get(Integer.valueOf(String.valueOf(key)));
        }
        try {
            JsonNode jsonNode1 = (JsonNode) jsonNode;
            return jsonNode1.get(key);
        } catch (Exception ex) {
            return jsonNode;
        }
    }

}
