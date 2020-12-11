package com.webank.oracle.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.properties.EventRegisterProperties;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * return encrypt type to web 0 is standard, 1 is guomi
 */
@Api(value = "/chain", tags = "Query chain and group id")
@Slf4j
@RestController
@RequestMapping(value = "/chain")
public class ChainController {
    @Autowired private EventRegisterProperties eventRegisterProperties;

    @GetMapping("/group/list")
    public BaseResponse query() {
        return new BaseResponse(ConstantCode.SUCCESS, eventRegisterProperties.getChainGroupList());
    }
}
