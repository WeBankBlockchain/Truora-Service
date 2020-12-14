 ### 方式一：获取链下API数据
  用户可以参考 [APISampleOracle.sol](../contracts/0.4/sol-0.4/oracle/APISampleOracle.sol) 合约实现自己的oracle业务合约。  
  合约解析如下：         
  - 用户合约需继承 `FiscoOracleClient` 合约
   ```
    contract APISampleOracle is FiscoOracleClient
   ``` 
  - 构造函数需要传入指定的 `TrustOracle` 服务方地址。地址可以通过[接口](./develop.md/#api_get)获取。  
   ```
      constructor(address oracleAddress) public {  
            oracleCoreAddress = oracleAddress;      
      }  
   ```       
  - 设定自己要访问的 `url`。修改 `url` 变量赋值即可。  
  
   ```
      function request() public returns (bytes32 requestId)
        {
    
          // Set your URL
          // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
             url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
             bytes32  requestId = oracleQuery(oracleCoreAddress, url, timesAmount);
             validIds[requestId] = true;
             return requestId;
              
        }
   ```
  - 必须实现 **__callback(bytes32 _requestId, int256 _result)** 方法，用于`TrustOracle`预言机回调获取的结果。  
  - **get()** 方法获取本次请求结果, 可自行修改此函数, 获取结果后进行自己业务逻辑的计算。  
  
     
   ***URL格式规范***  
   目前支持 `json` 和 `text/plain` 两种访问格式。并且链下API的`url`必须支持`HTTPS`访问。  
   `json`格式遵循`jsonpath`格式，子元素 用 ***"."*** 表示；       
   `text/plain`格式默认取第一行结果值；
  ``` 
     //获取链下随机数API
       plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)
     //获取人民币对日元汇率API 
       json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY
  ``` 
    
   


<span id="api_get" />

### 获取合约地址
 
  获取 `Oracle-Service` 服务方的 `OracleCore` 合约地址只需调用以下接口即可。
  返回结果中***oracleCoreContractAddress***字段即为结果。
```Bash
http://{deployIP}:{port}/Oracle-Service/oracle/address?chainId=1&groupId=1
    
# 示例：
curl "http://localhost:5012/Oracle-Service/oracle/address?chainId=1&groupId=1"
```

返回结果如下：

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "chainId": 1,
      "group": 1,
      // OracleCore 合约地址
      "oracleCoreContractAddress": "0x66c3631ced2c63379f2133180f70aedf1d728869",
      // VRF 合约地址
      "vrfContractAddress": "0x741a6ddfa69c6dbeee8ecea651f2748f80404009",
      "fromBlock": "latest",
      "toBlock": "latest",
      "operator": "operator",
      "url": "http://localhost"
    }
  ]
}