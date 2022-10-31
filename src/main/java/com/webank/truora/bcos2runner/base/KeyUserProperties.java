package com.webank.truora.bcos2runner.base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * properties of key-user.
 */
@Data
@Configuration
@ConfigurationProperties("fiscobcos2userkey")
public class KeyUserProperties {
    private String store_file;
}
