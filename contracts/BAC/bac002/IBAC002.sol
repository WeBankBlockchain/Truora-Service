pragma solidity ^0.6.6;


interface IBAC002  {
    event Send(address indexed from, address indexed to, uint256 assetId, bytes data);
    event Approval( address indexed owner, address approved, uint256 assetId);
    event ApprovalForAll( address indexed owner, address indexed operator, bool approved);

    function balance(address owner) external view returns (uint256 balance);

    function ownerOf(uint256 assetId) external view returns (address owner);

    function sendFrom(address from, address to, uint256 assetId, bytes calldata data) external;

    function batchSendFrom(address from, address[] calldata to, uint256[] calldata assetId, bytes calldata data) external;

    function destroy(uint256 assetId, bytes calldata data) external;

    function issueWithAssetURI(address to, uint256 assetId, string calldata assetURI, bytes  calldata data) external  returns (bool);

    function assetURI(uint256 assetId) external view returns (string calldata);

    function approve(address to, uint256 assetId) external;
    function getApproved(uint256 assetId) external view returns (address operator);

    function setApprovalForAll(address to, bool _approved) external;
    function isApprovedForAll(address owner, address operator) external view returns (bool);

    function assetOfOwnerByIndex(address owner, uint256 index) external view returns (uint256) ;

    function safeTransferFrom(address from, address to, uint256 tokenId, bytes calldata data) external;
}
