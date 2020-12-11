
/**
 *Submitted for verification at Etherscan.io on 2020-08-27
*/
pragma solidity ^0.6.6;
import  "./SafeMath.sol";
import "./VRFRequestIDBase.sol";
import "./VRF.sol";
/**
 * @title VRFCoordinator coordinates on-chain verifiable-randomness requests
 * @title with off-chain responses
 */

 interface VRFConsumerBaseInterface  {

  function rawFulfillRandomness(bytes32 requestId, uint256 randomness) external ;

}

contract VRFCoordinator is VRF, VRFRequestIDBase {

  using SafeMath for uint256;

  //BlockHashStoreInterface internal blockHashStore;

//  constructor(address _link, address _blockHashStore) public {
//  //  blockHashStore = BlockHashStoreInterface(_blockHashStore);
//  }

  struct Callback { // Tracks an ongoing request
    address callbackContract; // Requesting contract, which will receive response

    // Commitment to seed passed to oracle by this contract, and the number of
    // the block in which the request appeared. This is the keccak256 of the
    // concatenation of those values. Storing this commitment saves a word of
    // storage.
    bytes32 seedAndBlockNum;
  }


//  struct ServiceAgreement { // Tracks oracle commitments to VRF service
//    address vRFOracle; // Oracle committing to respond with VRF service
//  }


  mapping(bytes32 /* (provingKey, seed) */ => Callback) public callbacks;
//  mapping(bytes32 /* provingKey */ => ServiceAgreement)
//    public serviceAgreements;

  mapping(bytes32 /* provingKey */ => mapping(address /* consumer */ => uint256))
    private nonces;

  // The oracle only needs the jobID to look up the VRF, but specifying public
  // key as well prevents a malicious oracle from inducing VRF outputs from
  // another oracle by reusing the jobID.
  event RandomnessRequest(
    bytes32 keyHash,
    uint256 seed,
    uint256 blockNumber,
    address sender,
    bytes32 requestId,
    bytes32  seedAndBlockNum);

  event RandomnessRequestFulfilled(bytes32 requestId, uint256 output);


  /**
   * @notice creates the chainlink request for randomness
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
    address _sender)
    public
  {
    uint256 nonce = nonces[_keyHash][_sender];
    uint256 preSeed = makeVRFInputSeed(_keyHash, _consumerSeed, _sender, nonce);
    bytes32 requestId = makeRequestId(_keyHash, preSeed);
    // Cryptographically guaranteed by preSeed including an increasing nonce
    assert(callbacks[requestId].callbackContract == address(0));
    callbacks[requestId].callbackContract = _sender;
    callbacks[requestId].seedAndBlockNum = keccak256(abi.encodePacked(
      preSeed, block.number));
    emit RandomnessRequest(_keyHash, preSeed, block.number,
      _sender, requestId,callbacks[requestId].seedAndBlockNum);
    nonces[_keyHash][_sender] = nonces[_keyHash][_sender].add(1);
  }

  // Offsets into fulfillRandomnessRequest's _proof of various values
  //
  // Public key. Skips byte array's length prefix.
  uint256 public constant PUBLIC_KEY_OFFSET = 0x20;
  // Seed is 7th word in proof, plus word for length, (6+1)*0x20=0xe0
  uint256 public constant PRESEED_OFFSET = 0xe0;

  /**
   * @notice Called by the chainlink node to fulfill requests
   *
   * @param _proof the proof of randomness. Actual random output built from this
   *
   * @dev The structure of _proof corresponds to vrf.MarshaledOnChainResponse,
   * @dev in the node source code. I.e., it is a vrf.MarshaledProof with the
   * @dev seed replaced by the preSeed, followed by the hash of the requesting
   * @dev block.
   */
  function fulfillRandomnessRequest(bytes memory _proof) public {

    (bytes32 currentKeyHash, Callback memory callback, bytes32 requestId,
     uint256 randomness) = getRandomnessFromProof(_proof);

    // Pay oracle

    // Forget request. Must precede callback (prevents reentrancy)
    delete callbacks[requestId];
    callBackWithRandomness(requestId, randomness, callback.callbackContract);

    emit RandomnessRequestFulfilled(requestId, randomness);
  }

  function callBackWithRandomness(bytes32 requestId, uint256 randomness,
    address consumerContract) internal {
    // Dummy variable; allows access to method selector in next line. See
    // https://github.com/ethereum/solidity/issues/3506#issuecomment-553727797
    bytes4 s =  bytes4(keccak256("rawFulfillRandomness(bytes32,uint256)"));
    bytes memory resp = abi.encodeWithSelector(
      s, requestId, randomness);
    (bool success,) = consumerContract.call(resp);
    // Avoid unused-local-variable warning. (success is only present to prevent
    // a warning that the return value of consumerContract.call is unused.)
   (success);

  }

  function getRandomnessFromProof(bytes memory _proof)
    internal view returns (bytes32 currentKeyHash, Callback memory callback,
      bytes32 requestId, uint256 randomness) {
    // blockNum follows proof, which follows length word (only direct-number
    // constants are allowed in assembly, so have to compute this in code)
    uint256 BLOCKNUM_OFFSET = 0x20 + PROOF_LENGTH;
    // _proof.length skips the initial length word, so not including the
    // blocknum in this length check balances out.
    require(_proof.length == BLOCKNUM_OFFSET, "wrong proof length");
    uint256[2] memory publicKey;
    uint256 preSeed;
    uint256 blockNum;
    assembly { // solhint-disable-line no-inline-assembly
      publicKey := add(_proof, PUBLIC_KEY_OFFSET)
      preSeed := mload(add(_proof, PRESEED_OFFSET))
      blockNum := mload(add(_proof, BLOCKNUM_OFFSET))
    }
    currentKeyHash = hashOfKey(publicKey);
    requestId = makeRequestId(currentKeyHash, preSeed);
    callback = callbacks[requestId];
    require(callback.callbackContract != address(0), "no corresponding request");
    require(callback.seedAndBlockNum == keccak256(abi.encodePacked(preSeed,
      blockNum)), "wrong preSeed or block num");

    bytes32 blockHash = blockhash(blockNum);
    // The seed actually used by the VRF machinery, mixing in the blockhash
    uint256 actualSeed = uint256(keccak256(abi.encodePacked(preSeed, blockHash)));
    // solhint-disable-next-line no-inline-assembly
    assembly { // Construct the actual proof from the remains of _proof
   //   mstore(add(_proof, PRESEED_OFFSET), actualSeed)
      mstore(_proof, PROOF_LENGTH)
    }
    randomness = VRF.randomValueFromVRFProof(_proof); // Reverts on failure
  }



 function getRandomnessFromProof1(bytes memory _proof)
    public view returns (bytes32 currentKeyHash,
      bytes32 requestId, uint256 randomness, uint256[2] memory publicKey,uint256 preSeed) {
    // blockNum follows proof, which follows length word (only direct-number
    // constants are allowed in assembly, so have to compute this in code)
    uint256 BLOCKNUM_OFFSET = 0x20 + PROOF_LENGTH;
    // _proof.length skips the initial length word, so not including the
    // blocknum in this length check balances out.
    require(_proof.length == BLOCKNUM_OFFSET, "wrong proof length");
//    uint256[2] memory publicKey;
//    uint256 preSeed;
    uint256 blockNum;
    assembly { // solhint-disable-line no-inline-assembly
      publicKey := add(_proof, PUBLIC_KEY_OFFSET)
      preSeed := mload(add(_proof, PRESEED_OFFSET))
      blockNum := mload(add(_proof, BLOCKNUM_OFFSET))
    }
    currentKeyHash = hashOfKey(publicKey);
    requestId = makeRequestId(currentKeyHash, preSeed);
    Callback memory callback = callbacks[requestId];
//    require(callback.callbackContract != address(0), "no corresponding request");
//    require(callback.seedAndBlockNum == keccak256(abi.encodePacked(preSeed,
//      blockNum)), "wrong preSeed or block num");

    bytes32 blockHash = blockhash(blockNum);
//    if (blockHash == bytes32(0)) {
//      blockHash = blockHashStore.getBlockhash(blockNum);
//      require(blockHash != bytes32(0), "please prove blockhash");
//    }
    // The seed actually used by the VRF machinery, mixing in the blockhash
    uint256 actualSeed = uint256(keccak256(abi.encodePacked(preSeed, blockHash)));
    // solhint-disable-next-line no-inline-assembly
    assembly { // Construct the actual proof from the remains of _proof
     // mstore(add(_proof, PRESEED_OFFSET), actualSeed)
      mstore(_proof, PROOF_LENGTH)
    }
    randomness = VRF.randomValueFromVRFProof(_proof); // Reverts on failure
  }

  /**
   * @notice Returns the serviceAgreements key associated with this public key
   * @param _publicKey the key to return the address for
   */
  function hashOfKey(uint256[2] memory _publicKey) public pure returns (bytes32) {
    return keccak256(abi.encodePacked(_publicKey));
  }

}