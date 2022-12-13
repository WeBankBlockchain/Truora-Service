package com.webank.truora.restcontroller;

import com.webank.truora.base.pojo.ChainGroup;
import com.webank.truora.base.pojo.vo.BaseResponse;
import com.webank.truora.base.pojo.vo.ConstantCode;
import com.webank.truora.base.utils.JsonUtils;
import com.webank.truora.bcos2runner.Web3jMapService;
import com.webank.truora.bcos2runner.base.EventRegisterProperties;
import com.webank.truora.bcos3runner.Bcos3ClientConfig;
import com.webank.truora.bcos3runner.Bcos3SdkFactory;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BcosGroupInfo;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.BlockNumber;
import org.fisco.bcos.web3j.protocol.core.methods.response.NodeVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired protected Web3jMapService web3jMapService;
    @Autowired protected Bcos3SdkFactory bcos3SdkFactory;
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

    @GetMapping("/group/ping")
    public BaseResponse ping(){
        List<HashMap<String,String>> result = new ArrayList();
        List<HashMap<String,Object>> resultobject = new ArrayList();
        if(eventRegisterProperties!=null) {
            for(ChainGroup cg : eventRegisterProperties.getChainGroupList()){
                for(String groupId: cg.getGroupIdList()) {

                    String name = "FISCOBCOS2_Chain"+cg.getChainId()+"_Group"+groupId;

                    HashMap<String,Object> ko = new HashMap<String,Object>();
                    try {
                        Web3j web3j =  web3jMapService.getNotNullWeb3j(cg.getChainId(), groupId);
                        BlockNumber bn = web3j.getBlockNumber().send();

                        NodeVersion nodeVersion =  web3j.getNodeVersion().send();
                        ko.put("name",name);
                        ko.put("BlockNumber",bn.getBlockNumber());
                        ko.put("nodeVersion",nodeVersion.getResult());
                    }catch (Exception e){

                        ko.put(name,e.getMessage());
                    }

                    resultobject.add(ko);
                }
            }

        }
        if(bcos3ClientConfig!=null) {
            for(ChainGroup cg : bcos3ClientConfig.getChainGroupList()){
                for(String groupId: cg.getGroupIdList()) {


                    String name = "FISCOBCOS3_"+cg.getChainId()+"_"+groupId;

                    HashMap<String,Object> ko = new HashMap<String,Object>();
                    try {
                        Client client = bcos3SdkFactory.getClientByChainIdAndGroupId(cg.getChainId(),groupId);
                        org.fisco.bcos.sdk.v3.client.protocol.response.BlockNumber bn = client.getBlockNumber();
                        BcosGroupInfo groupInfo = client.getGroupInfo();
                        String s = JsonUtils.toJSONString(groupInfo);
                        ko.put("name",name);
                        ko.put("BlockNumber",bn.getBlockNumber());
                        ko.put("groupInfo",groupInfo.getResult());
                    }catch(Exception e){

                        ko.put(name,e.getMessage());
                    }

                    resultobject.add(ko);

                }

            }
        }
        return BaseResponse.pageResponse(ConstantCode.SUCCESS, resultobject,resultobject.size());
    }
}
