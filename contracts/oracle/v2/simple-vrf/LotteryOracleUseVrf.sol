pragma solidity ^0.6.0;

import "./RandomNumberSampleVRF.sol";

contract LotteryOracleUseVrf {

    enum LOTTERY_STATE { OPEN, CLOSED }
    LOTTERY_STATE public lottery_state;
    address[] public players;
    uint256 public lotteryId;
    RandomNumberSampleVRF private oracle;
    bytes32  private requestId;
    event Winner(uint256  lotteryId, address winner, uint256 ramdomness);


    constructor(address randomOracle) public {
        oracle = RandomNumberSampleVRF(randomOracle);
        lotteryId = 0;
        lottery_state = LOTTERY_STATE.CLOSED;
    }


    function start_new_lottery(address[] memory _players) public {
        require(lottery_state == LOTTERY_STATE.CLOSED, "can't start a new lottery yet");
        lottery_state = LOTTERY_STATE.OPEN;
        players = _players;
        lotteryId++;
        requestId = oracle.getRandomNumber(lotteryId);
    }

    function pickWinner() public returns(address) {
        require(oracle.checkIdFulfilled(requestId) == false, " oracle query has not been fulfilled!");

        uint256 randomness  = oracle.getById(requestId);
        uint256 index = (randomness) % players.length;
        address winner = players[index];
        players = new address[](0);
        lottery_state = LOTTERY_STATE.CLOSED;
        emit Winner(lotteryId, winner, randomness);
        return winner;
    }

}