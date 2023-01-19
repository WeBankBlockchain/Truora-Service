package com.webank.truora.contract.bcos3.simplevrf;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Crypto extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"bytes\",\"name\":\"message\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"publicKey\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"curve25519VRFVerify\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes\",\"name\":\"data\",\"type\":\"bytes\"}],\"name\":\"keccak256Hash\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"message\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"publicKey\",\"type\":\"bytes\"},{\"internalType\":\"bytes32\",\"name\":\"r\",\"type\":\"bytes32\"},{\"internalType\":\"bytes32\",\"name\":\"s\",\"type\":\"bytes32\"}],\"name\":\"sm2Verify\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"},{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes\",\"name\":\"data\",\"type\":\"bytes\"}],\"name\":\"sm3\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CURVE25519VRFVERIFY = "curve25519VRFVerify";

    public static final String FUNC_KECCAK256HASH = "keccak256Hash";

    public static final String FUNC_SM2VERIFY = "sm2Verify";

    public static final String FUNC_SM3 = "sm3";

    protected Crypto(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public Tuple2<Boolean, BigInteger> curve25519VRFVerify(byte[] message, byte[] publicKey,
            byte[] proof) throws ContractException {
        final Function function = new Function(FUNC_CURVE25519VRFVERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(message), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(publicKey), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<Boolean, BigInteger>(
                (Boolean) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue());
    }

    public byte[] keccak256Hash(byte[] data) throws ContractException {
        final Function function = new Function(FUNC_KECCAK256HASH, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(data)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeCallWithSingleValueReturn(function, byte[].class);
    }

    public Tuple2<Boolean, String> sm2Verify(byte[] message, byte[] publicKey, byte[] r, byte[] s)
            throws ContractException {
        final Function function = new Function(FUNC_SM2VERIFY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(message), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(publicKey), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Address>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<Boolean, String>(
                (Boolean) results.get(0).getValue(), 
                (String) results.get(1).getValue());
    }

    public byte[] sm3(byte[] data) throws ContractException {
        final Function function = new Function(FUNC_SM3, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(data)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeCallWithSingleValueReturn(function, byte[].class);
    }

    public static Crypto load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Crypto(contractAddress, client, credential);
    }

    public static Crypto deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Crypto.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
