// SPDX-License-Identifier: MIT
/**
 *Submitted for verification at Etherscan.io on 2020-08-27
*/
pragma solidity ^0.6.6;
import  "./SafeMath.sol";
import "./VRFUtil.sol";
import "./Ownable.sol";
import "./Crypto.sol";

contract VRF25519Core is VRFUtil, Ownable {

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
  
  event doVerify(bool res,bytes actualSeed, bytes _publicKey, bytes _proof,uint256 randomness);


  string  chainId;
  string  groupId;

  constructor(string memory _chainId, string memory _groupId) public Ownable() {
    chainId = _chainId;
    groupId = _groupId;
  }

  function getChainIdAndGroupId()  public view  returns(string memory,string memory){
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

  /**
   *
   * @param _proof the proof of randomness. Actual random output built from this
   *
   * @dev The structure of _proof corresponds to vrf.MarshaledOnChainResponse,
   * @dev in the node source code. I.e., it is a vrf.MarshaledProof with the
   * @dev seed replaced by the preSeed, followed by the hash of the requesting
   * @dev block.
   */
  function fulfillRandomnessRequest(bytes memory _publicKey, bytes memory _proof, uint256 preSeed, uint blockNumber) public {

    (bytes32 currentKeyHash, Callback memory callback, bytes32 requestId,
    uint256 randomness) = getRandomnessFromProof(_publicKey, _proof, preSeed, blockNumber);

    // Pay oracle

    // Forget request. Must precede callback (prevents reentrancy)
    delete callbacks[requestId];
    bool result  = callBackWithRandomness(requestId, randomness, callback.callbackContract);
    require(result, "call back failed!");
    emit RandomnessRequestFulfilled(requestId, randomness);
  }



  function getRandomnessFromProof( bytes memory _publicKey, bytes memory _proof, uint256  preSeed, uint blockNumber)
  internal returns (bytes32 currentKeyHash, Callback memory callback,
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
	// 注意:这里把keccak的hash 结果转hex字符串，再从字符串取bytes，是因为当前版本的java只提供了string类型的vrf25519接口
	// 如java端seed种子为 "09B2BF6EB5..."，则实际上拿去生成vrf随机数的种子是个ASCII字符串，而不是bytes，为了对应，这里先转一下。后续java端支持了bytes，再改简单点
    string memory actualSeedstr = bytes32tohex(keccak256(abi.encodePacked(preSeed, blockHash)));
	bytes memory actualSeed = bytes(actualSeedstr);
    
	bool verifyResult;
	//使用fisco bcos 内置的VRF25519 预编译合约校验
	Crypto cryptoContract = Crypto(address(0x100a));
	(verifyResult, randomness) = cryptoContract.curve25519VRFVerify(actualSeed, _publicKey, _proof);  
	emit doVerify(verifyResult,actualSeed, _publicKey, _proof,randomness);
	require(verifyResult == true,"proof check failed!");
  }
  /**
   * @notice Returns the serviceAgreements key associated with this public key
   * @param _publicKey the key to return the address for
   */
  function hashOfKey(bytes memory _publicKey) public pure returns (bytes32) {
    return keccak256(abi.encodePacked(_publicKey));
  }

  function bytes32ToBytes(bytes32 _bytes32) public pure returns (bytes memory){
    // string memory str = string(_bytes32);
    // TypeError: Explicit type conversion not allowed from "bytes32" to "string storage pointer"
    bytes memory bytesArray = new bytes(32);
    for (uint256 i; i < 32; i++) {
      bytesArray[i] = _bytes32[i];
    }
    return bytesArray;
  }
	uint8[] private array = [48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70];

	function bytes2hex(bytes memory data)
		public view
		returns (string memory)
	{
		bytes memory ret = new bytes(64);
		for(uint i=0;i<32;i++) {
			uint8 b = uint8(data[i]);

			uint8 code1 = array[uint256((b >> 4))];
			ret[2*i ] = bytes1(code1);

			uint8 code2 = array[uint256(b & 0x0F)];
			ret[2*i+1] = bytes1(code2);

		}
		return string(ret);

	}
	
	function bytes32tohex(bytes32 data)
		public view
		returns (string memory)
	{
		bytes memory ret = new bytes(64);
		for(uint i=0;i<32;i++) {
			uint8 b = uint8(data[i]);

			uint8 code1 = array[uint256((b >> 4))];
			ret[2*i ] = bytes1(code1);

			uint8 code2 = array[uint256(b & 0x0F)];
			ret[2*i+1] = bytes1(code2);

		}
		return string(ret);

	}

}