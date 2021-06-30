pragma solidity ^0.4.25;

import "./VRFClient.sol";

contract RandomNumberSampleVRF is VRFClient {

    bytes32 internal keyHash;
    uint256 public randomResult;
    mapping(bytes32=>uint256) private resultMap;

    mapping(bytes32=>bool) private validIds;
    //指定处理的 vrf
    address private vrfCoreAddress;

    constructor(address _vrfCore, bytes32 _keyHash) public {
        vrfCoreAddress = _vrfCore;
        keyHash = _keyHash;
    }
    /**
     * Requests randomness from a user-provided seed
     */
    function getRandomNumber(uint256 userProvidedSeed) public returns (bytes32 ) {
        bytes32  requestId =  vrfQuery(vrfCoreAddress, keyHash, userProvidedSeed);
        validIds[requestId] = true;
        return requestId;
    }

    /**
     * Callback function used by VRF Coordinator
     */
    function __callbackRandomness(bytes32 requestId, uint256 randomness) internal  {
        require(validIds[requestId], "id must be not used!") ;
        randomResult = randomness;
        resultMap[requestId]=  randomResult;
        delete validIds[requestId];
    }

    function get()  public view  returns(uint256){
        return randomResult;
    }

    function getById(bytes32 id)  public view  returns(uint256){
        return resultMap[id];
    }

    function checkIdFulfilled(bytes32 id)  public view  returns(bool){
        return validIds[id];
    }
}