/**
 *
 */


package com.webank.oracle.base.enums;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    private int id;
    private String type;

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
