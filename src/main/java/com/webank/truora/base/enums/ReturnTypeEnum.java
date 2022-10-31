/**
 *
 */


package com.webank.truora.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

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

}
