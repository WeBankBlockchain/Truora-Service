package com.webank.oracle.trial.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
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
public class RandomNumberSampleOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"60a060405234801561001057600080fd5b506040516105ec3803806105ec8339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050818073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b815250505080600181905550505060805160601c61053a6100b26000398061018652806102c4525061053a6000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c806310835dbb1461005c57806342619f66146100a857806394985ddd146100c65780639e317f12146100fe578063b37217a414610140575b600080fd5b6100926004803603604081101561007257600080fd5b810190808035906020019092919080359060200190929190505050610182565b6040518082815260200191505060405180910390f35b6100b06102bc565b6040518082815260200191505060405180910390f35b6100fc600480360360408110156100dc57600080fd5b8101908080359060200190929190803590602001909291905050506102c2565b005b61012a6004803603602081101561011457600080fd5b8101908080359060200190929190505050610391565b6040518082815260200191505060405180910390f35b61016c6004803603602081101561015657600080fd5b81019080803590602001909291905050506103a9565b6040518082815260200191505060405180910390f35b60007f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff1663ac917f848484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561023357600080fd5b505af1158015610247573d6000803e3d6000fd5b50505050600061026b848430600080898152602001908152602001600020546103be565b905061029360016000808781526020019081526020016000205461043890919063ffffffff16565b600080868152602001908152602001600020819055506102b384826104c0565b91505092915050565b60025481565b7f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610383576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4f6e6c7920565246436f6f7264696e61746f722063616e2066756c66696c6c0081525060200191505060405180910390fd5b61038d82826104f9565b5050565b60006020528060005260406000206000915090505481565b60006103b760015483610182565b9050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b6000808284019050838110156104b6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b60008282604051602001808381526020018281526020019250505060405160208183030381529060405280519060200120905092915050565b80600281905550505056fea26469706673582212207480434058953c9ac423497fd722fc0a9fddde83e3957f94cf42e3f655e477f664736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_coordinator\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"getRandomNumber\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"randomResult\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"rawFulfillRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"requestRandomness\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"60a060405234801561001057600080fd5b506040516105ec3803806105ec8339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050818073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b815250505080600181905550505060805160601c61053a6100b2600039806101845280610273525061053a6000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c806353ad19711461005c57806369dabf78146100945780638db1363d146100b2578063e3f92d07146100f4578063f724a27914610140575b600080fd5b6100926004803603604081101561007257600080fd5b810190808035906020019092919080359060200190929190505050610182565b005b61009c610251565b6040518082815260200191505060405180910390f35b6100de600480360360208110156100c857600080fd5b8101908080359060200190929190505050610257565b6040518082815260200191505060405180910390f35b61012a6004803603604081101561010a57600080fd5b81019080803590602001909291908035906020019092919050505061026f565b6040518082815260200191505060405180910390f35b61016c6004803603602081101561015657600080fd5b81019080803590602001909291905050506103a9565b6040518082815260200191505060405180910390f35b7f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610243576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f4f6e6c7920565246436f6f7264696e61746f722063616e2066756c66696c6c0081525060200191505060405180910390fd5b61024d82826103be565b5050565b60025481565b60006020528060005260406000206000915090505481565b60007f000000000000000000000000000000000000000000000000000000000000000073ffffffffffffffffffffffffffffffffffffffff16631290c07c8484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050600060405180830381600087803b15801561032057600080fd5b505af1158015610334573d6000803e3d6000fd5b505050506000610358848430600080898152602001908152602001600020546103c9565b905061038060016000808781526020019081526020016000205461044390919063ffffffff16565b600080868152602001908152602001600020819055506103a084826104cb565b91505092915050565b60006103b76001548361026f565b9050919050565b806002819055505050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b6000808284019050838110156104c1576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000828260405160200180838152602001828152602001925050506040516020818303038152906040528051906020012090509291505056fea26469706673582212206163269588fe3d65276292aa791a32c43669aa30945233281f19414883021f4d64736f6c634300060a0033"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_GETRANDOMNUMBER = "getRandomNumber";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_RANDOMRESULT = "randomResult";

    public static final String FUNC_RAWFULFILLRANDOMNESS = "rawFulfillRandomness";

    public static final String FUNC_REQUESTRANDOMNESS = "requestRandomness";

    @Deprecated
    protected RandomNumberSampleOracle(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RandomNumberSampleOracle(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RandomNumberSampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RandomNumberSampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
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

    public RemoteCall<TransactionReceipt> rawFulfillRandomness(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC_RAWFULFILLRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void rawFulfillRandomness(byte[] requestId, BigInteger randomness, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_RAWFULFILLRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String rawFulfillRandomnessSeq(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC_RAWFULFILLRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(requestId),
                new Uint256(randomness)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], BigInteger> getRawFulfillRandomnessInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RAWFULFILLRANDOMNESS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> requestRandomness(byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void requestRandomness(byte[] _keyHash, BigInteger _seed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String requestRandomnessSeq(byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNESS,
                Arrays.<Type>asList(new Bytes32(_keyHash),
                new Uint256(_seed)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], BigInteger> getRequestRandomnessInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REQUESTRANDOMNESS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<byte[]> getRequestRandomnessOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REQUESTRANDOMNESS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    @Deprecated
    public static RandomNumberSampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RandomNumberSampleOracle(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RandomNumberSampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RandomNumberSampleOracle(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RandomNumberSampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RandomNumberSampleOracle(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RandomNumberSampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RandomNumberSampleOracle(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<RandomNumberSampleOracle> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _coordinator, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_coordinator),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleOracle.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<RandomNumberSampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _coordinator, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_coordinator),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleOracle.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleOracle> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _coordinator, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_coordinator),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleOracle.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<RandomNumberSampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _coordinator, byte[] _keyHash) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_coordinator),
                new Bytes32(_keyHash)));
        return deployRemoteCall(RandomNumberSampleOracle.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }
}
