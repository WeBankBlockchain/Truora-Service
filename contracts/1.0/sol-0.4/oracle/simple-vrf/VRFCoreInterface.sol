pragma solidity ^0.4.25;

interface VRFCoreInterface  {

    function randomnessRequest( bytes32 _keyHash,
        uint256 _consumerSeed,
        address _sender) external returns(bool) ;

    function getChainIdAndGroupId()  external view  returns(int256,int256) ;


}

