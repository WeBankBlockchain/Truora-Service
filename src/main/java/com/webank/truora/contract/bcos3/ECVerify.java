package com.webank.truora.contract.bcos3;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class ECVerify extends Contract {
    public static final String[] BINARY_ARRAY = {"610580610026600b82828239805160001a60731461001957fe5b30600052607381538281f3fe73000000000000000000000000000000000000000030146080604052600436106100565760003560e01c806325d869821461005b57806331bcbf8214610160578063d29325e5146101ef578063f1d58f9f14610276575b600080fd5b61011e6004803603604081101561007157600080fd5b81019080803590602001909291908035906020019064010000000081111561009857600080fd5b8201836020820111156100aa57600080fd5b803590602001918460018302840111640100000000831117156100cc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610373565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101ad6004803603608081101561017657600080fd5b8101908080359060200190929190803560ff16906020019092919080359060200190929190803590602001909291905050506103b7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61025c600480360360a081101561020557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803560ff1690602001909291908035906020019092919080359060200190929190505050610498565b604051808215151515815260200191505060405180910390f35b6103596004803603606081101561028c57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156102d357600080fd5b8201836020820111156102e557600080fd5b8035906020019184600183028401116401000000008311171561030757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506104f3565b604051808215151515815260200191505060405180910390f35b6000604182511461038357600080fd5b60008060006020850151925060408501519150606085015160001a90506103ac868285856103b7565b935050505092915050565b6000601b8460ff1610156103cc57601b840193505b601b8460ff1614806103e15750601c8460ff16145b6103ea57600080fd5b60018585858560405160008152602001604052604051808581526020018460ff1660ff1681526020018381526020018281526020019450505050506020604051602081039080840390855afa158015610447573d6000803e3d6000fd5b505050602060405103519050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561048d57600080fd5b809050949350505050565b6000806104a7868686866103b7565b9050600091508673ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614156104e657600191505b8191505095945050505050565b6000806105008484610373565b9050600091508473ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561053f57600191505b81915050939250505056fea26469706673582212202786f80569c0c05c964b1e8deec3455cb5c7735d694907994ff0b664fdeee8eb64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"610580610026600b82828239805160001a60731461001957fe5b30600052607381538281f3fe73000000000000000000000000000000000000000030146080604052600436106100565760003560e01c8063c0f585741461005b578063e949507a14610160578063eeb9926b146101e7578063fcc7aeeb146102e4575b600080fd5b61011e6004803603604081101561007157600080fd5b81019080803590602001909291908035906020019064010000000081111561009857600080fd5b8201836020820111156100aa57600080fd5b803590602001918460018302840111640100000000831117156100cc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610373565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101cd600480360360a081101561017657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803560ff16906020019092919080359060200190929190803590602001909291905050506103b7565b604051808215151515815260200191505060405180910390f35b6102ca600480360360608110156101fd57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561024457600080fd5b82018360208201111561025657600080fd5b8035906020019184600183028401116401000000008311171561027857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610412565b604051808215151515815260200191505060405180910390f35b610331600480360360808110156102fa57600080fd5b8101908080359060200190929190803560ff1690602001909291908035906020019092919080359060200190929190505050610469565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000604182511461038357600080fd5b60008060006020850151925060408501519150606085015160001a90506103ac86828585610469565b935050505092915050565b6000806103c686868686610469565b9050600091508673ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561040557600191505b8191505095945050505050565b60008061041f8484610373565b9050600091508473ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561045e57600191505b819150509392505050565b6000601b8460ff16101561047e57601b840193505b601b8460ff1614806104935750601c8460ff16145b61049c57600080fd5b60018585858560405160008152602001604052604051808581526020018460ff1660ff1681526020018381526020018281526020019450505050506020604051602081039080840390855afa1580156104f9573d6000803e3d6000fd5b505050602060405103519050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561053f57600080fd5b80905094935050505056fea26469706673582212203c150fe9f4b294a4fc454cafd22ae3e02d2ddc0c6db7583d266551f17721476c64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"hash\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"signature\",\"type\":\"bytes\"}],\"name\":\"recover_sig\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"signature_address\",\"type\":\"address\"}],\"stateMutability\":\"pure\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"hash\",\"type\":\"bytes32\"},{\"internalType\":\"uint8\",\"name\":\"v\",\"type\":\"uint8\"},{\"internalType\":\"bytes32\",\"name\":\"r\",\"type\":\"bytes32\"},{\"internalType\":\"bytes32\",\"name\":\"s\",\"type\":\"bytes32\"}],\"name\":\"recover_vrs\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"signature_address\",\"type\":\"address\"}],\"stateMutability\":\"pure\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"signAddr\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"hash\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"signature\",\"type\":\"bytes\"}],\"name\":\"verify_sig\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"ret\",\"type\":\"bool\"}],\"stateMutability\":\"pure\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"signAddr\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"hash\",\"type\":\"bytes32\"},{\"internalType\":\"uint8\",\"name\":\"v\",\"type\":\"uint8\"},{\"internalType\":\"bytes32\",\"name\":\"r\",\"type\":\"bytes32\"},{\"internalType\":\"bytes32\",\"name\":\"s\",\"type\":\"bytes32\"}],\"name\":\"verify_vrs\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"ret\",\"type\":\"bool\"}],\"stateMutability\":\"pure\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_RECOVER_SIG = "recover_sig";

    public static final String FUNC_RECOVER_VRS = "recover_vrs";

    public static final String FUNC_VERIFY_SIG = "verify_sig";

    public static final String FUNC_VERIFY_VRS = "verify_vrs";

    protected ECVerify(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt recover_sig(byte[] hash, byte[] signature) {
        final Function function = new Function(
                FUNC_RECOVER_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String recover_sig(byte[] hash, byte[] signature, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RECOVER_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRecover_sig(byte[] hash, byte[] signature) {
        final Function function = new Function(
                FUNC_RECOVER_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple2<byte[], byte[]> getRecover_sigInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECOVER_SIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue()
                );
    }

    public Tuple1<String> getRecover_sigOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_RECOVER_SIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt recover_vrs(byte[] hash, BigInteger v, byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_RECOVER_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String recover_vrs(byte[] hash, BigInteger v, byte[] r, byte[] s,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RECOVER_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRecover_vrs(byte[] hash, BigInteger v, byte[] r,
            byte[] s) {
        final Function function = new Function(
                FUNC_RECOVER_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple4<byte[], BigInteger, byte[], byte[]> getRecover_vrsInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECOVER_VRS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint8>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<byte[], BigInteger, byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (byte[]) results.get(2).getValue(), 
                (byte[]) results.get(3).getValue()
                );
    }

    public Tuple1<String> getRecover_vrsOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_RECOVER_VRS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt verify_sig(String signAddr, byte[] hash, byte[] signature) {
        final Function function = new Function(
                FUNC_VERIFY_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String verify_sig(String signAddr, byte[] hash, byte[] signature,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_VERIFY_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForVerify_sig(String signAddr, byte[] hash,
            byte[] signature) {
        final Function function = new Function(
                FUNC_VERIFY_SIG, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(signature)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple3<String, byte[], byte[]> getVerify_sigInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VERIFY_SIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, byte[], byte[]>(

                (String) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue(), 
                (byte[]) results.get(2).getValue()
                );
    }

    public Tuple1<Boolean> getVerify_sigOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_VERIFY_SIG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt verify_vrs(String signAddr, byte[] hash, BigInteger v, byte[] r,
            byte[] s) {
        final Function function = new Function(
                FUNC_VERIFY_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String verify_vrs(String signAddr, byte[] hash, BigInteger v, byte[] r, byte[] s,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_VERIFY_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForVerify_vrs(String signAddr, byte[] hash, BigInteger v,
            byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_VERIFY_VRS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(signAddr), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(hash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(v), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(r), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(s)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple5<String, byte[], BigInteger, byte[], byte[]> getVerify_vrsInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_VERIFY_VRS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint8>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<String, byte[], BigInteger, byte[], byte[]>(

                (String) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (byte[]) results.get(3).getValue(), 
                (byte[]) results.get(4).getValue()
                );
    }

    public Tuple1<Boolean> getVerify_vrsOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_VERIFY_VRS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public static ECVerify load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new ECVerify(contractAddress, client, credential);
    }

    public static ECVerify deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(ECVerify.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
