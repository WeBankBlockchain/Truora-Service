package com.webank.oracle.test.temp;

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
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
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
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class RandomNumberSampleVRF extends Contract {
    public static final String[] BINARY_ARRAY = {"60a060405234801561001057600080fd5b506040516106d33803806106d38339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050818073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b815250505080600281905550505060805160601c6106216100b2600039806102de528061041152506106216000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c806342619f661461005c57806368b3b1cc1461007a5780639e317f12146100b2578063b37217a4146100f4578063b9e3c78b14610136575b600080fd5b610064610182565b6040518082815260200191505060405180910390f35b6100b06004803603604081101561009057600080fd5b810190808035906020019092919080359060200190929190505050610188565b005b6100de600480360360208110156100c857600080fd5b81019080803590602001909291905050506102ad565b6040518082815260200191505060405180910390f35b6101206004803603602081101561010a57600080fd5b81019080803590602001909291905050506102c5565b6040518082815260200191505060405180910390f35b61016c6004803603604081101561014c57600080fd5b8101908080359060200190929190803590602001909291905050506102da565b6040518082815260200191505060405180910390f35b60035481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461023f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001806105c36029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a281600381905550505050565b60016020528060005260406000206000915090505481565b60006102d3600254836102da565b9050919050565b60007f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff1663ac917f848484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561038b57600080fd5b505af115801561039f573d6000803e3d6000fd5b5050505060006103c48484306001600089815260200190815260200160002054610487565b90506103ec60018060008781526020019081526020016000205461050190919063ffffffff16565b600160008681526020019081526020016000208190555061040d8482610589565b91507f000000000000000000000000000000000000000000000000000000000000000060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505092915050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b60008082840190508381101561057f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000828260405160200180838152602001828152602001925050506040516020818303038152906040528051906020012090509291505056fe536f75726365206d7573742062652074686520767266636f7265206f66207468652072657175657374a2646970667358221220c3768b7e9b00cf64601756f9b7325f59d03fd78d2db75b3d53e877dd0ec38a4f64736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfcore\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"__callbackRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"getRandomNumber\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"randomResult\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"vrfQuery\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"60a060405234801561001057600080fd5b506040516106d33803806106d38339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050818073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b815250505080600281905550505060805160601c6106216100b26000398061018652806102b952506106216000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80633297603b1461005c57806369dabf78146100a85780638db1363d146100c6578063b9d6d81114610108578063f724a27914610140575b600080fd5b6100926004803603604081101561007257600080fd5b810190808035906020019092919080359060200190929190505050610182565b6040518082815260200191505060405180910390f35b6100b061032f565b6040518082815260200191505060405180910390f35b6100f2600480360360208110156100dc57600080fd5b8101908080359060200190929190505050610335565b6040518082815260200191505060405180910390f35b61013e6004803603604081101561011e57600080fd5b81019080803590602001909291908035906020019092919050505061034d565b005b61016c6004803603602081101561015657600080fd5b8101908080359060200190929190505050610472565b6040518082815260200191505060405180910390f35b60007f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff16631290c07c8484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561023357600080fd5b505af1158015610247573d6000803e3d6000fd5b50505050600061026c8484306001600089815260200190815260200160002054610487565b905061029460018060008781526020019081526020016000205461050190919063ffffffff16565b60016000868152602001908152602001600020819055506102b58482610589565b91507f000000000000000000000000000000000000000000000000000000000000000060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505092915050565b60035481565b60016020528060005260406000206000915090505481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610404576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001806105c36029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a281600381905550505050565b600061048060025483610182565b9050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b60008082840190508381101561057f576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000828260405160200180838152602001828152602001925050506040516020818303038152906040528051906020012090509291505056fe536f75726365206d7573742062652074686520767266636f7265206f66207468652072657175657374a2646970667358221220b43365be8c6df3bb4233b17681e7412c1419bb62b5e4b5afc64997e3de516dd464736f6c634300060a0033"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC___CALLBACKRANDOMNESS = "__callbackRandomness";

    public static final String FUNC_GETRANDOMNUMBER = "getRandomNumber";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_RANDOMRESULT = "randomResult";

    public static final String FUNC_VRFQUERY = "vrfQuery";

    public static final Event FULFILLED_EVENT = new Event("Fulfilled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event REQUESTED_EVENT = new Event("Requested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    @Deprecated
    protected RandomNumberSampleVRF(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RandomNumberSampleVRF(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RandomNumberSampleVRF(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RandomNumberSampleVRF(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public List<FulfilledEventResponse> getFulfilledEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FULFILLED_EVENT, transactionReceipt);
        ArrayList<FulfilledEventResponse> responses = new ArrayList<FulfilledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FulfilledEventResponse typedResponse = new FulfilledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerFulfilledEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerFulfilledEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<RequestedEventResponse> getRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REQUESTED_EVENT, transactionReceipt);
        ArrayList<RequestedEventResponse> responses = new ArrayList<RequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RequestedEventResponse typedResponse = new RequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerRequestedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerRequestedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<TransactionReceipt> __callbackRandomness(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void __callbackRandomness(byte[] requestId, BigInteger randomness, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String __callbackRandomnessSeq(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], BigInteger> get__callbackRandomnessInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC___CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> getRandomNumber(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void getRandomNumber(BigInteger userProvidedSeed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getRandomNumberSeq(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<BigInteger> getGetRandomNumberInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple1<byte[]> getGetRandomNumberOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> nonces(byte[] param0) {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> randomResult() {
        final Function function = new Function(FUNC_RANDOMRESULT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> vrfQuery(byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void vrfQuery(byte[] _keyHash, BigInteger _seed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String vrfQuerySeq(byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], BigInteger> getVrfQueryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VRFQUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<byte[]> getVrfQueryOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_VRFQUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    @Deprecated
    public static RandomNumberSampleVRF load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RandomNumberSampleVRF(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RandomNumberSampleVRF load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RandomNumberSampleVRF(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RandomNumberSampleVRF load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RandomNumberSampleVRF(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RandomNumberSampleVRF load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RandomNumberSampleVRF(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _vrfcore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_vrfcore), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _vrfcore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_vrfcore), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _vrfcore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_vrfcore), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _vrfcore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_vrfcore), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class FulfilledEventResponse {
        public Log log;

        public byte[] id;
    }

    public static class RequestedEventResponse {
        public Log log;

        public byte[] id;
    }
}
