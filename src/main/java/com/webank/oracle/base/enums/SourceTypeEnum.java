/**
 *
 */


package com.webank.oracle.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SourceTypeEnum {
    URL(0, "url","eg. Fetch value from url"),

    VRF(1, "vrf","eg. Fetch a random value by VRF")
    ;

    private int id;
    private String type;
    private String description;


}
