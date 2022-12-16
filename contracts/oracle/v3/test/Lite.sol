// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;
import "./EllipticCurve.sol";

contract Lite {
    int256 ii = 0;
	event ondoit(int256 i);
	event onblock(uint256 blockNumber,bytes32 blockHash);
	event onblocknumber(uint256 blockNumber );
    function doit(int256 i ) external   returns(int) {
		ii = ii+i;
		emit ondoit(i);
		return ii;
	}

	function get()  external view  returns(int256) {
		return ii;
	}

	function getblock(uint256 blockNumber) external   returns(bytes32) {
		bytes32 blockHash = blockhash(blockNumber);
		emit onblocknumber(block.number );
		emit onblock(blockNumber,blockHash);
		return blockHash;
	}
	
	function callblock(uint256 blockNumber) external  view returns(bytes32) {
		bytes32 blockHash = blockhash(blockNumber);
		return blockHash;	
	}
	
	function ec()external  view returns(uint256,uint256)
	{
	        (uint256 r1, uint256 r2) = EllipticCurve.ecSub(
            1,2,3,4,5,6);
			return (r1,r2);
			
	}
	
	function ecm()external  view returns(uint256,uint256)
	{
	    return EllipticCurve.ecMul(
            1,2,3,4,5
        );
			
			
	}
	
	function eci()external  view returns(uint256,uint256)
	{
		uint256 _x = 100;
        uint256 _y = 200;
        uint256 _pp = 300;
	    return  (_x, (_pp - _y) % _pp);
			
			
	}
}

