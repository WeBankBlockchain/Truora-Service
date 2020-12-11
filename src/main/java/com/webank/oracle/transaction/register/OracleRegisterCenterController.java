package com.webank.oracle.transaction.register;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;

@RestController
@RequestMapping(value = "/center")
public class OracleRegisterCenterController {

    @Autowired private OracleRegisterCenterService oracleRegisterCenterService;

    @GetMapping("/list")
    public BaseResponse getOracleCoreAddress(@RequestParam(defaultValue = "1") int chainId,
                                             @RequestParam(defaultValue = "1") int groupId) {
        try {
            if (chainId <= 0 || groupId <= 0){
                return new BaseResponse(ConstantCode.CHAIN_OR_GROUP_ID_PARAM_ERROR);
            }
            return new BaseResponse(ConstantCode.SUCCESS, oracleRegisterCenterService.getOracleServiceList(chainId,groupId));
        } catch (Exception e) {
            return new BaseResponse(ConstantCode.FETCH_ORACLE_SERVICE_LIST_ERROR);
        }
    }
}
