// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

import "./Ownable.sol";
import "./SafeMath.sol";


contract OracleCore is  Ownable {
  using SafeMath for uint256;

  mapping(bytes32 => bytes32) private commitments;
  mapping(bytes32 => uint256) timeoutMap;
  string  chainId = "chain0";
  string  groupId = "group0";

  bytes4 private callbackFunctionId = bytes4(keccak256("callback(bytes32,bytes,bytes)"));



  event OracleRequest(
    address coreAddress,
    address callbackAddr,
    bytes32 requestId,
    string url,
    uint256  expiration,
    uint256 timesAmount,
    bool needProof,
    uint returnType
  );

  constructor(string  memory _chainId, string  memory _groupId) public Ownable()
  {
    chainId = _chainId;
    groupId = _groupId;
  }

  function getChainIdAndGroupId() public view  returns(string memory,string memory){
    return (chainId, groupId);
  }


	
  function query(
    address _callbackAddress,
    uint256 _nonce,
    string calldata _url,
    uint256 _timesAmount,
    uint256 _expiryTime,
    bool _needProof,
    uint _returnType
  )
    external
  returns(bool)
  {
    bytes32 requestId = keccak256(abi.encodePacked(chainId, groupId, _callbackAddress, _nonce));
    //require(commitments[requestId] == 0, "Must use a unique ID");
    uint256 expiration = now.add(_expiryTime);
    timeoutMap[requestId] = expiration;
    commitments[requestId] = keccak256(
      abi.encodePacked(
        _callbackAddress,
        expiration
      )
    );

    emit OracleRequest(
      address(this),
      _callbackAddress,
      requestId,
      _url,
      expiration,
     _timesAmount,
     _needProof,
     _returnType);

	
	return true;
  }


  function fulfillRequest(
    bytes32 _requestId,
    address _callbackAddress,
    uint256 _expiration,
    bytes calldata _result,
    bytes calldata proof
  )
    public
    onlyOwner
    isValidRequest(_requestId)
    returns (bool)
  {
    bytes32 paramsHash = keccak256(
      abi.encodePacked(
        _callbackAddress,
        _expiration
      )
    );
    require(commitments[_requestId] == paramsHash, "Params do not match request ID");
    delete commitments[_requestId];
    delete timeoutMap[_requestId];
    (bool success, ) = _callbackAddress.call(abi.encodeWithSelector(callbackFunctionId, _requestId, _result, proof)); // solhint-disable-line avoid-low-level-calls

    return success;
  }

  function checkRequestId(bytes32 _requestId) public  view returns  (bool,string memory){
    if(commitments[_requestId] == 0)return (false,"Must have a valid requestId");
    if(timeoutMap[_requestId] < now)return (false, "fulfill request time out");
    return (true,"ok");
  }


  /**
   * @dev Reverts if request ID does not exist or time out.
   * @param _requestId The given request ID to check in stored `commitments`
   */
  modifier isValidRequest(bytes32 _requestId) {
    (bool res,string memory msg) = checkRequestId(_requestId);
	require(res,msg);
    _;
  }


}
