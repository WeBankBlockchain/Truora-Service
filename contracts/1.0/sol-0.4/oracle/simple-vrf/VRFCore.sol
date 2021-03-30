
/**
 *Submitted for verification at Etherscan.io on 2020-08-27
*/
pragma solidity ^0.4.12;

import  "./SafeMath.sol";
import "./VRFUtil.sol";
import "./VRF.sol";
import "./Ownable.sol";

contract VRFCore is VRFUtil, Ownable {

  using SafeMath for uint256;

  struct Callback { // Tracks an ongoing request
    address callbackContract; // Requesting contract, which will receive response

    // Commitment to seed passed to oracle by this contract, and the number of
    // the block in which the request appeared. This is the keccak256 of the
    // concatenation of those values. Storing this commitment saves a word of
    // storage.
    bytes32 seedAndBlockNum;
  }

  mapping(bytes32 /* (provingKey, seed) */ => Callback) public callbacks;

  mapping(bytes32 /* provingKey */ => mapping(address /* consumer */ => uint256))

  private nonces;

  // The oracle only needs the jobID to look up the VRF, but specifying public
  // key as well prevents a malicious oracle from inducing VRF outputs from
  // another oracle by reusing the jobID.
  event RandomnessRequest(
    address coreAddress,
    bytes32 keyHash,
    uint256 seed,
    uint256 blockNumber,
    address sender,
    bytes32 requestId,
    bytes32  seedAndBlockNum,
    uint256 consumerSeed );

  event RandomnessRequestFulfilled(bytes32 requestId, uint256 output);


  int256 private chainId;
  int256 private groupId;

  constructor(int256 _chainId, int256 _groupId) public Ownable() {
    chainId = _chainId;
    groupId = _groupId;
  }

  function getChainIdAndGroupId()  public view  returns(int256,int256){
    return (chainId, groupId);
  }

  /**
   *
   * @param _keyHash ID of the VRF public key against which to generate output
   * @param _consumerSeed Input to the VRF, from which randomness is generated
   * @param _sender Requesting contract; to be called back with VRF output
   *
   * @dev _consumerSeed is mixed with key hash, sender address and nonce to
   * @dev obtain preSeed, which is passed to VRF oracle, which mixes it with the
   * @dev hash of the block containing this request, to compute the final seed.
   *
   * @dev The requestId used to store the request data is constructed from the
   * @dev preSeed and keyHash.
   */
  function randomnessRequest(
    bytes32 _keyHash,
    uint256 _consumerSeed,
    address _sender) external returns(bool) {
    // record nonce
    uint256 nonce = nonces[_keyHash][_sender];
    // preseed
    uint256 preSeed = makeVRFInputSeed( _keyHash, _consumerSeed, _sender, nonce);

    bytes32 requestId = makeRequestId(chainId, groupId, _keyHash, preSeed);
    // Cryptographically guaranteed by preSeed including an increasing nonce
    assert(callbacks[requestId].callbackContract == address(0));
    callbacks[requestId].callbackContract = _sender;
    callbacks[requestId].seedAndBlockNum = keccak256(abi.encodePacked(
        preSeed, block.number));
    emit RandomnessRequest(address (this), _keyHash, preSeed, block.number,
      _sender, requestId, callbacks[requestId].seedAndBlockNum, _consumerSeed);
    nonces[_keyHash][_sender] = nonces[_keyHash][_sender].add(1);
    return true;
  }

  /**
   *
   * @param _proof the proof of randomness. Actual random output built from this
   *
   * @dev The structure of _proof corresponds to vrf.MarshaledOnChainResponse,
   * @dev in the node source code. I.e., it is a vrf.MarshaledProof with the
   * @dev seed replaced by the preSeed, followed by the hash of the requesting
   * @dev block.
   */
  function fulfillRandomnessRequest(uint256[2] memory _publicKey, bytes memory _proof, uint256 preSeed, uint blockNumber) public {

    (bytes32 currentKeyHash, Callback memory callback, bytes32 requestId,
    uint256 randomness) = getRandomnessFromProof(_publicKey, _proof, preSeed, blockNumber);

    // Pay oracle

    // Forget request. Must precede callback (prevents reentrancy)
    delete callbacks[requestId];
    bool result  = callBackWithRandomness(requestId, randomness, callback.callbackContract);
    require(result, "call back failed!");
    emit RandomnessRequestFulfilled(requestId, randomness);
  }

  function callBackWithRandomness(bytes32 requestId, uint256 randomness, address consumerContract) internal returns (bool) {

    bytes4 s =  bytes4(keccak256("callbackRandomness(bytes32,uint256)"));
    bytes memory resp = abi.encodeWithSelector(
      s, requestId, randomness);
    (bool success,) = consumerContract.call(resp);
    // Avoid unused-local-variable warning. (success is only present to prevent
    // a warning that the return value of consumerContract.call is unused.)
    (success);
    return success;

  }


  function getRandomnessFromProof( uint256[2] memory _publicKey, bytes memory _proof, uint256 preSeed, uint blockNumber)
  internal view returns (bytes32 currentKeyHash, Callback memory callback,
    bytes32 requestId, uint256 randomness) {

    // blockNum follows proof, which follows length word (only direct-number
    // constants are allowed in assembly, so have to compute this in code)
    currentKeyHash = hashOfKey(_publicKey);
    requestId = makeRequestId(chainId,groupId,currentKeyHash, preSeed);
    callback = callbacks[requestId];
    require(callback.callbackContract != address(0), "no corresponding request");
    require(callback.seedAndBlockNum == keccak256(abi.encodePacked(preSeed,
      blockNumber)), "wrong preSeed or block num");

    bytes32 blockHash = blockhash(blockNumber);
    // The seed actually used by the VRF machinery, mixing in the blockHash
    bytes32 actualSeed = (keccak256(abi.encodePacked(preSeed, blockHash)));
    // solhint-disable-next-line no-inline-assembly

    uint256[4] memory proofParam =  VRF.decodeProof(_proof);

    require(VRF.verify(_publicKey, proofParam, bytes32ToBytes(actualSeed)), "proof check failed!");
    randomness = uint256 (VRF.gammaToHash(proofParam[0], proofParam[1])); // Reverts on failure
  }
  /**
   * @notice Returns the serviceAgreements key associated with this public key
   * @param _publicKey the key to return the address for
   */
  function hashOfKey(uint256[2] memory _publicKey) public pure returns (bytes32) {
    return keccak256(abi.encodePacked(_publicKey));
  }

  function bytes32ToBytes(bytes32 _bytes32) public view returns (bytes memory){
    // string memory str = string(_bytes32);
    // TypeError: Explicit type conversion not allowed from "bytes32" to "string storage pointer"
    bytes memory bytesArray = new bytes(32);
    for (uint256 i; i < 32; i++) {
      bytesArray[i] = _bytes32[i];
    }
    return bytesArray;
  }


}