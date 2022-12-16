package com.webank.truora.dapps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "dapps.apisample")
public class ApiSampleConfig {
    String chainId;
    String groupId;
    String contractAddress;
    String url;
}
