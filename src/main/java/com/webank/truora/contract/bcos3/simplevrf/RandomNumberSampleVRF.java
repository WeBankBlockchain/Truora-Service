package com.webank.truora.contract.bcos3.simplevrf;

import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.*;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class RandomNumberSampleVRF extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051610c30380380610c308339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806003819055505050610b89806100a76000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c80636d4ce63c1161005b5780636d4ce63c146101915780639e317f12146101af578063b37217a4146101f1578063c75f695a1461023357610088565b80632dff0d0d1461008d578063366e3360146100cf57806342619f661461013b5780635a75a8f814610159575b600080fd5b6100b9600480360360208110156100a357600080fd5b8101908080359060200190929190505050610279565b6040518082815260200191505060405180910390f35b610125600480360360608110156100e557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190505050610296565b6040518082815260200191505060405180910390f35b6101436106b6565b6040518082815260200191505060405180910390f35b61018f6004803603604081101561016f57600080fd5b8101908080359060200190929190803590602001909291905050506106bc565b005b6101996107b7565b6040518082815260200191505060405180910390f35b6101db600480360360208110156101c557600080fd5b81019080803590602001909291905050506107c1565b6040518082815260200191505060405180910390f35b61021d6004803603602081101561020757600080fd5b81019080803590602001909291905050506107d9565b6040518082815260200191505060405180910390f35b61025f6004803603602081101561024957600080fd5b8101908080359060200190929190505050610842565b604051808215151515815260200191505060405180910390f35b600060056000838152602001908152602001600020549050919050565b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ac917f848484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b15801561038a57600080fd5b505af115801561039e573d6000803e3d6000fd5b505050506040513d60208110156103b457600080fd5b81019080805190602001909291905050505060006103e7848430600260008981526020019081526020016000205461086c565b9050610410600160026000878152602001908152602001600020546108e690919063ffffffff16565b6002600086815260200190815260200160002081905550606080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b815260040160006040518083038186803b15801561049257600080fd5b505afa1580156104a6573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156104d057600080fd5b81019080805160405193929190846401000000008211156104f057600080fd5b8382019150602082018581111561050657600080fd5b825186600182028301116401000000008211171561052357600080fd5b8083526020830192505050908051906020019080838360005b8381101561055757808201518184015260208101905061053c565b50505050905090810190601f1680156105845780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156105a757600080fd5b838201915060208201858111156105bd57600080fd5b82518660018202830111640100000000821117156105da57600080fd5b8083526020830192505050908051906020019080838360005b8381101561060e5780820151818401526020810190506105f3565b50505050905090810190601f16801561063b5780820380516001836020036101000a031916815260200191505b5060405250505080925081935050506106568282888661096e565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60045481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610773576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180610b2b6029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556107b28383610a4f565b505050565b6000600454905090565b60026020528060005260406000206000915090505481565b60008061080b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660035485610296565b905060016006600083815260200190815260200160002060006101000a81548160ff02191690831515021790555080915050919050565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b600080828401905083811015610964576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085805190602001908083835b602083106109aa5780518252602082019150602081019050602083039250610987565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b602083106109fb57805182526020820191506020810190506020830392506109d8565b6001836020036101000a038019825116818451168082178552505050505050905001838152602001828152602001945050505050604051602081830303815290604052805190602001209050949350505050565b6006600083815260200190815260200160002060009054906101000a900460ff16610ae2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b8060048190555060045460056000848152602001908152602001600020819055506006600083815260200190815260200160002060006101000a81549060ff0219169055505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a2646970667358221220fb4892b3f06110cca9b84574d85fe18c252b5e481acfd794852e4a93ccd6eb1264736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50604051610c30380380610c308339818101604052604081101561003357600080fd5b81019080805190602001909291908051906020019092919050505081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806003819055505050610b89806100a76000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c80638db1363d1161005b5780638db1363d1461017b578063ae1ba0e6146101bd578063b791ee54146101f5578063f724a2791461023757610088565b8063299f7f9d1461008d578063365aab7a146100ab57806356ddd8ed1461011757806369dabf781461015d575b600080fd5b610095610279565b6040518082815260200191505060405180910390f35b610101600480360360608110156100c157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190505050610283565b6040518082815260200191505060405180910390f35b6101436004803603602081101561012d57600080fd5b81019080803590602001909291905050506106a3565b604051808215151515815260200191505060405180910390f35b6101656106cd565b6040518082815260200191505060405180910390f35b6101a76004803603602081101561019157600080fd5b81019080803590602001909291905050506106d3565b6040518082815260200191505060405180910390f35b6101f3600480360360408110156101d357600080fd5b8101908080359060200190929190803590602001909291905050506106eb565b005b6102216004803603602081101561020b57600080fd5b81019080803590602001909291905050506107e6565b6040518082815260200191505060405180910390f35b6102636004803603602081101561024d57600080fd5b8101908080359060200190929190505050610803565b6040518082815260200191505060405180910390f35b6000600454905090565b600083600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631290c07c8484306040518463ffffffff1660e01b8152600401808481526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b15801561037757600080fd5b505af115801561038b573d6000803e3d6000fd5b505050506040513d60208110156103a157600080fd5b81019080805190602001909291905050505060006103d4848430600260008981526020019081526020016000205461086c565b90506103fd600160026000878152602001908152602001600020546108e690919063ffffffff16565b6002600086815260200190815260200160002081905550606080600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b815260040160006040518083038186803b15801561047f57600080fd5b505afa158015610493573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156104bd57600080fd5b81019080805160405193929190846401000000008211156104dd57600080fd5b838201915060208201858111156104f357600080fd5b825186600182028301116401000000008211171561051057600080fd5b8083526020830192505050908051906020019080838360005b83811015610544578082015181840152602081019050610529565b50505050905090810190601f1680156105715780820380516001836020036101000a031916815260200191505b506040526020018051604051939291908464010000000082111561059457600080fd5b838201915060208201858111156105aa57600080fd5b82518660018202830111640100000000821117156105c757600080fd5b8083526020830192505050908051906020019080838360005b838110156105fb5780820151818401526020810190506105e0565b50505050905090810190601f1680156106285780820380516001836020036101000a031916815260200191505b5060405250505080925081935050506106438282888661096e565b93508660008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508393505050509392505050565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b60045481565b60026020528060005260406000206000915090505481565b8160008082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146107a2576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401808060200182810382526029815260200180610b2b6029913960400191505060405180910390fd5b60008082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556107e18383610a4f565b505050565b600060056000838152602001908152602001600020549050919050565b600080610835600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660035485610283565b905060016006600083815260200190815260200160002060006101000a81548160ff02191690831515021790555080915050919050565b600084848484604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019450505050506040516020818303038152906040528051906020012060001c9050949350505050565b600080828401905083811015610964576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b6000848484846040516020018085805190602001908083835b602083106109aa5780518252602082019150602081019050602083039250610987565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b602083106109fb57805182526020820191506020810190506020830392506109d8565b6001836020036101000a038019825116818451168082178552505050505050905001838152602001828152602001945050505050604051602081830303815290604052805190602001209050949350505050565b6006600083815260200190815260200160002060009054906101000a900460ff16610ae2576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b8060048190555060045460056000848152602001908152602001600020819055506006600083815260200190815260200160002060006101000a81549060ff0219169055505056fe536f75726365206d7573742062652074686520767266436f7265206f66207468652072657175657374a264697066735822122049c3b71736476fd77c7e1599c004b13a8f68a44a0b16ea44b8530fec4e8f488464736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCore\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]},{\"kind\":3,\"slot\":5,\"value\":[0]},{\"kind\":3,\"slot\":6,\"value\":[0]},{\"kind\":4,\"value\":[4]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"randomness\",\"type\":\"uint256\"}],\"name\":\"callbackRandomness\",\"outputs\":[],\"selector\":[1517660408,2921046246],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":6,\"value\":[0]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[3344918874,1457379565],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[4]}],\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[1833756220,698318749],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":5,\"value\":[0]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[771689741,3079794260],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"getRandomNumber\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"selector\":[3010598820,4146373241],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":2,\"value\":[0]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"name\":\"nonces\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[2654043922,2377201213],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[4]}],\"inputs\":[],\"name\":\"randomResult\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[1113694054,1775943544],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"_vrfCoreAddress\",\"type\":\"address\"},{\"internalType\":\"bytes32\",\"name\":\"_keyHash\",\"type\":\"bytes32\"},{\"internalType\":\"uint256\",\"name\":\"_seed\",\"type\":\"uint256\"}],\"name\":\"vrfQuery\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"}],\"selector\":[913191776,911911802],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CALLBACKRANDOMNESS = "callbackRandomness";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_GETRANDOMNUMBER = "getRandomNumber";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_RANDOMRESULT = "randomResult";

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

    public TransactionReceipt getRandomNumber(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getRandomNumber(BigInteger userProvidedSeed, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetRandomNumber(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(userProvidedSeed)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getGetRandomNumberInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public Tuple1<byte[]> getGetRandomNumberOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETRANDOMNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public BigInteger nonces(byte[] param0) throws ContractException {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger randomResult() throws ContractException {
        final Function function = new Function(FUNC_RANDOMRESULT, 
                Arrays.<Type>asList(), 
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
