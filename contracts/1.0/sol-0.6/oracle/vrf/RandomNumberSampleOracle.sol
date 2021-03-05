pragma solidity ^0.6.6;

import "./VRFConsumerBase.sol";

contract RandomNumberSampleOracle is VRFConsumerBase {

    bytes32 internal keyHash;
    uint256 public randomResult;

    constructor(address _coordinator, bytes32 _keyHash)
        VRFConsumerBase(
            _coordinator // VRF Coordinator
        ) public
    {
          keyHash = _keyHash;
    }

    /**
     * Requests randomness from a user-provided seed
     */
    function getRandomNumber(uint256 userProvidedSeed) public returns (bytes32 requestId) {
        return requestRandomness(keyHash, userProvidedSeed);
    }

    /**
     * Callback function used by VRF Coordinator
     */
    function fulfillRandomness(bytes32 requestId, uint256 randomness) internal override {
        randomResult = randomness;
    }
}