package com.webank.oracle.base.config;

import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GroupChannelConnectionsExtend  extends  GroupChannelConnectionsConfig{

    private int chainId;
}
