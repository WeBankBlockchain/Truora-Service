package com.webank.oracle.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * properties of event register.
 */
@Data
@Configuration
@ConfigurationProperties("contract.version")
public class ContractVersion {
    protected String oracleCoreVersion;
    protected String vrfCoreVersion;
}
