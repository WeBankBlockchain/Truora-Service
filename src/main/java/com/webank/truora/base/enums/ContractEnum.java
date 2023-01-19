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
    VRF_K1_CORE(1, "VRFK1Core"),
    VRF_25519_CORE(2, "VRF25519Core"),
    ;

    private int type;
    private String name;


}
