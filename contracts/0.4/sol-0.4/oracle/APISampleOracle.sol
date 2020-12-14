pragma solidity ^0.4.4;

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


    function request() public returns (bytes32 )
    {

        // Set your URL
        // url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
        bytes32  requestId = oracleQuery(oracleCoreAddress, url, timesAmount);
        validIds[requestId] = true;
        return requestId;

    }

    /**
     * Receive the response in the form of int256
     */
    function __callback(bytes32 _requestId, int256 _result) public  onlyOracleCoreInvoke(_requestId)
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