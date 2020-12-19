package com.webank.oracle.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "group-channel-connections-configs")
public class GroupChannelConnectionsPropertyConfigs {

    List<GroupChannelConnectionsExtend> configs;

    @Bean
    public List<GroupChannelConnectionsExtend> getGroupChannelConnections() {
        List<GroupChannelConnectionsExtend> groupChannelConnectionsConfigs = new ArrayList<>();
        for(int i= 0 ;i< configs.size();i++) {
            GroupChannelConnectionsExtend groupChannelConnectionsConfig =
                    new GroupChannelConnectionsExtend();
            groupChannelConnectionsConfig.setCaCert(configs.get(i).getCaCert());
            groupChannelConnectionsConfig.setSslCert(configs.get(i).getSslCert());
            groupChannelConnectionsConfig.setSslKey(configs.get(i).getSslKey());
            groupChannelConnectionsConfig.setAllChannelConnections(configs.get(i).getAllChannelConnections());
            groupChannelConnectionsConfig.setChainId(configs.get(i).getChainId());
            groupChannelConnectionsConfigs.add( groupChannelConnectionsConfig);

        }
        return groupChannelConnectionsConfigs;
    }

    
}
