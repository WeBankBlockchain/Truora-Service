package com.webank.oracle.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.config.ServerConfig;
import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@RestController
@RequestMapping("server")
public class ServerController {

    @Autowired private ServerConfig serverConfig;

    @GetMapping(value = "version")
    public BaseResponse version() {
        log.info("Request version:[{}]", System.currentTimeMillis());
        return new BaseResponse(ConstantCode.SUCCESS, serverConfig.getVersion());
    }


}