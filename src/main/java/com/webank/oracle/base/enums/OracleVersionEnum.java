/**
 *
 */


package com.webank.oracle.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OracleVersionEnum {
    ORACLIZE_4000(4000,"eg. Oraclize init version"),




    VRF_4000(4000,"eg. VRF init version")
    ;

    private int id;
    private String description;

}
