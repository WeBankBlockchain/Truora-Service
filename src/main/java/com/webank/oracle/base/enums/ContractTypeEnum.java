/**
 *
 */


package com.webank.oracle.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractTypeEnum {
    ORACLE_CORE(0, "oracle core contract"),
    VRF(1, "VRF"),
    ;

    private int id;
    private String type;


}
