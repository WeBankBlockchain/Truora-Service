


### 方式一：获取链下API数据
 用户可以参考contracts/0.4/oracle/APISampleOracle.sol合约实现自己的oracle合约。 只须改动url值即可。  
 oracleCoreAddress地址可以通过界面或者接口(http://ip:5012/Oracle-Service/oracle/address?chainId=1&groupId=1)查询到。  
   
 
  必须继承FiscoOracleClient合约即可。实现__callback方法，以便oracle-service将结果回写。  
  在request方法中填入要获取的链下url。  
  url格式：  
     目前支持json和text/plain两种访问格式。并且链下API的url必须支持HTTPS访问。
   - plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)
   - json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY
    
   合约解析如下：  
```

pragma solidity ^0.6.0;

import "./FiscoOracleClient.sol";

contract APISampleOracle is FiscoOracleClient {


    //指定处理的oracle
    address private oracleCoreAddress;

    // Multiply the result by 1000000000000000000 to remove decimals
    uint256 private timesAmount  = 10**18;

    mapping(bytes32=>int256) private resultMap;

    mapping(bytes32=>bool) private validIds;

    int256 public result;
    string url;

    constructor(address oracleAddress) public {
        oracleCoreAddress = oracleAddress;
    }


    function request() public returns (bytes32 requestId)
    {

          // Set your URL
          // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
         url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
         bytes32  requestId = oracleQuery(oracleCoreAddress, url, timesAmount);
         validIds[requestId] = true;
    }

    /**
     * Receive the response in the form of int256
     */
    function __callback(bytes32 _requestId, int256 _result) public override onlyOracleCoreInvoke(_requestId)
    {
        require(validIds[_requestId], "id must be not used!") ;
        resultMap[_requestId]= _result;
        delete validIds[_requestId];
        result = _result ;
    }


      function get()  public view  returns(int256){
         return result;
      }
}
```

### VRF获取可验证随机数(暂不支持国密)
  
  用户合约开发只需继承VRFConsumerBase（contracts/0.4/oracle目录下）合约即可。必须实现fulfillRandomness方法，以便oracle-service将结果回写。

```
pragma solidity 0.6.6;

import "./VRFConsumerBase.sol";

contract RandomNumberConsumer is VRFConsumerBase {

    // hash of VRF Coordinator'pk
    bytes32 internal keyHash;
    uint256 public randomResult;

    // 填写 coordinator地址 和 oracleservice 的keyhash
    constructor(address _coordinator, bytes32 _keyHash)
        VRFConsumerBase(
            _coordinator // VRF Coordinator
        ) public
    {
         // keyHash = 0xeedf1a9c68b3f4a8b1a1032b2b5ad5c4795c026514f8317c7a215e218dccd6cf;
          keyHash = _keyHash;
    }

    /**
     * Requests randomness from a user-provided seed
     */
    function getRandomNumber(uint256 userProvidedSeed) public returns (bytes32 requestId) {
        return requestRandomness(keyHash, userProvidedSeed);
    }

    /**
     * Callback function used by VRF Coordinator
     */
    function fulfillRandomness(bytes32 requestId, uint256 randomness) internal override {
        randomResult = randomness;
    }
}
  ```
  
  
### 去中心化部署获取聚合结果



  latestAnswer()最新的聚合结果
  
  latestTimestamp() 最新一次聚合的时间戳
  
  latestRound()最新一次聚合的轮次号
  
  getAnswer(uint256 roundId) 通过轮次号获取历史结果
  
  getTimestamp(uint256 roundId)通过轮次号获取历史时间戳