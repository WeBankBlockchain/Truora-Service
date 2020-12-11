package com.webank.oracle.test.transaction.oracle;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class Aggregator extends Contract {
    public static String BINARY = "6080604052600180556001600955670de0b6b3a7640000600f553480156200002657600080fd5b5060405162001fde38038062001fde833981810160405260408110156200004c57600080fd5b8101908080519060200190929190805160405193929190846401000000008211156200007757600080fd5b838201915060208201858111156200008e57600080fd5b8251866020820283011164010000000082111715620000ac57600080fd5b8083526020830192505050908051906020019060200280838360005b83811015620000e5578082015181840152602081019050620000c8565b5050505090500160405250505033600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3620001c28282620001ca60201b60201c565b5050620004bb565b620001da6200038e60201b60201c565b6200024d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b816fffffffffffffffffffffffffffffffff1681601c81511115620002da576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f63616e6e6f742068617665206d6f7265207468616e203435206f7261636c657381525060200191505060405180910390fd5b818151101562000336576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f81526020018062001faf602f913960400191505060405180910390fd5b83600760006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff160217905550826008908051906020019062000387929190620003e6565b5050505050565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b82805482825590600052602060002090810192821562000462579160200282015b82811115620004615782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055509160200191906001019062000407565b5b50905062000471919062000475565b5090565b620004b891905b80821115620004b457600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055506001016200047c565b5090565b90565b611ae480620004cb6000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80638f32d59b1161008c578063b633620c11610066578063b633620c14610360578063daa6d556146103a2578063e8d0a0d2146103ac578063f2fde38b146103e4576100cf565b80638f32d59b14610228578063b0f7756c1461024a578063b5ab58dc1461031e576100cf565b806350d25bcd146100d457806354bcd7ff146100f25780635b69a7d814610134578063668a0f02146101a25780638205bf6a146101c05780638da5cb5b146101de575b600080fd5b6100dc610428565b6040518082815260200191505060405180910390f35b6100fa610445565b60405180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101606004803603602081101561014a57600080fd5b8101908080359060200190929190505050610467565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101aa6104a3565b6040518082815260200191505060405180910390f35b6101c86104ad565b6040518082815260200191505060405180910390f35b6101e66104ca565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102306104f4565b604051808215151515815260200191505060405180910390f35b61031c6004803603604081101561026057600080fd5b8101908080356fffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561029957600080fd5b8201836020820111156102ab57600080fd5b803590602001918460208302840111640100000000831117156102cd57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f82011690508083019250505050505050919291929050505061054c565b005b61034a6004803603602081101561033457600080fd5b8101908080359060200190929190505050610702565b6040518082815260200191505060405180910390f35b61038c6004803603602081101561037657600080fd5b810190808035906020019092919050505061071f565b6040518082815260200191505060405180910390f35b6103aa61073c565b005b6103e2600480360360408110156103c257600080fd5b810190808035906020019092919080359060200190929190505050610a04565b005b610426600480360360208110156103fa57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610be8565b005b6000600c6000600654815260200190815260200160002054905090565b600760009054906101000a90046fffffffffffffffffffffffffffffffff1681565b6008818154811061047457fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600654905090565b6000600d6000600654815260200190815260200160002054905090565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b6105546104f4565b6105c6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b816fffffffffffffffffffffffffffffffff1681601c81511115610652576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f63616e6e6f742068617665206d6f7265207468616e203435206f7261636c657381525060200191505060405180910390fd5b81815110156106ac576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f815260200180611a58602f913960400191505060405180910390fd5b83600760006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff16021790555082600890805190602001906106fb9291906118fd565b5050505050565b6000600c6000838152602001908152602001600020549050919050565b6000600d6000838152602001908152602001600020549050919050565b6107446104f4565b6107b6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600080600090505b6008805490508110156108d2576108a9600882815481106107db57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600e8054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561089c5780601f106108715761010080835404028352916020019161089c565b820191906000526020600020905b81548152906001019060200180831161087f57829003601f168201915b5050505050600f54610c6e565b9150600954600a60008481526020019081526020016000208190555080806001019150506107be565b50600760009054906101000a90046fffffffffffffffffffffffffffffffff16600b6000600954815260200190815260200160002060000160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff160217905550600880549050600b6000600954815260200190815260200160002060000160106101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff1602179055503373ffffffffffffffffffffffffffffffffffffffff166009547f0109fc6f55cf40689f02fbaad7af7fe7bbac8a3d2186600afc7d3e10cac60271426040518082815260200191505060405180910390a36109fb6001600954610f7790919063ffffffff16565b60098190555050565b816002600082815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610abc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526028815260200180611a876028913960400191505060405180910390fd5b6002600082815260200190815260200160002060006101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055807f6930ee93026433301a14b1baa0b52ca0d4640cb1edaa703beeb9056e2352698460405160405180910390a26000600a6000858152602001908152602001600020549050600a600085815260200190815260200160002060009055600b60008281526020019081526020016000206001018390806001815401808255809150506001900390600052602060002001600090919091909150553373ffffffffffffffffffffffffffffffffffffffff1681847fb51168059c83c860caf5b830c5d2e64c2172c6fb2fe9f25447d9838e18d93b6060405160405180910390a4610bd981610fff565b610be2816112e1565b50505050565b610bf06104f4565b610c62576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b610c6b816113b8565b50565b600030600154604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b815260140182815260200192505050604051602081830303815290604052805190602001209050836002600083815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550807f9dc80e952f32e8aef7828795a4cc11bbe605c2ad3cc626df8115be4f78338f6960405160405180910390a2836000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fab4cace3060015486866040518563ffffffff1660e01b8152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b83811015610e61578082015181840152602081019050610e46565b50505050905090810190601f168015610e8e5780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610eb057600080fd5b505af1158015610ec4573d6000803e3d6000fd5b505050506040513d6020811015610eda57600080fd5b8101908080519060200190929190505050610f5d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f6f7261636c652d636f726520696e766f6b65206661696c65642100000000000081525060200191505060405180910390fd5b600180600082825401925050819055508090509392505050565b600080828401905083811015610ff5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f536166654d6174683a206164646974696f6e206f766572666c6f77000000000081525060200191505060405180910390fd5b8091505092915050565b80600b600082815260200190815260200160002060000160009054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16600b600083815260200190815260200160002060010180549050106112dd578180600654116112db576000600b600085815260200190815260200160002060010180549050905060006110a26002836114fe90919063ffffffff16565b9050600080600284816110b157fe5b0614156111d5576000611128600b600089815260200190815260200160002060010180548060200260200160405190810160405280929190818152602001828054801561111d57602002820191906000526020600020905b815481526020019060010190808311611109575b50505050508461158d565b905060006111ad600b60008a815260200190815260200160002060010180548060200260200160405190810160405280929190818152602001828054801561118f57602002820191906000526020600020905b81548152602001906001019080831161117b575b50505050506111a8600187610f7790919063ffffffff16565b61158d565b905060026111c482846117d690919063ffffffff16565b816111cb57fe5b0592505050611259565b611256600b600088815260200190815260200160002060010180548060200260200160405190810160405280929190818152602001828054801561123857602002820191906000526020600020905b815481526020019060010190808311611224575b5050505050611251600185610f7790919063ffffffff16565b61158d565b90505b80600481905550856006819055504260058190555042600d60008881526020019081526020016000208190555080600c60008881526020019081526020016000208190555085817f0559884fd3a460db3073b7fc896cc77986f16e378210ded43186175bf646fc5f426040518082815260200191505060405180910390a35050505b505b5050565b80600b600082815260200190815260200160002060000160109054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16600b60008381526020019081526020016000206001018054905014156113b457600b6000838152602001908152602001600020600080820160006101000a8154906fffffffffffffffffffffffffffffffff02191690556000820160106101000a8154906fffffffffffffffffffffffffffffffff02191690556001820160006113b19190611987565b50505b5050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561143e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180611a116026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff16600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a380600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000808211611575576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f536166654d6174683a206469766973696f6e206279207a65726f00000000000081525060200191505060405180910390fd5b600082848161158057fe5b0490508091505092915050565b60006060839050600083905060008251905060608167ffffffffffffffff811180156115b857600080fd5b506040519080825280602002602001820160405280156115e75781602001602082028036833780820191505090505b50905060608267ffffffffffffffff8111801561160357600080fd5b506040519080825280602002602001820160405280156116325781602001602082028036833780820191505090505b5090506000806000805b6001156117c657886116586002896114fe90919063ffffffff16565b8151811061166257fe5b602002602001015191506000935060009250600090505b86811015611733578189828151811061168e57fe5b602002602001015112156116d5578881815181106116a857fe5b60200260200101518685815181106116bc57fe5b6020026020010181815250508380600101945050611726565b818982815181106116e257fe5b60200260200101511315611725578881815181106116fc57fe5b602002602001015185848151811061171057fe5b60200260200101818152505082806001019350505b5b8080600101915050611679565b838811611754578396506117478987611864565b809750819a5050506117c1565b611767838861187490919063ffffffff16565b8811156117af57611793611784848961187490919063ffffffff16565b8961187490919063ffffffff16565b97508296506117a28986611864565b809650819a5050506117c0565b8199505050505050505050506117d0565b5b61163c565b5050505050505050505b92915050565b6000808284019050600083121580156117ef5750838112155b80611805575060008312801561180457508381125b5b61185a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526021815260200180611a376021913960400191505060405180910390fd5b8091505092915050565b6060808284915091509250929050565b6000828211156118ec576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601e8152602001807f536166654d6174683a207375627472616374696f6e206f766572666c6f77000081525060200191505060405180910390fd5b600082840390508091505092915050565b828054828255906000526020600020908101928215611976579160200282015b828111156119755782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055509160200191906001019061191d565b5b50905061198391906119a8565b5090565b50805460008255906000526020600020908101906119a591906119eb565b50565b6119e891905b808211156119e457600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055506001016119ae565b5090565b90565b611a0d91905b80821115611a095760008160009055506001016119f1565b5090565b9056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f20616464726573735369676e6564536166654d6174683a206164646974696f6e206f766572666c6f776d7573742068617665206174206c65617374206173206d616e79206f7261636c657320617320726573706f6e736573536f75726365206d75737420626520746865206f7261636c65206f66207468652072657175657374a2646970667358221220841d9dfbb710c7aa4341b0c0599a78802afc7d9d959a3697f24946824c3adcff64736f6c634300060600336d7573742068617665206174206c65617374206173206d616e79206f7261636c657320617320726573706f6e736573";

    public static final String ABI = "[{\"inputs\": [{\"internalType\": \"uint128\", \"name\": \"_minimumResponses\", \"type\": \"uint128\"}, {\"internalType\": \"address[]\", \"name\": \"_oracles\", \"type\": \"address[]\"}], \"stateMutability\": \"nonpayable\", \"type\": \"constructor\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"int256\", \"name\": \"current\", \"type\": \"int256\"}, {\"indexed\": true, \"internalType\": \"uint256\", \"name\": \"roundId\", \"type\": \"uint256\"}, {\"indexed\": false, \"internalType\": \"uint256\", \"name\": \"timestamp\", \"type\": \"uint256\"}], \"name\": \"AnswerUpdated\", \"type\": \"event\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"bytes32\", \"name\": \"id\", \"type\": \"bytes32\"}], \"name\": \"Fulfilled\", \"type\": \"event\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"uint256\", \"name\": \"roundId\", \"type\": \"uint256\"}, {\"indexed\": true, \"internalType\": \"address\", \"name\": \"startedBy\", \"type\": \"address\"}, {\"indexed\": false, \"internalType\": \"uint256\", \"name\": \"startedAt\", \"type\": \"uint256\"}], \"name\": \"NewRound\", \"type\": \"event\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"address\", \"name\": \"previousOwner\", \"type\": \"address\"}, {\"indexed\": true, \"internalType\": \"address\", \"name\": \"newOwner\", \"type\": \"address\"}], \"name\": \"OwnershipTransferred\", \"type\": \"event\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"bytes32\", \"name\": \"id\", \"type\": \"bytes32\"}], \"name\": \"Requested\", \"type\": \"event\"}, {\"anonymous\": false, \"inputs\": [{\"indexed\": true, \"internalType\": \"int256\", \"name\": \"response\", \"type\": \"int256\"}, {\"indexed\": true, \"internalType\": \"uint256\", \"name\": \"answerId\", \"type\": \"uint256\"}, {\"indexed\": true, \"internalType\": \"address\", \"name\": \"sender\", \"type\": \"address\"}], \"name\": \"ResponseReceived\", \"type\": \"event\"}, {\"inputs\": [{\"internalType\": \"bytes32\", \"name\": \"requestId\", \"type\": \"bytes32\"}, {\"internalType\": \"int256\", \"name\": \"result\", \"type\": \"int256\"}], \"name\": \"__callback\", \"outputs\": [], \"stateMutability\": \"nonpayable\", \"type\": \"function\"}, {\"inputs\": [{\"internalType\": \"uint256\", \"name\": \"_roundId\", \"type\": \"uint256\"}], \"name\": \"getAnswer\", \"outputs\": [{\"internalType\": \"int256\", \"name\": \"\", \"type\": \"int256\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [{\"internalType\": \"uint256\", \"name\": \"_roundId\", \"type\": \"uint256\"}], \"name\": \"getTimestamp\", \"outputs\": [{\"internalType\": \"uint256\", \"name\": \"\", \"type\": \"uint256\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"isOwner\", \"outputs\": [{\"internalType\": \"bool\", \"name\": \"\", \"type\": \"bool\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"latestAnswer\", \"outputs\": [{\"internalType\": \"int256\", \"name\": \"\", \"type\": \"int256\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"latestRound\", \"outputs\": [{\"internalType\": \"uint256\", \"name\": \"\", \"type\": \"uint256\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"latestTimestamp\", \"outputs\": [{\"internalType\": \"uint256\", \"name\": \"\", \"type\": \"uint256\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"minimumResponses\", \"outputs\": [{\"internalType\": \"uint128\", \"name\": \"\", \"type\": \"uint128\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [{\"internalType\": \"uint256\", \"name\": \"\", \"type\": \"uint256\"}], \"name\": \"oracles\", \"outputs\": [{\"internalType\": \"address\", \"name\": \"\", \"type\": \"address\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"owner\", \"outputs\": [{\"internalType\": \"address\", \"name\": \"\", \"type\": \"address\"}], \"stateMutability\": \"view\", \"type\": \"function\"}, {\"inputs\": [], \"name\": \"requestRateUpdate\", \"outputs\": [], \"stateMutability\": \"nonpayable\", \"type\": \"function\"}, {\"inputs\": [{\"internalType\": \"address\", \"name\": \"newOwner\", \"type\": \"address\"}], \"name\": \"transferOwnership\", \"outputs\": [], \"stateMutability\": \"nonpayable\", \"type\": \"function\"}, {\"inputs\": [{\"internalType\": \"uint128\", \"name\": \"_minimumResponses\", \"type\": \"uint128\"}, {\"internalType\": \"address[]\", \"name\": \"_oracles\", \"type\": \"address[]\"}], \"name\": \"updateRequestDetails\", \"outputs\": [], \"stateMutability\": \"nonpayable\", \"type\": \"function\"}]";

    public static final String FUNC___CALLBACK = "__callback";

    public static final String FUNC_GETANSWER = "getAnswer";

    public static final String FUNC_GETTIMESTAMP = "getTimestamp";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_LATESTANSWER = "latestAnswer";

    public static final String FUNC_LATESTROUND = "latestRound";

    public static final String FUNC_LATESTTIMESTAMP = "latestTimestamp";

    public static final String FUNC_MINIMUMRESPONSES = "minimumResponses";

    public static final String FUNC_ORACLES = "oracles";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REQUESTRATEUPDATE = "requestRateUpdate";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPDATEREQUESTDETAILS = "updateRequestDetails";

    public static final Event ANSWERUPDATED_EVENT = new Event("AnswerUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event FULFILLED_EVENT = new Event("Fulfilled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event NEWROUND_EVENT = new Event("NewRound", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event REQUESTED_EVENT = new Event("Requested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event RESPONSERECEIVED_EVENT = new Event("ResponseReceived", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Aggregator(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Aggregator(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Aggregator(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Aggregator(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AnswerUpdatedEventResponse> getAnswerUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ANSWERUPDATED_EVENT, transactionReceipt);
        ArrayList<AnswerUpdatedEventResponse> responses = new ArrayList<AnswerUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AnswerUpdatedEventResponse typedResponse = new AnswerUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.current = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.roundId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerAnswerUpdatedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ANSWERUPDATED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerAnswerUpdatedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(ANSWERUPDATED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
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

    public void registerFulfilledEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerFulfilledEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(FULFILLED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<NewRoundEventResponse> getNewRoundEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWROUND_EVENT, transactionReceipt);
        ArrayList<NewRoundEventResponse> responses = new ArrayList<NewRoundEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewRoundEventResponse typedResponse = new NewRoundEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roundId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.startedBy = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.startedAt = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerNewRoundEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(NEWROUND_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerNewRoundEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(NEWROUND_EVENT);
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

    public void registerOwnershipTransferredEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerOwnershipTransferredEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT);
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

    public void registerRequestedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerRequestedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(REQUESTED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<ResponseReceivedEventResponse> getResponseReceivedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(RESPONSERECEIVED_EVENT, transactionReceipt);
        ArrayList<ResponseReceivedEventResponse> responses = new ArrayList<ResponseReceivedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ResponseReceivedEventResponse typedResponse = new ResponseReceivedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.response = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.answerId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerResponseReceivedEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RESPONSERECEIVED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerResponseReceivedEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(RESPONSERECEIVED_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] requestId, BigInteger result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new Int256(result)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void __callback(byte[] requestId, BigInteger result, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new Int256(result)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String __callbackSeq(byte[] requestId, BigInteger result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new Bytes32(requestId),
                new Int256(result)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> getAnswer(BigInteger _roundId) {
        final Function function = new Function(
                FUNC_GETANSWER, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void getAnswer(BigInteger _roundId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_GETANSWER, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getAnswerSeq(BigInteger _roundId) {
        final Function function = new Function(
                FUNC_GETANSWER, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> getTimestamp(BigInteger _roundId) {
        final Function function = new Function(
                FUNC_GETTIMESTAMP, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void getTimestamp(BigInteger _roundId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_GETTIMESTAMP, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getTimestampSeq(BigInteger _roundId) {
        final Function function = new Function(
                FUNC_GETTIMESTAMP, 
                Arrays.<Type>asList(new Uint256(_roundId)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> isOwner() {
        final Function function = new Function(
                FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void isOwner(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String isOwnerSeq() {
        final Function function = new Function(
                FUNC_ISOWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> latestAnswer() {
        final Function function = new Function(
                FUNC_LATESTANSWER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void latestAnswer(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_LATESTANSWER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String latestAnswerSeq() {
        final Function function = new Function(
                FUNC_LATESTANSWER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> latestRound() {
        final Function function = new Function(
                FUNC_LATESTROUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void latestRound(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_LATESTROUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String latestRoundSeq() {
        final Function function = new Function(
                FUNC_LATESTROUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> latestTimestamp() {
        final Function function = new Function(
                FUNC_LATESTTIMESTAMP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void latestTimestamp(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_LATESTTIMESTAMP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String latestTimestampSeq() {
        final Function function = new Function(
                FUNC_LATESTTIMESTAMP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> minimumResponses() {
        final Function function = new Function(
                FUNC_MINIMUMRESPONSES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void minimumResponses(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_MINIMUMRESPONSES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String minimumResponsesSeq() {
        final Function function = new Function(
                FUNC_MINIMUMRESPONSES, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> oracles(BigInteger param0) {
        final Function function = new Function(
                FUNC_ORACLES, 
                Arrays.<Type>asList(new Uint256(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void oracles(BigInteger param0, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ORACLES, 
                Arrays.<Type>asList(new Uint256(param0)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String oraclesSeq(BigInteger param0) {
        final Function function = new Function(
                FUNC_ORACLES, 
                Arrays.<Type>asList(new Uint256(param0)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> owner() {
        final Function function = new Function(
                FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void owner(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String ownerSeq() {
        final Function function = new Function(
                FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<TransactionReceipt> requestRateUpdate() {
        final Function function = new Function(
                FUNC_REQUESTRATEUPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void requestRateUpdate(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_REQUESTRATEUPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String requestRateUpdateSeq() {
        final Function function = new Function(
                FUNC_REQUESTRATEUPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
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

    public RemoteCall<TransactionReceipt> updateRequestDetails(BigInteger _minimumResponses, List<String> _oracles) {
        final Function function = new Function(
                FUNC_UPDATEREQUESTDETAILS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void updateRequestDetails(BigInteger _minimumResponses, List<String> _oracles, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEREQUESTDETAILS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String updateRequestDetailsSeq(BigInteger _minimumResponses, List<String> _oracles) {
        final Function function = new Function(
                FUNC_UPDATEREQUESTDETAILS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    @Deprecated
    public static Aggregator load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Aggregator(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Aggregator load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Aggregator(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Aggregator load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Aggregator(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Aggregator load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Aggregator(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Aggregator> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _minimumResponses, List<String> _oracles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))));
        return deployRemoteCall(Aggregator.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Aggregator> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _minimumResponses, List<String> _oracles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))));
        return deployRemoteCall(Aggregator.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Aggregator> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _minimumResponses, List<String> _oracles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))));
        return deployRemoteCall(Aggregator.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Aggregator> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _minimumResponses, List<String> _oracles) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_minimumResponses), 
                _oracles.isEmpty()?org.fisco.bcos.web3j.abi.datatypes.DynamicArray.empty("address[]"):new org.fisco.bcos.web3j.abi.datatypes.DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_oracles, Address.class))));
        return deployRemoteCall(Aggregator.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AnswerUpdatedEventResponse {
        public Log log;

        public BigInteger current;

        public BigInteger roundId;

        public BigInteger timestamp;
    }

    public static class FulfilledEventResponse {
        public Log log;

        public byte[] id;
    }

    public static class NewRoundEventResponse {
        public Log log;

        public BigInteger roundId;

        public String startedBy;

        public BigInteger startedAt;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }

    public static class RequestedEventResponse {
        public Log log;

        public byte[] id;
    }

    public static class ResponseReceivedEventResponse {
        public Log log;

        public BigInteger response;

        public BigInteger answerId;

        public String sender;
    }
}
