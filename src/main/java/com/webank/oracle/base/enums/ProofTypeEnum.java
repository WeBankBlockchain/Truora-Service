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

    SIGN(1, "Sign by private key"),
    ;

    private int id;
    private String description;


}
