// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;
import "./ILite.sol";

contract CallLite  {
	
	ILite liteinterface;
	int256 i=0;
	event oncall(int256 i);
	
    function calllite(address liteaddr,int256 _i ) external   returns(int256) {
		liteinterface = ILite(liteaddr);
		int256 res = liteinterface.doit(_i);
		i=i+res;
		emit oncall(res);
		return i;
	}

    function get()  external view  returns(int256){
		return i;
	} 
}
