pragma solidity ^0.6.0;

import { SafeMath } from "./SafeMath.sol";
import "./VRFCore.sol";
import "./VRFID.sol";

abstract contract VRFClient  is VRFID {

    using SafeMath for uint256;

    mapping(bytes32 => address) private pendingRequests;
    address immutable  vrfCore;

    // Nonces for each VRF key from which randomness has been requested.
    //
    // Must stay in sync with VRFCoordinator[_keyHash][this]
    mapping(bytes32 /* keyHash */ => uint256 /* nonce */) public nonces;

    event Requested(bytes32 indexed id);
    event Fulfilled(bytes32 indexed id);

    constructor(address _vrfCore) public {
        vrfCore = _vrfCore;
    }


    function __callbackRandomness(bytes32 requestId, uint256 randomness) public virtual;


    function vrfQuery(bytes32 _keyHash, uint256 _seed)
    public returns (bytes32 requestId)
    {

        VRFCore(vrfCore).randomnessRequest(_keyHash, _seed, address(this));
        // This is the seed passed to VRFCoordinator. The oracle will mix this with
        // the hash of the block containing this request to obtain the seed/input
        // which is finally passed to the VRF cryptographic machinery.
        uint256 vRFSeed  = makeVRFInputSeed(_keyHash, _seed, address(this), nonces[_keyHash]);
        // nonces[_keyHash] must stay in sync with
        nonces[_keyHash] = nonces[_keyHash].add(1);
        requestId = makeRequestId(_keyHash, vRFSeed);
        pendingRequests[requestId] = vrfCore;
    }



    /**
  * @dev Reverts if the sender is not the oracle of the request.
  * @param _requestId The request ID for fulfillment
  */
    modifier onlyVRFCoreInvoke(bytes32 _requestId) {
        require(msg.sender == pendingRequests[_requestId],
            "Source must be the vrfcore of the request");
        delete pendingRequests[_requestId];
        emit Fulfilled(_requestId);
        _;
    }

}
