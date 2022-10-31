package com.webank.truora.bcos3runner;

import com.webank.truora.base.properties.EventRegisterConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Data
@Slf4j
@Component
@DependsOn("Bcos3SdkFactory")
public class Bcos3EventRegisterFactory {
    @Autowired Bcos3ClientConfig bcos3ClientConfig;
    @Autowired Bcos3SdkFactory bcos3SdkFactory;
    Map<String,Bcos3EventRegister> eventRegistersMapping =new HashMap<String,Bcos3EventRegister>();

    @PostConstruct
    public void init() {
        List<EventRegisterConfig> eventRegisters = bcos3ClientConfig.getEventRegisters();
        if(CollectionUtils.isEmpty(eventRegisters)){
            return ;
        }
        for(EventRegisterConfig erconfig: eventRegisters) {
            erconfig.setOracleCoreVersion(bcos3ClientConfig.getContractVersion().getOracleCoreVersion());
            erconfig.setVrfCoreVersion(bcos3ClientConfig.getContractVersion().getVrfCoreVersion());
            Bcos3EventRegister er = new Bcos3EventRegister();
            Client client = null;
            try{
                client = bcos3SdkFactory.getClientByChainIdAndGroupIdNotNull(erconfig.getChainId(),erconfig.getGroupId());
                if(client == null){
                    log.error("not found when bcos3SdkFactory.getClientByChainIdAndGroupIdNotNull {} :{}",
                            erconfig.getChainId(),erconfig.getGroupId());
                    continue;
                }
                er.setBcos3client(client);
            }catch (Exception e){
                log.error("Exception when bcos3SdkFactory.getClientByChainIdAndGroupIdNotNull {} :{}, {}",
                        erconfig.getChainId(),erconfig.getGroupId(),e.getMessage());
                continue;
            }
            er.setPlatform(bcos3ClientConfig.getPlatform());
            er.setConfig(erconfig);

            if (erconfig.getKeyfile().isEmpty())
            {
                //没有配置私钥，跑不了服务，跳过
                continue;
            }
            try {
                CryptoKeyPair keyPair = Bcos3KeyTools.getKeyPairByFile(client,erconfig.getKeyfile());
                er.setKeyPair(keyPair);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            put(er);
        }

    }

    public String chaingroupKey(String chainId,String groupId){
        return String.format("%s|%s",chainId,groupId);
    }
    public void put(Bcos3EventRegister eventRegister)
    {
        String chainId = eventRegister.getConfig().getChainId();
        String groupId = eventRegister.getConfig().getGroupId();
        this.eventRegistersMapping.put(chaingroupKey(chainId,groupId),eventRegister);
    }

    public Bcos3EventRegister get(String chainId,String groupId){
        String key = chaingroupKey(chainId,groupId);
        Bcos3EventRegister eventRegister = this.eventRegistersMapping.get(key) ;
        return eventRegister;
    }


    public List<EventRegisterConfig> getConfigByChainIdAndGroupId(String chainId, String groupId) {
        if (CollectionUtils.isEmpty(eventRegistersMapping.values())) {
            return Collections.emptyList();
        }
        List<EventRegisterConfig> erconfigList = new ArrayList();
        for(Bcos3EventRegister eventRegister: eventRegistersMapping.values())
        {
            if (!chainId.isEmpty() && !groupId.isEmpty()){
                if(eventRegister.getConfig().getChainId().compareTo(chainId)==0
                        && eventRegister.getConfig().getGroupId().compareTo(groupId)==0){
                    erconfigList.add(eventRegister.getConfig());
                }

            }
            else if(!chainId.isEmpty()){
                if(eventRegister.getConfig().getChainId().compareTo(chainId)==0) {
                    erconfigList.add(eventRegister.getConfig());
                }
            }else{
                erconfigList.add(eventRegister.getConfig());
            }
        }

        return erconfigList == null ? Collections.emptyList() : erconfigList;
    }
}
