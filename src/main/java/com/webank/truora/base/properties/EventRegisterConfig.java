package com.webank.truora.base.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * properties of event register.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EventRegisterConfig extends ContractVersion{
    private String platform ="";
    private String chainId = "";
    private String groupId = "";
    private String oracleCoreAddress;
    private String vrfK1CoreAddress;
    private String vrf25519CoreAddress;
    private String fromBlock = "latest";
    private String toBlock = "latest";
    private String keyfile ="";
}

