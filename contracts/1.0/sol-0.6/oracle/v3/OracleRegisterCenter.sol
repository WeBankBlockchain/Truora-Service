pragma solidity ^0.6.6;

contract Owned {

  address public owner;
  address private pendingOwner;

  event OwnershipTransferRequested(
    address indexed from,
    address indexed to
  );
  event OwnershipTransferred(
    address indexed from,
    address indexed to
  );

  constructor() public {
    owner = msg.sender;
  }

  /**
   * @dev Allows an owner to begin transferring ownership to a new address,
   * pending.
   */
  function transferOwnership(address _to)
    external
    onlyOwner()
  {
    pendingOwner = _to;

    emit OwnershipTransferRequested(owner, _to);
  }

  /**
   * @dev Allows an ownership transfer to be completed by the recipient.
   */
  function acceptOwnership()
    external
  {
    require(msg.sender == pendingOwner, "Must be proposed owner");

    address oldOwner = owner;
    owner = msg.sender;
    pendingOwner = address(0);

    emit OwnershipTransferred(oldOwner, msg.sender);
  }

  /**
   * @dev Reverts if called by anyone other than the contract owner.
   */
  modifier onlyOwner() {
    require(msg.sender == owner, "Only callable by owner");
    _;
  }

}



contract OracleRegisterCenter is Owned {

    // oracleCore instance
    struct OracleService {
        uint index;
        // TODO. equal to public key ?
        address oracleServiceAddress;
        // support for vrf
        // TODO. when register : publicKey == msg.sender ???
        uint256[2] publicKey;
        bytes32 keyhash;
        string operator;
        string url;
        uint256 createTime;
        // TODO. last update time ??
        uint256 latestRequstProcessedTime;
        bool status;
        uint256 processedRequestAmount;
    }

    // oracleOre service map
    mapping(address => OracleService) private oracleServiceMap;
    // oracleOre service list
    address[] private oracleServiceList;



    // register new oracleCore instance
    event LogNewOracleService   (uint indexed index, address indexed oracleServiceAddress, uint256[2] publicKey, bytes32 keyhash, string operator, string url, uint256 createTime);
    // update oracleCore instance
    event LogUpdateOracleService   (uint indexed index, uint256[2] oldPublicKey, bytes32 oldKeyhash, uint256[2] publicKey, bytes32 keyhash,string oldOperator, string operator, string oldUrl, string url);



    // register
    function oracleRegister(string memory _operatorInfo, string memory _url, uint256[2] memory _publicKey) public returns (bool) {

        require(!isOracleExist(msg.sender), "OracleService has registerd!");

        // TODO. equal to publicKey????
        address oracleAddress = msg.sender;

        // register only not register
        oracleServiceList.push(oracleAddress);
        uint newIndex = oracleServiceList.length - 1;
        bytes32 _keyhash =  keccak256(abi.encodePacked(_publicKey));
        // new service
        OracleService memory service = OracleService({
            index : newIndex, oracleServiceAddress : oracleAddress, publicKey : _publicKey, keyhash : _keyhash,
            operator : _operatorInfo, url : _url, createTime : now, status : true, latestRequstProcessedTime : 0, processedRequestAmount : 0
            });

        // add mapping
        oracleServiceMap[oracleAddress] = service;

        // event
        LogNewOracleService(newIndex, oracleAddress, _publicKey, _keyhash, _operatorInfo, _url, service.createTime);
        return true;
    }


    // check oracleCore instance exists
    function isOracleExist(address oracleAddress) public view returns (bool exists) {
        if (oracleServiceList.length == 0) return false;
        return (oracleServiceMap[oracleAddress].oracleServiceAddress == oracleAddress);
    }

    // get oracleCore instance by address of oracleService
    function getOracleServiceInfo(address oracleAddress) public view returns (uint, address, uint256[2] memory  , bytes32 , string memory, string memory, uint256, uint256, bool, uint256) {
        // TODO. use if and return null when not exists???
        require(isOracleExist(oracleAddress), "Oracle service not exists.");

        OracleService memory service = oracleServiceMap[oracleAddress];
        return (
        service.index, service.oracleServiceAddress, service.publicKey, service.keyhash, service.operator, service.url,
        service.createTime, service.latestRequstProcessedTime, service.status, service.processedRequestAmount
        );
    }

    // get oracleCore instance by index
    function getOracleServiceAtIndex(uint index) public view returns (address oracleAddress) {
        // TODO. use require ??
        require(index < 0 || index >= oracleServiceList.length, "index is wrong!");

        return oracleServiceList[index];
    }

    // get all oracleCore instances
    function getAllOracleServiceInfo() public view returns (address[] memory serviceList) {
        return oracleServiceList;
    }

    // get available oracleCore instances
    // function getAvailableOracleServiceInfo() public view returns (address[] memory serviceList) {
    //     uint count = oracleServiceList.length;
    //     address[] memory oracleList =  new address[](count);

    //     for (uint i = 0; i < count; i++) {
    //         OracleService memory service = oracleServiceMap[oracleServiceList[i]];
    //         if (service.status) {
    //             oracleList[i] =service.oracleServiceAddress ;
    //         }
    //     }

    //     return oracleList;
    // }


    // update oracleCore instalce
    function updateOracleInfo(uint256[2] memory _publicKey, string memory _operator, string memory _url) onlyOwner public returns (bool success)  {
        OracleService storage service = oracleServiceMap[msg.sender];
        require(service.oracleServiceAddress == msg.sender, "Oracle service not exists.");


        uint256[2] memory oldPublicKey = service.publicKey;
        string memory oldOperator = service.operator;
        string memory oldUrl = service.url;
        bytes32 oldKeyhash = service.keyhash;

        service.publicKey = _publicKey;
        service.operator = _operator;
        service.url = _url;
        service.keyhash = keccak256(abi.encodePacked(_publicKey));

        LogUpdateOracleService(service.index, oldPublicKey, oldKeyhash,_publicKey, service.keyhash, oldOperator, _operator, oldUrl, _url);
        return true;
    }


    // get count of oracleCore instances
    function getOracleServiceCount() public view returns (uint count) {
        return oracleServiceList.length;
    }


}
