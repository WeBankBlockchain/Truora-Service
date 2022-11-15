// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

import "./FiscoOracleClient.sol";

contract GeneralOracle is FiscoOracleClient {


    //指定处理的oracle
    address private oracleCoreAddress;



    mapping(bytes32=>uint256) private resultMap;

    mapping(bytes32=>bool) private validIds;

    uint256 public result;
    
	// samples:
	// url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
    // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
	// url = "{name='URL',url='https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new'}"}
	// url = "{name='HashURL',url='www.anyurl.com'}"} 获取url的文本，计算hash
	string private url = ""; 
	
	//结果再乘以一个系数
	uint256 private timesAmount = 1;


    constructor(address oracleAddress) public {
        oracleCoreAddress = oracleAddress;
    }


    function request() public returns (bytes32)
    {

         return requestSource(url,timesAmount,returnType);
          
    }

    function requestSource(string memory url_,uint256 timesAmount_,ReturnType returnType_) public returns (bytes32)
    {

          // Set your URL
         bytes32  requestId = oracleQuery(oracleCoreAddress, url_, timesAmount_, returnType_);
         validIds[requestId] = true;
         return requestId;
          
    }


    /**
     * Receive the response in the form of int256
     */
    function __callback(bytes32 _requestId, bytes memory _result) internal override
    {
        require(validIds[_requestId], "id must be not used!") ;
        result =   uint256(bytesToBytes32(_result));
        resultMap[_requestId]= result;
        delete validIds[_requestId];

    }

    function get()  public view  returns(uint256){
         return result;
    }
	  
	  
    function getById(bytes32 id)  public view  returns(uint256){
        return resultMap[id];
    }


    function checkIdFulfilled(bytes32 id)  public view  returns(bool){
        return validIds[id];
    }


    function setUrl(string memory _url) public {
         url = _url;
    }
	
	function setTimesAmount(uint256 timesAmount_) public{
		timesAmount = timesAmount_;
	}

    function setSource(string memory url_,uint256 timesAmount_,ReturnType returnType_) public{
		setUrl(url_);
		setTimesAmount(timesAmount_);
		setReturnType(returnType_);
		
	}
	
	function getUrl() public view  returns(string memory){
        return url;
    }
	function getTimesAmount() public view returns (uint256){
		return timesAmount;
	}

	function getSource() public view returns(string memory,uint256,ReturnType)
	{
		return (url,timesAmount,returnType);
	}
	

    function bytesToBytes32(bytes memory source) private pure returns (bytes32 result_) {
        if (source.length == 0) {
            return 0x0;
        }
        assembly {
            result_ := mload(add(source, 32))
        }
    }
}