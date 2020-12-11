pragma solidity ^0.6.0;

import "./SafeMath.sol";
import "./OracleCoreInterface.sol";

abstract contract FiscoOracleClient {

  using SafeMath for uint256;

  OracleCoreInterface private oracle;
  uint256 private requestCount = 1;
  mapping(bytes32 => address) private pendingRequests;
  mapping (address => uint) private reqc;
  uint256 constant public EXPIRY_TIME = 10 * 60 * 1000;

  event Requested(bytes32 indexed id);
  event Fulfilled(bytes32 indexed id);

  function __callback(bytes32 requestId, int256 result) public virtual;

  // __callback with proof
 // function __callback(bytes32 requestId, int256 result, bytes calldata proof) public virtual;


  function oracleQuery(address _oracle, string memory url, uint256 timesAmount)
    internal
    returns (bytes32 requestId)
  {
     return oracleQuery(EXPIRY_TIME,"url", _oracle, url, timesAmount, false);
  }

  function oracleQuery(uint expiryTime, string memory datasource, address _oracle, string memory url, uint256 timesAmount, bool needProof) internal
  returns (bytes32 requestId) {
    // calculate the id;
    requestId = keccak256(abi.encodePacked(this, requestCount));
    pendingRequests[requestId] = _oracle;
    emit Requested(requestId);

    oracle = OracleCoreInterface(_oracle);
    require(oracle.query(address(this),requestCount, url,timesAmount, expiryTime,needProof),"oracle-core invoke failed!");
    requestCount++;
    reqc[msg.sender]++;

    return requestId;
  }


  /**
   * @notice Sets the stored oracle core address
   * @param _oracle The address of the oracle core contract
   */
  function setOracleCoreAddress(address _oracle) internal {
    oracle = OracleCoreInterface(_oracle);
  }


  /**
   * @notice Retrieves the stored address of the oracle contract
   * @return The address of the oracle contract
   */
  function getOracleCoreAddress()
    internal
    view
    returns (address)
  {
    return address(oracle);
  }



  /**
   * @dev Reverts if the sender is not the oracle of the request.
   * @param _requestId The request ID for fulfillment
   */
  modifier onlyOracleCoreInvoke(bytes32 _requestId) {
    require(msg.sender == pendingRequests[_requestId],
            "Source must be the oracle of the request");
    delete pendingRequests[_requestId];
    emit Fulfilled(_requestId);
    _;
  }
}
