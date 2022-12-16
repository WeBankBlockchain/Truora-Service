package com.webank.truora.dapps;

import com.webank.truora.base.enums.ReturnTypeEnum;
import lombok.Data;

import java.math.BigInteger;

@Data
public class GeneralOracleSource {
    String url;
    BigInteger timesAmount;
    ReturnTypeEnum returnType;

    public  GeneralOracleSource(){

    }

    /*timesAmount : 爬回来的结果乘以一个系数，
    * returnType： 0：大整形， 1：字符串 2： 字节
    * */
    public GeneralOracleSource(String url,BigInteger timesAmount,ReturnTypeEnum returnType){
        this.url = url;
        this.timesAmount = timesAmount;
        this.returnType = returnType;
    }
}
