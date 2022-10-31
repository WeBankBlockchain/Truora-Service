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
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class OracleCore extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040526040518060400160405280600681526020017f636861696e30000000000000000000000000000000000000000000000000000081525060039080519060200190620000519291906200039a565b506040518060400160405280600681526020017f67726f7570300000000000000000000000000000000000000000000000000000815250600490805190602001906200009f9291906200039a565b5060405180807f63616c6c6261636b28627974657333322c62797465732c627974657329000000815250601d0190506040518091039020600560006101000a81548163ffffffff021916908360e01c02179055503480156200010057600080fd5b50604051620013f2380380620013f2833981810160405260408110156200012657600080fd5b81019080805160405193929190846401000000008211156200014757600080fd5b838201915060208201858111156200015e57600080fd5b82518660018202830111640100000000821117156200017c57600080fd5b8083526020830192505050908051906020019080838360005b83811015620001b257808201518184015260208101905062000195565b50505050905090810190601f168015620001e05780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200020457600080fd5b838201915060208201858111156200021b57600080fd5b82518660018202830111640100000000821117156200023957600080fd5b8083526020830192505050908051906020019080838360005b838110156200026f57808201518184015260208101905062000252565b50505050905090810190601f1680156200029d5780820380516001836020036101000a031916815260200191505b50604052505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a38160039080519060200190620003789291906200039a565b508060049080519060200190620003919291906200039a565b50505062000449565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620003dd57805160ff19168380011785556200040e565b828001600101855582156200040e579182015b828111156200040d578251825591602001919060010190620003f0565b5b5090506200041d919062000421565b5090565b6200044691905b808211156200044257600081600090555060010162000428565b5090565b90565b610f9980620004596000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80637b5a20331461006757806389f081121461014c5780638da5cb5b1461023b5780638f32d59b14610285578063f2fde38b146102a7578063f3baba2d146102eb575b600080fd5b610132600480360360e081101561007d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156100c457600080fd5b8201836020820111156100d657600080fd5b803590602001918460018302840111640100000000831117156100f857600080fd5b9091929391929390803590602001909291908035906020019092919080351515906020019092919080359060200190929190505050610405565b604051808215151515815260200191505060405180910390f35b6101546106cd565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561019857808201518184015260208101905061017d565b50505050905090810190601f1680156101c55780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156101fe5780820151818401526020810190506101e3565b50505050905090810190601f16801561022b5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b610243610814565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61028d61083d565b604051808215151515815260200191505060405180910390f35b6102e9600480360360208110156102bd57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610894565b005b6103eb600480360360a081101561030157600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561035257600080fd5b82018360208201111561036457600080fd5b8035906020019184600183028401116401000000008311171561038657600080fd5b9091929391929390803590602001906401000000008111156103a757600080fd5b8201836020820111156103b957600080fd5b803590602001918460018302840111640100000000831117156103db57600080fd5b909192939192939050505061091a565b604051808215151515815260200191505060405180910390f35b600080600360048b8b604051602001808580546001816001161561010002031660029004801561046c5780601f1061044a57610100808354040283529182019161046c565b820191906000526020600020905b815481529060010190602001808311610458575b5050848054600181600116156101000203166002900480156104c55780601f106104a35761010080835404028352918201916104c5565b820191906000526020600020905b8154815290600101906020018083116104b1575b50508373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b815260140182815260200194505050505060405160208183030381529060405280519060200120905060006105358642610d7190919063ffffffff16565b90508060026000848152602001908152602001600020819055508a81604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012060016000848152602001908152602001600020819055507fbfa61b1af63d18b42d712f1f698b554a963ad19f86120d2e08f0afb46a28fd49308c848c8c868d8c8c604051808a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200188815260200180602001868152602001858152602001841515151581526020018381526020018281038252888882818152602001925080828437600081840152601f19601f8201169050808301925050509a505050505050505050505060405180910390a160019250505098975050505050505050565b60608060036004818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107695780601f1061073e57610100808354040283529160200191610769565b820191906000526020600020905b81548152906001019060200180831161074c57829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108055780601f106107da57610100808354040283529160200191610805565b820191906000526020600020905b8154815290600101906020018083116107e857829003601f168201915b50505050509050915091509091565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b61089c61083d565b61090e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b61091781610df9565b50565b600061092461083d565b610996576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b876000801b60016000838152602001908152602001600020541415610a23576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4d757374206861766520612076616c696420726571756573744964000000000081525060200191505060405180910390fd5b42600260008381526020019081526020016000205411610aab576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c66696c6c20726571756573742074696d65206f7574000000000000000081525060200191505060405180910390fd5b60008888604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018281526020019250505060405160208183030381529060405280519060200120905080600160008c81526020019081526020016000205414610b95576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f506172616d7320646f206e6f74206d617463682072657175657374204944000081525060200191505060405180910390fd5b600160008b815260200190815260200160","002060009055600260008b81526020019081526020016000206000905560008973ffffffffffffffffffffffffffffffffffffffff16600560009054906101000a900460e01b8c8a8a8a8a6040516024018086815260200180602001806020018381038352878782818152602001925080828437600081840152601f19601f8201169050808301925050508381038252858582818152602001925080828437600081840152601f19601f820116905080830192505050975050505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310610cf55780518252602082019150602081019050602083039250610cd2565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610d57576040519150601f19603f3d011682016040523d82523d6000602084013e610d5c565b606091505b50509050809350505050979650505050505050565b600080828401905083811015610def576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610e7f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180610f3e6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373a2646970667358221220def9012b7a8839b9f5c373772b647a84bbe539e7c6bb8f13b2df63bab7d9c08864736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"60806040526040518060400160405280600681526020017f636861696e30000000000000000000000000000000000000000000000000000081525060039080519060200190620000519291906200039a565b506040518060400160405280600681526020017f67726f7570300000000000000000000000000000000000000000000000000000815250600490805190602001906200009f9291906200039a565b5060405180807f63616c6c6261636b28627974657333322c62797465732c627974657329000000815250601d0190506040518091039020600560006101000a81548163ffffffff021916908360e01c02179055503480156200010057600080fd5b50604051620013f2380380620013f2833981810160405260408110156200012657600080fd5b81019080805160405193929190846401000000008211156200014757600080fd5b838201915060208201858111156200015e57600080fd5b82518660018202830111640100000000821117156200017c57600080fd5b8083526020830192505050908051906020019080838360005b83811015620001b257808201518184015260208101905062000195565b50505050905090810190601f168015620001e05780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200020457600080fd5b838201915060208201858111156200021b57600080fd5b82518660018202830111640100000000821117156200023957600080fd5b8083526020830192505050908051906020019080838360005b838110156200026f57808201518184015260208101905062000252565b50505050905090810190601f1680156200029d5780820380516001836020036101000a031916815260200191505b50604052505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a38160039080519060200190620003789291906200039a565b508060049080519060200190620003919291906200039a565b50505062000449565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620003dd57805160ff19168380011785556200040e565b828001600101855582156200040e579182015b828111156200040d578251825591602001919060010190620003f0565b5b5090506200041d919062000421565b5090565b6200044691905b808211156200044257600081600090555060010162000428565b5090565b90565b610f9980620004596000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631667e4341461006757806316cad12a146101565780634479bc111461019a5780635089e2c81461027f578063e2ef2d47146102c9578063ede8e529146103e3575b600080fd5b61006f610405565b604051808060200180602001838103835285818151815260200191508051906020019080838360005b838110156100b3578082015181840152602081019050610098565b50505050905090810190601f1680156100e05780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156101195780820151818401526020810190506100fe565b50505050905090810190601f1680156101465780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b6101986004803603602081101561016c57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061054c565b005b610265600480360360e08110156101b057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156101f757600080fd5b82018360208201111561020957600080fd5b8035906020019184600183028401116401000000008311171561022b57600080fd5b90919293919293908035906020019092919080359060200190929190803515159060200190929190803590602001909291905050506105d2565b604051808215151515815260200191505060405180910390f35b61028761089a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103c9600480360360a08110156102df57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561033057600080fd5b82018360208201111561034257600080fd5b8035906020019184600183028401116401000000008311171561036457600080fd5b90919293919293908035906020019064010000000081111561038557600080fd5b82018360208201111561039757600080fd5b803590602001918460018302840111640100000000831117156103b957600080fd5b90919293919293905050506108c3565b604051808215151515815260200191505060405180910390f35b6103eb610d1a565b604051808215151515815260200191505060405180910390f35b60608060036004818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104a15780601f10610476576101008083540402835291602001916104a1565b820191906000526020600020905b81548152906001019060200180831161048457829003601f168201915b50505050509150808054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561053d5780601f106105125761010080835404028352916020019161053d565b820191906000526020600020905b81548152906001019060200180831161052057829003601f168201915b50505050509050915091509091565b610554610d1a565b6105c6576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b6105cf81610d71565b50565b600080600360048b8b60405160200180858054600181600116156101000203166002900480156106395780601f10610617576101008083540402835291820191610639565b820191906000526020600020905b815481529060010190602001808311610625575b5050848054600181600116156101000203166002900480156106925780601f10610670576101008083540402835291820191610692565b820191906000526020600020905b81548152906001019060200180831161067e575b50508373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b815260140182815260200194505050505060405160208183030381529060405280519060200120905060006107028642610eb590919063ffffffff16565b90508060026000848152602001908152602001600020819055508a81604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b8152601401828152602001925050506040516020818303038152906040528051906020012060016000848152602001908152602001600020819055507ff518b8f3aed243322323f4169e636c0907c1d8e335f7af792f38d59d06fe9de4308c848c8c868d8c8c604051808a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200188815260200180602001868152602001858152602001841515151581526020018381526020018281038252888882818152602001925080828437600081840152601f19601f8201169050808301925050509a505050505050505050505060405180910390a160019250505098975050505050505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60006108cd610d1a565b61093f576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b876000801b600160008381526020019081526020016000205414156109cc576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f4d757374206861766520612076616c696420726571756573744964000000000081525060200191505060405180910390fd5b42600260008381526020019081526020016000205411610a54576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c66696c6c20726571756573742074696d65206f7574000000000000000081525060200191505060405180910390fd5b60008888604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018281526020019250505060405160208183030381529060405280519060200120905080600160008c81526020019081526020016000205414610b3e576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f506172616d7320646f206e6f74206d617463682072657175657374204944000081525060200191505060405180910390fd5b600160008b815260200190815260200160002060009055600260008b81526020019081526020016000206000905560008973ffffffffffffffffffffffffffffffffffffffff16600560009054906101000a900460e01b8c8a8a8a8a604051602401808681526020","0180602001806020018381038352878782818152602001925080828437600081840152601f19601f8201169050808301925050508381038252858582818152602001925080828437600081840152601f19601f820116905080830192505050975050505050505050604051602081830303815290604052907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b60208310610c9e5780518252602082019150602081019050602083039250610c7b565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610d00576040519150601f19603f3d011682016040523d82523d6000602084013e610d05565b606091505b50509050809350505050979650505050505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610df7576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180610f3e6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600080828401905083811015610f33576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b809150509291505056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373a2646970667358221220717956915d98801ce573f6d7e028ff018b65267c205c51d6293c768048623a9f64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"_chainId\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"_groupId\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"address\",\"name\":\"coreAddress\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"callbackAddr\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"bytes32\",\"name\":\"requestId\",\"type\":\"bytes32\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"url\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"expiration\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"timesAmount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"bool\",\"name\":\"needProof\",\"type\":\"bool\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"returnType\",\"type\":\"uint256\"}],\"name\":\"OracleRequest\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"bytes32\",\"name\":\"_requestId\",\"type\":\"bytes32\"},{\"internalType\":\"address\",\"name\":\"_callbackAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_expiration\",\"type\":\"uint256\"},{\"internalType\":\"bytes\",\"name\":\"_result\",\"type\":\"bytes\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"fulfillRequest\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[4089100845,3807325511],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[3]},{\"kind\":4,\"value\":[4]}],\"inputs\":[],\"name\":\"getChainIdAndGroupId\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[2314240274,375907380],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"inputs\":[],\"name\":\"isOwner\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[2402473371,3991463209],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"selector\":[2376452955,1351213768],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":4,\"value\":[3]},{\"kind\":4,\"value\":[4]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"_callbackAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"_nonce\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"_url\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"_timesAmount\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"_expiryTime\",\"type\":\"uint256\"},{\"internalType\":\"bool\",\"name\":\"_needProof\",\"type\":\"bool\"},{\"internalType\":\"uint256\",\"name\":\"_returnType\",\"type\":\"uint256\"}],\"name\":\"query\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[2069504051,1148828689],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"selector\":[4076725131,382390570],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_FULFILLREQUEST = "fulfillRequest";

    public static final String FUNC_GETCHAINIDANDGROUPID = "getChainIdAndGroupId";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_QUERY = "query";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event ORACLEREQUEST_EVENT = new Event("OracleRequest", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected OracleCore(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<OracleRequestEventResponse> getOracleRequestEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ORACLEREQUEST_EVENT, transactionReceipt);
        ArrayList<OracleRequestEventResponse> responses = new ArrayList<OracleRequestEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OracleRequestEventResponse typedResponse = new OracleRequestEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.coreAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.callbackAddr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.requestId = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.url = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.expiration = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.timesAmount = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.needProof = (Boolean) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.returnType = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public TransactionReceipt fulfillRequest(byte[] _requestId, String _callbackAddress,
            BigInteger _expiration, byte[] _result, byte[] proof) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiration), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(_result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String fulfillRequest(byte[] _requestId, String _callbackAddress, BigInteger _expiration,
            byte[] _result, byte[] proof, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiration), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(_result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForFulfillRequest(byte[] _requestId, String _callbackAddress,
            BigInteger _expiration, byte[] _result, byte[] proof) {
        final Function function = new Function(
                FUNC_FULFILLREQUEST, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32(_requestId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiration), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(_result), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple5<byte[], String, BigInteger, byte[], byte[]> getFulfillRequestInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_FULFILLREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<byte[], String, BigInteger, byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (byte[]) results.get(3).getValue(), 
                (byte[]) results.get(4).getValue()
                );
    }

    public Tuple1<Boolean> getFulfillRequestOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_FULFILLREQUEST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public Tuple2<String, String> getChainIdAndGroupId() throws ContractException {
        final Function function = new Function(FUNC_GETCHAINIDANDGROUPID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple2<String, String>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue());
    }

    public Boolean isOwner() throws ContractException {
        final Function function = new Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public String owner() throws ContractException {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt query(String _callbackAddress, BigInteger _nonce, String _url,
            BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof,
            BigInteger _returnType) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(_needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String query(String _callbackAddress, BigInteger _nonce, String _url,
            BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof,
            BigInteger _returnType, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(_needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForQuery(String _callbackAddress, BigInteger _nonce,
            String _url, BigInteger _timesAmount, BigInteger _expiryTime, Boolean _needProof,
            BigInteger _returnType) {
        final Function function = new Function(
                FUNC_QUERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_callbackAddress), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_nonce), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_url), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_timesAmount), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_expiryTime), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Bool(_needProof), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(_returnType)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple7<String, BigInteger, String, BigInteger, BigInteger, Boolean, BigInteger> getQueryInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_QUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple7<String, BigInteger, String, BigInteger, BigInteger, Boolean, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue(), 
                (Boolean) results.get(5).getValue(), 
                (BigInteger) results.get(6).getValue()
                );
    }

    public Tuple1<Boolean> getQueryOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_QUERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String transferOwnership(String newOwner, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForTransferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<String> getTransferOwnershipInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public static OracleCore load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new OracleCore(contractAddress, client, credential);
    }

    public static OracleCore deploy(Client client, CryptoKeyPair credential, String _chainId,
            String _groupId) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_chainId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(_groupId)));
        return deploy(OracleCore.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
    }

    public static class OracleRequestEventResponse {
        public TransactionReceipt.Logs log;

        public String coreAddress;

        public String callbackAddr;

        public byte[] requestId;

        public String url;

        public BigInteger expiration;

        public BigInteger timesAmount;

        public Boolean needProof;

        public BigInteger returnType;
    }

    public static class OwnershipTransferredEventResponse {
        public TransactionReceipt.Logs log;

        public String previousOwner;

        public String newOwner;
    }
}
