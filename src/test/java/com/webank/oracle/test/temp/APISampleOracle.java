package com.webank.oracle.test.temp;

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
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
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
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class APISampleOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff0219169083600281111561002757fe5b0217905550670de0b6b3a76400006005556040518060600160405280603e815260200162000ffe603e9139600990805190602001906100679291906100f1565b5034801561007457600080fd5b506040516200103c3803806200103c8339818101604052602081101561009957600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610196565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013257805160ff1916838001178555610160565b82800160010185558215610160579182015b8281111561015f578251825591602001919060010190610144565b5b50905061016d9190610171565b5090565b61019391905b8082111561018f576000816000905550600101610177565b5090565b90565b610e5880620001a66000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c8063653721471161007157806365372147146102135780636d4ce63c146102315780638ab4be9e1461024f578063a5298d5414610369578063c75f695a1461042e578063d6bd872714610474576100a9565b8063252498a2146100ae5780632d1a193d146101695780632dff0d0d14610195578063338cdca1146101d75780634b602282146101f5575b600080fd5b610167600480360360208110156100c457600080fd5b81019080803590602001906401000000008111156100e157600080fd5b8201836020820111156100f357600080fd5b8035906020019184600183028401116401000000008311171561011557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506104f7565b005b610171610511565b6040518082600281111561018157fe5b60ff16815260200191505060405180910390f35b6101c1600480360360208110156101ab57600080fd5b8101908080359060200190929190505050610524565b6040518082815260200191505060405180910390f35b6101df610541565b6040518082815260200191505060405180910390f35b6101fd610652565b6040518082815260200191505060405180910390f35b61021b610659565b6040518082815260200191505060405180910390f35b61023961065f565b6040518082815260200191505060405180910390f35b6103676004803603606081101561026557600080fd5b81019080803590602001909291908035906020019064010000000081111561028c57600080fd5b82018360208201111561029e57600080fd5b803590602001918460018302840111640100000000831117156102c057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561032357600080fd5b82018360208201111561033557600080fd5b8035906020019184600183028401116401000000008311171561035757600080fd5b9091929391929390505050610669565b005b61042c6004803603604081101561037f57600080fd5b8101908080359060200190929190803590602001906401000000008111156103a657600080fd5b8201836020820111156103b857600080fd5b803590602001918460018302840111640100000000831117156103da57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610795565b005b61045a6004803603602081101561044457600080fd5b81019080803590602001909291905050506107c5565b604051808215151515815260200191505060405180910390f35b61047c6107ef565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104bc5780820151818401526020810190506104a1565b50505050905090810190601f1680156104e95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806009908051906020019061050d929190610d55565b5050565b600460009054906101000a900460ff1681565b600060066000838152602001908152602001600020549050919050565b60008061061d600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106005780601f106105d557610100808354040283529160200191610600565b820191906000526020600020905b8154815290600101906020018083116105e357829003601f168201915b5050505050600554600460009054906101000a900460ff16610891565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b620927c081565b60085481565b6000600854905090565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610721576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180610dfb6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a261078e8585610795565b5050505050565b61079e816108e5565b60001c60088190555060085460066000848152602001908152602001600020819055505050565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108875780601f1061085c57610100808354040283529160200191610887565b820191906000526020600020905b81548152906001019060200180831161086a57829003601f168201915b5050505050905090565b60006108db620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610908565b9050949350505050565b600080825114156108fb576000801b9050610903565b602082015190505b919050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b8152600401604080518083038186803b1580156109b357600080fd5b505afa1580156109c7573d6000803e3d6000fd5b505050506040513d60408110156109dd57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550827f9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f6960405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610b4257fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610be5578082015181840152602081019050610bca565b50505050905090810190601f168015610c125780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610c3757600080fd5b505af1158015610c4b573d6000803e3d6000fd5b505050506040513d6020811015610c6157600080fd5b8101908080519060200190929190505050610ce4576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610d9657805160ff1916838001178555610dc4565b82800160010185558215610dc4579182015b82811115610dc3578251825591602001919060010190610da8565b5b509050610dd19190610dd5565b5090565b610df791905b80821115610df3576000816000905550600101610ddb565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a264697066735822122039f4d825d297410d89045a1e8164a5c3cf6318d8ffa77eb79221ad56cb2aee5e64736f6c634300060a00336a73","6f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"_result\",\"type\":\"bytes\"}],\"name\":\"__callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff0219169083600281111561002757fe5b0217905550670de0b6b3a76400006005556040518060600160405280603e815260200162000ffe603e9139600990805190602001906100679291906100f1565b5034801561007457600080fd5b506040516200103c3803806200103c8339818101604052602081101561009957600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610196565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013257805160ff1916838001178555610160565b82800160010185558215610160579182015b8281111561015f578251825591602001919060010190610144565b5b50905061016d9190610171565b5090565b61019391905b8082111561018f576000816000905550600101610177565b5090565b90565b610e5880620001a66000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c806356ddd8ed1161007157806356ddd8ed14610384578063a06be5f7146103ca578063a9a02827146103e8578063b791ee541461046b578063df71c15f146104ad578063e97aecc3146104d9576100a9565b806310d64222146100ae57806322949cdb146101c8578063299f7f9d1461028d5780633fdc9181146102ab5780634f54079814610366575b600080fd5b6101c6600480360360608110156100c457600080fd5b8101908080359060200190929190803590602001906401000000008111156100eb57600080fd5b8201836020820111156100fd57600080fd5b8035906020019184600183028401116401000000008311171561011f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561018257600080fd5b82018360208201111561019457600080fd5b803590602001918460018302840111640100000000831117156101b657600080fd5b90919293919293905050506104f7565b005b61028b600480360360408110156101de57600080fd5b81019080803590602001909291908035906020019064010000000081111561020557600080fd5b82018360208201111561021757600080fd5b8035906020019184600183028401116401000000008311171561023957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610623565b005b610295610653565b6040518082815260200191505060405180910390f35b610364600480360360208110156102c157600080fd5b81019080803590602001906401000000008111156102de57600080fd5b8201836020820111156102f057600080fd5b8035906020019184600183028401116401000000008311171561031257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061065d565b005b61036e610677565b6040518082815260200191505060405180910390f35b6103b06004803603602081101561039a57600080fd5b810190808035906020019092919050505061067d565b604051808215151515815260200191505060405180910390f35b6103d26106a7565b6040518082815260200191505060405180910390f35b6103f06106ae565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610430578082015181840152602081019050610415565b50505050905090810190601f16801561045d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104976004803603602081101561048157600080fd5b8101908080359060200190929190505050610750565b6040518082815260200191505060405180910390f35b6104b561076d565b604051808260028111156104c557fe5b60ff16815260200191505060405180910390f35b6104e1610780565b6040518082815260200191505060405180910390f35b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146105af576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180610dfb6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a261061c8585610623565b5050505050565b61062c81610891565b60001c60088190555060085460066000848152602001908152602001600020819055505050565b6000600854905090565b8060099080519060200190610673929190610d55565b5050565b60085481565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b620927c081565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107465780601f1061071b57610100808354040283529160200191610746565b820191906000526020600020905b81548152906001019060200180831161072957829003601f168201915b5050505050905090565b600060066000838152602001908152602001600020549050919050565b600460009054906101000a900460ff1681565b60008061085c600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561083f5780601f106108145761010080835404028352916020019161083f565b820191906000526020600020905b81548152906001019060200180831161082257829003601f168201915b5050505050600554600460009054906101000a900460ff166108b4565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b600080825114156108a7576000801b90506108af565b602082015190505b919050565b60006108fe620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610908565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b8152600401604080518083038186803b1580156109b357600080fd5b505afa1580156109c7573d6000803e3d6000fd5b505050506040513d60408110156109dd57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550827f4b37035b2c306439a8084687e88341d3a62264044588063d535c6a83d952846360405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634479bc11306001548a8a8f8b8b6002811115610b4257fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610be5578082015181840152602081019050610bca565b50505050905090810190601f168015610c125780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610c3757600080fd5b505af1158015610c4b573d6000803e3d6000fd5b505050506040513d6020811015610c6157600080fd5b8101908080519060200190929190505050610ce4576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610d9657805160ff1916838001178555610dc4565b82800160010185558215610dc4579182015b82811115610dc3578251825591602001919060010190610da8565b5b509050610dd19190610dd5565b5090565b610df791905b80821115610df3576000816000905550600101610ddb565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212205e1dbb6023d11b8d7b2c40d9f679e53f24c2e235388409a8c484b66ff3a7e08564736f6c634300060a00336a73","6f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_EXPIRY_TIME = "EXPIRY_TIME";

    public static final String FUNC___CALLBACK = "__callback";

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
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public List<FulfilledEventResponse> getFulfilledEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FULFILLED_EVENT, transactionReceipt);
        ArrayList<FulfilledEventResponse> responses = new ArrayList<FulfilledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REQUESTED_EVENT, transactionReceipt);
        ArrayList<RequestedEventResponse> responses = new ArrayList<RequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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

    public RemoteCall<TransactionReceipt> __callback(byte[] _requestId, byte[] _result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void __callback(byte[] _requestId, byte[] _result, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(_result)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String __callbackSeq(byte[] _requestId, byte[] _result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], byte[]> get__callbackInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC___CALLBACK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> callback(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void callback(byte[] requestId, byte[] result, byte[] proof, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String callbackSeq(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(proof)), 
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
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(id)), 
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
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(id)), 
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
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void setUrl(String _url, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String setUrlSeq(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
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
