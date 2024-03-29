package com.webank.truora.bcos3runner;

import com.webank.truora.base.exception.OracleException;
import com.webank.truora.base.pojo.vo.ConstantCode;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@ConditionalOnProperty(name = "runner.fiscobcos3",havingValue = "true")
@Component("Bcos3SdkFactory")
@Slf4j
public class Bcos3SdkFactory {
    @Autowired Bcos3ClientConfig bcos3ClientConfig;
    HashMap<String,BcosSDK> chainSdkMapping = new HashMap<String,BcosSDK>();

    @Autowired
    public void init(){
        //根据配置，构建sdk对象，每个链chainid一个sdk，通过groupid可以获得client
        bcos3ClientConfig.getSdkconfigs().forEach((chainid,configfileResource)->{
            try {
                //String filename = configfileResource.getFile().getAbsolutePath();
                String filename = configfileResource.getFilename();
                Bcos3SdkHelper sdkHelper = new Bcos3SdkHelper(filename);
                chainSdkMapping.put(chainid, sdkHelper.getSdk());
                Client client = sdkHelper.getSdk().getClient("group0");
                log.info("Bcos3sdkFactory build sdk "+chainid+",group"+client);
            }catch (Exception e){
                log.error("Bcos3sdkFactory build sdk error io exception!",e);
            }
        });
    }

    public BcosSDK getByChainId(String chainId) {
        BcosSDK sdk = chainSdkMapping.get(chainId);
        return sdk;
    }
    public BcosSDK getByChainIdNotNull(String chainId) throws OracleException
    {
        BcosSDK sdk = getByChainId(chainId);
        if (sdk == null){
            throw new OracleException(ConstantCode.NO_CHAIN_SDK_ERROR,chainId);
        }
        return sdk;
    }

    public Client getClientByChainIdAndGroupId(String chainId,String groupId){
        BcosSDK sdk = chainSdkMapping.get(chainId);
        if (sdk == null){
            return null;
        }
        return sdk.getClient(groupId);
    }

    public Client getClientByChainIdAndGroupIdNotNull(String chainId,String groupId){
        Client client = getClientByChainIdAndGroupId(chainId,groupId);
        if(client == null){
            throw new OracleException(ConstantCode.NO_GROUP_CLIENT_ERROR,String.format("%s-%s",chainId,groupId));
        }
        return client;
    }
}
