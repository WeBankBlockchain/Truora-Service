pragma solidity ^0.6.6;

interface OracleCoreInterface  {

    function query(
    address _callbackAddress,
    uint256 _nonce,
    string calldata _url,
    uint256 _timesAmount,
    uint256 _expiryTime,
    bool needProof
  ) external
   returns(bool) ;

}

