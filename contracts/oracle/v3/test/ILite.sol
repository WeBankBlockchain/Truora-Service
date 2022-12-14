// SPDX-License-Identifier: MIT
pragma solidity ^0.6.0;

interface ILite  {

	
    function doit(int256 i ) external   returns(int) ;

    function get()  external view  returns(int256) ;


}
