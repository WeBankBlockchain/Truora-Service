pragma solidity ^0.4.25;

contract IBAC002  {
    event Send(address indexed from, address indexed to, uint256 assetId, bytes data);
    event Approval( address indexed owner, address approved, uint256 assetId);
    event ApprovalForAll( address indexed owner, address indexed operator, bool approved);

    function balance(address owner) public view returns (uint256 balance);

    function ownerOf(uint256 assetId) public view returns (address owner);

    function sendFrom(address from, address to, uint256 assetId, bytes memory data) public;

    function batchSendFrom(address from, address[] to, uint256[] assetId, bytes memory data) public;

    function destroy(uint256 assetId, bytes data) public;

    function issueWithAssetURI(address to, uint256 assetId, string memory assetURI, bytes data) public  returns (bool);

    function assetURI(uint256 assetId) external view returns (string memory);

    function approve(address to, uint256 assetId) public;
    function getApproved(uint256 assetId) public view returns (address operator);

    function setApprovalForAll(address to, bool _approved) public;
    function isApprovedForAll(address owner, address operator) public view returns (bool);

    function assetOfOwnerByIndex(address owner, uint256 index) public view returns (uint256) ;

    function safeTransferFrom(address from, address to, uint256 tokenId, bytes memory data) public;
}
