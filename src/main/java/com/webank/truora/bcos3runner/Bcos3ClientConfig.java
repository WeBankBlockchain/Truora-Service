package com.webank.truora.bcos3runner;

import com.webank.truora.base.pojo.ChainGroup;
import com.webank.truora.base.pojo.keystore.KeyStoreInfo;
import com.webank.truora.base.properties.ContractVersion;
import com.webank.truora.base.properties.EventRegisterConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Configuration
@ConditionalOnProperty(name = "runner.fiscobcos3",havingValue = "true")
@ConfigurationProperties(prefix = "fiscobcos3")
public class Bcos3ClientConfig {
    private String platform;
    private Map<String, Resource> sdkconfigs;
    private List<EventRegisterConfig> eventRegisters;
    private String defaultKeyfile;
    KeyStoreInfo keyStoreInfo;
    @Autowired
    protected ContractVersion contractVersion;

    /*
    @PostConstruct
    public void init() {

    }*/
    public List<ChainGroup> getChainGroupList(){
        ArrayList<ChainGroup> chainGroupArrayList = new ArrayList<ChainGroup>();
        Map<String,List<String>> chainGroupMap = new HashedMap<String,List<String>>();
        for(EventRegisterConfig erconfig : eventRegisters){
            String chainId = erconfig.getChainId();
            if(chainGroupMap.get(chainId)==null){
                List<String> groupList = new ArrayList<String>();
                chainGroupMap.put(chainId,groupList);
            }
            chainGroupMap.get(chainId).add(erconfig.getGroupId());
        }
        for(Map.Entry<String,List<String>> item : chainGroupMap.entrySet()){
            ChainGroup chainGroup = new ChainGroup();
            chainGroup.setPlatform(this.getPlatform());
            chainGroup.setChainId(item.getKey());
            chainGroup.setGroupIdList(item.getValue());
            chainGroupArrayList.add(chainGroup);
        }
        return chainGroupArrayList;
    }
}
