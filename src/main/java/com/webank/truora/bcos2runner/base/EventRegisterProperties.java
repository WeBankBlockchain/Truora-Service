package com.webank.truora.bcos2runner.base;

import com.webank.truora.base.pojo.ChainGroup;
import com.webank.truora.base.properties.ContractVersion;
import com.webank.truora.base.properties.EventRegisterConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * properties of event register.
 */
@Slf4j
@Data
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@Configuration
@ConfigurationProperties("fiscobcos2event")
public class EventRegisterProperties {

    private List<EventRegisterConfig> eventRegisters;
    @Autowired protected ContractVersion contractVersion;

    @PostConstruct
    public void init() {
        log.info("Set contract version to event register...");
        if (CollectionUtils.isNotEmpty(eventRegisters)) {
            eventRegisters.forEach(eventRegister -> {
                eventRegister.setOracleCoreVersion(contractVersion.getOracleCoreVersion());
                eventRegister.setVrfCoreVersion(contractVersion.getVrfCoreVersion());
                eventRegister.setPlatform("fiscobcos2");
            });
        }
    }


    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public List<EventRegisterConfig> getByChainIdAndGroupId(String chainId, String groupId) {
        if (CollectionUtils.isEmpty(eventRegisters)) {
            return Collections.emptyList();
        }
        List<EventRegisterConfig> eventRegisterList = eventRegisters.stream().filter(eventRegister -> {
            if (!chainId.isEmpty() && !groupId.isEmpty()) { // get by chainId and groupId
                return eventRegister.getChainId().compareTo(chainId)==0 && eventRegister.getGroupId().compareTo(groupId)==0;
            } else if (!chainId.isEmpty()) {  // get only by chainId
                return (eventRegister.getChainId().compareTo(chainId) == 0);
            }
            // get all chain and group
            return true;
        }).collect(Collectors.toList());

        return eventRegisterList == null ? Collections.emptyList() : eventRegisterList;
    }

    /**
     * @return
     */
    public List<ChainGroup> getChainGroupList() {
        List<ChainGroup> chainGroupList = new ArrayList<>();
        if (CollectionUtils.isEmpty(eventRegisters)) {
            return chainGroupList;
        }

        Map<String, List<EventRegisterConfig>> chainGroupListMap
                = eventRegisters.stream().collect(Collectors.groupingBy(EventRegisterConfig::getChainId));
        if (MapUtils.isEmpty(chainGroupListMap)) {
            return chainGroupList;
        }

        chainGroupListMap.entrySet().forEach((chainGroupListEntry) -> {
            if (CollectionUtils.isEmpty(chainGroupListEntry.getValue())) {
                return;
            }
            ChainGroup chainGroup = new ChainGroup();
            chainGroup.setChainId(chainGroupListEntry.getKey());

            List<String> groupIdList = chainGroupListEntry.getValue().stream().map(EventRegisterConfig::getGroupId).collect(Collectors.toList());
            chainGroup.setGroupIdList(groupIdList);
            chainGroupList.add(chainGroup);
        });

        return chainGroupList;
    }
}
