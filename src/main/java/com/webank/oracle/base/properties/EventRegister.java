package com.webank.oracle.base.properties;

import lombok.Data;

/**
 * properties of event register.
 */
@Data
public class EventRegister {
    private int chainId = 1;
    private int group = 1;
    private String oracleCoreContractAddress;
    private String vrfContractAddress;
    private String fromBlock = "latest";
    private String toBlock = "latest";
}
