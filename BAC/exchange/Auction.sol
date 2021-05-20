//https://etherscan.io/address/0x3f18b2e57acd6f6c111936577529ac0dce7148b8#code



// for fixed price sell
pragma solidity ^0.4.23;


contract Ownable {
    address public owner;


    event OwnershipRenounced(address indexed previousOwner);
    event OwnershipTransferred(
        address indexed previousOwner,
        address indexed newOwner
    );


    /**
     * @dev The Ownable constructor sets the original `owner` of the contract to the sender
     * account.
     */
    constructor() public {
        owner = msg.sender;
    }

    /**
     * @dev Throws if called by any account other than the owner.
     */
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    /**
     * @dev Allows the current owner to relinquish control of the contract.
     */
    function renounceOwnership() public onlyOwner {
        emit OwnershipRenounced(owner);
        owner = address(0);
    }

    /**
     * @dev Allows the current owner to transfer control of the contract to a newOwner.
     * @param _newOwner The address to transfer ownership to.
     */
    function transferOwnership(address _newOwner) public onlyOwner {
        _transferOwnership(_newOwner);
    }

    /**
     * @dev Transfers control of the contract to a newOwner.
     * @param _newOwner The address to transfer ownership to.
     */
    function _transferOwnership(address _newOwner) internal {
        require(_newOwner != address(0));
        emit OwnershipTransferred(owner, _newOwner);
        owner = _newOwner;
    }
}

/**
 * @title Pausable
 * @dev Base contract which allows children to implement an emergency stop mechanism.
 */
contract Pausable is Ownable {
    event Pause();
    event Unpause();

    bool public paused = false;


    /**
     * @dev Modifier to make a function callable only when the contract is not paused.
     */
    modifier whenNotPaused() {
        require(!paused);
        _;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is paused.
     */
    modifier whenPaused() {
        require(paused);
        _;
    }

    /**
     * @dev called by the owner to pause, triggers stopped state
     */
    function pause() onlyOwner whenNotPaused public {
        paused = true;
        emit Pause();
    }

    /**
     * @dev called by the owner to unpause, returns to normal state
     */
    function unpause() onlyOwner whenPaused public {
        paused = false;
        emit Unpause();
    }
}



/**
 * @title CryptoArteSales
 * CryptoArteSales - a sales contract for CryptoArte non-fungible tokens
 * corresponding to paintings from the www.cryptoarte.io collection
 */
contract AuctionFixedPrice is Ownable, Pausable {

    event Sent(address indexed payee, uint256 amount, uint256 balance);
    event Received(address indexed payer, uint tokenId, uint256 amount, uint256 balance);

    BAC002 public nftAddress;
    BAC001 public assetAddress;
    uint256 public currentPrice;

    /**
    * @dev Contract Constructor
    * @param _nftAddress address for Crypto Arte non-fungible token contract
    * @param _currentPrice initial sales price
    */
    constructor(address _nftAddress, address _assetAddress, uint256 _currentPrice) public {
        require(_nftAddress != address(0) && _nftAddress != address(this));
        require(_assetAddress != address(0) && _assetAddress != address(this));
        require(_currentPrice > 0);
        nftAddress = BAC002(_nftAddress);
        tokenAddress = BAC001(_assetAddress);
        currentPrice = _currentPrice;
    }

    /**
    * @dev Purchase _tokenId
    * @param _tokenId uint256 token ID (painting number)
    */
    function purchaseToken(uint256 _tokenId) public payable whenNotPaused {
        require(msg.sender != address(0) && msg.sender != address(this));
        //require(msg.value >= currentPrice);
        assetAddress.safeTransferFrom();
        require(nftAddress.exists(_tokenId));
        address tokenSeller = nftAddress.ownerOf(_tokenId);
        nftAddress.safeTransferFrom(tokenSeller, msg.sender, _tokenId);
        emit Received(msg.sender, _tokenId, msg.value, address(this).balance);
    }

    /**
    * @dev send / withdraw _amount to _payee
    */
    function withdraw(address _payee, uint256 _amount) public onlyOwner {
        require(_payee != address(0) && _payee != address(this));
        require(_amount > 0 && _amount <= address(this).balance);
        _payee.transfer(_amount);
        emit Sent(_payee, _amount, address(this).balance);
    }

    /**
    * @dev Updates _currentPrice
    * @dev Throws if _currentPrice is zero
    */
    function setCurrentPrice(uint256 _currentPrice) public onlyOwner {
        require(_currentPrice > 0);
        currentPrice = _currentPrice;
    }

}