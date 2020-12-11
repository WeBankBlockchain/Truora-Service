package com.webank.oracle.base.properties;

import java.math.BigInteger;

import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("constant")
public class ConstantProperties {

    /**
     * Gas 常量配置。
     */
    public static final BigInteger GAS_PRICE = new BigInteger("100000000");
    public static final BigInteger GAS_LIMIT = new BigInteger("2100000000");
    public static final BigInteger INITIAL_WEI_VALUE = new BigInteger("0");
    public static ContractGasProvider GAS_PROVIDER = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);

    public static final String TYPE_CONSTRUCTOR = "constructor";
    public static final String TYPE_FUNCTION = "function";
    public static final String TYPE_EVENT = "event";

    public static final int MAX_ERROR_LENGTH = 512;

    public static String version;
    public static int chainId;

    private int connectTimeout = 5000;
    private int readTimeout = 5000;
    private int writeTimeout = 5000;

    /**
     * Oracle register contract name and version for CNS.
     */
    private String registerContractName = "register";
    private String registerContractVersion = "1.0";
    //todo
    private String registerContractNameAndVersion = String.format("%s:%s", registerContractName, registerContractVersion);
}
