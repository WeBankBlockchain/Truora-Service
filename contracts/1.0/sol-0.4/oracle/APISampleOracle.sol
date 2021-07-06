pragma solidity ^0.4.25;

import "./FiscoOracleClient.sol";

contract APISampleOracle is FiscoOracleClient {


    //指定处理的oracle
    address private oracleCoreAddress;

    // Multiply the result by 1000000000000000000 to remove decimals
    uint256 private timesAmount  = 10**18;

    mapping(bytes32=>int256) private resultMap;

    mapping(bytes32=>bool) private validIds;

    int256 public result;
    string private url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";


    constructor(address oracleAddress) public {
        oracleCoreAddress = oracleAddress;
    }


    function request() public returns (bytes32)
    {

          // Set your URL
          // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
         bytes32  requestId = oracleQuery(oracleCoreAddress, url, timesAmount, returnType);
         validIds[requestId] = true;
         return requestId;
          
    }

    /**
     * Receive the response in the form of int256
     */
    function __callback(bytes32 _requestId, bytes _result) internal
    {
        require(validIds[_requestId], "id must be not used!") ;
        result =   int256(bytesToBytes32(_result));
        resultMap[_requestId]= result;
        delete validIds[_requestId];

    }

      function get()  public view  returns(int256){
         return result;
      }

    function getById(bytes32 id)  public view  returns(int256){
        return resultMap[id];
    }

    function checkIdFulfilled(bytes32 id)  public view  returns(bool){
        return validIds[id];
    }


     function setUrl(string memory _url) public {
         url = _url;
     }

    function getUrl() public view  returns(string memory){
        return url;
    }

    function bytesToBytes32(bytes memory source) private pure returns (bytes32 result) {
        if (source.length == 0) {
            return 0x0;
        }
        assembly {
            result := mload(add(source, 32))
        }
    }
}