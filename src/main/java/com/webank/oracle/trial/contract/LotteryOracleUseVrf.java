package com.webank.oracle.trial.contract;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.DynamicArray;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint8;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class LotteryOracleUseVrf extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061094c8339810180604052810190808051906020019092919050505080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060028190555060016000806101000a81548160ff0219169083600181111561009a57fe5b02179055505061089d806100af6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635d495aea1461007257806381447999146100c9578063af440aad14610102578063e580f47b14610168578063f71d96cb14610193575b600080fd5b34801561007e57600080fd5b50610087610200565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100d557600080fd5b506100de61058c565b604051808260018111156100ee57fe5b60ff16815260200191505060405180910390f35b34801561010e57600080fd5b506101666004803603810190808035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919291929050505061059e565b005b34801561017457600080fd5b5061017d610760565b6040518082815260200191505060405180910390f35b34801561019f57600080fd5b506101be60048036038101908080359060200190929190505050610766565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60008060008060001515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c75f695a6004546040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156102a557600080fd5b505af11580156102b9573d6000803e3d6000fd5b505050506040513d60208110156102cf57600080fd5b8101908080519060200190929190505050151514151561037d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001807f206f7261636c6520717565727920686173206e6f74206265656e2066756c666981526020017f6c6c65642100000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632dff0d0d6004546040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561041857600080fd5b505af115801561042c573d6000803e3d6000fd5b505050506040513d602081101561044257600080fd5b810190808051906020019092919050505092506001805490508381151561046557fe5b06915060018281548110151561047757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905060006040519080825280602002602001820160405280156104d45781602001602082028038833980820191505090505b50600190805190602001906104ea9291906107a4565b5060016000806101000a81548160ff0219169083600181111561050957fe5b02179055507f1c2ae1f7e99dbbbfe25aac964f3889ea68259d88b74709f53037a6fb930bb02a6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b6000809054906101000a900460ff1681565b6001808111156105aa57fe5b6000809054906101000a900460ff1660018111156105c457fe5b141515610639576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b60008060006101000a81548160ff0219169083600181111561065757fe5b021790555080600190805190602001906106729291906107a4565b50600260008154809291906001019190505550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b37217a46002546040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561071857600080fd5b505af115801561072c573d6000803e3d6000fd5b505050506040513d602081101561074257600080fd5b81019080805190602001909291905050506004816000191690555050565b60025481565b60018181548110151561077557fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b82805482825590600052602060002090810192821561081d579160200282015b8281111561081c5782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906107c4565b5b50905061082a919061082e565b5090565b61086e91905b8082111561086a57600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610834565b5090565b905600a165627a7a7230582088fbb4b702f419c3b773730ca9c3914552a4d597595603cb5fa00ed563d1a9740029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[],\"name\":\"pickWinner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"lottery_state\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_players\",\"type\":\"address[]\"}],\"name\":\"start_new_lottery\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"lotteryId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"players\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"randomOracle\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"lotteryId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"winner\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"ramdomness\",\"type\":\"uint256\"}],\"name\":\"Winner\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061094c8339810180604052810190808051906020019092919050505080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060028190555060016000806101000a81548160ff0219169083600181111561009a57fe5b02179055505061089d806100af6000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806301fbc5721461007257806386e053e3146100d8578063975fcaa014610145578063d865927d1461019c578063e5501039146101c7575b600080fd5b34801561007e57600080fd5b506100d660048036038101908080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610200565b005b3480156100e457600080fd5b50610103600480360381019080803590602001909291905050506103c2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561015157600080fd5b5061015a610400565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101a857600080fd5b506101b161078c565b6040518082815260200191505060405180910390f35b3480156101d357600080fd5b506101dc610792565b604051808260018111156101ec57fe5b60ff16815260200191505060405180910390f35b60018081111561020c57fe5b6000809054906101000a900460ff16600181111561022657fe5b14151561029b576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b60008060006101000a81548160ff021916908360018111156102b957fe5b021790555080600190805190602001906102d49291906107a4565b50600260008154809291906001019190505550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f724a2796002546040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b15801561037a57600080fd5b505af115801561038e573d6000803e3d6000fd5b505050506040513d60208110156103a457600080fd5b81019080805190602001909291905050506004816000191690555050565b6001818154811015156103d157fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008060001515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166356ddd8ed6004546040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b1580156104a557600080fd5b505af11580156104b9573d6000803e3d6000fd5b505050506040513d60208110156104cf57600080fd5b8101908080519060200190929190505050151514151561057d576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001807f206f7261636c6520717565727920686173206e6f74206265656e2066756c666981526020017f6c6c65642100000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b791ee546004546040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808260001916600019168152602001915050602060405180830381600087803b15801561061857600080fd5b505af115801561062c573d6000803e3d6000fd5b505050506040513d602081101561064257600080fd5b810190808051906020019092919050505092506001805490508381151561066557fe5b06915060018281548110151561067757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905060006040519080825280602002602001820160405280156106d45781602001602082028038833980820191505090505b50600190805190602001906106ea9291906107a4565b5060016000806101000a81548160ff0219169083600181111561070957fe5b02179055507f16f7a6a62614f6f8f716bc3e58eb0bc352ca0fe22427017ad411d8256eaa900b6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b60025481565b6000809054906101000a900460ff1681565b82805482825590600052602060002090810192821561081d579160200282015b8281111561081c5782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906107c4565b5b50905061082a919061082e565b5090565b61086e91905b8082111561086a57600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101610834565b5090565b905600a165627a7a723058207f2e28e856e9c8d662f398999a6df06c08cb5830cadd18a9245a8ca1724f50740029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_PICKWINNER = "pickWinner";

    public static final String FUNC_LOTTERY_STATE = "lottery_state";

    public static final String FUNC_START_NEW_LOTTERY = "start_new_lottery";

    public static final String FUNC_LOTTERYID = "lotteryId";

    public static final String FUNC_PLAYERS = "players";

    public static final Event WINNER_EVENT = new Event("Winner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected LotteryOracleUseVrf(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LotteryOracleUseVrf(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LotteryOracleUseVrf(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LotteryOracleUseVrf(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
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

    public RemoteCall<BigInteger> lottery_state() {
        final Function function = new Function(FUNC_LOTTERY_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> start_new_lottery(List<String> _players) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<org.fisco.bcos.web3j.abi.datatypes.Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, org.fisco.bcos.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void start_new_lottery(List<String> _players, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<org.fisco.bcos.web3j.abi.datatypes.Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, org.fisco.bcos.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String start_new_lotterySeq(List<String> _players) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(_players.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<org.fisco.bcos.web3j.abi.datatypes.Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, org.fisco.bcos.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<List<String>> getStart_new_lotteryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<List<String>>(

                convertToNative((List<Address>) results.get(0).getValue())
                );
    }

    public RemoteCall<BigInteger> lotteryId() {
        final Function function = new Function(FUNC_LOTTERYID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> players(BigInteger param0) {
        final Function function = new Function(FUNC_PLAYERS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<WinnerEventResponse> getWinnerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WINNER_EVENT, transactionReceipt);
        ArrayList<WinnerEventResponse> responses = new ArrayList<WinnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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

    @Deprecated
    public static LotteryOracleUseVrf load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryOracleUseVrf(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LotteryOracleUseVrf load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryOracleUseVrf(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LotteryOracleUseVrf load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LotteryOracleUseVrf(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LotteryOracleUseVrf load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LotteryOracleUseVrf(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LotteryOracleUseVrf> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String randomOracle) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(randomOracle)));
        return deployRemoteCall(LotteryOracleUseVrf.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<LotteryOracleUseVrf> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String randomOracle) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(randomOracle)));
        return deployRemoteCall(LotteryOracleUseVrf.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryOracleUseVrf> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String randomOracle) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(randomOracle)));
        return deployRemoteCall(LotteryOracleUseVrf.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryOracleUseVrf> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String randomOracle) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(randomOracle)));
        return deployRemoteCall(LotteryOracleUseVrf.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class WinnerEventResponse {
        public Log log;

        public BigInteger lotteryId;

        public String winner;

        public BigInteger ramdomness;
    }
}
