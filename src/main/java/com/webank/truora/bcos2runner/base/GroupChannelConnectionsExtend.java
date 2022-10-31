package com.webank.truora.bcos2runner.base;

import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GroupChannelConnectionsExtend  extends  GroupChannelConnectionsConfig{

    private String chainId;
}
