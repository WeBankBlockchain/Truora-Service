pragma solidity ^0.6.0;

import "./APISampleOracle.sol";

contract LotteryOracle {

    enum LOTTERY_STATE { OPEN, CLOSED }
    LOTTERY_STATE public lottery_state;
    address[] public players;
    uint256 public lotteryId;
    APISampleOracle private oracle;
    bytes32  private requestId;
    event Winner(uint256  lotteryId, address winner ,int256 ramdomness);


    constructor(address randomOracle) public {
        oracle = APISampleOracle(randomOracle);
        lotteryId = 0;
        lottery_state = LOTTERY_STATE.CLOSED;
    }



    function start_new_lottery(address[] memory _players) public {
        require(lottery_state == LOTTERY_STATE.CLOSED, "can't start a new lottery yet");
        lottery_state = LOTTERY_STATE.OPEN;
        players = _players;
        lotteryId++;
        requestId = oracle.request();
    }


    function pickWinner() public returns(address) {
        require(oracle.checkIdFulfiled(requestId) == false, " oracle query has not been fulfilled!");

        int256 ramdonness  = oracle.getById(requestId);
        uint256 index = uint256(ramdonness) % players.length;
        address winner = players[index];
        players = new address[](0);
        lottery_state = LOTTERY_STATE.CLOSED;
        emit Winner(lotteryId, winner, ramdonness);
        return winner;
    }

}