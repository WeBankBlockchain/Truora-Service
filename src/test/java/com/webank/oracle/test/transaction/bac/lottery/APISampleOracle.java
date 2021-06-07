package com.webank.oracle.test.transaction.bac.lottery;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.DynamicBytes;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint8;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
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
 * <p>Generated with web3j version null.
 */
@SuppressWarnings("unchecked")
public class APISampleOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff0219169083600281111561002757fe5b0217905550670de0b6b3a76400006005556040518060600160405280603e815260200162000fe4603e9139600990805190602001906100679291906100f1565b5034801561007457600080fd5b5060405162001022380380620010228339818101604052602081101561009957600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610196565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013257805160ff1916838001178555610160565b82800160010185558215610160579182015b8281111561015f578251825591602001919060010190610144565b5b50905061016d9190610171565b5090565b61019391905b8082111561018f576000816000905550600101610177565b5090565b90565b610e3e80620001a66000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063653721471161006657806365372147146102085780636d4ce63c146102265780638ab4be9e14610244578063c75f695a1461035e578063d6bd8727146103a45761009e565b8063252498a2146100a35780632d1a193d1461015e5780632dff0d0d1461018a578063338cdca1146101cc5780634b602282146101ea575b600080fd5b61015c600480360360208110156100b957600080fd5b81019080803590602001906401000000008111156100d657600080fd5b8201836020820111156100e857600080fd5b8035906020019184600183028401116401000000008311171561010a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610427565b005b610166610441565b6040518082600281111561017657fe5b60ff16815260200191505060405180910390f35b6101b6600480360360208110156101a057600080fd5b8101908080359060200190929190505050610454565b6040518082815260200191505060405180910390f35b6101d4610471565b6040518082815260200191505060405180910390f35b6101f2610582565b6040518082815260200191505060405180910390f35b610210610589565b6040518082815260200191505060405180910390f35b61022e61058f565b6040518082815260200191505060405180910390f35b61035c6004803603606081101561025a57600080fd5b81019080803590602001909291908035906020019064010000000081111561028157600080fd5b82018360208201111561029357600080fd5b803590602001918460018302840111640100000000831117156102b557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561031857600080fd5b82018360208201111561032a57600080fd5b8035906020019184600183028401116401000000008311171561034c57600080fd5b9091929391929390505050610599565b005b61038a6004803603602081101561037457600080fd5b81019080803590602001909291905050506106c5565b604051808215151515815260200191505060405180910390f35b6103ac6106ef565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103ec5780820151818401526020810190506103d1565b50505050905090810190601f1680156104195780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806009908051906020019061043d929190610d3b565b5050565b600460009054906101000a900460ff1681565b600060066000838152602001908152602001600020549050919050565b60008061054d600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105305780601f1061050557610100808354040283529160200191610530565b820191906000526020600020905b81548152906001019060200180831161051357829003601f168201915b5050505050600554600460009054906101000a900460ff16610791565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b620927c081565b60085481565b6000600854905090565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610651576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180610de16028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26106be85856107e5565b5050505050565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107875780601f1061075c57610100808354040283529160200191610787565b820191906000526020600020905b81548152906001019060200180831161076a57829003601f168201915b5050505050905090565b60006107db620927c06040518060400160405280600381526020017f75726c00000000000000000000000000000000000000000000000000000000008152508787876000886108cb565b9050949350505050565b6007600083815260200190815260200160002060009054906101000a900460ff16610878576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b61088181610d18565b60001c60088190555060085460066000848152602001908152602001600020819055506007600083815260200190815260200160002060006101000a81549060ff02191690555050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b8152600401604080518083038186803b15801561097657600080fd5b505afa15801561098a573d6000803e3d6000fd5b505050506040513d60408110156109a057600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550827f9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f6960405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610b0557fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610ba8578082015181840152602081019050610b8d565b50505050905090810190601f168015610bd55780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610bfa57600080fd5b505af1158015610c0e573d6000803e3d6000fd5b505050506040513d6020811015610c2457600080fd5b8101908080519060200190929190505050610ca7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b60008082511415610d2e576000801b9050610d36565b602082015190505b919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610d7c57805160ff1916838001178555610daa565b82800160010185558215610daa579182015b82811115610da9578251825591602001919060010190610d8e565b5b509050610db79190610dbb565b5090565b610ddd91905b80821115610dd9576000816000905550600101610dc1565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212204fe159aa4dd3c0acc8ef8338b6ea7fb7090b4b30c9f86da9e557baba3222bf7864736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174","652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"oracleAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"name\":null,\"outputs\":null,\"type\":\"constructor\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":true}],\"name\":\"Fulfilled\",\"outputs\":null,\"type\":\"event\",\"payable\":false,\"stateMutability\":null},{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":true}],\"name\":\"Requested\",\"outputs\":null,\"type\":\"event\",\"payable\":false,\"stateMutability\":null},{\"constant\":true,\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"requestId\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false},{\"name\":\"result\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false},{\"name\":\"proof\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"callback\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"name\":\"getById\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"name\":\"\",\"type\":\"string\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"_url\",\"type\":\"string\",\"type0\":null,\"indexed\":false}],\"name\":\"setUrl\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_EXPIRY_TIME = "EXPIRY_TIME";

    public static final String FUNC_CALLBACK = "callback";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_GETURL = "getUrl";

    public static final String FUNC_REQUEST = "request";

    public static final String FUNC_RESULT = "result";

    public static final String FUNC_RETURNTYPE = "returnType";

    public static final String FUNC_SETURL = "setUrl";

    public static final Event FULFILLED_EVENT = new Event("Fulfilled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event REQUESTED_EVENT = new Event("Requested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    @Deprecated
    protected APISampleOracle(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected APISampleOracle(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected APISampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected APISampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public List<FulfilledEventResponse> getFulfilledEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(FULFILLED_EVENT, transactionReceipt);
        ArrayList<FulfilledEventResponse> responses = new ArrayList<FulfilledEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FulfilledEventResponse typedResponse = new FulfilledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerFulfilledEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerFulfilledEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<RequestedEventResponse> getRequestedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REQUESTED_EVENT, transactionReceipt);
        ArrayList<RequestedEventResponse> responses = new ArrayList<RequestedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RequestedEventResponse typedResponse = new RequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerRequestedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerRequestedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<BigInteger> EXPIRY_TIME() {
        final Function function = new Function(FUNC_EXPIRY_TIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> callback(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new DynamicBytes(result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void callback(byte[] requestId, byte[] result, byte[] proof, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new DynamicBytes(result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String callbackSeq(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new DynamicBytes(result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<byte[], byte[], byte[]> getCallbackInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CALLBACK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<byte[], byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue(), 
                (byte[]) results.get(2).getValue()
                );
    }

    public RemoteCall<Boolean> checkIdFulfilled(byte[] id) {
        final Function function = new Function(FUNC_CHECKIDFULFILLED, 
                Arrays.<Type>asList(new Bytes32(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getById(byte[] id) {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new Bytes32(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getUrl() {
        final Function function = new Function(FUNC_GETURL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> request() {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void request(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String requestSeq() {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<byte[]> getRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public RemoteCall<BigInteger> result() {
        final Function function = new Function(FUNC_RESULT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> returnType() {
        final Function function = new Function(FUNC_RETURNTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setUrl(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new Utf8String(_url)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void setUrl(String _url, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new Utf8String(_url)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String setUrlSeq(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new Utf8String(_url)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getSetUrlInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETURL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    @Deprecated
    public static APISampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new APISampleOracle(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static APISampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new APISampleOracle(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static APISampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new APISampleOracle(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static APISampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new APISampleOracle(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<APISampleOracle> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracle.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<APISampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracle.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<APISampleOracle> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracle.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<APISampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracle.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class FulfilledEventResponse {
        public Log log;

        public byte[] id;
    }

    public static class RequestedEventResponse {
        public Log log;

        public byte[] id;
    }
}
