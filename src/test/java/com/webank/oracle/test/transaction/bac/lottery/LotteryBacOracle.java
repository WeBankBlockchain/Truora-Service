package com.webank.oracle.test.transaction.bac.lottery;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.DynamicArray;
import org.fisco.bcos.web3j.abi.datatypes.DynamicBytes;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes4;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint8;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version null.
 */
@SuppressWarnings("unchecked")
public class LotteryBacOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516111a13803806111a18339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505080600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060028190555060026000806101000a81548160ff021916908360028111156100f657fe5b021790555050506110958061010c6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063c73d16ae11610066578063c73d16ae146101af578063d0e30db014610306578063dc3a212b14610310578063e580f47b146103d2578063f71d96cb146103f05761009e565b80634e21d4da146100a35780635d495aea146100ad57806381447999146100f7578063a2fb117514610123578063bead45cf14610191575b600080fd5b6100ab61045e565b005b6100b56105c8565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100ff610b99565b6040518082600281111561010f57fe5b60ff16815260200191505060405180910390f35b61014f6004803603602081101561013957600080fd5b8101908080359060200190929190505050610bab565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610199610bde565b6040518082815260200191505060405180910390f35b6102b2600480360360808110156101c557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561022c57600080fd5b82018360208201111561023e57600080fd5b8035906020019184600183028401116401000000008311171561026057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610be4565b60405180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060405180910390f35b61030e610bf8565b005b6103d06004803603604081101561032657600080fd5b810190808035906020019064010000000081111561034357600080fd5b82018360208201111561035557600080fd5b8035906020019184602083028401116401000000008311171561037757600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190505050610dc4565b005b6103da610f2b565b6040518082815260200191505060405180910390f35b61041c6004803603602081101561040657600080fd5b8101908080359060200190929190505050610f31565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000600281111561046b57fe5b6000809054906101000a900460ff16600281111561048557fe5b146104f8576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663338cdca16040518163ffffffff1660e01b8152600401602060405180830381600087803b15801561056257600080fd5b505af1158015610576573d6000803e3d6000fd5b505050506040513d602081101561058c57600080fd5b810190808051906020019092919050505060048190555060016000806101000a81548160ff021916908360028111156105c157fe5b0217905550565b6000600160028111156105d757fe5b6000809054906101000a900460ff1660028111156105f157fe5b14610664576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f63616e2774207069636b2077696e6e657220796574210000000000000000000081525060200191505060405180910390fd5b60001515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c75f695a6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b1580156106dd57600080fd5b505afa1580156106f1573d6000803e3d6000fd5b505050506040513d602081101561070757600080fd5b8101908080519060200190929190505050151514610770576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602581526020018061103b6025913960400191505060405180910390fd5b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632dff0d0d6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b1580156107e757600080fd5b505afa1580156107fb573d6000803e3d6000fd5b505050506040513d602081101561081157600080fd5b810190808051906020019092919050505090506000600180549050828161083457fe5b06905060006001828154811061084657fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc682600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663e3d670d7306040518263ffffffff1660e01b8152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b15801561095157600080fd5b505afa158015610965573d6000803e3d6000fd5b505050506040513d602081101561097b57600080fd5b81019080805190602001909291905050506040518363ffffffff1660e01b8152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001806020018281038252601f8152602001807f72657761726420746f20746865206c75636b7920647261772077696e6e6572008152506020019350505050600060405180830381600087803b158015610a2e57600080fd5b505af1158015610a42573d6000803e3d6000fd5b505050508060056000600254815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600067ffffffffffffffff81118015610ab257600080fd5b50604051908082528060200260200182016040528015610ae15781602001602082028036833780820191505090505b5060019080519060200190610af7929190610f6d565b5060026000806101000a81548160ff02191690836002811115610b1657fe5b02179055507fa82c91863944c874adf26b337e14822bf1031e367408fdb08e3db6a6b741d7706002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b6000809054906101000a900460ff1681565b60056020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60075481565b600063c73d16ae60e01b9050949350505050565b60006002811115610c0557fe5b6000809054906101000a900460ff166002811115610c1f57fe5b14610c92576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d0e7d61133306007546040518463ffffffff1660e01b8152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200180602001828103825260128152602001807f6465706f73697420746f206c6f74746572790000000000000000000000000000815250602001945050505050600060405180830381600087803b158015610daa57600080fd5b505af1158015610dbe573d6000803e3d6000fd5b50505050565b600280811115610dd057fe5b6000809054906101000a900460ff166002811115610dea57fe5b14610e5d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b6001811015610ed4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f746865206d696e696d756d2076616c7565206f6620616d6f756e74206973203181525060200191505060405180910390fd5b60008060006101000a81548160ff02191690836002811115610ef257fe5b02","179055508160019080519060200190610f0d929190610f6d565b50806007819055506002600081548092919060010191905055505050565b60025481565b60018181548110610f3e57fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b828054828255906000526020600020908101928215610fe6579160200282015b82811115610fe55782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555091602001919060010190610f8d565b5b509050610ff39190610ff7565b5090565b61103791905b8082111561103357600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610ffd565b5090565b9056fe206f7261636c6520717565727920686173206e6f74206265656e2066756c66696c6c656421a2646970667358221220a041bf82ae0da203f010b1f79f55d04e0b9da1ec09206a64342133ba2b2a376c64736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"randomOracle\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"bac001Address\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"name\":null,\"outputs\":null,\"type\":\"constructor\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"lotteryId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"winner\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"ramdomness\",\"type\":\"int256\",\"type0\":null,\"indexed\":false}],\"name\":\"Winner\",\"outputs\":null,\"type\":\"event\",\"payable\":false,\"stateMutability\":null},{\"constant\":false,\"inputs\":[],\"name\":\"deposit\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[],\"name\":\"deposit_amount\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[],\"name\":\"lotteryId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[],\"name\":\"lottery_state\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"onBAC001Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[],\"name\":\"pickWinner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"players\",\"outputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"_players\",\"type\":\"address[]\",\"type0\":null,\"indexed\":false},{\"name\":\"amount\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"start_new_lottery\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[],\"name\":\"stop_deposit\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"winners\",\"outputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_DEPOSIT_AMOUNT = "deposit_amount";

    public static final String FUNC_LOTTERYID = "lotteryId";

    public static final String FUNC_LOTTERY_STATE = "lottery_state";

    public static final String FUNC_ONBAC001RECEIVED = "onBAC001Received";

    public static final String FUNC_PICKWINNER = "pickWinner";

    public static final String FUNC_PLAYERS = "players";

    public static final String FUNC_START_NEW_LOTTERY = "start_new_lottery";

    public static final String FUNC_STOP_DEPOSIT = "stop_deposit";

    public static final String FUNC_WINNERS = "winners";

    public static final Event WINNER_EVENT = new Event("Winner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Int256>() {}));
    ;

    @Deprecated
    protected LotteryBacOracle(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LotteryBacOracle(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LotteryBacOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LotteryBacOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public List<WinnerEventResponse> getWinnerEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(WINNER_EVENT, transactionReceipt);
        ArrayList<WinnerEventResponse> responses = new ArrayList<WinnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            WinnerEventResponse typedResponse = new WinnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lotteryId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ramdomness = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerWinnerEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(WINNER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerWinnerEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(WINNER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<TransactionReceipt> deposit() {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void deposit(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String depositSeq() {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<BigInteger> deposit_amount() {
        final Function function = new Function(FUNC_DEPOSIT_AMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> lotteryId() {
        final Function function = new Function(FUNC_LOTTERYID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> lottery_state() {
        final Function function = new Function(FUNC_LOTTERY_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String onBAC001ReceivedSeq(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple4<String, String, BigInteger, byte[]> getOnBAC001ReceivedInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple4<String, String, BigInteger, byte[]>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (byte[]) results.get(3).getValue()
                );
    }

    public Tuple1<byte[]> getOnBAC001ReceivedOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> pickWinner() {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void pickWinner(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String pickWinnerSeq() {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getPickWinnerOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public RemoteCall<String> players(BigInteger param0) {
        final Function function = new Function(FUNC_PLAYERS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> start_new_lottery(List<String> _players, BigInteger amount) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void start_new_lottery(List<String> _players, BigInteger amount, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String start_new_lotterySeq(List<String> _players, BigInteger amount) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<List<String>, BigInteger> getStart_new_lotteryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<List<String>, BigInteger>(

                convertToNative((List<Address>) results.get(0).getValue()), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> stop_deposit() {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void stop_deposit(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String stop_depositSeq() {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<String> winners(BigInteger param0) {
        final Function function = new Function(FUNC_WINNERS, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static LotteryBacOracle load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryBacOracle(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LotteryBacOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryBacOracle(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LotteryBacOracle load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LotteryBacOracle(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LotteryBacOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LotteryBacOracle(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LotteryBacOracle> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacOracle.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<LotteryBacOracle> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacOracle.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryBacOracle> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacOracle.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryBacOracle> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacOracle.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class WinnerEventResponse {
        public Log log;

        public BigInteger lotteryId;

        public String winner;

        public BigInteger ramdomness;
    }
}
