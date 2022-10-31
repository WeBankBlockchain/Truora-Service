package com.webank.truora.contract.bcos3;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class OracleCoreInterface extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"name\":\"getChainIdAndGroupId\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[2314240274,375907380],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_callbackAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_nonce\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"_timesAmount\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_expiryTime\",\"type\":\"uint256\"},{\"internalType\":\"bool\",\"name\":\"needProof\",\"type\":\"bool\"},{\"internalType\":\"uint256\",\"name\":\"returnType\",\"type\":\"uint256\"}],\"name\":\"query\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[2069504051,1148828689],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETCHAINIDANDGROUPID = "getChainIdAndGroupId";

    public static final String FUNC_QUERY = "query";

    protected OracleCoreInterface(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public Tuple2<String, String> getChainIdAndGroupId() throws ContractException {
        final Function function = new Function(FUNC_GETCHAINIDANDGROUPID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<String, String>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue());
    }

    public TransactionReceipt query(String _callbackAddress, BigInteger _nonce, String _url,
            BigInteger _timesAmount, BigInteger _expiryTime, Boolean needProof,
            BigInteger returnType) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String query(String _callbackAddress, BigInteger _nonce, String _url,
            BigInteger _timesAmount, BigInteger _expiryTime, Boolean needProof,
            BigInteger returnType, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForQuery(String _callbackAddress, BigInteger _nonce,
            String _url, BigInteger _timesAmount, BigInteger _expiryTime, Boolean needProof,
            BigInteger returnType) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple7<String, BigInteger, String, BigInteger, BigInteger, Boolean, BigInteger> getQueryInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_QUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple7<String, BigInteger, String, BigInteger, BigInteger, Boolean, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue(), 
                (Boolean) results.get(5).getValue(), 
                (BigInteger) results.get(6).getValue()
                );
    }

    public Tuple1<Boolean> getQueryOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_QUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public static OracleCoreInterface load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new OracleCoreInterface(contractAddress, client, credential);
    }

    public static OracleCoreInterface deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(OracleCoreInterface.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
