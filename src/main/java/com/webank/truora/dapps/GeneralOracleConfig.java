package com.webank.truora.dapps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "dapps.generaloracle")
public class GeneralOracleConfig {
    String chainId;
    String groupId;

    String contractAddress;
    String url;
    BigInteger timesAmount;
    BigInteger returnType;

    //3个预设url，为了使测试时有较强的确定性，所以，配置为指定名字，不用数组方式
    String url1;
    BigInteger timesAmount1;
    BigInteger returnType1;
    String url2;
    BigInteger timesAmount2;
    BigInteger returnType2;
    String url3;
    BigInteger timesAmount3;
    BigInteger returnType3;

}
