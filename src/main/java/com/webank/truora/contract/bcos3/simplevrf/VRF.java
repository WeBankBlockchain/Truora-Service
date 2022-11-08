package com.webank.truora.contract.bcos3.simplevrf;

import java.math.BigInteger;
import java.util.Arrays;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class VRF extends Contract {
    public static final String[] BINARY_ARRAY = {"6101f5610026600b82828239805160001a60731461001957fe5b30600052607381538281f3fe730000000000000000000000000000000000000000301460806040526004361061006c5760003560e01c806303a507be146100715780635727dc5c1461008f5780637a308a4c146100ad5780639870e2a4146100cb578063997da8d4146100e9578063eeeac01e14610107575b600080fd5b610079610125565b6040518082815260200191505060405180910390f35b610097610149565b6040518082815260200191505060405180910390f35b6100b561014e565b6040518082815260200191505060405180910390f35b6100d3610172565b6040518082815260200191505060405180910390f35b6100f1610196565b6040518082815260200191505060405180910390f35b61010f61019b565b6040518082815260200191505060405180910390f35b7f79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f8179881565b600781565b7f483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b881565b7ffffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd036414181565b600081565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f8156fea26469706673582212204af569d9d4dc1e678b20cfbb42a5dadbb844c58310c93ec02b9bcbda79bfe53b64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6101f5610026600b82828239805160001a60731461001957fe5b30600052607381538281f3fe730000000000000000000000000000000000000000301460806040526004361061006c5760003560e01c80636444700b146100715780636ad346661461008f578063876034b2146100ad578063974da038146100cb578063c4f1832c146100e9578063d4ceee2114610107575b600080fd5b610079610125565b6040518082815260200191505060405180910390f35b610097610149565b6040518082815260200191505060405180910390f35b6100b561016d565b6040518082815260200191505060405180910390f35b6100d3610191565b6040518082815260200191505060405180910390f35b6100f1610196565b6040518082815260200191505060405180910390f35b61010f61019b565b6040518082815260200191505060405180910390f35b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f81565b7f79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f8179881565b7ffffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd036414181565b600081565b600781565b7f483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b88156fea264697066735822122021d42faadbe7fcccf1cd57760ac90a23d7824f53f509ee7808a159bf2fd5599a64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"name\":\"AA\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[2575149268,2538446904],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"BB\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[1462230108,3304162092],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"GX\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[61147070,1792231014],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"GY\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[2050001484,3570331169],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"NN\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[2557534884,2271229106],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"PP\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[4008362014,1682206731],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_AA = "AA";

    public static final String FUNC_BB = "BB";

    public static final String FUNC_GX = "GX";

    public static final String FUNC_GY = "GY";

    public static final String FUNC_NN = "NN";

    public static final String FUNC_PP = "PP";

    protected VRF(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public BigInteger AA() throws ContractException {
        final Function function = new Function(FUNC_AA, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger BB() throws ContractException {
        final Function function = new Function(FUNC_BB, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger GX() throws ContractException {
        final Function function = new Function(FUNC_GX, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger GY() throws ContractException {
        final Function function = new Function(FUNC_GY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger NN() throws ContractException {
        final Function function = new Function(FUNC_NN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger PP() throws ContractException {
        final Function function = new Function(FUNC_PP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public static VRF load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new VRF(contractAddress, client, credential);
    }

    public static VRF deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(VRF.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
