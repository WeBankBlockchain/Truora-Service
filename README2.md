# TrustOracle-Servcie

[![CodeFactor](https://www.codefactor.io/repository/github/yuanmomo/webase-oracle/badge/v0.3)](https://www.codefactor.io/repository/github/yuanmomo/webase-oracle/overview/v0.3)
[![Latest release](https://img.shields.io/github/release/yuanmomo/WeBASE-Oracle.svg)](https://github.com/yuanmomo/WeBASE-Oracle/releases/latest)
![Docker Hub](https://github.com/yuanmomo/WeBASE-Oracle/workflows/Docker%20Hub/badge.svg)
![](https://img.shields.io/github/license/yuanmomo/WeBASE-Oracle)

### 1 原理简介：  
#### 1.1 原理
   
   TrustOracle是一个基于[FISCO-BCOS](https://github.com/FISCO-BCOS/FISCO-BCOS)平台的预言机服务。
   由后端服务[TrustOracle-Servcie]()和前端服务[TrustOracle-Web]()组成。
   
**此版本只支持**[FISCO BCOS 2.0](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/)及以上版本。

    
#### 1.2 作用
  保险方面，预言机的设计让智能合约保险在实现权利义务明确和自动执行（消除传统保险合约因陈述条款模糊而导致的纠纷）的基础上，更进一步的遏制保险欺诈，防止投保人和保险人的委托代理风险。  
  金融方面，获取汇率  
  游戏方面，获取链上安全随机数  
  
#### 1.3 特性
 
 - 支持API访问链下数据源
 - 支持产生链上可验证随机数
 - 支持国密
 - 支持中心化部署和去中心化部署
 - 支持多链多群组
 - 支持多数据格式访问
 - 支持请求状态查询
 
     
## 环境要求

 //todo yuan  
 
在使用本组件前，请确认系统环境已安装相关依赖软件，清单如下：

| 依赖软件 | 说明 |备注|
| --- | --- | --- |
| FISCO-BCOS | >= 2.6， 1.x版本请参考V0.5版本 dev分支 |
| Bash | 需支持Bash（理论上来说支持所有ksh、zsh等其他unix shell，但未测试）|
| Java | JDK[1.8] ||
| Git | 下载的安装包使用Git | |
| MySQL | >= mysql-community-server[5.7] | 理论上来说支持主流数据库，但未测试|
| docker    | >= docker[18.0.0] | 只有需要可视化监控页面的时候才需要安装|


## 文档
- [**TrustOracle介绍**](https://data-doc.readthedocs.io/zh_CN/latest/docs/WeBankBlockchain-Data-Export/index.html)
- [**快速安装**](https://data-doc.readthedocs.io/zh_CN/latest/docs/WeBankBlockchain-Data-Export/install.html)

   
### 2 安装部署
当前 TrustOracle 服务默认支持两种部署方式：

* Docker 部署
    * 提供一键部署脚本 `deploy.sh`
    * Docker 部署，请参考：[TrustOracle Docker 部署](./docker/README.md)

* 手动编译部署
    * 手动部署，请参考：[Oracle-Service服务安装部署](doc/install.md)
    
    
## 贡献代码
欢迎参与本项目的社区建设：
- 如项目对您有帮助，欢迎点亮我们的小星星(点击项目左上方Star按钮)。
- 欢迎提交代码(Pull requests)。
- [提问和提交BUG](https://github.com/WeBankBlockchain/WeBankBlockchain-Data-Export/issues)。
- 如果发现代码存在安全漏洞，请在[这里](https://security.webank.com)上报。

## 加入我们的社区

FISCO BCOS开源社区是国内活跃的开源社区，社区长期为机构和个人开发者提供各类支持与帮助。已有来自各行业的数千名技术爱好者在研究和使用FISCO BCOS。如您对FISCO BCOS开源技术及应用感兴趣，欢迎加入社区获得更多支持与帮助。


![](https://media.githubusercontent.com/media/FISCO-BCOS/LargeFiles/master/images/QR_image.png)


## License
![license](http://img.shields.io/badge/license-Apache%20v2-blue.svg)

开源协议为[Apache License 2.0](http://www.apache.org/licenses/). 详情参考[LICENSE](../LICENSE)。
    





