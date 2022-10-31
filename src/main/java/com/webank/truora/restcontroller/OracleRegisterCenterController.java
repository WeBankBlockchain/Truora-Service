package com.webank.truora.restcontroller;


import com.webank.truora.bcos2runner.oracle.OracleRegisterCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/center")
public class OracleRegisterCenterController {

    /*todo 从restful去update有点危险，考虑去掉*/
    @Autowired private OracleRegisterCenterService oracleRegisterCenterService;
/*
    @GetMapping("/list")
    public BaseResponse getOracleCoreAddress(@RequestParam(defaultValue = "1") String chainId,
                                             @RequestParam(defaultValue = "1") String groupId) {
        try {
            if (chainId.isEmpty()|| groupId.isEmpty()){
                return new BaseResponse(ConstantCode.CHAIN_OR_GROUP_ID_PARAM_ERROR);
            }
            return new BaseResponse(ConstantCode.SUCCESS, oracleRegisterCenterService.getOracleServiceList(chainId,groupId));
        } catch (Exception e) {
            return new BaseResponse(ConstantCode.FETCH_ORACLE_SERVICE_LIST_ERROR);
        }
    }*/
/*
    // todo
    @PostMapping("/update")
    public void updateOracleInfo(@RequestBody OracleServiceInfo oracleServiceInfo) {

        oracleRegisterCenterService.updateOracleInfo(oracleServiceInfo.getOperator(),oracleServiceInfo.getUrl());

    }*/
}
