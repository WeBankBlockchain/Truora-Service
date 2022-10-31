// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

contract Lite {
    int256 ii = 0;
	event ondoit(int256 i);
	
    function doit(int256 i ) external   returns(int) {
		ii = ii+i;
		emit ondoit(i);
		return ii;
   }

    function get()  external view  returns(int256) {
	    return ii;
	}


}

