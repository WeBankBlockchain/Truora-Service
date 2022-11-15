package com.webank.truora.dapps;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GeneralOracleSource {
    String url;
    BigInteger timesAmount;
    BigInteger returnType;

    public  GeneralOracleSource(){

    }

    public GeneralOracleSource(String url,BigInteger timesAmount,BigInteger returnType){
        this.url = url;
        this.timesAmount = timesAmount;
        this.returnType = returnType;
    }
}
