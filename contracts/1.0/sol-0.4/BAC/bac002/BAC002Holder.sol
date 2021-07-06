pragma solidity ^0.4.25;


contract IBAC002Receiver {
    /**
     * @notice Handle the receipt of an NFT
     * @dev The BAC002 smart contract calls this function on the recipient
     */
    function onBAC002Received(address operator, address from, uint256 assetId, bytes memory data)
    public returns (bytes4);
}

contract BAC002Holder is IBAC002Receiver {
    function onBAC002Received(address, address, uint256, bytes memory) public returns (bytes4) {
        return this.onBAC002Received.selector;
    }
}