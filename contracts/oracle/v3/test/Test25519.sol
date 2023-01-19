// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.6.10;
pragma experimental ABIEncoderV2;

import "./Crypto.sol";

contract Test25519{

	address cryptoContract = address(0x100a);

	function verify(bytes memory message, bytes memory publicKey, bytes memory proof) public view returns(bool, uint256){
		Crypto crypto = Crypto(cryptoContract);
		return crypto.curve25519VRFVerify(message,publicKey,proof);
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
	
	function verifyhex(bytes  memory message, bytes memory publicKey, bytes memory proof) public view returns(bool, uint256){
		Crypto crypto = Crypto(cryptoContract);
		string memory hexMessage = bytes2hex(message);
		return crypto.curve25519VRFVerify(bytes(hexMessage),publicKey,proof);
	}

}