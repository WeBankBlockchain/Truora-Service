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
public class APISampleOracleReturnString extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b02179055506040518060600160405280603e8152602001620013c3603e9139600890805190602001906200005e929190620000eb565b503480156200006c57600080fd5b506040516200140138038062001401833981810160405260208110156200009257600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506200019a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012e57805160ff19168380011785556200015f565b828001600101855582156200015f579182015b828111156200015e57825182559160200191906001019062000141565b5b5090506200016e919062000172565b5090565b6200019791905b808211156200019357600081600090555060010162000179565b5090565b90565b61121980620001aa6000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c8063653721471161007157806365372147146102785780636d4ce63c146102fb5780638ab4be9e1461037e578063a5298d5414610498578063c75f695a1461055d578063d6bd8727146105a3576100a9565b8063252498a2146100ae5780632d1a193d146101695780632dff0d0d14610195578063338cdca11461023c5780634b6022821461025a575b600080fd5b610167600480360360208110156100c457600080fd5b81019080803590602001906401000000008111156100e157600080fd5b8201836020820111156100f357600080fd5b8035906020019184600183028401116401000000008311171561011557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610626565b005b610171610640565b6040518082600281111561018157fe5b60ff16815260200191505060405180910390f35b6101c1600480360360208110156101ab57600080fd5b8101908080359060200190929190505050610653565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102015780820151818401526020810190506101e6565b50505050905090810190601f16801561022e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610244610708565b6040518082815260200191505060405180910390f35b61026261083d565b6040518082815260200191505060405180910390f35b610280610844565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102c05780820151818401526020810190506102a5565b50505050905090810190601f1680156102ed5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103036108e2565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610343578082015181840152602081019050610328565b50505050905090810190601f1680156103705780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104966004803603606081101561039457600080fd5b8101908080359060200190929190803590602001906401000000008111156103bb57600080fd5b8201836020820111156103cd57600080fd5b803590602001918460018302840111640100000000831117156103ef57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561045257600080fd5b82018360208201111561046457600080fd5b8035906020019184600183028401116401000000008311171561048657600080fd5b9091929391929390505050610984565b005b61055b600480360360408110156104ae57600080fd5b8101908080359060200190929190803590602001906401000000008111156104d557600080fd5b8201836020820111156104e757600080fd5b8035906020019184600183028401116401000000008311171561050957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610ab0565b005b6105896004803603602081101561057357600080fd5b8101908080359060200190929190505050610ba9565b604051808215151515815260200191505060405180910390f35b6105ab610bd3565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105eb5780820151818401526020810190506105d0565b50505050905090810190601f1680156106185780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806008908051906020019061063c929190611116565b5050565b600460009054906101000a900460ff1681565b6060600560008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106fc5780601f106106d1576101008083540402835291602001916106fc565b820191906000526020600020905b8154815290600101906020018083116106df57829003601f168201915b50505050509050919050565b60006001600460006101000a81548160ff0219169083600281111561072957fe5b02179055506000610808600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660088054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107ec5780601f106107c1576101008083540402835291602001916107ec565b820191906000526020600020905b8154815290600101906020018083116107cf57829003601f168201915b50505050506000600460009054906101000a900460ff16610c75565b905060016006600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b620927c081565b60078054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108da5780601f106108af576101008083540402835291602001916108da565b820191906000526020600020905b8154815290600101906020018083116108bd57829003601f168201915b505050505081565b606060078054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561097a5780601f1061094f5761010080835404028352916020019161097a565b820191906000526020600020905b81548152906001019060200180831161095d57829003601f168201915b5050505050905090565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610a3c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806111bc6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a2610aa98585610ab0565b5050505050565b6006600083815260200190815260200160002060009054906101000a900460ff16610b43576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b80600560008481526020019081526020016000209080519060200190610b6a929190611116565b506006600083815260200190815260200160002060006101000a81549060ff02191690558060079080519060200190610ba4929190611116565b505050565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b606060088054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c6b5780601f10610c4057610100808354040283529160200191610c6b565b820191906000526020600020905b815481529060010190602001808311610c4e57829003601f168201915b5050505050905090565b6000610cbf620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610cc9565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b8152600401604080518083038186803b158015610d7457600080fd5b505afa158015610d88573d6000803e3d6000fd5b505050506040513d6040811015610d9e57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffff","ffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550827f9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f6960405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610f0357fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610fa6578082015181840152602081019050610f8b565b50505050905090810190601f168015610fd35780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610ff857600080fd5b505af115801561100c573d6000803e3d6000fd5b505050506040513d602081101561102257600080fd5b81019080805190602001909291905050506110a5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061115757805160ff1916838001178555611185565b82800160010185558215611185579182015b82811115611184578251825591602001919060010190611169565b5b5090506111929190611196565b5090565b6111b891905b808211156111b457600081600090555060010161119c565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212204d7f738492fe5f6e527dbbdcee1cf227c57bef0634fac043a2e0d5c53f3e4dce64736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"_result\",\"type\":\"bytes\"}],\"name\":\"__callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b02179055506040518060600160405280603e8152602001620013c3603e9139600890805190602001906200005e929190620000eb565b503480156200006c57600080fd5b506040516200140138038062001401833981810160405260208110156200009257600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506200019a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012e57805160ff19168380011785556200015f565b828001600101855582156200015f579182015b828111156200015e57825182559160200191906001019062000141565b5b5090506200016e919062000172565b5090565b6200019791905b808211156200019357600081600090555060010162000179565b5090565b90565b61121980620001aa6000396000f3fe608060405234801561001057600080fd5b50600436106100a95760003560e01c806356ddd8ed1161007157806356ddd8ed1461044e578063a06be5f714610494578063a9a02827146104b2578063b791ee5414610535578063df71c15f146105dc578063e97aecc314610608576100a9565b806310d64222146100ae57806322949cdb146101c8578063299f7f9d1461028d5780633fdc9181146103105780634f540798146103cb575b600080fd5b6101c6600480360360608110156100c457600080fd5b8101908080359060200190929190803590602001906401000000008111156100eb57600080fd5b8201836020820111156100fd57600080fd5b8035906020019184600183028401116401000000008311171561011f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561018257600080fd5b82018360208201111561019457600080fd5b803590602001918460018302840111640100000000831117156101b657600080fd5b9091929391929390505050610626565b005b61028b600480360360408110156101de57600080fd5b81019080803590602001909291908035906020019064010000000081111561020557600080fd5b82018360208201111561021757600080fd5b8035906020019184600183028401116401000000008311171561023957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610752565b005b61029561084b565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102d55780820151818401526020810190506102ba565b50505050905090810190601f1680156103025780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103c96004803603602081101561032657600080fd5b810190808035906020019064010000000081111561034357600080fd5b82018360208201111561035557600080fd5b8035906020019184600183028401116401000000008311171561037757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506108ed565b005b6103d3610907565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104135780820151818401526020810190506103f8565b50505050905090810190601f1680156104405780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61047a6004803603602081101561046457600080fd5b81019080803590602001909291905050506109a5565b604051808215151515815260200191505060405180910390f35b61049c6109cf565b6040518082815260200191505060405180910390f35b6104ba6109d6565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104fa5780820151818401526020810190506104df565b50505050905090810190601f1680156105275780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6105616004803603602081101561054b57600080fd5b8101908080359060200190929190505050610a78565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105a1578082015181840152602081019050610586565b50505050905090810190601f1680156105ce5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6105e4610b2d565b604051808260028111156105f457fe5b60ff16815260200191505060405180910390f35b610610610b40565b6040518082815260200191505060405180910390f35b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146106de576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806111bc6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a261074b8585610752565b5050505050565b6006600083815260200190815260200160002060009054906101000a900460ff166107e5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b8060056000848152602001908152602001600020908051906020019061080c929190611116565b506006600083815260200190815260200160002060006101000a81549060ff02191690558060079080519060200190610846929190611116565b505050565b606060078054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108e35780601f106108b8576101008083540402835291602001916108e3565b820191906000526020600020905b8154815290600101906020018083116108c657829003601f168201915b5050505050905090565b8060089080519060200190610903929190611116565b5050565b60078054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561099d5780601f106109725761010080835404028352916020019161099d565b820191906000526020600020905b81548152906001019060200180831161098057829003601f168201915b505050505081565b60006006600083815260200190815260200160002060009054906101000a900460ff169050919050565b620927c081565b606060088054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a6e5780601f10610a4357610100808354040283529160200191610a6e565b820191906000526020600020905b815481529060010190602001808311610a5157829003601f168201915b5050505050905090565b6060600560008381526020019081526020016000208054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b215780601f10610af657610100808354040283529160200191610b21565b820191906000526020600020905b815481529060010190602001808311610b0457829003601f168201915b50505050509050919050565b600460009054906101000a900460ff1681565b60006001600460006101000a81548160ff02191690836002811115610b6157fe5b02179055506000610c40600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660088054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c245780601f10610bf957610100808354040283529160200191610c24565b820191906000526020600020905b815481529060010190602001808311610c0757829003601f168201915b50505050506000600460009054906101000a900460ff16610c75565b905060016006600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b6000610cbf620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610cc9565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b8152600401604080518083038186803b158015610d7457600080fd5b505afa158015610d88573d6000803e3d6000fd5b505050506040513d6040811015610d9e57600080fd5b8101908080519060200190929190805190602001909291905050508092508193505050818130600154604051602001808581526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffff","ffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550827f4b37035b2c306439a8084687e88341d3a62264044588063d535c6a83d952846360405160405180910390a26000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634479bc11306001548a8a8f8b8b6002811115610f0357fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610fa6578082015181840152602081019050610f8b565b50505050905090810190601f168015610fd35780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610ff857600080fd5b505af115801561100c573d6000803e3d6000fd5b505050506040513d602081101561102257600080fd5b81019080805190602001909291905050506110a5576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061115757805160ff1916838001178555611185565b82800160010185558215611185579182015b82811115611184578251825591602001919060010190611169565b5b5090506111929190611196565b5090565b6111b891905b808211156111b457600081600090555060010161119c565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a2646970667358221220501086abee3165ea287234ec094827295bcf7eccb853e55ececaab14289bb56f64736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

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
    protected APISampleOracleReturnString(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected APISampleOracleReturnString(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected APISampleOracleReturnString(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected APISampleOracleReturnString(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteCall<String> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getById(byte[] id) {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteCall<String> result() {
        final Function function = new Function(FUNC_RESULT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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
    public static APISampleOracleReturnString load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new APISampleOracleReturnString(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static APISampleOracleReturnString load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new APISampleOracleReturnString(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static APISampleOracleReturnString load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new APISampleOracleReturnString(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static APISampleOracleReturnString load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new APISampleOracleReturnString(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<APISampleOracleReturnString> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracleReturnString.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<APISampleOracleReturnString> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracleReturnString.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<APISampleOracleReturnString> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracleReturnString.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<APISampleOracleReturnString> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String oracleAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)));
        return deployRemoteCall(APISampleOracleReturnString.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
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
