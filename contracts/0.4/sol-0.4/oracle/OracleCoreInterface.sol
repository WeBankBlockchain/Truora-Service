pragma solidity ^0.4.4;

interface OracleCoreInterface  {

    function query(
    address _callbackAddress,
    uint256 _nonce,
    string  _url,
    uint256 _timesAmount,
    uint256 _expiryTime,
    bool needProof
  ) external
   returns(bool) ;

}