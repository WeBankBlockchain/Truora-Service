package com.webank.truora.dapps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
//@PropertySource(value = {"classpath:application-dapps.yml",}, encoding = "utf-8")
//@Configuration
//@ConfigurationProperties
public class DappsConfig {
    Map<String, Map<String,String>> dapps;
}
