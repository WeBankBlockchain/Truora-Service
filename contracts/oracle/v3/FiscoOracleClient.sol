// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

import "./SafeMath.sol";
import "./OracleCoreInterface.sol";

abstract contract FiscoOracleClient {

  using SafeMath for uint256;

  OracleCoreInterface private oracle;
  uint256 private requestCount = 1;
  mapping(bytes32 => address) private pendingRequests;
  mapping (address => uint) private reqc;
  uint256 constant public EXPIRY_TIME = 10 * 60 * 1000;

  //support int256 ,string ,bytes
  // 0, 1, 2 
  enum ReturnType{ INT256, STRING, BYTES} 
   ReturnType  public returnType = ReturnType.INT256;

  event Requested(bytes32 indexed id,address oracleAddress,uint256 requestCount,string url);
  event Fulfilled(bytes32 indexed id);

  // internal
  function __callback(bytes32 requestId, bytes memory result) internal virtual;

  // __callback with proof
  function callback(bytes32 requestId, bytes memory result, bytes calldata proof) public onlyOracleCoreInvoke(requestId){
      __callback(requestId,result);
  }

  function setReturnType(ReturnType returnType_) public   {
	returnType = returnType_;
  }
  
  function oracleQuery(address _oracle, string memory url, uint256 timesAmount,ReturnType returnType_)
    internal
    returns (bytes32 requestId)
  {
     return oracleQuery(EXPIRY_TIME,"url", _oracle, url, timesAmount, false, returnType_);
  }

  function oracleQuery(uint expiryTime, string memory datasource, address _oracle, string memory url, uint256 timesAmount, bool needProof, ReturnType returnType_) internal
  returns (bytes32 requestId) {
    // calculate the id;
    oracle = OracleCoreInterface(_oracle);
    string memory chainId;
    string memory groupId;
    ( chainId, groupId) = oracle.getChainIdAndGroupId();
    requestId = keccak256(abi.encodePacked(chainId, groupId, this, requestCount));
    pendingRequests[requestId] = _oracle;
    require(oracle.query(address(this),requestCount, url,timesAmount, expiryTime,needProof, uint(returnType_)),"oracle-core invoke failed!");
    requestCount++;
    reqc[msg.sender]++;
	emit Requested(requestId,_oracle,requestCount,url);
    return requestId;
  }


  /**
   * @notice Sets the stored oracle core address
   * @param _oracle The address of the oracle core contract
   */
  function setOracleCoreAddress(address _oracle) internal {
    oracle = OracleCoreInterface(_oracle);
  }


  /**
   * @notice Retrieves the stored address of the oracle contract
   * @return The address of the oracle contract
   */
  function getOracleCoreAddress()
    internal
    view
    returns (address)
  {
    return address(oracle);
  }


  function bytesToBytes32(bytes memory source) public pure returns (bytes32 result_) {
	if (source.length == 0) {
		return 0x0;
	}
	assembly {
		result_ := mload(add(source, 32))
	}
  }


  /**
   * @dev Reverts if the sender is not the oracle of the request.
   * @param _requestId The request ID for fulfillment
   */
  modifier onlyOracleCoreInvoke(bytes32 _requestId) {
    require(msg.sender == pendingRequests[_requestId],
            "Source must be the oracle of the request");
    delete pendingRequests[_requestId];
    emit Fulfilled(_requestId);
    _;
  }
}
