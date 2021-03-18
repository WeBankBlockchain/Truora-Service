package com.webank.oracle.transaction.oracle;


import java.util.List;

import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webank.oracle.base.pojo.vo.BaseResponse;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import com.webank.oracle.base.properties.EventRegister;
import com.webank.oracle.base.properties.EventRegisterProperties;
import com.webank.oracle.base.utils.JsonUtils;

@RestController
@RequestMapping(value = "/oracle")
public class OracleController {

    //    @Autowired private ContractDeployService contractDeployService;
    @Autowired private EventRegisterProperties eventRegisterProperties;

    @GetMapping("/address")
    public BaseResponse getOracleCoreAddress(@RequestParam(defaultValue = "0") int chainId,
                                             @RequestParam(defaultValue = "0") int groupId,
                                             @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumberParam,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSizeParam
    ) {
        // 默认从第 1 页开始
        int pageNumber = pageNumberParam <= 0 ? 0 : pageNumberParam - 1;
        // 默认一页 10 条，不能超过 20 条每页
        int pageSize = pageSizeParam <= 0 || pageSizeParam > 20 ? 10 : pageSizeParam;

        String listToJson = JsonUtils.toJSONString(eventRegisterProperties.getByChainIdAndGroupId(chainId, groupId));

        List<EventRegister> eventRegisterList = JsonUtils.toJavaObjectList(listToJson,EventRegister.class);
        if (EncryptType.encryptType == 1){
            for (EventRegister eventRegister : eventRegisterList) {
                eventRegister.setVrfContractAddress("国密链暂时不支持 VRF 功能!!");
            }
        }

        return BaseResponse.pageResponse(ConstantCode.SUCCESS, eventRegisterList, eventRegisterList.size());

//        Page<ContractDeploy> contractDeploys = contractDeployService.selectContractDeployList(chainId, groupId, pageNumber, pageSize);
//
//        return BaseResponse.pageResponse(ConstantCode.SUCCESS, contractDeploys.getContent(), contractDeploys.getTotalElements());
    }
}
