pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "../bac002/IBAC002.sol";
import "../bac001/IBAC001.sol";
import "../bac002/BAC002Holder.sol";
import "../bac001/BAC001Holder.sol";


contract AuctionFixedPrice is BAC002Holder, BAC001Holder {

    struct auctionDetails {
        address seller;
        uint256 price;
        uint256 duration;
        address ftAssetAddress;
        bool isActive;
    }

    mapping(address => mapping(uint256 => auctionDetails)) public nftAssetToAuction;


    /**
       Seller puts the item on auction
    */
    function createNFTAssetAuction(
        address _nft,
        uint256 _nftAssetId,
        address _ftAssetAddress,
        uint256 _price,
        uint256 _duration
    ) external {
        require(msg.sender != address(0));
        require(_nft != address(0));
        require(_price > 0);
        require(_duration > 0);
        auctionDetails memory _auction = auctionDetails({
        seller: msg.sender,
        price: _price,
        duration: _duration,
        ftAssetAddress: _ftAssetAddress,
        isActive: true
        });
        address owner = msg.sender;
        IBAC002(_nft).sendFrom(owner, address(this), _nftAssetId,"");
        nftAssetToAuction[_nft][_nftAssetId] = _auction;
    }

    /**
       Purchaser buy the NFT Asset when the auction duration is not over the limit
    */
    function purchaseNFTAsset(address _nft, uint256 _nftAssetId) external {
        auctionDetails storage auction = nftAssetToAuction[_nft][_nftAssetId];
        require(auction.duration > block.timestamp, "Deadline already passed");
        require(auction.isActive);
        auction.isActive = false;
        address seller = auction.seller;
        uint price = auction.price;
        IBAC001(auction.ftAssetAddress).sendFrom(msg.sender,seller,price,"");
        IBAC002(_nft).sendFrom(address(this),msg.sender , _nftAssetId,"");
    }

    /**
       Called by the seller if they want to cancel the auction for their nft so the bidders get back the locked eeth and the seller get's back the nft
    */
    function cancelAution(address _nft, uint256 _nftAssetId) external {
        auctionDetails storage auction = nftAssetToAuction[_nft][_nftAssetId];
        require(auction.seller == msg.sender);
        require(auction.isActive);
        auction.isActive = false;
        IBAC002(_nft).sendFrom(address(this), auction.seller, _nftAssetId, "");
    }

    function getNftAssetAuctionDetails(address _nft, uint256 _nftAssetId) public view returns (address,uint256) {
        auctionDetails memory auction = nftAssetToAuction[_nft][_nftAssetId];
        return (auction.ftAssetAddress,auction.price);
    }

}