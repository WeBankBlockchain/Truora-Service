// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

import "./FiscoOracleClient.sol";

contract GeneralOracle is FiscoOracleClient {


    //指定处理的oracle
    address private oracleCoreAddress;

	
	mapping(bytes32=>bytes) private rawResultMap;

    mapping(bytes32=>int) private validIds;
	
	mapping(bytes32=>ReturnType) private typeMap;

    
	// samples:
	// url = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
    // url = "json(https://api.exchangerate-api.com/v4/latest/CNY).rates.JPY";
	// url = "{name='URL',url='https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new'}"}
	// url = "{name='HashURL',url='www.anyurl.com'}"} 获取url的文本，计算hash
	string private url = ""; 
	



    constructor(address oracleAddress) public {
        oracleCoreAddress = oracleAddress;
    }


    function requestSource(string memory url_,uint256 timesAmount_,ReturnType returnType_) public returns (bytes32)
    {

         bytes32  requestId = oracleQuery(oracleCoreAddress, url_, timesAmount_, returnType_);
         validIds[requestId] = 1;
		 typeMap[requestId] = returnType_;
         return requestId;
          
    }


    /**
     * Receive the response
     */
    function __callback(bytes32 _requestId, bytes memory _result) internal override
    {
        require((validIds[_requestId] == 1), "id must be ongoing!") ;
		rawResultMap[_requestId] = _result;
        validIds[_requestId] = 2;
    }
	
	 function getById(bytes32 id)  public view  returns(bytes memory){
		
        return rawResultMap[id];
    }

	function getIntById(bytes32 id) public view returns(uint256){
		ReturnType rtype = typeMap[id];
		if(rtype == ReturnType.INT256){
			uint256 result;
			result =  uint256(bytesToBytes32(rawResultMap[id]));
			return result;
		}else{
			return 0;
		}
		
	}	

    function checkIdFulfilled(bytes32 id)  public view  returns(int){
        return validIds[id];
    }
	
	function getReqType(bytes32 id) public view returns(ReturnType){
		return typeMap[id];
	}

}