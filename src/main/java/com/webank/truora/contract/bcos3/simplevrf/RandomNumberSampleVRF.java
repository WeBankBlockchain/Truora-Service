package com.webank.truora.contract.bcos3.simplevrf;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
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
public class RandomNumberSampleVRF extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051610c01380380610c018339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806003819055505050610b5a806100a76000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80636d4ce63c1161005b5780636d4ce63c146101685780639e317f1214610186578063b1eb96c9146101c8578063c75f695a1461020a5761007d565b80632dff0d0d14610082578063366e3360146100c45780635a75a8f814610130575b600080fd5b6100ae6004803603602081101561009857600080fd5b8101908080359060200190929190505050610250565b6040518082815260200191505060405180910390f35b61011a600480360360608110156100da57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019092919050505061026d565b6040518082815260200191505060405180910390f35b6101666004803603604081101561014657600080fd5b81019080803590602001909291908035906020019092919050505061068d565b005b610170610788565b6040518082815260200191505060405180910390f35b6101b26004803603602081101561019c57600080fd5b8101908080359060200190929190505050610792565b6040518082815260200191505060405180910390f35b6101f4600480360360208110156101de57600080fd5b81019080803590602001909291905050506107aa565b6040518082815260200191505060405180910390f35b6102366004803603602081101561022057600080fd5b8101908080359060200190929190505050610813565b604051808215151515815260200191505060405180910390f35b600060056000838152602001908152602001600020549050919050565b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ac917f848484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b15801561036157600080fd5b505af1158015610375573d6000803e3d6000fd5b505050506040513d602081101561038b57600080fd5b81019080805190602001909291905050505060006103be848430600260008981526020019081526020016000205461083d565b90506103e7600160026000878152602001908152602001600020546108b790919063ffffffff16565b6002600086815260200190815260200160002081905550606080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b815260040160006040518083038186803b15801561046957600080fd5b505afa15801561047d573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156104a757600080fd5b81019080805160405193929190846401000000008211156104c757600080fd5b838201915060208201858111156104dd57600080fd5b82518660018202830111640100000000821117156104fa57600080fd5b8083526020830192505050908051906020019080838360005b8381101561052e578082015181840152602081019050610513565b50505050905090810190601f16801561055b5780820380516001836020036101000a031916815260200191505b506040526020018051604051939291908464010000000082111561057e57600080fd5b8382019150602082018581111561059457600080fd5b82518660018202830111640100000000821117156105b157600080fd5b8083526020830192505050908051906020019080838360005b838110156105e55780820151818401526020810190506105ca565b50505050905090810190601f1680156106125780820380516001836020036101000a031916815260200191505b50604052505050809250819350505061062d8282888661093f565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610744576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180610afc6029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556107838383610a20565b505050565b6000600454905090565b60026020528060005260406000206000915090505481565b6000806107dc600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166003548561026d565b905060016006600083815260200190815260200160002060006101000a81548160ff02191690831515021790555080915050919050565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b600080828401905083811015610935576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085805190602001908083835b6020831061097b5780518252602082019150602081019050602083039250610958565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b602083106109cc57805182526020820191506020810190506020830392506109a9565b6001836020036101000a038019825116818451168082178552505050505050905001838152602001828152602001945050505050604051602081830303815290604052805190602001209050949350505050565b6006600083815260200190815260200160002060009054906101000a900460ff16610ab3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b8060048190555060045460056000848152602001908152602001600020819055506006600083815260200190815260200160002060006101000a81549060ff0219169055505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a26469706673582212207171979c7d1a1738876ae8863dda3f39c010152cbed2ec5adad455611df7e46764736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051610c01380380610c018339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806003819055505050610b5a806100a76000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80637525595d1161005b5780637525595d146101525780638db1363d14610194578063ae1ba0e6146101d6578063b791ee541461020e5761007d565b8063299f7f9d14610082578063365aab7a146100a057806356ddd8ed1461010c575b600080fd5b61008a610250565b6040518082815260200191505060405180910390f35b6100f6600480360360608110156100b657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019092919050505061025a565b6040518082815260200191505060405180910390f35b6101386004803603602081101561012257600080fd5b810190808035906020019092919050505061067a565b604051808215151515815260200191505060405180910390f35b61017e6004803603602081101561016857600080fd5b81019080803590602001909291905050506106a4565b6040518082815260200191505060405180910390f35b6101c0600480360360208110156101aa57600080fd5b810190808035906020019092919050505061070d565b6040518082815260200191505060405180910390f35b61020c600480360360408110156101ec57600080fd5b810190808035906020019092919080359060200190929190505050610725565b005b61023a6004803603602081101561022457600080fd5b8101908080359060200190929190505050610820565b6040518082815260200191505060405180910390f35b6000600454905090565b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631290c07c8484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b15801561034e57600080fd5b505af1158015610362573d6000803e3d6000fd5b505050506040513d602081101561037857600080fd5b81019080805190602001909291905050505060006103ab848430600260008981526020019081526020016000205461083d565b90506103d4600160026000878152602001908152602001600020546108b790919063ffffffff16565b6002600086815260200190815260200160002081905550606080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b815260040160006040518083038186803b15801561045657600080fd5b505afa15801561046a573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250604081101561049457600080fd5b81019080805160405193929190846401000000008211156104b457600080fd5b838201915060208201858111156104ca57600080fd5b82518660018202830111640100000000821117156104e757600080fd5b8083526020830192505050908051906020019080838360005b8381101561051b578082015181840152602081019050610500565b50505050905090810190601f1680156105485780820380516001836020036101000a031916815260200191505b506040526020018051604051939291908464010000000082111561056b57600080fd5b8382019150602082018581111561058157600080fd5b825186600182028301116401000000008211171561059e57600080fd5b8083526020830192505050908051906020019080838360005b838110156105d25780820151818401526020810190506105b7565b50505050905090810190601f1680156105ff5780820380516001836020036101000a031916815260200191505b50604052505050809250819350505061061a8282888661093f565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b6000806106d6600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166003548561025a565b905060016006600083815260200190815260200160002060006101000a81548160ff02191690831515021790555080915050919050565b60026020528060005260406000206000915090505481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146107dc576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180610afc6029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905561081b8383610a20565b505050565b600060056000838152602001908152602001600020549050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b600080828401905083811015610935576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085805190602001908083835b6020831061097b5780518252602082019150602081019050602083039250610958565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b602083106109cc57805182526020820191506020810190506020830392506109a9565b6001836020036101000a038019825116818451168082178552505050505050905001838152602001828152602001945050505050604051602081830303815290604052805190602001209050949350505050565b6006600083815260200190815260200160002060009054906101000a900460ff16610ab3576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b8060048190555060045460056000848152602001908152602001600020819055506006600083815260200190815260200160002060006101000a81549060ff0219169055505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a264697066735822122016cf4daa95e7cdc5274d238992f1c500bdd59feb05d016a5ade8d7353b0d5abf64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCore\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"callbackRandomness\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"requestRandomNumber\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCoreAddress\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"vrfQuery\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CALLBACKRANDOMNESS = "callbackRandomness";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_REQUESTRANDOMNUMBER = "requestRandomNumber";

    public static final String FUNC_VRFQUERY = "vrfQuery";

    protected RandomNumberSampleVRF(String contractAddress, Client client,
            CryptoKeyPair credential) {
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

    public Boolean checkIdFulfilled(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_CHECKIDFULFILLED, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public BigInteger get() throws ContractException {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger getById(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger nonces(byte[] param0) throws ContractException {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt requestRandomNumber(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String requestRandomNumber(BigInteger userProvidedSeed, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRequestRandomNumber(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_REQUESTRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getRequestRandomNumberInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REQUESTRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple1<byte[]> getRequestRandomNumberOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REQUESTRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
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

    public static RandomNumberSampleVRF load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new RandomNumberSampleVRF(contractAddress, client, credential);
    }

    public static RandomNumberSampleVRF deploy(Client client, CryptoKeyPair credential,
            String _vrfCore, byte[] _keyHash) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_vrfCore), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_keyHash)));
        return deploy(RandomNumberSampleVRF.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
    }
}
