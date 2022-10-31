### 部署说明：

## 1. 前提条件

| 依赖软件 | 支持版本 |
| :-: | :-: |
| Java | JDK8或以上版本 |
| FISCO-BCOS | V2.5+版本 ， V3.x版本 |

**备注：** Java推荐使用[OpenJDK](./appendix.html#java )，建议从[OpenJDK网站](https://jdk.java.net/java-se-ri/11) 自行下载（CentOS的yum仓库的OpenJDK缺少JCE(Java Cryptography Extension)，导致Web3SDK无法正常连接区块链节点）


## 2. 拉取代码
执行命令：
```
git clone https://github.com/WeBankBlockchain/Truora-Service.git
```

进入目录：

```
cd Truora-Service
```

## 3. 编译代码


方式一：如果服务器已安装Gradle，且版本为Gradle-4.10或以上

```shell
gradle build -x test
```

方式二：如果服务器未安装Gradle，或者版本不是Gradle-4.10或以上，使用gradlew编译

```shell
chmod +x ./gradlew && ./gradlew build -x test
```

构建完成后，会在根目录Truora-Service下生成已编译的代码目录dist。

## 4. 修改配置

（1）进入dist目录

```
cd dist
```

dist目录提供了一份配置模板conf：


（2）进入conf目录：

```shell
cd conf
```

**注意：** 需要将节点所在目录`nodes/${ip}/sdk`下的`ca.crt`、`node.crt`和`node.key`文件拷贝到当前conf目录，供SDK与节点建立连接时使用。

（3）修改配置application.yml文件（根据实际情况修改）：

  修改数据库ip地址和用户名和密码。 
   
```
 datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/truora?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false
    username: "defaultAccount"
    password: "defaultPassword"
```  

### FISCO BCOS v2.x相关配置(使用FISCO BCOSv 3.x可跳过此节 )  
  
  多链多群组配置，注意不同的chain的节点没有任何p2p联系。
``` 
   group-channel-connections-configs:
     configs:
       - caCert: classpath:ca.crt   # 证书位置
         sslCert: classpath:node.crt
         sslKey: classpath:node.key
         chainId: 1           # 不同的链须自己表示ID
         all-channel-connections:
            - group-id: 1     #group ID
              connections-str:
                 - 127.0.0.1:20201 # node listen_ip:channel_listen_port
       - caCert: classpath:ca1.crt   
         sslCert: classpath:node1.crt
         sslKey: classpath:node1.key
         chainId: 2
         all-channel-connections:
            - group-id: 1 #group ID
              connections-str:
                 - 127.0.0.1:20200 # node listen_ip:channel_listen_port

```

  多链多群组监听配置
```
    event:
      eventRegisters:
        - {chainId: 1, group: 1}
        - {chainId: 2, group: 1}

```  

  国密配置：  
 ```
sdk:
  encryptType: 1 #0:standard, 1:guomi
``` 
  
### FISCO BCOS v3.x相关配置(使用FISCO BCOSv 2.x可跳过此节 )    

FISCO BCOS3.x的每个链实例采用独立的toml文件配置，可参照项目所带的bcos3_config-template.toml进行配置。

需要将FISCO BCOS3.x节点的sdk目录下的证书复制到本地的相应目录，确保所有相关配置文件和证书文件均可被项目正确访问。

具体的目录结构和访问权限，和开发者自身的开发环境或部署运行环境有关，
需要开发者本身自行理解java/spring/docker等相关的环境和目录结构。


```
fiscobcos3:
  platform: fiscobcos3
  # 可忽略，每个chain/group可配置不同的key file
  defaultKeyfile: "./ECC/keyStoreUser.txt"
  sdkconfigs:
    # 配置指向一到多个链/群组实例的sdk。
    # fisco bcos3.x版本的sdk使用独立的toml配置文件，每个链客户端实例写一个toml配置，里面可以配置连接多个节点
    chain0: bcos3sdk_config.toml
    #chain1: bcos3sdk_config_1.toml
  eventRegisters:
    # 配置一到多个chain/group的Oracle/VRF监听服务，如果数据库里没有合约地址记录，会做自动部署
    - chainId: chain0
      groupId: group0
      oracleCoreAddress:
      vrfCoreAddress:
      fromBlock:
      toBlock:
      #为每个监听实例指定不同的keyflie
      keyfile: "./ECC/keyStoreUser.txt"
```
  
  
## 5. 服务启停

返回到dist目录执行：
```shell
启动: bash start.sh
停止: bash stop.sh
检查: bash status.sh
```
**备注**：服务进程起来后，需通过日志确认是否正常启动，出现以下内容表示正常；如果服务出现异常，确认修改配置后，重启提示服务进程在运行，则先执行stop.sh，再执行start.sh。

```
	Application() - main run success...
```

## 6. 查询不同不同链和群组的oracle-core合约地址

```
http://{deployIP}:{port}/Oracle-Service/oracle/oracle-core-address?chainId=1&groupId=1
示例：http://localhost:5102/Oracle-Service/oracle/oracle-core-address?chainId=1&groupId=1
```

- 部署服务器IP和服务端口需对应修改，网络策略需开通
- 基于可视化控制台，可以开发智能合约，部署合约和发送交易，并查看交易和区块详情。还可以管理私钥，对节点健康度进行监控和统计

## 7. 查看日志

在dist目录查看：

```
前置服务日志：tail -f log/Oracle-Service.log
web3连接日志：tail -f log/web3sdk.log
```
