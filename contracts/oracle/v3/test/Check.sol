// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;


contract Check {

function check(uint256 i) public  view returns  (bool,string memory){
    if(i == 0)return(false, "Must have a valid requestId");
    if(i<10)return(false, "fulfill request time out");
    return (true,"ok");
  }

  function isValid(uint256 i) public  view returns  (bool){
    (bool res,string memory desc) = check(i);
	require(res==true,desc);
	return true;
    
  }


}



