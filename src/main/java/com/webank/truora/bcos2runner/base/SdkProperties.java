package com.webank.truora.bcos2runner.base;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@ConfigurationProperties("fiscobcos2sdk")
public class SdkProperties {
    private String orgName;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    public int timeout = 30000;
    private int idleTimeout = 20;
    private int keepAlive;
    private int encryptType;//0:standard, 1:guomi
}
