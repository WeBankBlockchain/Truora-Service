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
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b021790555060016005556040518060600160405280603e8152602001620013d9603e91396009908051906020019062000063929190620000f0565b503480156200007157600080fd5b506040516200141738038062001417833981810160405260208110156200009757600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506200019f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200013357805160ff191683800117855562000164565b8280016001018555821562000164579182015b828111156200016357825182559160200191906001019062000146565b5b50905062000173919062000177565b5090565b6200019c91905b80821115620001985760008160009055506001016200017e565b5090565b90565b61122a80620001af6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c80636537214711610071578063653721471461024f5780636d4ce63c1461026d5780638ab4be9e1461028b578063bfe370d9146103a5578063c75f695a14610474578063d6bd8727146104ba576100b4565b8063252498a2146100b95780632d1a193d146101745780632dff0d0d146101a0578063338cdca1146101e257806340201b04146102005780634b60228214610231575b600080fd5b610172600480360360208110156100cf57600080fd5b81019080803590602001906401000000008111156100ec57600080fd5b8201836020820111156100fe57600080fd5b8035906020019184600183028401116401000000008311171561012057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061053d565b005b61017c610557565b6040518082600281111561018c57fe5b60ff16815260200191505060405180910390f35b6101cc600480360360208110156101b657600080fd5b810190808035906020019092919050505061056a565b6040518082815260200191505060405180910390f35b6101ea610587565b6040518082815260200191505060405180910390f35b61022f6004803603602081101561021657600080fd5b81019080803560ff169060200190929190505050610698565b005b6102396106be565b6040518082815260200191505060405180910390f35b6102576106c5565b6040518082815260200191505060405180910390f35b6102756106cb565b6040518082815260200191505060405180910390f35b6103a3600480360360608110156102a157600080fd5b8101908080359060200190929190803590602001906401000000008111156102c857600080fd5b8201836020820111156102da57600080fd5b803590602001918460018302840111640100000000831117156102fc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561035f57600080fd5b82018360208201111561037157600080fd5b8035906020019184600183028401116401000000008311171561039357600080fd5b90919293919293905050506106d5565b005b61045e600480360360208110156103bb57600080fd5b81019080803590602001906401000000008111156103d857600080fd5b8201836020820111156103ea57600080fd5b8035906020019184600183028401116401000000008311171561040c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610801565b6040518082815260200191505060405180910390f35b6104a06004803603602081101561048a57600080fd5b8101908080359060200190929190505050610824565b604051808215151515815260200191505060405180910390f35b6104c261084e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105025780820151818401526020810190506104e7565b50505050905090810190601f16801561052f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060099080519060200190610553929190611127565b5050565b600460009054906101000a900460ff1681565b600060066000838152602001908152602001600020549050919050565b600080610663600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106465780601f1061061b57610100808354040283529160200191610646565b820191906000526020600020905b81548152906001019060200180831161062957829003601f168201915b5050505050600554600460009054906101000a900460ff166108f0565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b80600460006101000a81548160ff021916908360028111156106b657fe5b021790555050565b620927c081565b60085481565b6000600854905090565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461078d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806111cd6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26107fa8585610944565b5050505050565b60008082511415610817576000801b905061081f565b602082015190505b919050565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b606060098054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108e65780601f106108bb576101008083540402835291602001916108e6565b820191906000526020600020905b8154815290600101906020018083116108c957829003601f168201915b5050505050905090565b600061093a620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610a2a565b9050949350505050565b6007600083815260200190815260200160002060009054906101000a900460ff166109d7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b6109e081610801565b60001c60088190555060085460066000848152602001908152602001600020819055506007600083815260200190815260200160002060006101000a81549060ff02191690555050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b815260040160006040518083038186803b158015610ad657600080fd5b505afa158015610aea573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506040811015610b1457600080fd5b8101908080516040519392919084640100000000821115610b3457600080fd5b83820191506020820185811115610b4a57600080fd5b8251866001820283011164010000000082111715610b6757600080fd5b8083526020830192505050908051906020019080838360005b83811015610b9b578082015181840152602081019050610b80565b50505050905090810190601f168015610bc85780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610beb57600080fd5b83820191506020820185811115610c0157600080fd5b8251866001820283011164010000000082111715610c1e57600080fd5b8083526020830192505050908051906020019080838360005b83811015610c52578082015181840152602081019050610c37565b50505050905090810190601f168015610c7f5780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610cca5780518252602082019150602081019050602083039250610ca7565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610d1b5780518252602082019150602081019050602083039250610cf8565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610e3957fe5b6040518863ffffffff1660e01b8152600401808873ffff","ffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610edc578082015181840152602081019050610ec1565b50505050905090810190601f168015610f095780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610f2e57600080fd5b505af1158015610f42573d6000803e3d6000fd5b505050506040513d6020811015610f5857600080fd5b8101908080519060200190929190505050610fdb576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827fc67282d29c8a89472b064748f3fba5bf01832ac411e55d7e281e3eef3d07e797896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156110db5780820151818401526020810190506110c0565b50505050905090810190601f1680156111085780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061116857805160ff1916838001178555611196565b82800160010185558215611196579182015b8281111561119557825182559160200191906001019061117a565b5b5090506111a391906111a7565b5090565b6111c991905b808211156111c55760008160009055506001016111ad565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212203fcf77aebd728076720f60db4f62c13c5ebf81dc47e36601782b90191ad585fa64736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b021790555060016005556040518060600160405280603e8152602001620013d9603e91396009908051906020019062000063929190620000f0565b503480156200007157600080fd5b506040516200141738038062001417833981810160405260208110156200009757600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506200019f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200013357805160ff191683800117855562000164565b8280016001018555821562000164579182015b828111156200016357825182559160200191906001019062000146565b5b50905062000173919062000177565b5090565b6200019c91905b80821115620001985760008160009055506001016200017e565b5090565b90565b61122a80620001af6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c8063a06be5f711610071578063a06be5f7146103df578063a9a02827146103fd578063afe34e4214610480578063b791ee54146104b1578063df71c15f146104f3578063e97aecc31461051f576100b4565b806310d64222146100b9578063299f7f9d146101d35780633fdc9181146101f15780634f540798146102ac57806356ddd8ed146102ca57806376d4ebae14610310575b600080fd5b6101d1600480360360608110156100cf57600080fd5b8101908080359060200190929190803590602001906401000000008111156100f657600080fd5b82018360208201111561010857600080fd5b8035906020019184600183028401116401000000008311171561012a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561018d57600080fd5b82018360208201111561019f57600080fd5b803590602001918460018302840111640100000000831117156101c157600080fd5b909192939192939050505061053d565b005b6101db610669565b6040518082815260200191505060405180910390f35b6102aa6004803603602081101561020757600080fd5b810190808035906020019064010000000081111561022457600080fd5b82018360208201111561023657600080fd5b8035906020019184600183028401116401000000008311171561025857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610673565b005b6102b461068d565b6040518082815260200191505060405180910390f35b6102f6600480360360208110156102e057600080fd5b8101908080359060200190929190505050610693565b604051808215151515815260200191505060405180910390f35b6103c96004803603602081101561032657600080fd5b810190808035906020019064010000000081111561034357600080fd5b82018360208201111561035557600080fd5b8035906020019184600183028401116401000000008311171561037757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506106bd565b6040518082815260200191505060405180910390f35b6103e76106e0565b6040518082815260200191505060405180910390f35b6104056106e7565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561044557808201518184015260208101905061042a565b50505050905090810190601f1680156104725780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104af6004803603602081101561049657600080fd5b81019080803560ff169060200190929190505050610789565b005b6104dd600480360360208110156104c757600080fd5b81019080803590602001909291905050506107af565b6040518082815260200191505060405180910390f35b6104fb6107cc565b6040518082600281111561050b57fe5b60ff16815260200191505060405180910390f35b6105276107df565b6040518082815260200191505060405180910390f35b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146105f5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806111cd6028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a261066285856108f0565b5050505050565b6000600854905090565b8060099080519060200190610689929190611127565b5050565b60085481565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b600080825114156106d3576000801b90506106db565b602082015190505b919050565b620927c081565b606060098054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561077f5780601f106107545761010080835404028352916020019161077f565b820191906000526020600020905b81548152906001019060200180831161076257829003601f168201915b5050505050905090565b80600460006101000a81548160ff021916908360028111156107a757fe5b021790555050565b600060066000838152602001908152602001600020549050919050565b600460009054906101000a900460ff1681565b6000806108bb600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660098054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561089e5780601f106108735761010080835404028352916020019161089e565b820191906000526020600020905b81548152906001019060200180831161088157829003601f168201915b5050505050600554600460009054906101000a900460ff166109d6565b905060016007600083815260200190815260200160002060006101000a81548160ff0219169083151502179055508091505090565b6007600083815260200190815260200160002060009054906101000a900460ff16610983576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b61098c816106bd565b60001c60088190555060085460066000848152602001908152602001600020819055506007600083815260200190815260200160002060006101000a81549060ff02191690555050565b6000610a20620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610a2a565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b815260040160006040518083038186803b158015610ad657600080fd5b505afa158015610aea573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506040811015610b1457600080fd5b8101908080516040519392919084640100000000821115610b3457600080fd5b83820191506020820185811115610b4a57600080fd5b8251866001820283011164010000000082111715610b6757600080fd5b8083526020830192505050908051906020019080838360005b83811015610b9b578082015181840152602081019050610b80565b50505050905090810190601f168015610bc85780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610beb57600080fd5b83820191506020820185811115610c0157600080fd5b8251866001820283011164010000000082111715610c1e57600080fd5b8083526020830192505050908051906020019080838360005b83811015610c52578082015181840152602081019050610c37565b50505050905090810190601f168015610c7f5780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610cca5780518252602082019150602081019050602083039250610ca7565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610d1b5780518252602082019150602081019050602083039250610cf8565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634479bc11306001548a8a8f8b8b6002811115610e3957fe5b6040518863ffffffff1660e01b8152600401808873ffff","ffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610edc578082015181840152602081019050610ec1565b50505050905090810190601f168015610f095780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610f2e57600080fd5b505af1158015610f42573d6000803e3d6000fd5b505050506040513d6020811015610f5857600080fd5b8101908080519060200190929190505050610fdb576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827f961454072929e8177f535baf538a425bcece8c59464447e273f99c18a32db133896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156110db5780820151818401526020810190506110c0565b50505050905090810190601f1680156111085780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061116857805160ff1916838001178555611196565b82800160010185558215611196579182015b8281111561119557825182559160200191906001019061117a565b5b5090506111a391906111a7565b5090565b6111c991905b808211156111c55760008160009055506001016111ad565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a26469706673582212204ef5cef36224578beffca6553b3e323c2b464dbe8083881e2cfc38c2ee05aacc64736f6c634300060a00336a736f6e2868747470733a2f2f6170692e65786368616e6765726174652d6170692e636f6d2f76342f6c61746573742f434e59292e72617465732e4a5059"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"requestCount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"url\",\"type\":\"string\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes\",\"name\":\"source\",\"type\":\"bytes\"}],\"name\":\"bytesToBytes32\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"result_\",\"type\":\"bytes32\"}],\"stateMutability\":\"pure\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getUrl\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"request\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"result\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"returnType_\",\"type\":\"uint8\"}],\"name\":\"setReturnType\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_EXPIRY_TIME = "EXPIRY_TIME";

    public static final String FUNC_BYTESTOBYTES32 = "bytesToBytes32";

    public static final String FUNC_CALLBACK = "callback";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_GETURL = "getUrl";

    public static final String FUNC_REQUEST = "request";

    public static final String FUNC_RESULT = "result";

    public static final String FUNC_RETURNTYPE = "returnType";

    public static final String FUNC_SETRETURNTYPE = "setReturnType";

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

    public TransactionReceipt bytesToBytes32(byte[] source) {
        final Function function = new Function(
                FUNC_BYTESTOBYTES32, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(source)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String bytesToBytes32(byte[] source, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_BYTESTOBYTES32, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(source)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForBytesToBytes32(byte[] source) {
        final Function function = new Function(
                FUNC_BYTESTOBYTES32, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(source)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getBytesToBytes32Input(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_BYTESTOBYTES32, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public Tuple1<byte[]> getBytesToBytes32Output(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_BYTESTOBYTES32, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger getById(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger returnType() throws ContractException {
        final Function function = new Function(FUNC_RETURNTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt setReturnType(BigInteger returnType_) {
        final Function function = new Function(
                FUNC_SETRETURNTYPE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String setReturnType(BigInteger returnType_, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETRETURNTYPE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetReturnType(BigInteger returnType_) {
        final Function function = new Function(
                FUNC_SETRETURNTYPE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getSetReturnTypeInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETRETURNTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
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
