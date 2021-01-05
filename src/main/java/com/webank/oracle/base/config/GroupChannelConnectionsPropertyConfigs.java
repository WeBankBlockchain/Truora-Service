package com.webank.oracle.base.config;

import com.webank.oracle.base.properties.SdkProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "group-channel-connections-configs")
public class GroupChannelConnectionsPropertyConfigs {

    private List<GroupChannelConnectionsExtend> configs;

    @Autowired
    private SdkProperties sdkProperties;

    @Bean
    public List<GroupChannelConnectionsExtend> getGroupChannelConnections() {
        List<GroupChannelConnectionsExtend> groupChannelConnectionsConfigs = new ArrayList<>();
        for(int i= 0 ;i< configs.size();i++) {
            GroupChannelConnectionsExtend groupChannelConnectionsConfig =
                    new GroupChannelConnectionsExtend();
            //ecc
            groupChannelConnectionsConfig.setCaCert(configs.get(i).getCaCert());
            groupChannelConnectionsConfig.setSslCert(configs.get(i).getSslCert());
            groupChannelConnectionsConfig.setSslKey(configs.get(i).getSslKey());
            //gm
            groupChannelConnectionsConfig.setGmCaCert(configs.get(i).getGmCaCert());
            groupChannelConnectionsConfig.setGmSslCert(configs.get(i).getGmSslCert());
            groupChannelConnectionsConfig.setGmSslKey(configs.get(i).getGmSslKey());
            groupChannelConnectionsConfig.setGmEnSslCert(configs.get(i).getGmEnSslCert());
            groupChannelConnectionsConfig.setGmEnSslKey(configs.get(i).getGmEnSslKey());

            // each group set idle timeout
            for(int j = 0 ; j <configs.get(i).getAllChannelConnections().size(); j++) {
                configs.get(i).getAllChannelConnections().get(j).setIdleTimeout(sdkProperties.getIdleTimeout() );
            }

            groupChannelConnectionsConfig.setAllChannelConnections(configs.get(i).getAllChannelConnections());
            groupChannelConnectionsConfig.setChainId(configs.get(i).getChainId());
            groupChannelConnectionsConfigs.add( groupChannelConnectionsConfig);

        }
        return groupChannelConnectionsConfigs;
    }

    
}
