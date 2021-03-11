/**
 *
 */


package com.webank.oracle.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProofTypeEnum {
    DEFAULT(0, "Default with no proof"),

    VRF(1, "VRF"),
    ;

    private int id;
    private String description;


}
