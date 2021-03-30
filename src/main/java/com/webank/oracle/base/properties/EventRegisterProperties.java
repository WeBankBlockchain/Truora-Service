package com.webank.oracle.base.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.webank.oracle.chain.ChainGroup;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * properties of event register.
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties("event")
public class EventRegisterProperties {

    private List<EventRegister> eventRegisters;
    @Autowired protected ContractVersion contractVersion;

    @PostConstruct
    public void init() {
        log.info("Set contract version to event register...");
        if (CollectionUtils.isNotEmpty(eventRegisters)) {
            eventRegisters.forEach(eventRegister -> {
                eventRegister.setOracleCoreVersion(contractVersion.getOracleCoreVersion());
                eventRegister.setVrfCoreVersion(contractVersion.getVrfCoreVersion());
            });
        }
    }


    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public List<EventRegister> getByChainIdAndGroupId(int chainId, int groupId) {
        if (CollectionUtils.isEmpty(eventRegisters)) {
            return Collections.emptyList();
        }
        List<EventRegister> eventRegisterList = eventRegisters.stream().filter(eventRegister -> {
            if (chainId > 0 && groupId > 0) { // get by chainId and groupId
                return eventRegister.getChainId() == chainId && eventRegister.getGroup() == groupId;
            } else if (chainId > 0) {  // get only by chainId
                return eventRegister.getChainId() == chainId;
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

        Map<Integer, List<EventRegister>> chainGroupListMap
                = eventRegisters.stream().collect(Collectors.groupingBy(EventRegister::getChainId));
        if (MapUtils.isEmpty(chainGroupListMap)) {
            return chainGroupList;
        }

        chainGroupListMap.entrySet().forEach((chainGroupListEntry) -> {
            if (CollectionUtils.isEmpty(chainGroupListEntry.getValue())) {
                return;
            }
            ChainGroup chainGroup = new ChainGroup();
            chainGroup.setChainId(chainGroupListEntry.getKey());

            List<Integer> groupIdList = chainGroupListEntry.getValue().stream().map(EventRegister::getGroup).collect(Collectors.toList());
            chainGroup.setGroupIdList(groupIdList);
            chainGroupList.add(chainGroup);
        });

        return chainGroupList;
    }
}
