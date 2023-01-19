// SPDX-License-Identifier: MIT
/**
 *Submitted for verification at Etherscan.io on 2020-08-27
*/
pragma solidity ^0.6.6;
import  "./SafeMath.sol";
import "./VRFUtil.sol";
import "./VRFK1.sol";


contract VRFTest{
	event vvout(uint256 i);

	function vv() public returns (bool){
	   uint256[2] memory _publicKey = [64784621316681796431794315516721696027200125503592974926505750510826186957097, 64669574164092736917599233362437793703932400143995870498179191886091538845902];
	   bytes memory _proof = hex"034e5f5cfbc4d5bf5631e3c3b689f94b2981e3deb6e361b8bfcf02a854d9453af6d1d9008536cede12b587c9297bc50c8166f8fc35b8b5c85585001d47237a60c75694bdcdd12de88304a7d32688702abd";
	   uint256[4] memory proofParam = VRFK1.decodeProof(_proof);
	   uint256 preSeed = 25432971849541135861483806483065338780405471535107090712277189328551485752465;
	   bytes32 blockHash = 0x7a867e9f610c681fae2949afcd9b2317c4748d4d4fe0548bfd9c2787e4c26ff2;
	   bytes32 actualSeed = (keccak256(abi.encodePacked(preSeed, blockHash)));
	   bytes memory byteSeed = bytes32ToBytes(actualSeed);
	   bool res = VRFK1.verify(_publicKey, proofParam, byteSeed);
	   emit vvout(0);
	   return res;
	}

	function bytes32ToBytes(bytes32 _bytes32) public pure returns (bytes memory) {
	  // string memory str = string(_bytes32);
	  // TypeError: Explicit type conversion not allowed from "bytes32" to "string storage pointer"
	  bytes memory bytesArray = new bytes(32);
	  for (uint256 i; i < 32; i++) {
		bytesArray[i] = _bytes32[i];
	  }
	  return bytesArray;
	}


}