package com.webank.truora.contract.bcos3.simplevrf;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class VRFClient extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"callbackRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCoreAddress\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"vrfQuery\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CALLBACKRANDOMNESS = "callbackRandomness";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_VRFQUERY = "vrfQuery";

    protected VRFClient(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt callbackRandomness(byte[] requestId, BigInteger randomness) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String callbackRandomness(byte[] requestId, BigInteger randomness,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCallbackRandomness(byte[] requestId,
            BigInteger randomness) {
        final Function function = new Function(
                FUNC_CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(randomness)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple2<byte[], BigInteger> getCallbackRandomnessInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CALLBACKRANDOMNESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public BigInteger nonces(byte[] param0) throws ContractException {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt vrfQuery(String _vrfCoreAddress, byte[] _keyHash, BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_vrfCoreAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String vrfQuery(String _vrfCoreAddress, byte[] _keyHash, BigInteger _seed,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_vrfCoreAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForVrfQuery(String _vrfCoreAddress, byte[] _keyHash,
            BigInteger _seed) {
        final Function function = new Function(
                FUNC_VRFQUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_vrfCoreAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_keyHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_seed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple3<String, byte[], BigInteger> getVrfQueryInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VRFQUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
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
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public static VRFClient load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new VRFClient(contractAddress, client, credential);
    }

    public static VRFClient deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(VRFClient.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
