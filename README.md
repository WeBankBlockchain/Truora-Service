# Oracle-Service

[![CodeFactor](https://www.codefactor.io/repository/github/yuanmomo/webase-oracle/badge/v0.3)](https://www.codefactor.io/repository/github/yuanmomo/webase-oracle/overview/v0.3)
[![Latest release](https://img.shields.io/github/release/yuanmomo/WeBASE-Oracle.svg)](https://github.com/yuanmomo/WeBASE-Oracle/releases/latest)
![Docker Hub](https://github.com/yuanmomo/WeBASE-Oracle/workflows/Docker%20Hub/badge.svg)
![](https://img.shields.io/github/license/yuanmomo/WeBASE-Oracle)

### 1 原理简介：  
#### 1.1 原理
   
   区块链是一个确定性的、封闭的系统环境，目前区块链只能获取到链内的数据，而不能获取到链外真实世界的数据，区块链与现实世界是割裂的。
   
   区块链是确定性的环境，它不允许不确定的事情或因素，智能合约不管何时何地运行都必须是一致的结果，所以虚拟机（VM）不能让智能合约有 network call（网络调用），不然结果就是不确定的。
   当智能合约的触发条件是外部信息时（链外），就必须需要预言机来提供数据服务，通过预言机将现实世界的数据输入到区块链上，因为智能合约不支持对外请求。
   也就是说智能合约不能进行 I/O（Input/Output，即输入/输出），所以它是无法主动获取外部数据的，只能通过预言机将数据给到智能合约。
   
   我们可以通过引入预言机（Oracle）的功能来解决这一问题，预言机可以为智能合约提供与外部世界的连接性。   
   Oracle-Service 分为链上部分和链下部分。
   链上部分主要是oracle相关合约，链下部分主要是java服务，负责监听合约的事件，采集结果并回写到智能合约。  

#### 1.2 作用
  保险方面，预言机的设计让智能合约保险在实现权利义务明确和自动执行（消除传统保险合约因陈述条款模糊而导致的纠纷）的基础上，更进一步的遏制保险欺诈，防止投保人和保险人的委托代理风险。  
  金融方面，获取汇率  
  游戏方面，获取链上随机数  
  
#### 1.3 设计方案

  WeBASE-Oracle是FISCO BCOS链上的预言机服务，在广泛调研市场上预言机项目的基础上针对联盟链设计的预言机服务。同时支持国密，支持连接多链多群组，可同时为不同链和群组提供oracle服务。
  有三种使用方式：
  也支持去中心化方式获取链下数据，去中心化
    
  1 支持用户获取链下API数据     
    如图![oracle流程图](img/oracle.png)    
   中心化方式获取链下url数据，目前支持json和text/plain两种访问格式，链下API必须支持HTTPS访问 
   
  2 支持产生VRF随机数  
   支持获取链上安全的可验证随机数。  
   ![VRF随机数生成流程图](img/vrf.png)
   采用k1椭圆曲线的VRF算法。链上合约验证Proof。
   用户提供随机数种子，oracle service服务方提供自己私钥，产生VRF的 proof。链上合约验证proof。  
   [VRF介绍](doc/VRF.md)  
   
  3 支持去中心化部署和结果聚合
  
   去中心化方式获取链下数据。支持用户选择多个oracle service帮获取数据。并进行链上聚合，聚合支持取中位数，最终将聚合结果返回给用户合约。
   此外会维护OracleServiceCenter注册中心，所有启动的oracle service节点需要在注册中心注册自己的相关信息，以方便用户选择oracle service服务方。  
   ![去中心化oracle原理图](img/distributedOracle.png)
     

   
### 2 安装部署
当前 TrustOracle 服务默认支持两种部署方式：

* Docker 部署
    * 提供一键部署脚本 `deploy.sh`
    * Docker 部署，请参考：[TrustOracle Docker 部署](./docker/README.md)

* 手动编译部署
    * 手动部署，请参考：[Oracle-Service服务安装部署](doc/install.md)



### 3  快速开发  
   [快速开发自己的预言机](doc/develop.md)

### 4 使用注意事项：

