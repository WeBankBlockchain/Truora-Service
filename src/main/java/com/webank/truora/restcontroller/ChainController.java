package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.ChainGroup;
import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.bcos2runner.base.EventRegisterProperties;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * return encrypt type to web 0 is standard, 1 is guomi
 */
@Api(value = "/chain", tags = "Query chain and group id")
@Slf4j
@RestController
@RequestMapping(value = "/chain",produces = {"application/JSON"})
public class ChainController {
    @Autowired(required = false) private EventRegisterProperties eventRegisterProperties;
    @Autowired(required = false)  private Bcos3ClientConfig bcos3ClientConfig;

    @GetMapping("/group/list")
    public BaseResponse query() {

        List<ChainGroup> chainGroupList = new ArrayList<ChainGroup>();
        if(eventRegisterProperties!=null) {
            chainGroupList.addAll(eventRegisterProperties.getChainGroupList());
        }
        if(bcos3ClientConfig!=null) {
            chainGroupList.addAll(bcos3ClientConfig.getChainGroupList());
        }


        return BaseResponse.pageResponse(ConstantCode.SUCCESS, chainGroupList,chainGroupList.size());
    }
}
