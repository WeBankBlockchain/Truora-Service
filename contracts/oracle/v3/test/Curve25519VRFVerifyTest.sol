pragma solidity >=0.6.10 <0.8.20;
pragma experimental ABIEncoderV2;
import "./Crypto.sol";

contract Curve25519VRFVerifyTest
{
    Crypto crypto;
	constructor() public
	{
		crypto = Crypto(address(0x100a));
	}
    function curve25519VRFVerify(bytes memory input, bytes memory vrfPublicKey, bytes memory vrfProof) public returns(bool, uint256)
    {

        bool verifyResult;
		uint256 randomValue; 
		(verifyResult, randomValue) = crypto.curve25519VRFVerify(input, vrfPublicKey, vrfProof);
		require(verifyResult == true);
    }
}
