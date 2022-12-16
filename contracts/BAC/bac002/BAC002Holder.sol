pragma solidity ^0.6.6;

interface IBAC002Receiver {
    /**
     * @notice Handle the receipt of an NFT
     * @dev The BAC002 smart contract calls this function on the recipient
     */
    function onBAC002Received(address operator, address from, uint256 assetId, bytes calldata data)
    external returns (bytes4);
}

contract BAC002Holder is IBAC002Receiver {
    function onBAC002Received(address, address, uint256, bytes calldata ) public  override returns (bytes4) {
        return this.onBAC002Received.selector;
    }
}