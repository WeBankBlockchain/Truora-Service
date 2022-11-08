/**
 *
 */


package com.webank.truora.base.enums;

import com.webank.truora.base.exception.FullFillException;
import com.webank.truora.base.utils.CryptoUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

import static com.webank.truora.base.enums.ReqStatusEnum.UNSUPPORTED_RETURN_TYPE_ERROR;

/**
 *
 */
@Getter
@AllArgsConstructor
public enum ReturnTypeEnum {
    INT256(0, "Big Integer"),
    STRING(1, "String"),
    BYTES(2, "Bytes"),
    ;

    public  int id;
    public  String type;

    public static ReturnTypeEnum get(BigInteger type) {
        if (type == null) {
            return INT256;
        }
        for (ReturnTypeEnum returnType : ReturnTypeEnum.values()) {
            if (type.intValue() == returnType.getId()) {
                return returnType;
            }
        }
        return INT256;
    }

    //两个帮助函数
    //timesAmount的意思是在传入的数基础上再乘以一个系数
    public static  byte[] convert2Bytes(BigInteger returnType,Object data,BigInteger timesAmount) throws Exception
    {
        return convert2Bytes(get(returnType),data,timesAmount);
    }

    public static  byte[] convert2Bytes(ReturnTypeEnum returnTypeEnum,Object data,BigInteger timesAmount) throws Exception {

        byte[] bytesValue = null;
        switch (returnTypeEnum) {
            case INT256:
                String strValue =  String.valueOf(data);
                BigInteger bi =null;
                //先去掉0x前缀
                if (strValue.startsWith("0x")) {
                    strValue = strValue.substring(2);
                }
                if(strValue.length() == 64)
                {
                    //长度是64，则有可能是hex的32字节，256位整型数，则仅支持正数
                    //signum=1表示是无符号整数
                    bi = new BigInteger(1, Hex.decodeHex(strValue));
                }else {
                    //直接从字符串转，可能是负数,如 “123”,或“-123”
                    strValue = strValue.trim();
                    //处理返回的数据里可能有换行，多行的情况，只取第一行
                    if(strValue.indexOf("\n")>=0) {
                        String[] l = strValue.split("\n");
                        strValue = l[0];
                    }
                    bi = new BigInteger(strValue);
                }

                //BigDecimal bd = new BigDecimal(strValue);
                BigInteger afterTimesAmount = bi
                        .multiply(timesAmount);
                bytesValue = CryptoUtil.toBytes(afterTimesAmount);
                break;
            case STRING:
                bytesValue = String.valueOf(data).getBytes();
                break;
            case BYTES:
                bytesValue = CryptoUtil.toBytes(data);
                break;
            default:
                throw new FullFillException(UNSUPPORTED_RETURN_TYPE_ERROR, returnTypeEnum);
        }
        return bytesValue;
    }

}
