/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.oracle.base.config;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.stream.Collectors;

import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.handler.ChannelConnections;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.webank.oracle.base.properties.SdkProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * init web3sdk getService.
 */
@Data
@Slf4j
@Configuration
public class Web3Config {
    @Autowired
    private SdkProperties sdk;


    /**
     * 覆盖EncryptType构造函数
     * 放在web3sdk初始化前，否则当前类里的CnsServiceMap的credential为非国密的
     *
     * @return
     */
    @Bean(name = "encryptType")
    public EncryptType EncryptType() {
        log.info("*****init EncrytType:" + sdk.getEncryptType());
        return new EncryptType(sdk.getEncryptType());
    }

    /**
     * init getWeb3j.
     *
     * @return
     */
    @Bean
    public Map<Integer , Web3j> getWeb3jForChain(List<GroupChannelConnectionsExtend> groupChannelConnectionsConfigs) throws Exception {
        Map<Integer , Web3j>  web3jMapForChain = new HashMap<>();
       for(int i = 0 ;i < groupChannelConnectionsConfigs.size() ; i++) {
           Service service = new Service();
           service.setOrgID(sdk.getOrgName());

           service.setThreadPool(sdkThreadPool());
           GroupChannelConnectionsExtend groupChannelConnectionsExtend = groupChannelConnectionsConfigs.get(i);
           service.setAllChannelConnections(groupChannelConnectionsExtend);
           service.setGroupId(groupChannelConnectionsExtend.getAllChannelConnections().get(0).getGroupId());
           service.run();
           ChannelEthereumService channelEthereumService = new ChannelEthereumService();
           channelEthereumService.setTimeout(sdk.getTimeout());
           channelEthereumService.setChannelService(service);
           Web3j web3j = Web3j.build(channelEthereumService, service.getGroupId());
           web3jMapForChain.put(groupChannelConnectionsConfigs.get(i).getChainId(),web3j);
       }

        return web3jMapForChain;
    }

    /**
     * set sdk threadPool.
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor sdkThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(sdk.getCorePoolSize());
        executor.setMaxPoolSize(sdk.getMaxPoolSize());
        executor.setQueueCapacity(sdk.getQueueCapacity());
        executor.setKeepAliveSeconds(sdk.getKeepAlive());
        executor.setRejectedExecutionHandler(new AbortPolicy());
        executor.setThreadNamePrefix("sdkThreadPool-");
        executor.initialize();
        return executor;
    }

    /**
     * init channel service.
     * set setBlockNotifyCallBack
     *
     * @return
     */
    @Bean(name = "serviceMapWithChainId")
    @DependsOn("encryptType")
    public Map<Integer,Map<Integer, Service>>  serviceMap(Map<Integer , Web3j> web3jMapForChain,
                                            List<GroupChannelConnectionsExtend> groupChannelConnectionsConfigs) throws Exception {
        // whether front' encrypt type matches with chain's
        //isMatchEncryptType(web3j);
        Map serviceMapWithChainId = new ConcurrentHashMap<Integer, Map>();
        for (int chainId : web3jMapForChain.keySet()) {

            List<String> groupIdList = web3jMapForChain.get(chainId).getGroupList().send().getGroupList();
            for(int i=0;i< groupIdList.size();i++){
                log.info("*******chainId : {}, group: {} ",chainId, groupIdList.get(i));
            }
            List<GroupChannelConnectionsExtend> ilist = groupChannelConnectionsConfigs.stream().filter(x->x.getChainId()==(chainId)).collect(Collectors.toList());
            List<ChannelConnections> channelConnectionsList = ilist.get(0).getAllChannelConnections();
            //channelConnectionsList.clear();
            for (int i = 0; i < groupIdList.size(); i++) {
                List<String> connectionsList ;
                connectionsList = (channelConnectionsList.get(0).getConnectionsStr());
                ChannelConnections channelConnections = new ChannelConnections();
                channelConnections.setConnectionsStr(connectionsList);
                channelConnections.setGroupId(Integer.valueOf(groupIdList.get(i)));
                log.info("*** groupId " + groupIdList.get(i));
                channelConnectionsList.add(channelConnections);
            }
            Map serviceMap = new ConcurrentHashMap<Integer, Service>(groupIdList.size());
            for (int i = 0; i < groupIdList.size(); i++) {
                Service service = new Service();
                service.setOrgID(sdk.getOrgName());
                service.setGroupId(Integer.parseInt(groupIdList.get(i)));
                log.info("%%%%%%%, {}", sdkThreadPool().toString());
                service.setThreadPool(sdkThreadPool());
                service.setAllChannelConnections(ilist.get(0));
                service.run();
                serviceMap.put(Integer.valueOf(groupIdList.get(i)), service);
            }
            serviceMapWithChainId.put(chainId, serviceMap);

        }
        return serviceMapWithChainId;
    }

    /**
     * init Web3j
     *
     * @param
     * @return
     */
    @Bean
    @DependsOn("encryptType")
    @SuppressWarnings("deprecation")
    public Map<Integer,Map<Integer, Web3j>> web3jMap(Map<Integer,Map<Integer, Service>> serviceMapWithChainId) throws IOException {
//        Map web3jMapWithChainId = new ConcurrentHashMap<String,Map<Integer, Web3j>>();
        Map web3jMapWithChainId = new ConcurrentHashMap<Integer, Map<Integer, Web3j>>(serviceMapWithChainId.size());
        for(Integer s: serviceMapWithChainId.keySet()) {
            Map<Integer, Service> serviceMap = serviceMapWithChainId.get(s);
            Map web3jMap = new ConcurrentHashMap<Integer, Web3j>(serviceMap.size());
            for (Integer i : serviceMap.keySet()) {
                Service service = serviceMap.get(i);
                ChannelEthereumService channelEthereumService = new ChannelEthereumService();
                channelEthereumService.setTimeout(sdk.getTimeout());
                channelEthereumService.setChannelService(service);
                Web3j web3jSync = Web3j.build(channelEthereumService, service.getGroupId());
                // for getClockNumber local
                web3jSync.getBlockNumberCache();
                log.info("***********chainId: "+s+" groupid: " + i+ " "+web3jSync.getBlockNumber().send().getBlockNumber());
                web3jMap.put(Integer.valueOf(i), web3jSync);
            }
            web3jMapWithChainId.put(s, web3jMap);
        }
        return web3jMapWithChainId;
    }



}
