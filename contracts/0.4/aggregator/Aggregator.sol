pragma solidity ^0.6.6;

import "./FiscoOracleClient.sol";
import "./SignedSafeMath.sol";
import "./Ownable.sol";

contract Aggregator is  FiscoOracleClient, Ownable {
  using SignedSafeMath for int256;

  struct Answer {
    uint128 minimumResponses;
    uint128 maxResponses;
    int256[] responses;
  }

  event AnswerUpdated(int256 indexed current, uint256 indexed roundId, uint256 timestamp);
  event NewRound(uint256 indexed roundId, address indexed startedBy, uint256 startedAt);
  event ResponseReceived(int256 indexed response, uint256 indexed answerId, address indexed sender);

  int256 private currentAnswerValue;

  uint256 private updatedTimestampValue;
  uint256 private latestCompletedAnswer;
  uint128 public minimumResponses;
  address[] public oracles;

  //round
  uint256 private answerCounter = 1;
  //requestId=> round
  mapping(bytes32 => uint256) private requestAnswers;
  // round=>reponse
  mapping(uint256 => Answer) private answers;
  // round=>resonpse
  mapping(uint256 => int256) private currentAnswers;
  //round=>timestamp
  mapping(uint256 => uint256) private updatedTimestamps;

  uint256 constant private MAX_ORACLE_COUNT = 28;

  string private url;
  uint256 private timesAmount  = 10**18;

  /**
   * @notice Deploy with the address of the LINK token and arrays of matching
   * length containing the addresses of the oracles and their corresponding
   * Job IDs.
   * @dev Sets the LinkToken address for the network, addresses of the oracles,
   * and jobIds in storage.
   * @param _minimumResponses the minimum number of responses
   * before an answer will be calculated
   * @param _oracles An array of oracle addresses
   */
  constructor(
    uint128 _minimumResponses,
    address[] memory _oracles
  ) public Ownable() {
    updateRequestDetails(_minimumResponses, _oracles);
  }

  /**
   * @notice Creates a Chainlink request for each oracle in the oracles array.
   * @dev This example does not include request parameters. Reference any documentation
   * associated with the Job IDs used to determine the required parameters per-request.
   */
  function requestRateUpdate()
    external   onlyOwner()
  {

    bytes32 requestId;
    for (uint i = 0; i < oracles.length; i++) {
     // request = buildChainlinkRequest(jobIds[i], this, this.chainlinkCallback.selector);
      requestId = sendRequestTo(oracles[i], url, timesAmount);
      requestAnswers[requestId] = answerCounter;
    }
    answers[answerCounter].minimumResponses = minimumResponses;
    answers[answerCounter].maxResponses = uint128(oracles.length);

    emit NewRound(answerCounter, msg.sender, block.timestamp);

    answerCounter = answerCounter.add(1);
  }

  function  __callback(bytes32 requestId, int256 result)
    public  override onlyOracleCoreInvoke(requestId)
  {

    uint256 answerId = requestAnswers[requestId];
    delete requestAnswers[requestId];

    answers[answerId].responses.push(result);
    emit ResponseReceived(result, answerId, msg.sender);
    updateLatestAnswer(answerId);
    deleteAnswer(answerId);
  }

  /**
   * @notice Updates the arrays of oracles and jobIds with new values,
   * overwriting the old values.
   * @dev Arrays are validated to be equal length.
   * @param _minimumResponses the minimum number of responses
   * before an answer will be calculated
   * @param _oracles An array of oracle addresses
   */
  function updateRequestDetails(
    uint128 _minimumResponses,
    address[] memory _oracles
  )
    public
    onlyOwner()
    validateAnswerRequirements(_minimumResponses, _oracles)
  {
    minimumResponses = _minimumResponses;
    oracles = _oracles;
  }


  function updateLatestAnswer(uint256 _answerId)
    private
    ensureMinResponsesReceived(_answerId)
    ensureOnlyLatestAnswer(_answerId)
  {
    uint256 responseLength = answers[_answerId].responses.length;
    uint256 middleIndex = responseLength.div(2);
    int256 currentAnswerTemp;
    if (responseLength % 2 == 0) {
      int256 median1 = quickselect(answers[_answerId].responses, middleIndex);
      int256 median2 = quickselect(answers[_answerId].responses, middleIndex.add(1)); // quickselect is 1 indexed
      currentAnswerTemp = median1.add(median2) / 2; // signed integers are not supported by SafeMath
    } else {
      currentAnswerTemp = quickselect(answers[_answerId].responses, middleIndex.add(1)); // quickselect is 1 indexed
    }
    currentAnswerValue = currentAnswerTemp;
    latestCompletedAnswer = _answerId;
    updatedTimestampValue = now;
    updatedTimestamps[_answerId] = now;
    currentAnswers[_answerId] = currentAnswerTemp;
    emit AnswerUpdated(currentAnswerTemp, _answerId, now);
  }

  /**
   * @notice get the most recently reported answer
   */
  function latestAnswer()
    external
    view
    returns (int256)
  {
    return currentAnswers[latestCompletedAnswer];
  }

  /**
   * @notice get the last updated at block timestamp
   */
  function latestTimestamp()
    external
    view
    returns (uint256)
  {
    return updatedTimestamps[latestCompletedAnswer];
  }

  /**
   * @notice get past rounds answers
   * @param _roundId the answer number to retrieve the answer for
   */
  function getAnswer(uint256 _roundId)
    external
    view
    returns (int256)
  {
    return currentAnswers[_roundId];
  }

  /**
   * @notice get block timestamp when an answer was last updated
   * @param _roundId the answer number to retrieve the updated timestamp for
   */
  function getTimestamp(uint256 _roundId)
    external
    view
    returns (uint256)
  {
    return updatedTimestamps[_roundId];
  }

  /**
   * @notice get the latest completed round where the answer was updated
   */
  function latestRound()
    external
    view
    returns (uint256)
  {
    return latestCompletedAnswer;
  }


  /**
   * @dev Returns the kth value of the ordered array
   * See: http://www.cs.yale.edu/homes/aspnes/pinewiki/QuickSelect.html
   * @param _a The list of elements to pull from
   * @param _k The index, 1 based, of the elements you want to pull from when ordered
   */
  function quickselect(int256[] memory _a, uint256 _k)
    private
    pure
    returns (int256)
  {
    int256[] memory a = _a;
    uint256 k = _k;
    uint256 aLen = a.length;
    int256[] memory a1 = new int256[](aLen);
    int256[] memory a2 = new int256[](aLen);
    uint256 a1Len;
    uint256 a2Len;
    int256 pivot;
    uint256 i;

    while (true) {
      pivot = a[aLen.div(2)];
      a1Len = 0;
      a2Len = 0;
      for (i = 0; i < aLen; i++) {
        if (a[i] < pivot) {
          a1[a1Len] = a[i];
          a1Len++;
        } else if (a[i] > pivot) {
          a2[a2Len] = a[i];
          a2Len++;
        }
      }
      if (k <= a1Len) {
        aLen = a1Len;
        (a, a1) = swap(a, a1);
      } else if (k > (aLen.sub(a2Len))) {
        k = k.sub(aLen.sub(a2Len));
        aLen = a2Len;
        (a, a2) = swap(a, a2);
      } else {
        return pivot;
      }
    }
  }

  /**
   * @dev Swaps the pointers to two uint256 arrays in memory
   * @param _a The pointer to the first in memory array
   * @param _b The pointer to the second in memory array
   */
  function swap(int256[] memory _a, int256[] memory _b)
    private
    pure
    returns(int256[] memory, int256[] memory)
  {
    return (_b, _a);
  }


  /**
   * @dev Cleans up the answer record if all responses have been received.
   * @param _answerId The identifier of the answer to be deleted
   */
  function deleteAnswer(uint256 _answerId)
    private
    ensureAllResponsesReceived(_answerId)
  {
    delete answers[_answerId];
  }

  /**
   * @dev Prevents taking an action if the minimum number of responses has not
   * been received for an answer.
   * @param _answerId The the identifier of the answer that keeps track of the responses.
   */
  modifier ensureMinResponsesReceived(uint256 _answerId) {
    if (answers[_answerId].responses.length >= answers[_answerId].minimumResponses) {
      _;
    }
  }

  /**
   * @dev Prevents taking an action if not all responses are received for an answer.
   * @param _answerId The the identifier of the answer that keeps track of the responses.
   */
  modifier ensureAllResponsesReceived(uint256 _answerId) {
    if (answers[_answerId].responses.length == answers[_answerId].maxResponses) {
      _;
    }
  }

  /**
   * @dev Prevents taking an action if a newer answer has been recorded.
   * @param _answerId The current answer's identifier.
   * Answer IDs are in ascending order.
   */
  modifier ensureOnlyLatestAnswer(uint256 _answerId) {
    if (latestCompletedAnswer <= _answerId) {
      _;
    }
  }

  /**
   * @dev Ensures corresponding number of oracles and jobs.
   * @param _oracles The list of oracles.
   */
  modifier validateAnswerRequirements(
    uint256 _minimumResponses,
    address[] memory _oracles
  ) {
    require(_oracles.length <= MAX_ORACLE_COUNT, "cannot have more than 45 oracles");
    require(_oracles.length >= _minimumResponses, "must have at least as many oracles as responses");
    _;
  }

}
