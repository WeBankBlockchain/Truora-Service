package com.webank.truora.contract.bcos3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Int256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class APISampleOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b0217905550670de0b6b3a76400006005556040518060600160405280603e8152602001620012a4603e9139600990805190602001906200006a929190620000f7565b503480156200007857600080fd5b50604051620012e2380380620012e2833981810160405260208110156200009e57600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050620001a6565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200013a57805160ff19168380011785556200016b565b828001600101855582156200016b579182015b828111156200016a5782518255916020019190600101906200014d565b5b5090506200017a91906200017e565b5090565b620001a391905b808211156200019f57600081600090555060010162000185565b5090565b90565b6110ee80620001b66000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063653721471161006657806365372147146102085780636d4ce63c146102265780638ab4be9e14610244578063c75f695a1461035e578063d6bd8727146103a45761009e565b8063252498a2146100a35780632d1a193d1461015e5780632dff0d0d1461018a578063338cdca1146101cc5780634b602282146101ea575b600080fd5b61015c600480360360208110156100b957600080fd5b81019080803590602001906401000000008111156100d657600080fd5b8201836020820111156100e857600080fd5b8035906020019184600183028401116401000000008311171561010a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610427565b005b610166610441565b6040518082600281111561017657fe5b60ff16815260200191505060405180910390f35b6101b6600480360360208110156101a057600080fd5b8101908080359060200190929190505050610454565b6040518082815260200191505060405180910390f35b6101d4610471565b6040518082815260200191505060405180910390f35b6101f2610582565b6040518082815260200191505060405180910390f35b610210610589565b6040518082815260200191505060405180910390f35b61022e61058f565b6040518082815260200191505060405180910390f35b61035c6004803603606081101561025a57600080fd5b81019080803590602001909291908035906020019064010000000081111561028157600080fd5b82018360208201111561029357600080fd5b803590602001918460018302840111640100000000831117156102b557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561031857600080fd5b82018360208201111561032a57600080fd5b8035906020019184600183028401116401000000008311171561034c57600080fd5b9091929391929390505050610599565b005b61038a6004803603602081101561037457600080fd5b81019080803590602001909291905050506106c5565b604051808215151515815260200191505060405180910390f35b6103ac6106ef565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103ec5780820151818401526020810190506103d1565b50505050905090810190601f1680156104195780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b806009908051906020019061043d929190610feb565b5050565b600460009054906101000a900460ff1681565b600060066000838152602001908152602001600020549050919050565b60008061054d600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105305780601f1061050557610100808354040283529160200191610530565b820191906000526020600020905b81548152906001019060200180831161051357829003601f168201915b5050505050600554600460009054906101000a900460ff16610791565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b620927c081565b60085481565b6000600854905090565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610651576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806110916028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26106be85856107e5565b5050505050565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107875780601f1061075c57610100808354040283529160200191610787565b820191906000526020600020905b81548152906001019060200180831161076a57829003601f168201915b5050505050905090565b60006107db620927c06040518060400160405280600381526020017f75726c00000000000000000000000000000000000000000000000000000000008152508787876000886108cb565b9050949350505050565b6007600083815260200190815260200160002060009054906101000a900460ff16610878576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b61088181610fc8565b60001c60088190555060085460066000848152602001908152602001600020819055506007600083815260200190815260200160002060006101000a81549060ff02191690555050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b815260040160006040518083038186803b15801561097757600080fd5b505afa15801561098b573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156109b557600080fd5b81019080805160405193929190846401000000008211156109d557600080fd5b838201915060208201858111156109eb57600080fd5b8251866001820283011164010000000082111715610a0857600080fd5b8083526020830192505050908051906020019080838360005b83811015610a3c578082015181840152602081019050610a21565b50505050905090810190601f168015610a695780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610a8c57600080fd5b83820191506020820185811115610aa257600080fd5b8251866001820283011164010000000082111715610abf57600080fd5b8083526020830192505050908051906020019080838360005b83811015610af3578082015181840152602081019050610ad8565b50505050905090810190601f168015610b205780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610b6b5780518252602082019150602081019050602083039250610b48565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610bbc5780518252602082019150602081019050602083039250610b99565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610cda57fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610d7d578082015181840152602081019050610d62565b50505050905090810190601f168015610daa5780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610dcf57600080fd5b505af1158015610de3573d6000803e3d6000fd5b505050506040513d6020811015610df957600080fd5b8101908080519060200190929190505050610e7c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a815260200180","7f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827fc67282d29c8a89472b064748f3fba5bf01832ac411e55d7e281e3eef3d07e797896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610f7c578082015181840152602081019050610f61565b50505050905090810190601f168015610fa95780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b60008082511415610fde576000801b9050610fe6565b602082015190505b919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061102c57805160ff191683800117855561105a565b8280016001018555821561105a579182015b8281111561105957825182559160200191906001019061103e565b5b509050611067919061106b565b5090565b61108d91905b80821115611089576000816000905550600101611071565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a264697066735822122088e5ed53fea3a9f4f2d21334dc66b879a7ed7908c1dc5c5c1df19910df9ecb2e64736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b0217905550670de0b6b3a76400006005556040518060600160405280603e8152602001620012a4603e9139600990805190602001906200006a929190620000f7565b503480156200007857600080fd5b50604051620012e2380380620012e2833981810160405260208110156200009e57600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050620001a6565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200013a57805160ff19168380011785556200016b565b828001600101855582156200016b579182015b828111156200016a5782518255916020019190600101906200014d565b5b5090506200017a91906200017e565b5090565b620001a391905b808211156200019f57600081600090555060010162000185565b5090565b90565b6110ee80620001b66000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063a06be5f711610066578063a06be5f7146102fa578063a9a0282714610318578063b791ee541461039b578063df71c15f146103dd578063e97aecc3146104095761009e565b806310d64222146100a3578063299f7f9d146101bd5780633fdc9181146101db5780634f5407981461029657806356ddd8ed146102b4575b600080fd5b6101bb600480360360608110156100b957600080fd5b8101908080359060200190929190803590602001906401000000008111156100e057600080fd5b8201836020820111156100f257600080fd5b8035906020019184600183028401116401000000008311171561011457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561017757600080fd5b82018360208201111561018957600080fd5b803590602001918460018302840111640100000000831117156101ab57600080fd5b9091929391929390505050610427565b005b6101c5610553565b6040518082815260200191505060405180910390f35b610294600480360360208110156101f157600080fd5b810190808035906020019064010000000081111561020e57600080fd5b82018360208201111561022057600080fd5b8035906020019184600183028401116401000000008311171561024257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061055d565b005b61029e610577565b6040518082815260200191505060405180910390f35b6102e0600480360360208110156102ca57600080fd5b810190808035906020019092919050505061057d565b604051808215151515815260200191505060405180910390f35b6103026105a7565b6040518082815260200191505060405180910390f35b6103206105ae565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610360578082015181840152602081019050610345565b50505050905090810190601f16801561038d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103c7600480360360208110156103b157600080fd5b8101908080359060200190929190505050610650565b6040518082815260200191505060405180910390f35b6103e561066d565b604051808260028111156103f557fe5b60ff16815260200191505060405180910390f35b610411610680565b6040518082815260200191505060405180910390f35b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146104df576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806110916028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a261054c8585610791565b5050505050565b6000600854905090565b8060099080519060200190610573929190610feb565b5050565b60085481565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b620927c081565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106465780601f1061061b57610100808354040283529160200191610646565b820191906000526020600020905b81548152906001019060200180831161062957829003601f168201915b5050505050905090565b600060066000838152602001908152602001600020549050919050565b600460009054906101000a900460ff1681565b60008061075c600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561073f5780601f106107145761010080835404028352916020019161073f565b820191906000526020600020905b81548152906001019060200180831161072257829003601f168201915b5050505050600554600460009054906101000a900460ff16610877565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b6007600083815260200190815260200160002060009054906101000a900460ff16610824576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b61082d816108cb565b60001c60088190555060085460066000848152602001908152602001600020819055506007600083815260200190815260200160002060006101000a81549060ff02191690555050565b60006108c1620927c06040518060400160405280600381526020017f75726c00000000000000000000000000000000000000000000000000000000008152508787876000886108ee565b9050949350505050565b600080825114156108e1576000801b90506108e9565b602082015190505b919050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b815260040160006040518083038186803b15801561099a57600080fd5b505afa1580156109ae573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156109d857600080fd5b81019080805160405193929190846401000000008211156109f857600080fd5b83820191506020820185811115610a0e57600080fd5b8251866001820283011164010000000082111715610a2b57600080fd5b8083526020830192505050908051906020019080838360005b83811015610a5f578082015181840152602081019050610a44565b50505050905090810190601f168015610a8c5780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610aaf57600080fd5b83820191506020820185811115610ac557600080fd5b8251866001820283011164010000000082111715610ae257600080fd5b8083526020830192505050908051906020019080838360005b83811015610b16578082015181840152602081019050610afb565b50505050905090810190601f168015610b435780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610b8e5780518252602082019150602081019050602083039250610b6b565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610bdf5780518252602082019150602081019050602083039250610bbc565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634479bc11306001548a8a8f8b8b6002811115610cfd57fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610da0578082015181840152602081019050610d85565b50505050905090810190601f168015610dcd5780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610df257600080fd5b505af1158015610e06573d6000803e3d6000fd5b505050506040513d6020811015610e1c57600080fd5b8101908080519060200190929190505050610e9f576040517fc703cb1200000000000000000000000000000000","000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827f961454072929e8177f535baf538a425bcece8c59464447e273f99c18a32db133896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610f9f578082015181840152602081019050610f84565b50505050905090810190601f168015610fcc5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061102c57805160ff191683800117855561105a565b8280016001018555821561105a579182015b8281111561105957825182559160200191906001019061103e565b5b509050611067919061106b565b5090565b61108d91905b80821115611089576000816000905550600101611071565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212201d60582e500c583128617ee0730923413f3b51e2a69fa3769cc7a1288e11c80164736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"requestCount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"url\",\"type\":\"string\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":5}],\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[1264591490,2691425783],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":2,\"value\":[0]},{\"kind\":3,\"slot\":6,\"value\":[0]},{\"kind\":3,\"slot\":7,\"value\":[0]},{\"kind\":4,\"value\":[8]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"selector\":[2327101086,282477090],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":7,\"value\":[0]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[3344918874,1457379565],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[8]}],\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"selector\":[1833756220,698318749],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":6,\"value\":[0]}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"selector\":[771689741,3079794260],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[9]}],\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[3602745127,2845845543],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"selector\":[864869537,3917147331],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[8]}],\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"selector\":[1698111815,1330907032],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[4]}],\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"selector\":[756685117,3748774239],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[9]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"selector\":[623155362,1071419777],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected APISampleOracle(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
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

    public List<RequestedEventResponse> getRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REQUESTED_EVENT, transactionReceipt);
        ArrayList<RequestedEventResponse> responses = new ArrayList<RequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RequestedEventResponse typedResponse = new RequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oracleAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.requestCount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.url = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public BigInteger EXPIRY_TIME() throws ContractException {
        final Function function = new Function(FUNC_EXPIRY_TIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt callback(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String callback(byte[] requestId, byte[] result, byte[] proof,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCallback(byte[] requestId, byte[] result, byte[] proof) {
        final Function function = new Function(
                FUNC_CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple3<byte[], byte[], byte[]> getCallbackInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CALLBACK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<byte[], byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue(), 
                (byte[]) results.get(2).getValue()
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger getById(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public String getUrl() throws ContractException {
        final Function function = new Function(FUNC_GETURL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt request() {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String request(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRequest() {
        final Function function = new Function(
                FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public BigInteger result() throws ContractException {
        final Function function = new Function(FUNC_RESULT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger returnType() throws ContractException {
        final Function function = new Function(FUNC_RETURNTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt setUrl(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String setUrl(String _url, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetUrl(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetUrlInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETURL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public static APISampleOracle load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new APISampleOracle(contractAddress, client, credential);
    }

    public static APISampleOracle deploy(Client client, CryptoKeyPair credential,
            String oracleAddress) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(oracleAddress)));
        return deploy(APISampleOracle.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
    }

    public static class FulfilledEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] id;
    }

    public static class RequestedEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] id;

        public String oracleAddress;

        public BigInteger requestCount;

        public String url;
    }
}
