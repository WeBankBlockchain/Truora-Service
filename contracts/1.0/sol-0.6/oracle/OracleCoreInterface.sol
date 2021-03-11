pragma solidity ^0.6.0;

interface OracleCoreInterface  {

    function query(
    address _callbackAddress,
    uint256 _nonce,
    string calldata _url,
    uint256 _timesAmount,
    uint256 _expiryTime,
    bool needProof,
    uint returnType
  ) external
   returns(bool) ;

    function getChainIdAndGroupId()  external view  returns(int256,int256) ;


}

