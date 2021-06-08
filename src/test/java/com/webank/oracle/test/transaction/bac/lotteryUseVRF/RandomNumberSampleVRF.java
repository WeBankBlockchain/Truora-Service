package com.webank.oracle.test.transaction.bac.lotteryUseVRF;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
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

import java.math.BigInteger;
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
 * <p>Generated with web3j version null.
 */
@SuppressWarnings("unchecked")
public class RandomNumberSampleVRF extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051604080610bdd833981018060405281019080805190602001909291908051906020019092919050505081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600381600019169055505050610b438061009a6000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632dff0d0d14610093578063366e3360146100d857806342619f661461014f5780635a75a8f81461017a5780636d4ce63c146101b55780639e317f12146101e0578063b37217a414610225578063c75f695a1461026e575b600080fd5b34801561009f57600080fd5b506100c260048036038101908080356000191690602001909291905050506102b7565b6040518082815260200191505060405180910390f35b3480156100e457600080fd5b50610131600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035600019169060200190929190803590602001909291905050506102dc565b60405180826000191660001916815260200191505060405180910390f35b34801561015b57600080fd5b506101646105f1565b6040518082815260200191505060405180910390f35b34801561018657600080fd5b506101b36004803603810190808035600019169060200190929190803590602001909291905050506105f7565b005b3480156101c157600080fd5b506101ca610747565b6040518082815260200191505060405180910390f35b3480156101ec57600080fd5b5061020f6004803603810190808035600019169060200190929190505050610751565b6040518082815260200191505060405180910390f35b34801561023157600080fd5b5061025060048036038101908080359060200190929190505050610769565b60405180826000191660001916815260200191505060405180910390f35b34801561027a57600080fd5b5061029d60048036038101908080356000191690602001909291905050506107da565b604051808215151515815260200191505060405180910390f35b6000600560008360001916600019168152602001908152602001600020549050919050565b60008060008086600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ac917f848787306040518463ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018084600019166000191681526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b1580156103f857600080fd5b505af115801561040c573d6000803e3d6000fd5b505050506040513d602081101561042257600080fd5b81019080805190602001909291905050505061045b868630600260008b600019166000191681526020019081526020016000205461080c565b925061048c6001600260008960001916600019168152602001908152602001600020546108ea90919063ffffffff16565b60026000886000191660001916815260200190815260200160002081905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016040805180830381600087803b15801561053057600080fd5b505af1158015610544573d6000803e3d6000fd5b505050506040513d604081101561055a57600080fd5b810190808051906020019092919080519060200190929190505050809250819350505061058982828886610974565b935086600080866000191660001916815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60045481565b81600080826000191660001916815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156106fb576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f536f75726365206d7573742062652074686520767266436f7265206f6620746881526020017f652072657175657374000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600080826000191660001916815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556107428383610a22565b505050565b6000600454905090565b60026020528060005260406000206000915090505481565b60008061079b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600354856102dc565b9050600160066000836000191660001916815260200190815260200160002060006101000a81548160ff02191690831515021790555080915050919050565b600060066000836000191660001916815260200190815260200160002060009054906101000a900460ff169050919050565b6000848484846040516020018085600019166000191681526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040526040518082805190602001908083835b6020831015156108af578051825260208201915060208101905060208303925061088a565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600190049050949350505050565b600080828401905083811015151561096a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085815260200184815260200183600019166000191681526020018281526020019450505050506040516020818303038152906040526040518082805190602001908083835b6020831015156109eb57805182526020820191506020810190506020830392506109c6565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390209050949350505050565b60066000836000191660001916815260200190815260200160002060009054906101000a900460ff161515610abf576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b806004819055506004546005600084600019166000191681526020019081526020016000208190555060066000836000191660001916815260200190815260200160002060006101000a81549060ff021916905550505600a165627a7a72305820c3ad67137a19ffc2f807d63d2178f9989596ff0d0dadffad3bd0bbc3e6efd1dd0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":\"getById\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"_vrfCoreAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_keyHash\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false},{\"name\":\"_seed\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"vrfQuery\",\"outputs\":[{\"name\":\"requestId\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[],\"name\":\"randomResult\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"requestId\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false},{\"name\":\"randomness\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"callbackRandomness\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":\"nonces\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"userProvidedSeed\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"getRandomNumber\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"_vrfCore\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_keyHash\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":null,\"outputs\":null,\"type\":\"constructor\",\"payable\":false,\"stateMutability\":\"nonpayable\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_VRFQUERY = "vrfQuery";

    public static final String FUNC_RANDOMRESULT = "randomResult";

    public static final String FUNC_CALLBACKRANDOMNESS = "callbackRandomness";

    public static final String FUNC_GET = "get";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_GETRANDOMNUMBER = "getRandomNumber";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

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
        return BINARY;
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<BigInteger> getById(byte[] id) {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new Bytes32(id)),
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

    public RemoteCall<BigInteger> randomResult() {
        final Function function = new Function(FUNC_RANDOMRESULT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> nonces(byte[] param0) {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new Bytes32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<Boolean> checkIdFulfilled(byte[] id) {
        final Function function = new Function(FUNC_CHECKIDFULFILLED, 
                Arrays.<Type>asList(new Bytes32(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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
