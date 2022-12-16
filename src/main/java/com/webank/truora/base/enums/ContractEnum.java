/**
 *
 */


package com.webank.truora.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractEnum {
    ORACLE_CORE(0, "oracleCore"),
    VRF(1, "VRF"),
    ;

    private int type;
    private String name;


}
