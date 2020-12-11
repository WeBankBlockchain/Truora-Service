package com.webank.oracle.transaction.register;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.fisco.bcos.web3j.tuples.generated.Tuple10;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@Data
public class OracleServiceInfo {
    private BigInteger index;
    private String oracleServiceAddress;
    private List<String> publicKeyList;
    private String keyHash;
    private String operator;
    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creatTime;
    private BigInteger latestRequstProcessedTime;
    private boolean status;
    private BigInteger processedRequestAmount;


    public static OracleServiceInfo build(Tuple10<BigInteger, String, List<BigInteger>, byte[], String, String,
            BigInteger, BigInteger, Boolean, BigInteger> tuple10){
        OracleServiceInfo oracleServiceInfo = new OracleServiceInfo();
        oracleServiceInfo.index = tuple10.getValue1();
        oracleServiceInfo.oracleServiceAddress = tuple10.getValue2();
        List<BigInteger> publicKey = tuple10.getValue3();

        oracleServiceInfo.keyHash = Hex.encodeHexString(tuple10.getValue4());
        oracleServiceInfo.operator = tuple10.getValue5();
        oracleServiceInfo.url = tuple10.getValue6();
        oracleServiceInfo.latestRequstProcessedTime = tuple10.getValue8();
        oracleServiceInfo.status = tuple10.getValue9();
        oracleServiceInfo.processedRequestAmount = tuple10.getValue10();

        long createTimeValue = tuple10.getValue7().longValue();
        oracleServiceInfo.creatTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(createTimeValue),
                TimeZone.getDefault().toZoneId());

        // public key
        if (CollectionUtils.size(publicKey) == 2) {
            oracleServiceInfo.publicKeyList = Arrays.asList(new String[]{
                    Hex.encodeHexString(publicKey.get(0).toByteArray()),
                    Hex.encodeHexString(publicKey.get(1).toByteArray())
            });
        }

        return oracleServiceInfo;
    }

}