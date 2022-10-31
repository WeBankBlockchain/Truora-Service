package com.webank.truora.dapps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Slf4j
@Configuration
@ConfigurationProperties
public class DappsConfig {
    Map<String, Map<String,String>> dapps;
}
