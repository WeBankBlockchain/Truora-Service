########################################################################
# 配置 Truora 连接的链和群组信息（证书和地址）:
#   1. 同一条链可以配置多个群组
#   2. 可以配置多条链
########################################################################
group-channel-connections-configs:
  configs:
    ## 第一条链的连接信息，证书，群组列表和 IP:Port
    - chainId: 1
      gmCaCert: classpath:cert/1/gmca.crt
      gmSslCert: classpath:cert/1/gmsdk.crt
      gmSslKey: classpath:cert/1/gmsdk.key
      gmEnSslCert: classpath:cert/1/gmensdk.crt
      gmEnSslKey: classpath:cert/1/gmensdk.key
      all-channel-connections:
         - group-id: ${FISCO_BCOS_GROUP:1}
           connections-str:
             # node listen_ip:channel_listen
             - 127.0.0.1:${FISCO_BCOS_PORT:20200}
        ## 群组 2 的信息
        #- group-id: 2
        #  connections-str:
        #    - 127.0.0.1:20200

    ## 第二条链的连接信息，证书，群组列表以及对应的 IP:Port
    #- chainId: 2
    #  gmCaCert: classpath:cert/2/gmca.crt
    #  gmSslCert: classpath:cert/2/gmsdk.crt
    #  gmSslKey: classpath:cert/2/gmsdk.key
    #  gmEnSslCert: classpath:cert/2/gmensdk.key
    #  gmEnSslKey: classpath:cert/2/gmensdk.key
    #  all-channel-connections:
    #    - group-id: 1
    #      connections-str:
    #        - 127.0.0.1:20200


#fisco-bcos 2.2 or latest
#https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html#id14
########################################################################
# 配置事件监听：
#   1. 配置 Truora 需要监听的链（ChainId）和群组（groupId）
#   2. 配置的 chainId 和 groupId 需要配置在 group-channel-connections-configs
########################################################################
event:
  eventRegisters:
   # list of EventRegister objects. Use default value when property not configured.
   # Oracle contracts will be auto deployed when contractAddress is blank
   - {chainId: 1, group: 1}
   #- {chainId: 1, group: 2}
   #- {chainId: 2, group: 1}
   #- {chainId: 2, group: 2}

