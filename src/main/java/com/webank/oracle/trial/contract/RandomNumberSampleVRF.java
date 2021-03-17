package com.webank.oracle.trial.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516108483803806108488339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508060038190555050506107a1806100a76000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c8063366e33601461006757806342619f66146100d35780635a75a8f8146100f157806368b3b1cc146101295780639e317f1214610161578063b37217a4146101a3575b600080fd5b6100bd6004803603606081101561007d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001909291905050506101e5565b6040518082815260200191505060405180910390f35b6100db610499565b6040518082815260200191505060405180910390f35b6101276004803603604081101561010757600080fd5b81019080803590602001909291908035906020019092919050505061049f565b005b61015f6004803603604081101561013f57600080fd5b81019080803590602001909291908035906020019092919050505061059a565b005b61018d6004803603602081101561017757600080fd5b81019080803590602001909291905050506105a5565b6040518082815260200191505060405180910390f35b6101cf600480360360208110156101b957600080fd5b81019080803590602001909291905050506105bd565b6040518082815260200191505060405180910390f35b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ac917f848484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b1580156102d957600080fd5b505af11580156102ed573d6000803e3d6000fd5b505050506040513d602081101561030357600080fd5b810190808051906020019092919050505050600061033684843060026000898152602001908152602001600020546105f5565b905061035f6001600260008781526020019081526020016000205461066f90919063ffffffff16565b6002600086815260200190815260200160002081905550600080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b8152600401604080518083038186803b1580156103e057600080fd5b505afa1580156103f4573d6000803e3d6000fd5b505050506040513d604081101561040a57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050610439828288866106f7565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60045481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610556576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001806107436029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055610595838361059a565b505050565b806004819055505050565b60026020528060005260406000206000915090505481565b60006105ee600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600354846101e5565b9050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b6000808284019050838110156106ed576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085815260200184815260200183815260200182815260200194505050505060405160208183030381529060405280519060200120905094935050505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a26469706673582212207cb2f0daf500fc6cb8de2098332d7e0125c5d442f9f922344f4c8226b9ba9d9164736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCore\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"__callbackRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"callbackRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"getRandomNumber\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"randomResult\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCoreAddress\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"vrfQuery\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516108483803806108488339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508060038190555050506107a1806100a76000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c8063365aab7a1461006757806369dabf78146100d35780638db1363d146100f1578063ae1ba0e614610133578063b9d6d8111461016b578063f724a279146101a3575b600080fd5b6100bd6004803603606081101561007d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001909291905050506101e5565b6040518082815260200191505060405180910390f35b6100db610499565b6040518082815260200191505060405180910390f35b61011d6004803603602081101561010757600080fd5b810190808035906020019092919050505061049f565b6040518082815260200191505060405180910390f35b6101696004803603604081101561014957600080fd5b8101908080359060200190929190803590602001909291905050506104b7565b005b6101a16004803603604081101561018157600080fd5b8101908080359060200190929190803590602001909291905050506105b2565b005b6101cf600480360360208110156101b957600080fd5b81019080803590602001909291905050506105bd565b6040518082815260200191505060405180910390f35b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631290c07c8484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b1580156102d957600080fd5b505af11580156102ed573d6000803e3d6000fd5b505050506040513d602081101561030357600080fd5b810190808051906020019092919050505050600061033684843060026000898152602001908152602001600020546105f5565b905061035f6001600260008781526020019081526020016000205461066f90919063ffffffff16565b6002600086815260200190815260200160002081905550600080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b8152600401604080518083038186803b1580156103e057600080fd5b505afa1580156103f4573d6000803e3d6000fd5b505050506040513d604081101561040a57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050610439828288866106f7565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60045481565b60026020528060005260406000206000915090505481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461056e576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001806107436029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556105ad83836105b2565b505050565b806004819055505050565b60006105ee600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600354846101e5565b9050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b6000808284019050838110156106ed576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085815260200184815260200183815260200182815260200194505050505060405160208183030381529060405280519060200120905094935050505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a26469706673582212201468ec41ff691a2ab8b5af3343097d0d2c3e8f0ccccec0a500f0a86df4a7b0ff64736f6c634300060a0033"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC___CALLBACKRANDOMNESS = "__callbackRandomness";

    public static final String FUNC_CALLBACKRANDOMNESS = "callbackRandomness";

    public static final String FUNC_GETRANDOMNUMBER = "getRandomNumber";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_RANDOMRESULT = "randomResult";

    public static final String FUNC_VRFQUERY = "vrfQuery";

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

    public RemoteCall<TransactionReceipt> __callbackRandomness(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void __callbackRandomness(byte[] requestId, BigInteger randomness, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String __callbackRandomnessSeq(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC___CALLBACKRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
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

    public RemoteCall<TransactionReceipt> callbackRandomness(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void callbackRandomness(byte[] requestId, BigInteger randomness, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String callbackRandomnessSeq(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], BigInteger> getCallbackRandomnessInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CALLBACKRANDOMNESS,
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
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void getRandomNumber(BigInteger userProvidedSeed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER,
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getRandomNumberSeq(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER,
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
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
                Arrays.<Type>asList(new Bytes32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> randomResult() {
        final Function function = new Function(FUNC_RANDOMRESULT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> vrfQuery(String _vrfCoreAddress, byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY,
                Arrays.<Type>asList(new Address(_vrfCoreAddress),
                new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void vrfQuery(String _vrfCoreAddress, byte[] _keyHash, BigInteger _seed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_VRFQUERY,
                Arrays.<Type>asList(new Address(_vrfCoreAddress),
                new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String vrfQuerySeq(String _vrfCoreAddress, byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY,
                Arrays.<Type>asList(new Address(_vrfCoreAddress),
                new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<String, byte[], BigInteger> getVrfQueryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VRFQUERY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<String, byte[], BigInteger>(

                (String) results.get(0).getValue(),
                (byte[]) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue()
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

    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _vrfCore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_vrfCore),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _vrfCore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_vrfCore),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _vrfCore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_vrfCore),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleVRF> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _vrfCore, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_vrfCore),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleVRF.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }
}
