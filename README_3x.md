# Truora-Servcie

## 2022年11月重构版本说明


* 支持FISCO BCOS3.x，由于FISCO BCOS3.x使用的SDK和2.x不同，数据结构、调用方法都有所不同，所以在代码结构、目录层次都做了修改。

* 针对FISCO BCOS2.x/3.x的不同，将配置信息、事件注册、回调等代码，以及智能合约分为相应不同的包/目录。 一些基础通用代码如常量、异常等，继续复用。

* FISCO BCOS3.x的chainID，groupId采用字符串类型，如"chain0/group0",FISCO BCOS2.x采用整形。所以对数据库、合约接口等都做了适配。

* 智能合约增加适配FISCO BCOS 3.x的‘v3’版本，接口里的chainId和groupId修改为字符串，以适配FISCO BCOS3.x。原来面向fisco bcos2的合约不做修订，以保留为web3j原来生成的java代码。参见[智能合约](/contracts/1.0/sol-0.6/oracle)

* 数据库表增加‘platform’字段，以代表不同的平台类型，如fiscobcos2/fiscobcos3。建表脚本使用 [dbscripts/V2022.10__v1.0.0_init_table.sql](dbscripts/V2022.10__v1.0.0_init_table.sql)

* application.yml里增加了FISCO BCOS3.x相关的配置。FISCO BCOS3.x的sdk配置文件是独立的toml文件，要根据具体部署环境（Jar打包，Docker和非docker等），将toml和各种证书放到工程能访问的目录下，
  参见 [FISCO BCOS3.x JavaSDK](https://fisco-bcos-documentation.readthedocs.io/zh_CN/v3.0.0/docs/sdk/java_sdk/index.html) | [配置](https://fisco-bcos-documentation.readthedocs.io/zh_CN/v3.0.0/docs/sdk/java_sdk/configuration.html)

* 3.x的callback实现（如去http调用外部资源时），使用@Async注解采用线程池，避免堵塞回调线程

* 增加面向FISCO BCOS3.x的测试案例

* Restful接口主路径改为truora（根据项目名规整），端口换为5022（减少端口冲突可能）

* 目前truora-service基本适配完成。Web页面、docker部署、开发者文档等有待适配和验证


## 简易开发参考
参见[doc目录里的文档](/doc) 