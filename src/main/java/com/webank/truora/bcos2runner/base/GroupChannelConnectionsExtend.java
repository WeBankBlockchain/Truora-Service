package com.webank.truora.bcos2runner.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Data
@ConditionalOnProperty(name = "runner.fiscobcos2",havingValue="true")
@EqualsAndHashCode(callSuper=false)
public class GroupChannelConnectionsExtend  extends  GroupChannelConnectionsConfig{

    private String chainId;
}
