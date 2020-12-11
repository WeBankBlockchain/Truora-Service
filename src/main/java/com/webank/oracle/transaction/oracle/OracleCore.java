package com.webank.oracle.transaction.oracle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.DynamicBytes;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple5;
import org.fisco.bcos.web3j.tuples.generated.Tuple6;
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
public class OracleCore extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405260405180807f5f5f63616c6c6261636b28627974657333322c696e7432353629000000000000815250601a0190506040518091039020600360006101000a81548163ffffffff021916908360e01c021790555034801561006357600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3610c388061012f6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80630825b88e1461005c57806350538832146101375780638da5cb5b146102065780638f32d59b14610250578063f2fde38b14610272575b600080fd5b61011d600480360360c081101561007257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156100b957600080fd5b8201836020820111156100cb57600080fd5b803590602001918460018302840111640100000000831117156100ed57600080fd5b909192939192939080359060200190929190803590602001909291908035151590602001909291905050506102b6565b604051808215151515815260200191505060405180910390f35b6101ec600480360360a081101561014d57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190803590602001906401000000008111156101a857600080fd5b8201836020820111156101ba57600080fd5b803590602001918460018302840111640100000000831117156101dc57600080fd5b9091929391929390505050610514565b604051808215151515815260200191505060405180910390f35b61020e61090a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610258610933565b604051808215151515815260200191505060405180910390f35b6102b46004803603602081101561028857600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061098a565b005b6000808888604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012090506000801b6001600083815260200190815260200160002054146103a4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f4d75737420757365206120756e6971756520494400000000000000000000000081525060200191505060405180910390fd5b60006103b98542610a1090919063ffffffff16565b90508060026000848152602001908152602001600020819055508981604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012060016000848152602001908152602001600020819055507f8324f553b5f7e81db3b85c70c88ae63fcb9e00a9d6893af0872149a70cc121478a838a8a858b8a604051808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200187815260200180602001858152602001848152602001831515151581526020018281038252878782818152602001925080828437600081840152601f19601f8201169050808301925050509850505050505050505060405180910390a1600192505050979650505050505050565b600061051e610933565b610590576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b866000801b6001600083815260200190815260200160002054141561061d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4d757374206861766520612076616c696420726571756573744964000000000081525060200191505060405180910390fd5b426002600083815260200190815260200160002054116106a5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c66696c6c20726571756573742074696d65206f7574000000000000000081525060200191505060405180910390fd5b60008787604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018281526020019250505060405160208183030381529060405280519060200120905080600160008b8152602001908152602001600020541461078f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f506172616d7320646f206e6f74206d617463682072657175657374204944000081525060200191505060405180910390fd5b600160008a815260200190815260200160002060009055600260008a81526020019081526020016000206000905560008873ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900460e01b8b896040516024018083815260200182815260200192505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b6020831061088f578051825260208201915060208101905060208303925061086c565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146108f1576040519150601f19603f3d011682016040523d82523d6000602084013e6108f6565b606091505b505090508093505050509695505050505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b610992610933565b610a04576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b610a0d81610a98565b50565b600080828401905083811015610a8e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610b1e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180610bdd6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373a264697066735822122023bd32b3e60c05ee2100adfa9a4e1da9aaecdb92100302a866416fc3d4fbaa8f64736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"callbackAddr\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"url\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"expiration\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"timesAmount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"bool\",\"name\":\"needProof\",\"type\":\"bool\"}],\"name\":\"OracleRequest\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_requestId\",\"type\":\"bytes32\"},{\"internalType\":\"address\",\"name\":\"_callbackAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_expiration\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_result\",\"type\":\"uint256\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"fulfillRequest\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"isOwner\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"_callbackAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_nonce\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"_timesAmount\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_expiryTime\",\"type\":\"uint256\"},{\"internalType\":\"bool\",\"name\":\"_needProof\",\"type\":\"bool\"}],\"name\":\"query\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405260405180807f5f5f63616c6c6261636b28627974657333322c696e7432353629000000000000815250601a0190506040518091039020600360006101000a81548163ffffffff021916908360e01c021790555034801561006357600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a3610c388061012f6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c806316cad12a1461005c5780631a165b80146100a05780635089e2c81461016f578063ede8e529146101b9578063f3c3f0f1146101db575b600080fd5b61009e6004803603602081101561007257600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102b6565b005b610155600480360360a08110156100b657600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001909291908035906020019064010000000081111561011157600080fd5b82018360208201111561012357600080fd5b8035906020019184600183028401116401000000008311171561014557600080fd5b909192939192939050505061033c565b604051808215151515815260200191505060405180910390f35b610177610732565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101c161075b565b604051808215151515815260200191505060405180910390f35b61029c600480360360c08110156101f157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561023857600080fd5b82018360208201111561024a57600080fd5b8035906020019184600183028401116401000000008311171561026c57600080fd5b909192939192939080359060200190929190803590602001909291908035151590602001909291905050506107b2565b604051808215151515815260200191505060405180910390f35b6102be61075b565b610330576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b61033981610a10565b50565b600061034661075b565b6103b8576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b866000801b60016000838152602001908152602001600020541415610445576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4d757374206861766520612076616c696420726571756573744964000000000081525060200191505060405180910390fd5b426002600083815260200190815260200160002054116104cd576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c66696c6c20726571756573742074696d65206f7574000000000000000081525060200191505060405180910390fd5b60008787604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018281526020019250505060405160208183030381529060405280519060200120905080600160008b815260200190815260200160002054146105b7576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f506172616d7320646f206e6f74206d617463682072657175657374204944000081525060200191505060405180910390fd5b600160008a815260200190815260200160002060009055600260008a81526020019081526020016000206000905560008873ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900460e01b8b896040516024018083815260200182815260200192505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b602083106106b75780518252602082019150602081019050602083039250610694565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610719576040519150601f19603f3d011682016040523d82523d6000602084013e61071e565b606091505b505090508093505050509695505050505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b6000808888604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012090506000801b6001600083815260200190815260200160002054146108a0576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f4d75737420757365206120756e6971756520494400000000000000000000000081525060200191505060405180910390fd5b60006108b58542610b5490919063ffffffff16565b90508060026000848152602001908152602001600020819055508981604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012060016000848152602001908152602001600020819055507f050cd4f296ef9aea11a3a20de03cba424250f2490a0e13dbeae05ecb0dbc61328a838a8a858b8a604051808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200187815260200180602001858152602001848152602001831515151581526020018281038252878782818152602001925080828437600081840152601f19601f8201169050808301925050509850505050505050505060405180910390a1600192505050979650505050505050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610a96576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180610bdd6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600080828401905083811015610bd2576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b809150509291505056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373a264697066735822122020bdf906d5b0c52ffb451a765b773dab51d72a4c2530aae8e4fa377668fd843564736f6c634300060a0033"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_FULFILLREQUEST = "fulfillRequest";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_QUERY = "query";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event ORACLEREQUEST_EVENT = new Event("OracleRequest", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected OracleCore(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected OracleCore(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected OracleCore(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected OracleCore(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public List<OracleRequestEventResponse> getOracleRequestEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ORACLEREQUEST_EVENT, transactionReceipt);
        ArrayList<OracleRequestEventResponse> responses = new ArrayList<OracleRequestEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OracleRequestEventResponse typedResponse = new OracleRequestEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.callbackAddr = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.requestId = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.url = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.expiration = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timesAmount = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.needProof = (Boolean) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerOracleRequestEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ORACLEREQUEST_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerOracleRequestEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ORACLEREQUEST_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerOwnershipTransferredEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerOwnershipTransferredEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<TransactionReceipt> fulfillRequest(byte[] _requestId, String _callbackAddress, BigInteger _expiration, BigInteger _result, byte[] proof) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST,
                Arrays.<Type>asList(new Bytes32(_requestId),
                new Address(_callbackAddress),
                new Uint256(_expiration),
                new Uint256(_result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void fulfillRequest(byte[] _requestId, String _callbackAddress, BigInteger _expiration, BigInteger _result, byte[] proof, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST,
                Arrays.<Type>asList(new Bytes32(_requestId),
                new Address(_callbackAddress),
                new Uint256(_expiration),
                new Uint256(_result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String fulfillRequestSeq(byte[] _requestId, String _callbackAddress, BigInteger _expiration, BigInteger _result, byte[] proof) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST,
                Arrays.<Type>asList(new Bytes32(_requestId),
                new Address(_callbackAddress),
                new Uint256(_expiration),
                new Uint256(_result),
                new DynamicBytes(proof)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple5<byte[], String, BigInteger, BigInteger, byte[]> getFulfillRequestInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_FULFILLREQUEST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple5<byte[], String, BigInteger, BigInteger, byte[]>(

                (byte[]) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (byte[]) results.get(4).getValue()
                );
    }

    public Tuple1<Boolean> getFulfillRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_FULFILLREQUEST,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<Boolean> isOwner() {
        final Function function = new Function(FUNC_ISOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> query(String _callbackAddress, BigInteger _nonce, String _url, BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof) {
        final Function function = new Function(
                FUNC_QUERY,
                Arrays.<Type>asList(new Address(_callbackAddress),
                new Uint256(_nonce),
                new Utf8String(_url),
                new Uint256(_timesAmount),
                new Uint256(_expiryTime),
                new Bool(_needProof)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void query(String _callbackAddress, BigInteger _nonce, String _url, BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_QUERY,
                Arrays.<Type>asList(new Address(_callbackAddress),
                new Uint256(_nonce),
                new Utf8String(_url),
                new Uint256(_timesAmount),
                new Uint256(_expiryTime),
                new Bool(_needProof)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String querySeq(String _callbackAddress, BigInteger _nonce, String _url, BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof) {
        final Function function = new Function(
                FUNC_QUERY,
                Arrays.<Type>asList(new Address(_callbackAddress),
                new Uint256(_nonce),
                new Utf8String(_url),
                new Uint256(_timesAmount),
                new Uint256(_expiryTime),
                new Bool(_needProof)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean> getQueryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_QUERY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean>(

                (String) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (Boolean) results.get(5).getValue()
                );
    }

    public Tuple1<Boolean> getQueryOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_QUERY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void transferOwnership(String newOwner, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String transferOwnershipSeq(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getTransferOwnershipInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    @Deprecated
    public static OracleCore load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OracleCore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static OracleCore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OracleCore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static OracleCore load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new OracleCore(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static OracleCore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new OracleCore(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<OracleCore> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OracleCore.class, web3j, credentials, contractGasProvider, getBinary(), "");
    }

    public static RemoteCall<OracleCore> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OracleCore.class, web3j, transactionManager, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<OracleCore> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OracleCore.class, web3j, credentials, gasPrice, gasLimit, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<OracleCore> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OracleCore.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), "");
    }

    public static class OracleRequestEventResponse {
        public Log log;

        public String callbackAddr;

        public byte[] requestId;

        public String url;

        public BigInteger expiration;

        public BigInteger timesAmount;

        public Boolean needProof;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
