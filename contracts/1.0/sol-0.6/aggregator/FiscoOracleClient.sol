pragma solidity ^0.6.0;

import "./SafeMath.sol";
import "./OracleCoreInterface.sol";

abstract contract FiscoOracleClient {

  using SafeMath for uint256;

  OracleCoreInterface private oracle;
  uint256 private requestCount = 1;
  mapping(bytes32 => address) private pendingRequests;


  event Requested(bytes32 indexed id);
  event Fulfilled(bytes32 indexed id);

  function __callback(bytes32 requestId, int256 result)
    public virtual;


  function sendRequestTo(address _oracle, string memory url, uint256 timesAmount)
    internal
    returns (bytes32 requestId)
  {
    requestId = keccak256(abi.encodePacked(this, requestCount));
//    _req.nonce = requestCount;
    pendingRequests[requestId] = _oracle;
    emit Requested(requestId);

    oracle = OracleCoreInterface(_oracle);
    require(oracle.query(address(this),requestCount, url,timesAmount),"oracle-core invoke failed!");
    requestCount += 1;

    return requestId;
  }


  /**
   * @notice Sets the stored oracle address
   * @param _oracle The address of the oracle contract
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

  /**
   * @dev Reverts if the request is already pending
   * @param _requestId The request ID for fulfillment
   */
  modifier notPendingRequest(bytes32 _requestId) {
    require(pendingRequests[_requestId] == address(0), "Request is already pending");
    _;
  }
}
