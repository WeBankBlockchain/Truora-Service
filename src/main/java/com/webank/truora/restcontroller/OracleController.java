package com.webank.truora.restcontroller;


import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.properties.EventRegisterConfig;
import com.webank.truora.bcos2runner.base.EventRegisterProperties;
import com.webank.truora.bcos3runner.Bcos3EventRegisterFactory;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/oracle")
public class OracleController {

    //    @Autowired private ContractDeployService contractDeployService;
    @Autowired(required = false) private EventRegisterProperties eventRegisterProperties;
    @Autowired(required = false) private Bcos3EventRegisterFactory bcos3EventRegisterFactory;

    @GetMapping("/address")
    public BaseResponse getOracleCoreAddress(@RequestParam(defaultValue = "") String chainId,
                                             @RequestParam(defaultValue = "") String groupId,
                                             @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumberParam,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSizeParam
    ) {
        // 默认从第 1 页开始
        int pageNumber = pageNumberParam <= 0 ? 0 : pageNumberParam - 1;
        // 默认一页 10 条，不能超过 20 条每页
        int pageSize = pageSizeParam <= 0 || pageSizeParam > 20 ? 10 : pageSizeParam;

        List<EventRegisterConfig>  configList = new ArrayList();
        if (eventRegisterProperties!=null) {
            configList.addAll(eventRegisterProperties.getByChainIdAndGroupId(chainId, groupId));
        }
        if (bcos3EventRegisterFactory!=null) {
            configList.addAll(bcos3EventRegisterFactory.getConfigByChainIdAndGroupId(chainId, groupId));
        }

        if (EncryptType.encryptType == 1){
            for (EventRegisterConfig eventRegister : configList) {
                eventRegister.setVrfK1CoreAddress("国密链暂时不支持 VRF 功能!!");
            }
        }

        return BaseResponse.pageResponse(ConstantCode.SUCCESS, configList, configList.size());

//        Page<ContractDeploy> contractDeploys = contractDeployService.selectContractDeployList(chainId, groupId, pageNumber, pageSize);
//
//        return BaseResponse.pageResponse(ConstantCode.SUCCESS, contractDeploys.getContent(), contractDeploys.getTotalElements());
    }
}
