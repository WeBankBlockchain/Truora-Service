package com.webank.truora.base.config;

import com.webank.truora.base.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 */

@Slf4j
@Data
@Component
//@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@ConfigurationProperties(prefix = "server.config")
public class ServerConfig {

    /**
     * Server version.
     */
    private String version = "v1.1.0";
    // todo
    private String operator = "oracle operator";
    private String url = "http://localhost:5120";

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Server init with config: [{}] ", JsonUtils.toJSONString(this));
    }
}