pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "../bac002/IBAC002.sol";
import "../bac001/IBAC001.sol";
import "../bac002/BAC002Holder.sol";
import "../bac001/BAC001Holder.sol";

contract AuctionUnfixedPrice is  BAC002Holder, BAC001Holder {

    struct auctionDetails {
        address seller;
        uint128 price;
        uint256 duration;
        address ftAssetAddress;
        uint256 maxBid;
        address maxBidUser;
        bool isActive;
        uint256[] bidAmounts;
        address[] users;
    }

    mapping(address => mapping(uint256 => auctionDetails)) public nftAssetToAuction;

    mapping(address => mapping(uint256 => mapping(address => uint256))) public bids;

    /**
       Seller puts the item on auction
    */
    function createNFTAssetAuction(
        address _nft,
        uint256 _nftAssetId,
        address _ftAssetAddress,
        uint128 _price,
        uint256 _duration
    ) external {
        require(msg.sender != address(0));
        require(_nft != address(0));
        require(_price > 0);
        require(_duration > 0);

        auctionDetails memory _auction = auctionDetails({
        seller: msg.sender,
        price: uint128(_price),
        duration: _duration,
        ftAssetAddress: _ftAssetAddress,
        maxBid: 0,
        maxBidUser: address(0),
        isActive: true,
        bidAmounts: new uint256[](0),
        users: new address[](0)
        });
        address owner = msg.sender;
        IBAC002(_nft).sendFrom(owner, address(this), _nftAssetId,"");
        nftAssetToAuction[_nft][_nftAssetId] = _auction;
    }
    /**
       Users bid for a particular nft, the max bid is compared and set if the current bid id highest
    */
    function bid(address _nft, uint256 _nftAssetId, uint256 _amount) external payable {
        auctionDetails storage auction = nftAssetToAuction[_nft][_nftAssetId];
        require(_amount >= auction.price);
        require(auction.isActive);
        require(auction.duration > block.timestamp, "Deadline already passed");

        bool exist = false;
        if (bids[_nft][_nftAssetId][msg.sender] > 0) {

        IBAC001(auction.ftAssetAddress).send(msg.sender,bids[_nft][_nftAssetId][msg.sender],"");
        exist = true ;
        }
        bids[_nft][_nftAssetId][msg.sender] = _amount;
        IBAC001(auction.ftAssetAddress).sendFrom(msg.sender, this, _amount, "");


        if (auction.bidAmounts.length == 0) {
            auction.maxBid = _amount;
            auction.maxBidUser = msg.sender;
        } else {
            uint256 lastIndex = auction.bidAmounts.length - 1;
            require(auction.bidAmounts[lastIndex] < _amount, "Current max bid is higher than your bid");
            auction.maxBid = _amount;
            auction.maxBidUser = msg.sender;
        }
        if (!exist) {
        auction.users.push(msg.sender);
        auction.bidAmounts.push(_amount);
        }
    }
    /**
       Called by the seller when the auction duration is over the hightest bid user get's the nft and other bidders get eth back
    */
    function executeSale(address _nft, uint256 _nftAssetId) external {
        auctionDetails storage auction = nftAssetToAuction[_nft][_nftAssetId];
        require(auction.duration <= block.timestamp, "Deadline did not pass yet");
        require(auction.seller == msg.sender);
        require(auction.isActive);
        auction.isActive = false;
        if (auction.bidAmounts.length == 0) {
            IBAC002(_nft).sendFrom(
                address(this),
                auction.seller,
                _nftAssetId, "");
        } else {
            // bool success = IBAC001(auction.ftAssetAddress).send(auction.seller,auction.maxBid, "");
            // require(success);
            IBAC001(auction.ftAssetAddress).send(auction.seller,auction.maxBid, "");

            for (uint256 i = 0; i < auction.users.length; i++) {
                if (auction.users[i] != auction.maxBidUser) {

            // success= IBAC001(auction.ftAssetAddress).send(auction.users[i], bids[_nft][_nftAssetId][auction.users[i]], "");
            // require(success);
            IBAC001(auction.ftAssetAddress).send(auction.users[i], bids[_nft][_nftAssetId][auction.users[i]], "");
                }
            }
            IBAC002(_nft).sendFrom(
                address(this),
                auction.maxBidUser,
                _nftAssetId, ""
            );
        }
    }

    /**
       Called by the seller if they want to cancel the auction for their nft so the bidders get back the locked eeth and the seller get's back the nft
    */
    function cancelAution(address _nft, uint256 _nftAssetId) external {
        auctionDetails storage auction = nftAssetToAuction[_nft][_nftAssetId];
        require(auction.seller == msg.sender);
        require(auction.isActive);
        auction.isActive = false;
        bool success;
        for (uint256 i = 0; i < auction.users.length; i++) {

        // success = IBAC001(auction.ftAssetAddress).send(auction.users[i], bids[_nft][_nftAssetId][auction.users[i]], "");
        // require(success);
        IBAC001(auction.ftAssetAddress).send(auction.users[i], bids[_nft][_nftAssetId][auction.users[i]], "");
        }
        IBAC002(_nft).sendFrom(address(this), auction.seller, _nftAssetId, "");
    }

    function getNFTAssetAuctionDetails(address _nft, uint256 _nftAssetId) public view returns (uint256,address) {
        auctionDetails memory auction = nftAssetToAuction[_nft][_nftAssetId];
        return (auction.maxBid,auction.maxBidUser);
    }

}