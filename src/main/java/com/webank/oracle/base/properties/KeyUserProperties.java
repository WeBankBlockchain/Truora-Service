package com.webank.oracle.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * properties of key-user.
 */
@Data
@Configuration
@ConfigurationProperties("key-user")
public class KeyUserProperties {
    private String store_file;
}
