pragma solidity ^0.4.25;

import "./Register.sol";
import "./Counters.sol";
import "./Roles.sol";


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

contract IssuerRole {
    using Roles for Roles.Role;

    event IssuerAdded(address indexed account);
    event IssuerRemoved(address indexed account);

    Roles.Role private _issuers;

    constructor () internal {
        _addIssuer(msg.sender);
    }

    modifier onlyIssuer() {
        require(isIssuer(msg.sender), "IssuerRole: caller does not have the Issuer role");
        _;
    }

    function isIssuer(address account) public view returns (bool) {
        return _issuers.has(account);
    }

    function addIssuer(address account) public onlyIssuer {
        _addIssuer(account);
    }

    function renounceIssuer() public {
        _removeIssuer(msg.sender);
    }

    function _addIssuer(address account) internal {
        _issuers.add(account);
        emit IssuerAdded(account);
    }

    function _removeIssuer(address account) internal {
        _issuers.remove(account);
        emit IssuerRemoved(account);
    }
}

contract SuspenderRole {
    using Roles for Roles.Role;

    event SuspenderAdded(address indexed account);
    event SuspenderRemoved(address indexed account);

    Roles.Role private _suspenders;

    constructor () internal {
        _addSuspender(msg.sender);
    }

    modifier onlySuspender() {
        require(isSuspender(msg.sender), "SuspenderRole: caller does not have the Suspender role");
        _;
    }

    function isSuspender(address account) public view returns (bool) {
        return _suspenders.has(account);
    }

    function addSuspender(address account) public onlySuspender {
        _addSuspender(account);
    }

    function renounceSuspender() public {
        _removeSuspender(msg.sender);
    }

    function _addSuspender(address account) internal {
        _suspenders.add(account);
        emit SuspenderAdded(account);
    }

    function _removeSuspender(address account) internal {
        _suspenders.remove(account);
        emit SuspenderRemoved(account);
    }
}

contract Suspendable is SuspenderRole {

    event Suspended(address account);
    event UnSuspended(address account);

    bool private _suspended;

    constructor () internal {
        _suspended = false;
    }

    /**
     * @return True if the contract is suspended, false otherwise.
     */
    function suspended() public view returns (bool) {
        return _suspended;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is not suspended.
     */
    modifier whenNotSuspended() {
        require(!_suspended, "Suspendable: suspended");
        _;
    }

    /**
     * @dev Modifier to make a function callable only when the contract is suspended.
     */
    modifier whenSuspended() {
        require(_suspended, "Suspendable: not suspended");
        _;
    }

    /**
     * @dev Called by a Suspender to suspend, triggers stopped state.
     */
    function suspend() public onlySuspender whenNotSuspended {
        _suspended = true;
        emit Suspended(msg.sender);
    }

    /**
     * @dev Called by a Suspender to unSuspend, returns to normal state.
     */
    function unSuspend() public onlySuspender whenSuspended {
        _suspended = false;
        emit UnSuspended(msg.sender);
    }
}

//delete register
contract BAC002 is  IssuerRole, Suspendable {
    using SafeMath for uint256;
    using Address for address;
    using Counters for Counters.Counter;

    // Equals to `bytes4(keccak256("onBAC002Received(address,address,uint256,bytes)"))`
    bytes4 private constant _BAC002_RECEIVED = 0x31f6f50e;

    // Mapping from asset ID to owner
    mapping(uint256 => address) private _assetOwner;

    // Mapping from asset ID to approved address
    mapping(uint256 => address) private _assetApprovals;

    // Mapping from owner to number of owned asset
    mapping(address => Counters.Counter) private _ownedAssetsCount;

    // Mapping from owner to operator approvals
    mapping(address => mapping(address => bool)) private _operatorApprovals;

    string private _description;

    string private _shortName;

    // Optional mapping for asset URIs
    mapping(uint256 => string) private _assetURIs;

    // Mapping from owner to list of owned asset IDs
    mapping(address => uint256[]) private _ownedAssets;

    // Mapping from asset ID to index of the owner assets list
    mapping(uint256 => uint256) private _ownedAssetsIndex;

    // Array with all asset ids, used for enumeration
    uint256[] private _allAssets;

    // Mapping from asset id to position in the allAssets array
    mapping(uint256 => uint256) private _allAssetsIndex;

    event Send(address indexed operator, address indexed from, address indexed to, uint256 assetId, bytes data);
    event Approval( address indexed owner, address approved, uint256 assetId);
    event ApprovalForAll( address indexed owner, address indexed operator, bool approved);

    // constructor
    constructor(string description, string shortName) public
    {
        _description = description;
        _shortName = shortName;
    }

    /**
     * @dev Gets the balance of the specified address.
     */
    function balance(address owner) public view returns (uint256) {
        require(owner != address(0), "BAC002: balance query for the zero address");
        return _ownedAssetsCount[owner].current();
    }

    /**
     * @dev Gets the owner of the specified asset ID.
     */
    function ownerOf(uint256 assetId) public view returns (address) {
        address owner = _assetOwner[assetId];
        require(owner != address(0), "BAC002: owner query for nonexistent asset");
        return owner;
    }


    function assetOfOwnerByIndex(address owner, uint256 index) public view returns (uint256) {
        require(index < balance(owner), "BAC002Enumerable: owner index out of bounds");
        return _ownedAssets[owner][index];
    }

    function assetOfOwner(address owner) public view returns (uint256[]) {
        return _ownedAssets[owner];
    }


    function assetByIndex(uint256 index) public view returns (uint256) {
        require(index < totalSupply(), "BAC002Enumerable: global index out of bounds");
        return _allAssets[index];
    }

    /**
     * @dev Approves another address to send the given asset ID
     */
    function approve(address to, uint256 assetId) public whenNotSuspended {
        address owner = ownerOf(assetId);
        require(to != owner, "BAC002: approval to current owner");

        require(msg.sender == owner || isApprovedForAll(owner, msg.sender),
            "BAC002: approve caller is not owner nor approved for all"
        );
        _assetApprovals[assetId] = to;
        emit Approval( owner, to, assetId);
    }

    /**
     * @dev Gets the approved address for a asset ID, or zero if no address set
     */
    function getApproved(uint256 assetId) public view returns (address) {
        require(_exists(assetId), "BAC002: approved query for nonexistent asset");
        return _assetApprovals[assetId];
    }

    /**
     * @dev Sets or unsets the approval of a given operator
     */
    function setApprovalForAll(address to, bool approved) public whenNotSuspended {
        require(to != msg.sender, "BAC002: approve to caller");
        _operatorApprovals[msg.sender][to] = approved;
        emit ApprovalForAll( msg.sender, to, approved);
    }

    /**
     * @dev Tells whether an operator is approved by a given owner.
     */
    function isApprovedForAll(address owner, address operator) public view returns (bool) {
        return _operatorApprovals[owner][operator];
    }

//    /**
//     * @dev Sends the ownership of a given asset ID to another address.
//     */
//    function sendFrom(address from, address to, uint256 assetId, bytes memory data) public whenNotSuspended {
//        //solhint-disable-next-line max-line-length
//        require(_isApprovedOrOwner(msg.sender, assetId), "BAC002: send caller is not owner nor approved");
//        _sendFrom(from, to, assetId, data);
//    }

    // /**
    //  * @dev Safely sends the ownership of a given asset ID to another address
    //  */
    // function safeSendFrom(address from, address to, uint256 assetId) public whenNotSuspended {
    //     safeSendFrom(from, to, assetId, "");
    // }

    /**
     * @dev Safely sends the ownership of a given asset ID to another address
     */
    function sendFrom(address from, address to, uint256 assetId, bytes memory data) public whenNotSuspended {


        require(_isApprovedOrOwner(msg.sender, assetId), "BAC002: send caller is not owner nor approved");
        _sendFrom(from, to, assetId, data);
        require(_checkOnBAC002Received(from, to, assetId, data), "BAC002: send to non BAC002Receiver implementer");
    }


    function batchSendFrom(address from, address[] to, uint256[] assetId, bytes memory data) public whenNotSuspended {

        require(to.length == assetId.length, "to and assetId array lenght must match.");

        for (uint256 i = 0; i < to.length; ++i) {
            require(to[i] != address(0x0), "destination address must be non-zero.");
            sendFrom(from, to[i], assetId[i], data);

        }
    }


    function destroy(uint256 assetId, bytes data) public {
        //solhint-disable-next-line max-line-length
        require(_isApprovedOrOwner(msg.sender, assetId), "BAC002Burnable: caller is not owner nor approved");
        _destroy(assetId, data);
    }

    //add issuerAddress
    function issueWithAssetURI(address to, uint256 assetId, string memory assetURI, bytes data) public onlyIssuer returns (bool) {
        _issue( to, assetId, data);
        _setAssetURI(assetId, assetURI);
        return true;
    }

    function description() external view returns (string memory) {
        return _description;
    }

    function shortName() external view returns (string memory) {
        return _shortName;
    }

    /**
     * @dev Returns an URI for a given asset ID.
     */
    function assetURI(uint256 assetId) external view returns (string memory) {
        require(_exists(assetId), "BAC002Metadata: URI query for nonexistent asset");
        return _assetURIs[assetId];
    }

    /**
     * @dev Internal function to set the asset URI for a given asset.
     */
    function _setAssetURI(uint256 assetId, string memory uri) internal {
        require(_exists(assetId), "BAC002Metadata: URI set of nonexistent asset");
        _assetURIs[assetId] = uri;
    }

    /**
     * @dev Returns whether the specified asset exists.
     */
    function _exists(uint256 assetId) internal view returns (bool) {
        address owner = _assetOwner[assetId];
        return owner != address(0);
    }

    /**
     * @dev Returns whether the given spender can send a given asset ID.
     */
    function _isApprovedOrOwner(address spender, uint256 assetId) internal view returns (bool) {
        require(_exists(assetId), "BAC002: operator query for nonexistent asset");
        address owner = ownerOf(assetId);
        return (spender == owner || getApproved(assetId) == spender || isApprovedForAll(owner, spender));
    }

    /**
     * @dev Internal function to mint a new asset.
     */
    function _issue( address to, uint256 assetId, bytes data) internal {
        require(to != address(0), "BAC002: mint to the zero address");
        require(!_exists(assetId), "BAC002: asset already minted");

        _assetOwner[assetId] = to;
        _ownedAssetsCount[to].increment();

        emit Send(msg.sender, address(0), to, assetId, data);

        _addAssetToOwnerEnumeration(to, assetId);

        _addAssetToAllAssetsEnumeration(assetId);
    }

    /**
     * @dev Internal function to destroy a specific asset.
     * Reverts if the asset does not exist.
     */
    function _destroy(address owner, uint256 assetId, bytes data) internal {
        require(ownerOf(assetId) == owner, "BAC002: destroy of asset that is not own");

        _clearApproval(assetId);

        _ownedAssetsCount[owner].decrement();
        _assetOwner[assetId] = address(0);

        if (bytes(_assetURIs[assetId]).length != 0) {
            delete _assetURIs[assetId];
        }

        emit Send(this, owner, address(0), assetId, data);

        _removeAssetFromOwnerEnumeration(owner, assetId);
        // Since assetId will be deleted, we can clear its slot in _ownedAssetsIndex to trigger a gas refund
        _ownedAssetsIndex[assetId] = 0;

        _removeAssetFromAllAssetsEnumeration(assetId);
    }


    /**
     * @dev Gets the total amount of assets stored by the contract.
     * @return uint256 representing the total amount of assets
     */
    function totalSupply() public view returns (uint256) {
        return _allAssets.length;
    }


    function _assetsOfOwner(address owner) internal view returns (uint256[] storage) {
        return _ownedAssets[owner];
    }

    /**
     * @dev Private function to add a asset to this extension's ownership-tracking data structures.
     */
    function _addAssetToOwnerEnumeration(address to, uint256 assetId) private {
        _ownedAssetsIndex[assetId] = _ownedAssets[to].length;
        _ownedAssets[to].push(assetId);
    }

    /**
     * @dev Private function to add a asset to this extension's asset tracking data structures.
     */
    function _addAssetToAllAssetsEnumeration(uint256 assetId) private {
        _allAssetsIndex[assetId] = _allAssets.length;
        _allAssets.push(assetId);
    }

    /**
     * @dev Private function to remove a asset from this extension's ownership-tracking data structures. Note that
     */
    function _removeAssetFromOwnerEnumeration(address from, uint256 assetId) private {
        // To prevent a gap in from's assets array, we store the last asset in the index of the asset to delete, and
        // then delete the last slot (swap and pop).

        uint256 lastAssetIndex = _ownedAssets[from].length.sub(1);
        uint256 assetIndex = _ownedAssetsIndex[assetId];

        // When the asset to delete is the last asset, the swap operation is unnecessary
        if (assetIndex != lastAssetIndex) {
            uint256 lastAssetId = _ownedAssets[from][lastAssetIndex];

            _ownedAssets[from][assetIndex] = lastAssetId;
            // Move the last asset to the slot of the to-delete asset
            _ownedAssetsIndex[lastAssetId] = assetIndex;
            // Update the moved asset's index
        }

        // This also deletes the contents at the last position of the array
        _ownedAssets[from].length--;

        // Note that _ownedAssetsIndex[assetId] hasn't been cleared: it still points to the old slot (now occupied by
        // lastAssetId, or just over the end of the array if the asset was the last one).
    }

    /**
     * @dev Private function to remove a asset from this extension's asset tracking data structures.
     */
    function _removeAssetFromAllAssetsEnumeration(uint256 assetId) private {
        // To prevent a gap in the assets array, we store the last asset in the index of the asset to delete, and
        // then delete the last slot (swap and pop).

        uint256 lastAssetIndex = _allAssets.length.sub(1);
        uint256 assetIndex = _allAssetsIndex[assetId];

        // When the asset to delete is the last asset, the swap operation is unnecessary. However, since this occurs so
        // rarely (when the last minted asset is destroyt) that we still do the swap here to avoid the gas cost of adding
        // an 'if' statement (like in _removeAssetFromOwnerEnumeration)
        uint256 lastAssetId = _allAssets[lastAssetIndex];

        _allAssets[assetIndex] = lastAssetId;
        // Move the last asset to the slot of the to-delete asset
        _allAssetsIndex[lastAssetId] = assetIndex;
        // Update the moved asset's index

        // This also deletes the contents at the last position of the array
        _allAssets.length--;
        _allAssetsIndex[assetId] = 0;
    }

    /**
     * @dev Internal function to destroy a specific asset.
     */
    function _destroy(uint256 assetId, bytes data) internal {
        _destroy(ownerOf(assetId), assetId, data);
    }

    /**
     * @dev Internal function to send ownership of a given asset ID to another address.
     */
    function _sendFrom(address from, address to, uint256 assetId, bytes data) internal {
        require(ownerOf(assetId) == from, "BAC002: send of asset that is not own");
        require(to != address(0), "BAC002: send to the zero address");

        _clearApproval(assetId);
        _ownedAssetsCount[from].decrement();
        _ownedAssetsCount[to].increment();

        _assetOwner[assetId] = to;

        emit Send(msg.sender, from, to, assetId, data);

        _removeAssetFromOwnerEnumeration(from, assetId);

        _addAssetToOwnerEnumeration(to, assetId);
    }

    /**
     * @dev Internal function to invoke `onBAC002Received` on a target address.
     */
    function _checkOnBAC002Received(address from, address to, uint256 assetId, bytes memory _data)
    internal returns (bool)
    {
        if (!to.isContract()) {
            return true;
        }

        bytes4 retval = IBAC002Receiver(to).onBAC002Received(msg.sender, from, assetId, _data);
        return (retval == _BAC002_RECEIVED);
    }

    /**
     * @dev Private function to clear current approval of a given asset ID.
     */
    function _clearApproval(uint256 assetId) private {
        if (_assetApprovals[assetId] != address(0)) {
            _assetApprovals[assetId] = address(0);
        }
    }
}