pragma solidity ^0.6.6;

interface IBAC001Receiver {
    /**
     * @notice Handle the receipt of an NFT
     * @dev The BAC001 smart contract calls this function on the recipient
     */
    function onBAC001Received(address operator, address from, uint256 value, bytes calldata data)
    external returns (bytes4);
}

contract BAC001Holder is IBAC001Receiver {
    function onBAC001Received(address, address, uint256, bytes memory) public override returns (bytes4) {
        return this.onBAC001Received.selector;
    }
}

