// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

library ECVerify {

    function recover_sig(bytes32 hash, bytes memory signature) public pure returns (address signature_address)
    {
        require(signature.length == 65);

        bytes32 r;
        bytes32 s;
        uint8 v;

        // The signature format is a compact form of:
        //   {bytes32 r}{bytes32 s}{uint8 v}
        // Compact means, uint8 is not padded to 32 bytes.
        assembly {
            r := mload(add(signature, 32))
            s := mload(add(signature, 64))

            // Here we are loading the last 32 bytes, including 31 bytes following the signature.
            v := byte(0, mload(add(signature, 96)))
        }
        return recover_vrs(hash,v,r,s);
    }
	
	function verify_sig(address signAddr,bytes32 hash,bytes memory signature) public pure returns(bool ret)
	{
		address signature_address =  recover_sig(hash,signature);
		ret = false;
		if( signature_address == signAddr){
            ret =  true;
        }
		return ret;
	}

	function verify_vrs(address signAddr, bytes32 hash, uint8 v, bytes32 r, bytes32 s) public pure returns(bool ret)
    {
		address signature_address =  recover_vrs(hash,v, r, s);
        ret = false;
		if( signature_address == signAddr){
            ret =  true;
        }
		return ret;
        
    }
	
	function recover_vrs(bytes32 hash, uint8 v, bytes32 r, bytes32 s) public  pure  returns(address signature_address){
	    // Version of signature should be 27 or 28, but 0 and 1 are also possible
        if (v < 27) {
            v += 27;
        }
		require(v == 27 || v == 28);
        signature_address =  ecrecover(hash, v, r, s);
		require(signature_address != address(0x0));
		return signature_address;
    }
 
 
}
