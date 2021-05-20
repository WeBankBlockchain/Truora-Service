pragma solidity ^0.4.25;

library Address {

  function isContract(address account) internal view returns (bool) {
  uint256 size;

  assembly { size := extcodesize(account) }
return size > 0;
}
}

