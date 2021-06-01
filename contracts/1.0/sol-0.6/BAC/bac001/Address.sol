pragma solidity ^0.6.6;

library Address {

  function isContract(address account) internal view returns (bool) {
  uint256 size;

  assembly { size := extcodesize(account) }
return size > 0;
}
}

