pragma solidity ^0.4.25;

contract VRFUtil {


  function makeVRFInputSeed(bytes32 _keyHash, uint256 _userSeed,
    address _requester, uint256 _nonce)
    internal pure returns (uint256)
  {
    return  uint256(keccak256(abi.encode(_keyHash, _userSeed, _requester, _nonce)));
  }

  function makeRequestId(int256 _chainId, int256 _groupId, bytes32 _keyHash, uint256 _vRFInputSeed)
        internal pure returns (bytes32) {
    return keccak256(abi.encodePacked(_chainId, _groupId, _keyHash, _vRFInputSeed));
  }
}
