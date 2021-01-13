package com.webank.oracle.transaction.oracle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.properties.EventRegisterProperties;

@RestController
@RequestMapping(value = "/oracle")
public class OracleController {

    @Autowired
    private EventRegisterProperties eventRegisterProperties;

    @GetMapping("/address")
    public BaseResponse getOracleCoreAddress(@RequestParam(defaultValue = "0") int chainId,
                                             @RequestParam(defaultValue = "0") int groupId) {
        return new BaseResponse(ConstantCode.SUCCESS, eventRegisterProperties.getByChainIdAndGroupId(chainId,groupId));
    }
}
