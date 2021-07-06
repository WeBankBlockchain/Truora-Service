package com.webank.oracle.test.transaction.bac.lotteryUseVRF;

import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.*;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes4;
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
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

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
public class LotteryBacUseVrf extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516115e63803806115e68339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a380600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006002819055506002600060146101000a81548160ff021916908360028111156101b357fe5b0217905550505061141d806101c96000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063bead45cf1161008c578063dc3a212b11610066578063dc3a212b146103d1578063e580f47b14610493578063f2fde38b146104b1578063f71d96cb146104f5576100cf565b8063bead45cf14610252578063c73d16ae14610270578063d0e30db0146103c7576100cf565b80635d495aea146100d4578063814479991461011e5780638da5cb5b1461014a5780638f32d59b146101945780639156b7af146101b6578063a2fb1175146101e4575b600080fd5b6100dc610563565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610126610b36565b6040518082600281111561013657fe5b60ff16815260200191505060405180910390f35b610152610b49565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61019c610b72565b604051808215151515815260200191505060405180910390f35b6101e2600480360360208110156101cc57600080fd5b8101908080359060200190929190505050610bc9565b005b610210600480360360208110156101fa57600080fd5b8101908080359060200190929190505050610d41565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61025a610d74565b6040518082815260200191505060405180910390f35b6103736004803603608081101561028657600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156102ed57600080fd5b8201836020820111156102ff57600080fd5b8035906020019184600183028401116401000000008311171561032157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610d7a565b60405180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060405180910390f35b6103cf610d8e565b005b610491600480360360408110156103e757600080fd5b810190808035906020019064010000000081111561040457600080fd5b82018360208201111561041657600080fd5b8035906020019184602083028401116401000000008311171561043857600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190505050610f5b565b005b61049b6110c3565b6040518082815260200191505060405180910390f35b6104f3600480360360208110156104c757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110c9565b005b6105216004803603602081101561050b57600080fd5b810190808035906020019092919050505061114f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60006001600281111561057257fe5b600060149054906101000a900460ff16600281111561058d57fe5b14610600576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f63616e2774207069636b2077696e6e657220796574210000000000000000000081525060200191505060405180910390fd5b60001515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c75f695a6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561067957600080fd5b505afa15801561068d573d6000803e3d6000fd5b505050506040513d60208110156106a357600080fd5b810190808051906020019092919050505015151461070c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806113c36025913960400191505060405180910390fd5b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632dff0d0d6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561078357600080fd5b505afa158015610797573d6000803e3d6000fd5b505050506040513d60208110156107ad57600080fd5b81019080805190602001909291905050509050600060018054905082816107d057fe5b0690506000600182815481106107e257fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc682600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663e3d670d7306040518263ffffffff1660e01b8152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b1580156108ed57600080fd5b505afa158015610901573d6000803e3d6000fd5b505050506040513d602081101561091757600080fd5b81019080805190602001909291905050506040518363ffffffff1660e01b8152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001806020018281038252601f8152602001807f72657761726420746f20746865206c75636b7920647261772077696e6e6572008152506020019350505050600060405180830381600087803b1580156109ca57600080fd5b505af11580156109de573d6000803e3d6000fd5b505050508060056000600254815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600067ffffffffffffffff81118015610a4e57600080fd5b50604051908082528060200260200182016040528015610a7d5781602001602082028036833780820191505090505b5060019080519060200190610a939291906112cf565b506002600060146101000a81548160ff02191690836002811115610ab357fe5b02179055507f1c2ae1f7e99dbbbfe25aac964f3889ea68259d88b74709f53037a6fb930bb02a6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b600060149054906101000a900460ff1681565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b60006002811115610bd657fe5b600060149054906101000a900460ff166002811115610bf157fe5b14610c64576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b37217a4826040518263ffffffff1660e01b815260040180828152602001915050602060405180830381600087803b158015610cd957600080fd5b505af1158015610ced573d6000803e3d6000fd5b505050506040513d6020811015610d0357600080fd5b81019080805190602001909291905050506004819055506001600060146101000a81548160ff02191690836002811115610d3957fe5b021790555050565b60056020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60075481565b600063c73d16ae60e01b9050949350505050565b60006002811115610d9b57fe5b600060149054906101000a900460ff166002811115610db657fe5b14610e29576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600660009054906101000a9004","73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d0e7d61133306007546040518463ffffffff1660e01b8152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200180602001828103825260128152602001807f6465706f73697420746f206c6f74746572790000000000000000000000000000815250602001945050505050600060405180830381600087803b158015610f4157600080fd5b505af1158015610f55573d6000803e3d6000fd5b50505050565b600280811115610f6757fe5b600060149054906101000a900460ff166002811115610f8257fe5b14610ff5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b600181101561106c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f746865206d696e696d756d2076616c7565206f6620616d6f756e74206973203181525060200191505060405180910390fd5b60008060146101000a81548160ff0219169083600281111561108a57fe5b021790555081600190805190602001906110a59291906112cf565b50806007819055506002600081548092919060010191905055505050565b60025481565b6110d1610b72565b611143576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b61114c8161118b565b50565b6001818154811061115c57fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415611211576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602681526020018061139d6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b828054828255906000526020600020908101928215611348579160200282015b828111156113475782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906112ef565b5b5090506113559190611359565b5090565b61139991905b8082111561139557600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555060010161135f565b5090565b9056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373206f7261636c6520717565727920686173206e6f74206265656e2066756c66696c6c656421a26469706673582212200a1540d51cbbe2e715ee610e1605bc82cb6d052ed43bcce4f89985189c89b17664736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"randomOracle\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"bac001Address\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"lotteryId\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"winner\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"ramdomness\",\"type\":\"uint256\"}],\"name\":\"Winner\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"deposit\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"deposit_amount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"isOwner\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"lotteryId\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"lottery_state\",\"outputs\":[{\"internalType\":\"enum LotteryBacUseVrf.LOTTERY_STATE\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"bytes\",\"name\":\"\",\"type\":\"bytes\"}],\"name\":\"onBAC001Received\",\"outputs\":[{\"internalType\":\"bytes4\",\"name\":\"\",\"type\":\"bytes4\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"pickWinner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"players\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address[]\",\"name\":\"_players\",\"type\":\"address[]\"},{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"start_new_lottery\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"userProvidedSeed\",\"type\":\"uint256\"}],\"name\":\"stop_deposit\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"winners\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516115e63803806115e68339818101604052604081101561003357600080fd5b810190808051906020019092919080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a380600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006002819055506002600060146101000a81548160ff021916908360028111156101b357fe5b0217905550505061141d806101c96000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063975fcaa01161008c578063d8d50dc911610066578063d8d50dc9146104c9578063e5501039146104f7578063eafeeaf514610523578063ede8e52914610541576100cf565b8063975fcaa014610457578063d157ac76146104a1578063d865927d146104ab576100cf565b80630aa78678146100d457806316cad12a1461022b5780631c515b921461026f5780635089e2c8146102dd578063822111001461032757806386e053e3146103e9575b600080fd5b6101d7600480360360808110156100ea57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019064010000000081111561015157600080fd5b82018360208201111561016357600080fd5b8035906020019184600183028401116401000000008311171561018557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610563565b60405180827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815260200191505060405180910390f35b61026d6004803603602081101561024157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610577565b005b61029b6004803603602081101561028557600080fd5b81019080803590602001909291905050506105fd565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102e5610630565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103e76004803603604081101561033d57600080fd5b810190808035906020019064010000000081111561035a57600080fd5b82018360208201111561036c57600080fd5b8035906020019184602083028401116401000000008311171561038e57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190505050610659565b005b610415600480360360208110156103ff57600080fd5b81019080803590602001909291905050506107c1565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61045f6107fd565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6104a9610dd0565b005b6104b3610f9d565b6040518082815260200191505060405180910390f35b6104f5600480360360208110156104df57600080fd5b8101908080359060200190929190505050610fa3565b005b6104ff61111b565b6040518082600281111561050f57fe5b60ff16815260200191505060405180910390f35b61052b61112e565b6040518082815260200191505060405180910390f35b610549611134565b604051808215151515815260200191505060405180910390f35b6000630aa7867860e01b9050949350505050565b61057f611134565b6105f1576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b6105fa8161118b565b50565b60056020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60028081111561066557fe5b600060149054906101000a900460ff16600281111561068057fe5b146106f3576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b600181101561076a576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f746865206d696e696d756d2076616c7565206f6620616d6f756e74206973203181525060200191505060405180910390fd5b60008060146101000a81548160ff0219169083600281111561078857fe5b021790555081600190805190602001906107a39291906112cf565b50806007819055506002600081548092919060010191905055505050565b600181815481106107ce57fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60006001600281111561080c57fe5b600060149054906101000a900460ff16600281111561082757fe5b1461089a576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f63616e2774207069636b2077696e6e657220796574210000000000000000000081525060200191505060405180910390fd5b60001515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166356ddd8ed6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561091357600080fd5b505afa158015610927573d6000803e3d6000fd5b505050506040513d602081101561093d57600080fd5b81019080805190602001909291905050501515146109a6576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806113c36025913960400191505060405180910390fd5b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b791ee546004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b158015610a1d57600080fd5b505afa158015610a31573d6000803e3d6000fd5b505050506040513d6020811015610a4757600080fd5b8101908080519060200190929190505050905060006001805490508281610a6a57fe5b069050600060018281548110610a7c57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166393fa8f3e82600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639d4834e5306040518263ffffffff1660e01b8152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b158015610b8757600080fd5b505afa158015610b9b573d6000803e3d6000fd5b505050506040513d6020811015610bb157600080fd5b81019080805190602001909291905050506040518363ffffffff1660e01b8152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001806020018281038252601f8152602001807f72657761726420746f20746865206c75636b7920647261772077696e6e6572008152506020019350505050600060405180830381600087803b158015610c6457600080fd5b505af1158015610c78573d6000803e3d6000fd5b505050508060056000600254815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600067ffffffffffffffff81118015610ce857600080fd5b50604051908082528060200260200182016040528015610d175781602001602082028036833780820191505090505b5060019080519060200190610d2d9291906112cf565b506002600060146101000a81548160ff02191690836002811115610d4d57fe5b02179055507f16f7a6a62614f6f8f716bc3e58eb0bc352ca0fe22427017ad411d8256eaa900b6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b60006002811115610ddd57fe5b600060149054906101000a900460ff166002811115610df857fe5b14610e6b576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f81526020","01807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166380b30b3f33306007546040518463ffffffff1660e01b8152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200180602001828103825260128152602001807f6465706f73697420746f206c6f74746572790000000000000000000000000000815250602001945050505050600060405180830381600087803b158015610f8357600080fd5b505af1158015610f97573d6000803e3d6000fd5b50505050565b60025481565b60006002811115610fb057fe5b600060149054906101000a900460ff166002811115610fcb57fe5b1461103e576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f746865206c6f747465727920686173206e6f742079657420737461727465640081525060200191505060405180910390fd5b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f724a279826040518263ffffffff1660e01b815260040180828152602001915050602060405180830381600087803b1580156110b357600080fd5b505af11580156110c7573d6000803e3d6000fd5b505050506040513d60208110156110dd57600080fd5b81019080805190602001909291905050506004819055506001600060146101000a81548160ff0219169083600281111561111357fe5b021790555050565b600060149054906101000a900460ff1681565b60075481565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614905090565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415611211576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252602681526020018061139d6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff166000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f5c7c30d4a0f08950cb23be4132957b357fa5dfdb0fcf218f81b86a1c036e47d060405160405180910390a3806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b828054828255906000526020600020908101928215611348579160200282015b828111156113475782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906112ef565b5b5090506113559190611359565b5090565b61139991905b8082111561139557600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555060010161135f565b5090565b9056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373206f7261636c6520717565727920686173206e6f74206265656e2066756c66696c6c656421a2646970667358221220442a498da5517f9991e4df9d4e29938187214fe359bf4843822255e3edb287b864736f6c634300060a0033"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_DEPOSIT_AMOUNT = "deposit_amount";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_LOTTERYID = "lotteryId";

    public static final String FUNC_LOTTERY_STATE = "lottery_state";

    public static final String FUNC_ONBAC001RECEIVED = "onBAC001Received";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PICKWINNER = "pickWinner";

    public static final String FUNC_PLAYERS = "players";

    public static final String FUNC_START_NEW_LOTTERY = "start_new_lottery";

    public static final String FUNC_STOP_DEPOSIT = "stop_deposit";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_WINNERS = "winners";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event WINNER_EVENT = new Event("Winner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected LotteryBacUseVrf(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LotteryBacUseVrf(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LotteryBacUseVrf(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LotteryBacUseVrf(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
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

    public List<WinnerEventResponse> getWinnerEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(WINNER_EVENT, transactionReceipt);
        ArrayList<WinnerEventResponse> responses = new ArrayList<WinnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            WinnerEventResponse typedResponse = new WinnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lotteryId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ramdomness = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerWinnerEventLogFilter(String fromBlock, String toBlock, List<String> otherTopics, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(WINNER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void registerWinnerEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(WINNER_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public RemoteCall<TransactionReceipt> deposit() {
        final Function function = new Function(
                FUNC_DEPOSIT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void deposit(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_DEPOSIT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String depositSeq() {
        final Function function = new Function(
                FUNC_DEPOSIT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<BigInteger> deposit_amount() {
        final Function function = new Function(FUNC_DEPOSIT_AMOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isOwner() {
        final Function function = new Function(FUNC_ISOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> lotteryId() {
        final Function function = new Function(FUNC_LOTTERYID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> lottery_state() {
        final Function function = new Function(FUNC_LOTTERY_STATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED,
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED,
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String onBAC001ReceivedSeq(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED,
                Arrays.<Type>asList(new Address(param0),
                new Address(param1),
                new Uint256(param2),
                new DynamicBytes(param3)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple4<String, String, BigInteger, byte[]> getOnBAC001ReceivedInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ONBAC001RECEIVED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple4<String, String, BigInteger, byte[]>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (byte[]) results.get(3).getValue()
                );
    }

    public Tuple1<byte[]> getOnBAC001ReceivedOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ONBAC001RECEIVED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> pickWinner() {
        final Function function = new Function(
                FUNC_PICKWINNER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void pickWinner(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PICKWINNER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String pickWinnerSeq() {
        final Function function = new Function(
                FUNC_PICKWINNER,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getPickWinnerOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_PICKWINNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public RemoteCall<String> players(BigInteger param0) {
        final Function function = new Function(FUNC_PLAYERS,
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> start_new_lottery(List<String> _players, BigInteger amount) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY,
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void start_new_lottery(List<String> _players, BigInteger amount, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY,
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String start_new_lotterySeq(List<String> _players, BigInteger amount) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY,
                Arrays.<Type>asList(_players.isEmpty()? DynamicArray.empty("address[]"):new DynamicArray<Address>(
                        org.fisco.bcos.web3j.abi.Utils.typeMap(_players, Address.class)),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<List<String>, BigInteger> getStart_new_lotteryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_START_NEW_LOTTERY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<List<String>, BigInteger>(

                convertToNative((List<Address>) results.get(0).getValue()),
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> stop_deposit(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT,
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void stop_deposit(BigInteger userProvidedSeed, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT,
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String stop_depositSeq(BigInteger userProvidedSeed) {
        final Function function = new Function(
                FUNC_STOP_DEPOSIT,
                Arrays.<Type>asList(new Uint256(userProvidedSeed)),
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<BigInteger> getStop_depositInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_STOP_DEPOSIT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
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

    public RemoteCall<String> winners(BigInteger param0) {
        final Function function = new Function(FUNC_WINNERS,
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static LotteryBacUseVrf load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryBacUseVrf(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LotteryBacUseVrf load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryBacUseVrf(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LotteryBacUseVrf load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LotteryBacUseVrf(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LotteryBacUseVrf load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LotteryBacUseVrf(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LotteryBacUseVrf> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacUseVrf.class, web3j, credentials, contractGasProvider, getBinary(), encodedConstructor);
    }

    public static RemoteCall<LotteryBacUseVrf> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacUseVrf.class, web3j, transactionManager, contractGasProvider, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryBacUseVrf> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacUseVrf.class, web3j, credentials, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LotteryBacUseVrf> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String randomOracle, String bac001Address) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(randomOracle),
                new Address(bac001Address)));
        return deployRemoteCall(LotteryBacUseVrf.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), encodedConstructor);
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }

    public static class WinnerEventResponse {
        public Log log;

        public BigInteger lotteryId;

        public String winner;

        public BigInteger ramdomness;
    }
}
