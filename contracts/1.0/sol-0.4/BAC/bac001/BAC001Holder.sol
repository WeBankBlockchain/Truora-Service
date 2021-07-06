pragma solidity ^0.4.25;

contract IBAC001Receiver {
    /**
     * @notice Handle the receipt of an NFT
     * @dev The BAC001 smart contract calls this function on the recipient
     */
    function onBAC001Received(address operator, address from, uint256 value, bytes data)
    public returns (bytes4);
}

contract BAC001Holder is IBAC001Receiver {
    function onBAC001Received(address, address, uint256, bytes) public returns (bytes4) {
        return this.onBAC001Received.selector;
    }
}

