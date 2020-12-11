package com.webank.oracle.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sdk")
public class SdkProperties {
    private String orgName;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    public int timeout = 30000;
    private int keepAlive;
    private int encryptType;//0:standard, 1:guomi
}
