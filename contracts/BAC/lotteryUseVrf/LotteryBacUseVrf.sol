pragma solidity ^0.6.6;

import "../../oracle/simple-vrf/RandomNumberSampleVRF.sol";
import "../../oracle/simple-vrf/Ownable.sol";
import "../bac001/IBAC001.sol";
import "../bac001/BAC001Holder.sol";

contract LotteryBacUseVrf is BAC001Holder, Ownable {

    enum LOTTERY_STATE { OPEN, PENDING, CLOSED }
    LOTTERY_STATE public lottery_state;
    address[] public players;//主持人指定的参与者
    uint256 public lotteryId;//本轮抽奖编号

    RandomNumberSampleVRF private oracle;
    bytes32  private requestId;
    event Winner(uint256  lotteryId, address winner ,uint256 ramdomness);
    mapping(uint256=>address) public winners;//每轮投票的胜出者
    IBAC001 private bac001;//bac001对象
    uint256 public deposit_amount;//本轮投票将要从每个参与者账户锁定的额度

    constructor(address randomOracle,address bac001Address) public {
        bac001 = IBAC001(bac001Address);//

        oracle = RandomNumberSampleVRF(randomOracle);
        lotteryId = 0;
        lottery_state = LOTTERY_STATE.CLOSED;
    }


    function start_new_lottery(address[] memory _players,uint256 amount) public {
        require(lottery_state == LOTTERY_STATE.CLOSED, "can't start a new lottery yet");
        require(amount >= 1, "the minimum value of amount is 1");

        lottery_state = LOTTERY_STATE.OPEN;
        players = _players;
        deposit_amount = amount;
        lotteryId++;
    }

    function deposit()public{
        require(lottery_state == LOTTERY_STATE.OPEN, "the lottery has not yet started");
        bac001.sendFrom(msg.sender, address(this), deposit_amount, "deposit to lottery");
    }

    function stop_deposit(uint256 userProvidedSeed)public{
        require(lottery_state == LOTTERY_STATE.OPEN, "the lottery has not yet started");
        requestId = oracle.getRandomNumber(userProvidedSeed);
        lottery_state = LOTTERY_STATE.PENDING;
    }


    function pickWinner() public returns(address) {
        require(lottery_state == LOTTERY_STATE.PENDING, "can't pick winner yet!");
        require(oracle.checkIdFulfilled(requestId) == false, " oracle query has not been fulfilled!");

        uint256 randomness  = oracle.getById(requestId);
        uint256 index = uint256(randomness) % players.length;
        address winner = players[index];
        bac001.send(winner, bac001.balance(address(this)) , "reward to the lucky draw winner");
        winners[lotteryId] = winner;
        players = new address[](0);
        lottery_state = LOTTERY_STATE.CLOSED;
        emit Winner(lotteryId, winner, randomness);
        return winner;
    }

}