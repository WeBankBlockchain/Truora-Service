package com.webank.truora.test.bcos3;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

@Slf4j
public class TestTools  {


    //af19de0592fa0ac5cc11a8662646317df0eda0631069017d875607b98d26a750
    @Test
    public  void testHashToInt() throws Exception{
        String hashStr = "0xaf19de0592fa0ac5cc11a8662646317df0eda0631069017d875607b98d26a750";
        BigInteger bi = new BigInteger(1, Hex.decodeHex(hashStr.substring(2)));
        log.info("Big integer {}",bi);
    }
}
