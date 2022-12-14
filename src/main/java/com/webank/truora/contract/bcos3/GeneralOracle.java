package com.webank.truora.contract.bcos3;

import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.*;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class GeneralOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b0217905550604051806020016040528060008152506008908051906020019062000054929190620000e1565b503480156200006257600080fd5b506040516200145538038062001455833981810160405260208110156200008857600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505062000190565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012457805160ff191683800117855562000155565b8280016001018555821562000155579182015b828111156200015457825182559160200191906001019062000137565b5b50905062000164919062000168565b5090565b6200018d91905b80821115620001895760008160009055506001016200016f565b5090565b90565b6112b580620001a06000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638ab4be9e116100665780638ab4be9e146102155780639b28b0921461032f578063ba92594414610371578063bfe370d914610457578063c75f695a146105265761009e565b80632d1a193d146100a35780632dff0d0d146100cf57806340201b04146101765780634b602282146101a75780638963adab146101c5575b600080fd5b6100ab610568565b604051808260028111156100bb57fe5b60ff16815260200191505060405180910390f35b6100fb600480360360208110156100e557600080fd5b810190808035906020019092919050505061057b565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013b578082015181840152602081019050610120565b50505050905090810190601f1680156101685780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101a56004803603602081101561018c57600080fd5b81019080803560ff169060200190929190505050610630565b005b6101af610656565b6040518082815260200191505060405180910390f35b6101f1600480360360208110156101db57600080fd5b810190808035906020019092919050505061065d565b6040518082600281111561020157fe5b60ff16815260200191505060405180910390f35b61032d6004803603606081101561022b57600080fd5b81019080803590602001909291908035906020019064010000000081111561025257600080fd5b82018360208201111561026457600080fd5b8035906020019184600183028401116401000000008311171561028657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156102e957600080fd5b8201836020820111156102fb57600080fd5b8035906020019184600183028401116401000000008311171561031d57600080fd5b9091929391929390505050610687565b005b61035b6004803603602081101561034557600080fd5b81019080803590602001909291905050506107b3565b6040518082815260200191505060405180910390f35b6104416004803603606081101561038757600080fd5b81019080803590602001906401000000008111156103a457600080fd5b8201836020820111156103b657600080fd5b803590602001918460018302840111640100000000831117156103d857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190803560ff1690602001909291905050506108c8565b6040518082815260200191505060405180910390f35b6105106004803603602081101561046d57600080fd5b810190808035906020019064010000000081111561048a57600080fd5b82018360208201111561049c57600080fd5b803590602001918460018302840111640100000000831117156104be57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610953565b6040518082815260200191505060405180910390f35b6105526004803603602081101561053c57600080fd5b8101908080359060200190929190505050610976565b6040518082815260200191505060405180910390f35b600460009054906101000a900460ff1681565b6060600560008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106245780601f106105f957610100808354040283529160200191610624565b820191906000526020600020905b81548152906001019060200180831161060757829003601f168201915b50505050509050919050565b80600460006101000a81548160ff0219169083600281111561064e57fe5b021790555050565b620927c081565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461073f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806112586028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26107ac8585610993565b5050505050565b6000806007600084815260200190815260200160002060009054906101000a900460ff169050600060028111156107e657fe5b8160028111156107f257fe5b14156108bd5760006108ae600560008681526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108a45780601f10610879576101008083540402835291602001916108a4565b820191906000526020600020905b81548152906001019060200180831161088757829003601f168201915b5050505050610953565b60001c905080925050506108c3565b60009150505b919050565b6000806108f9600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16868686610a61565b905060016006600083815260200190815260200160002081905550826007600083815260200190815260200160002060006101000a81548160ff0219169083600281111561094357fe5b0217905550809150509392505050565b60008082511415610969576000801b9050610971565b602082015190505b919050565b600060066000838152602001908152602001600020549050919050565b6001600660008481526020019081526020016000205414610a1c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f6964206d757374206265206f6e676f696e67210000000000000000000000000081525060200191505060405180910390fd5b80600560008481526020019081526020016000209080519060200190610a439291906111b2565b50600260066000848152602001908152602001600020819055505050565b6000610aab620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610ab5565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166389f081126040518163ffffffff1660e01b815260040160006040518083038186803b158015610b6157600080fd5b505afa158015610b75573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506040811015610b9f57600080fd5b8101908080516040519392919084640100000000821115610bbf57600080fd5b83820191506020820185811115610bd557600080fd5b8251866001820283011164010000000082111715610bf257600080fd5b8083526020830192505050908051906020019080838360005b83811015610c26578082015181840152602081019050610c0b565b50505050905090810190601f168015610c535780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610c7657600080fd5b83820191506020820185811115610c8c57600080fd5b8251866001820283011164010000000082111715610ca957600080fd5b8083526020830192505050908051906020019080838360005b83811015610cdd578082015181840152602081019050610cc2565b50505050905090810190601f168015610d0a5780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610d555780518252602082019150602081019050602083039250610d32565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610da65780518252602082019150602081019050602083039250610d83565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffff","ffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637b5a2033306001548a8a8f8b8b6002811115610ec457fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610f67578082015181840152602081019050610f4c565b50505050905090810190601f168015610f945780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610fb957600080fd5b505af1158015610fcd573d6000803e3d6000fd5b505050506040513d6020811015610fe357600080fd5b8101908080519060200190929190505050611066576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827fc67282d29c8a89472b064748f3fba5bf01832ac411e55d7e281e3eef3d07e797896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561116657808201518184015260208101905061114b565b50505050905090810190601f1680156111935780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106111f357805160ff1916838001178555611221565b82800160010185558215611221579182015b82811115611220578251825591602001919060010190611205565b5b50905061122e9190611232565b5090565b61125491905b80821115611250576000816000905550600101611238565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a2646970667358221220fe22bfc38911f4f8070f1639f6c4f6cebafe83fa6964447ec643e7704412f9a264736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600180556000600460006101000a81548160ff021916908360028111156200002857fe5b0217905550604051806020016040528060008152506008908051906020019062000054929190620000e1565b503480156200006257600080fd5b506040516200145538038062001455833981810160405260208110156200008857600080fd5b810190808051906020019092919050505080600460016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505062000190565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012457805160ff191683800117855562000155565b8280016001018555821562000155579182015b828111156200015457825182559160200191906001019062000137565b5b50905062000164919062000168565b5090565b6200018d91905b80821115620001895760008160009055506001016200016f565b5090565b90565b6112b580620001a06000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063a06be5f711610066578063a06be5f714610360578063afe34e421461037e578063b791ee54146103af578063df71c15f14610456578063df952c36146104825761009e565b806310d64222146100a357806356ddd8ed146101bd57806368c8e42d146101ff5780636eb679dd1461024f57806376d4ebae14610291575b600080fd5b6101bb600480360360608110156100b957600080fd5b8101908080359060200190929190803590602001906401000000008111156100e057600080fd5b8201836020820111156100f257600080fd5b8035906020019184600183028401116401000000008311171561011457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561017757600080fd5b82018360208201111561018957600080fd5b803590602001918460018302840111640100000000831117156101ab57600080fd5b9091929391929390505050610568565b005b6101e9600480360360208110156101d357600080fd5b8101908080359060200190929190505050610694565b6040518082815260200191505060405180910390f35b61022b6004803603602081101561021557600080fd5b81019080803590602001909291905050506106b1565b6040518082600281111561023b57fe5b60ff16815260200191505060405180910390f35b61027b6004803603602081101561026557600080fd5b81019080803590602001909291905050506106db565b6040518082815260200191505060405180910390f35b61034a600480360360208110156102a757600080fd5b81019080803590602001906401000000008111156102c457600080fd5b8201836020820111156102d657600080fd5b803590602001918460018302840111640100000000831117156102f857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506107f0565b6040518082815260200191505060405180910390f35b610368610813565b6040518082815260200191505060405180910390f35b6103ad6004803603602081101561039457600080fd5b81019080803560ff16906020019092919050505061081a565b005b6103db600480360360208110156103c557600080fd5b8101908080359060200190929190505050610840565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561041b578082015181840152602081019050610400565b50505050905090810190601f1680156104485780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61045e6108f5565b6040518082600281111561046e57fe5b60ff16815260200191505060405180910390f35b6105526004803603606081101561049857600080fd5b81019080803590602001906401000000008111156104b557600080fd5b8201836020820111156104c757600080fd5b803590602001918460018302840111640100000000831117156104e957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190803560ff169060200190929190505050610908565b6040518082815260200191505060405180910390f35b836002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610620576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001806112586028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807fe3e8c09f2b386451fb08190a58cc7fa1703c75e83b075569ccc37139c0a6e51e60405160405180910390a261068d8585610993565b5050505050565b600060066000838152602001908152602001600020549050919050565b60006007600083815260200190815260200160002060009054906101000a900460ff169050919050565b6000806007600084815260200190815260200160002060009054906101000a900460ff1690506000600281111561070e57fe5b81600281111561071a57fe5b14156107e55760006107d6600560008681526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107cc5780601f106107a1576101008083540402835291602001916107cc565b820191906000526020600020905b8154815290600101906020018083116107af57829003601f168201915b50505050506107f0565b60001c905080925050506107eb565b60009150505b919050565b60008082511415610806576000801b905061080e565b602082015190505b919050565b620927c081565b80600460006101000a81548160ff0219169083600281111561083857fe5b021790555050565b6060600560008381526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108e95780601f106108be576101008083540402835291602001916108e9565b820191906000526020600020905b8154815290600101906020018083116108cc57829003601f168201915b50505050509050919050565b600460009054906101000a900460ff1681565b600080610939600460019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16868686610a61565b905060016006600083815260200190815260200160002081905550826007600083815260200190815260200160002060006101000a81548160ff0219169083600281111561098357fe5b0217905550809150509392505050565b6001600660008481526020019081526020016000205414610a1c576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f6964206d757374206265206f6e676f696e67210000000000000000000000000081525060200191505060405180910390fd5b80600560008481526020019081526020016000209080519060200190610a439291906111b2565b50600260066000848152602001908152602001600020819055505050565b6000610aab620927c06040518060400160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250878787600088610ab5565b9050949350505050565b6000856000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631667e4346040518163ffffffff1660e01b815260040160006040518083038186803b158015610b6157600080fd5b505afa158015610b75573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506040811015610b9f57600080fd5b8101908080516040519392919084640100000000821115610bbf57600080fd5b83820191506020820185811115610bd557600080fd5b8251866001820283011164010000000082111715610bf257600080fd5b8083526020830192505050908051906020019080838360005b83811015610c26578082015181840152602081019050610c0b565b50505050905090810190601f168015610c535780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115610c7657600080fd5b83820191506020820185811115610c8c57600080fd5b8251866001820283011164010000000082111715610ca957600080fd5b8083526020830192505050908051906020019080838360005b83811015610cdd578082015181840152602081019050610cc2565b50505050905090810190601f168015610d0a5780820380516001836020036101000a031916815260200191505b5060405250505080925081935050508181306001546040516020018085805190602001908083835b60208310610d555780518252602082019150602081019050602083039250610d32565b6001836020036101000a03801982511681845116808217855250505050505090500184805190602001908083835b60208310610da65780518252602082019150602081019050602083039250610d83565b6001836020036101000a0380198251168184511680821785525050505050509050018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001945050505050604051602081830303815290604052805190602001209250876002600085815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffff","ffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634479bc11306001548a8a8f8b8b6002811115610ec457fe5b6040518863ffffffff1660e01b8152600401808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018781526020018060200186815260200185815260200184151515158152602001838152602001828103825287818151815260200191508051906020019080838360005b83811015610f67578082015181840152602081019050610f4c565b50505050905090810190601f168015610f945780820380516001836020036101000a031916815260200191505b5098505050505050505050602060405180830381600087803b158015610fb957600080fd5b505af1158015610fcd573d6000803e3d6000fd5b505050506040513d6020811015610fe357600080fd5b8101908080519060200190929190505050611066576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600160008154809291906001019190505550600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008154809291906001019190505550827f961454072929e8177f535baf538a425bcece8c59464447e273f99c18a32db133896001548a604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561116657808201518184015260208101905061114b565b50505050905090810190601f1680156111935780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a28292505050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106111f357805160ff1916838001178555611221565b82800160010185558215611221579182015b82811115611220578251825591602001919060010190611205565b5b50905061122e9190611232565b5090565b61125491905b80821115611250576000816000905550600101611238565b5090565b9056fe536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a264697066735822122032b9a0642caeddb79b60b2f0e79e38d77c35430c55fec7a93873796b3f3f0e1064736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"Fulfilled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"oracleAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"requestCount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"url\",\"type\":\"string\"}],\"name\":\"Requested\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"EXPIRY_TIME\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes\",\"name\":\"source\",\"type\":\"bytes\"}],\"name\":\"bytesToBytes32\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"result_\",\"type\":\"bytes32\"}],\"stateMutability\":\"pure\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"internalType\":\"bytes\",\"name\":\"result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"callback\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"checkIdFulfilled\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getById\",\"outputs\":[{\"internalType\":\"bytes\",\"name\":\"\",\"type\":\"bytes\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getIntById\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"id\",\"type\":\"bytes32\"}],\"name\":\"getReqType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"url_\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"timesAmount_\",\"type\":\"uint256\"},{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"returnType_\",\"type\":\"uint8\"}],\"name\":\"requestSource\",\"outputs\":[{\"internalType\":\"bytes32\",\"name\":\"\",\"type\":\"bytes32\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"returnType\",\"outputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum FiscoOracleClient.ReturnType\",\"name\":\"returnType_\",\"type\":\"uint8\"}],\"name\":\"setReturnType\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_EXPIRY_TIME = "EXPIRY_TIME";

    public static final String FUNC_BYTESTOBYTES32 = "bytesToBytes32";

    public static final String FUNC_CALLBACK = "callback";

    public static final String FUNC_CHECKIDFULFILLED = "checkIdFulfilled";

    public static final String FUNC_GETBYID = "getById";

    public static final String FUNC_GETINTBYID = "getIntById";

    public static final String FUNC_GETREQTYPE = "getReqType";

    public static final String FUNC_REQUESTSOURCE = "requestSource";

    public static final String FUNC_RETURNTYPE = "returnType";

    public static final String FUNC_SETRETURNTYPE = "setReturnType";

    public static final Event FULFILLED_EVENT = new Event("Fulfilled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event REQUESTED_EVENT = new Event("Requested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected GeneralOracle(String contractAddress, Client client, CryptoKeyPair credential) {
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

    public BigInteger checkIdFulfilled(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_CHECKIDFULFILLED, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public byte[] getById(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeCallWithSingleValueReturn(function, byte[].class);
    }

    public BigInteger getIntById(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETINTBYID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger getReqType(byte[] id) throws ContractException {
        final Function function = new Function(FUNC_GETREQTYPE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt requestSource(String url_, BigInteger timesAmount_,
            BigInteger returnType_) {
        final Function function = new Function(
                FUNC_REQUESTSOURCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(url_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(timesAmount_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String requestSource(String url_, BigInteger timesAmount_, BigInteger returnType_,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REQUESTSOURCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(url_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(timesAmount_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRequestSource(String url_, BigInteger timesAmount_,
            BigInteger returnType_) {
        final Function function = new Function(
                FUNC_REQUESTSOURCE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(url_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(timesAmount_), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(returnType_)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple3<String, BigInteger, BigInteger> getRequestSourceInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REQUESTSOURCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, BigInteger, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public Tuple1<byte[]> getRequestSourceOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REQUESTSOURCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
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

    public static GeneralOracle load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new GeneralOracle(contractAddress, client, credential);
    }

    public static GeneralOracle deploy(Client client, CryptoKeyPair credential,
            String oracleAddress) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(oracleAddress)));
        return deploy(GeneralOracle.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
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
