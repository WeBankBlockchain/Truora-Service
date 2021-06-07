pragma solidity ^0.6.0;

import {SafeMath} from "../SafeMath.sol";
import "./VRFUtil.sol";
import "./VRFCoreInterface.sol";

abstract contract VRFClient is VRFUtil {

    using SafeMath for uint256;

    mapping(bytes32 => address) private pendingRequests;

    // VRF Core Contract
    VRFCoreInterface private vrfCore;

    // Nonces for each VRF key from which randomness has been requested.
    //
    // Must stay in sync with VRFCoordinator[_keyHash][this]
    mapping(bytes32 /* keyHash */ => uint256 /* nonce */) public nonces;

    // call by VRF Core and full fill random number
    function __callbackRandomness(bytes32 requestId, uint256 randomness) internal virtual;

    function callbackRandomness(bytes32 requestId, uint256 randomness) public onlyVRFCoreInvoke(requestId) {
        __callbackRandomness(requestId,randomness);
    }


    // call by VRF Client
    function vrfQuery(address _vrfCoreAddress, bytes32 _keyHash, uint256 _seed) public returns (bytes32 requestId) {
        vrfCore = VRFCoreInterface(_vrfCoreAddress);
        vrfCore.randomnessRequest(_keyHash, _seed, address(this));

        // get seed
        uint256 preSeed = makeVRFInputSeed(_keyHash, _seed, address(this), nonces[_keyHash]);

        // nonces[_keyHash] must stay in sync with
        nonces[_keyHash] = nonces[_keyHash].add(1);

        // get requestId for call back
        int256 chainId;
        int256 groupId;
        (chainId, groupId) = vrfCore.getChainIdAndGroupId();
        requestId = makeRequestId(chainId, groupId, _keyHash, preSeed);

        pendingRequests[requestId] = _vrfCoreAddress;

        return requestId;
    }



    /**
     * @dev Reverts if the sender is not the oracle of the request.
     * @param _requestId The request ID for fulfillment
     */
    modifier onlyVRFCoreInvoke(bytes32 _requestId) {
        require(msg.sender == pendingRequests[_requestId],
            "Source must be the vrfCore of the request");
        delete pendingRequests[_requestId];
        _;
    }

}
