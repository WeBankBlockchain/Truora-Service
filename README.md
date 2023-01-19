# Truora-Servcie

[![CodeFactor](https://www.codefactor.io/repository/github/webankblockchain/truora-service/badge)](https://www.codefactor.io/repository/github/webankblockchain/truora-service)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/8f8d7f6ba47f404d94f786dc505c9797)](https://www.codacy.com/gh/WeBankBlockchain/Truora-Service/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=WeBankBlockchain/Truora-Service&amp;utm_campaign=Badge_Grade)
<br />
[![Latest release](https://img.shields.io/github/release/WeBankBlockchain/Truora-Service.svg)](https://github.com/WeBankBlockchain/Truora-Service/releases/latest)
[![Join the chat at https://gitter.im/Truora-Service/community](https://badges.gitter.im/Truora-Service/community.svg)](https://gitter.im/Truora-Service/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
![LICENSE](https://img.shields.io/github/license/WeBankBlockchain/Truora-Service)
<a href="https://github.com/WeBankBlockchain/Truora-Service"><img src="https://sloc.xyz/github/WeBankBlockchain/Truora-Service" /></a>

## 简介：  
   
   Truora是一个基于[FISCO-BCOS](https://github.com/FISCO-BCOS/FISCO-BCOS)平台的预言机服务。
   详细介绍请查看[Truora在线文档](https://truora.readthedocs.io/)

   
**支持FISCO BCOS2.x和3.x版本（2022.11）**

[FISCO BCOS 2.x](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/) | [FISCO BCOS 3.x](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/)



# Truora-Servcie

## 概述

在链下运行的Java服务,监听链上预言机合约事件,发起链下相关的资源访问和计算任务,并将结果返回到链上预言机合约,供链上使用。

支持的特性：

1） 获取外部数据（http/https) 并将结果写回链上，供链上合约验证和使用。

2） 链下生成随机数,供链上验证使用,即VRF可验证随机数。

3） 面向对象的实现，可扩展更多链上事件监听、链下数据获取/计算并可信验证的逻辑。

## 版本历史


### 仅支持FISCO BCOS 2.x的版本,全部使用v2stable分支

* [Truora-Service (v2stable)](https://github.com/WeBankBlockchain/Truora-Service/tree/v2stable)

* [Truora-Web (v2stable)](https://github.com/WeBankBlockchain/Truora-Web/tree/v2stable)

* [Truora-Doc (v2stable)](https://truora.readthedocs.io/zh_CN/v2stable/)

### 面向FISCO BCOS3.x的版本,使用Master分支

此部分的分支支持FISCO 2.x / FISCO 3.x

[技术文档](https://truora.readthedocs.io/)

面向FISCO BCOS3.x的版本，暂时不支持一键安装、Docker、Web管理台等，可自行二次开发并贡献给社区

[适配3.x的技术备忘](README_V3.md)


## 开发参考
参见[doc里的开发文档](https://truora.readthedocs.io/zh_CN/main/dev-quick-start.html)


## 版本迭代规划

* 进一步支持服务集群多活,在并发时精细控制并发任务次序等

* 对结果进行多方验证的预言机机制,避免出现偶发异常数据

* 进一步重构代码结构,梳理监听、事件处理流程

* 插件化的链外信息源接入、计算任务调度

* 适配3.x的VRF(基于25519曲线)内置算法




## 贡献代码
欢迎参与本项目的社区建设：
- 如项目对您有帮助,欢迎点亮我们的小星星(点击项目左上方Star按钮)。
- 欢迎提交代码(Pull requests)。
- [提问和提交BUG](https://github.com/WeBankBlockchain/Truora-Service/issues)。
- 如果发现代码存在安全漏洞,请在[这里](https://security.webank.com)上报。

## 加入我们的社区

FISCO BCOS开源社区是国内活跃的开源社区,社区长期为机构和个人开发者提供各类支持与帮助。已有来自各行业的数千名技术爱好者在研究和使用FISCO BCOS。如您对FISCO BCOS开源技术及应用感兴趣,欢迎加入社区获得更多支持与帮助。


![](https://media.githubusercontent.com/media/FISCO-BCOS/LargeFiles/master/images/QR_image.png)

## License

开源协议为[Apache License 2.0](http://www.apache.org/licenses/). 详情参考[LICENSE](./LICENSE)。
