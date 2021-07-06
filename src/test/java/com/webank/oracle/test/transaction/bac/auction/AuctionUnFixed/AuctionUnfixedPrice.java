package com.webank.oracle.test.transaction.bac.auction.AuctionUnFixed;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.DynamicBytes;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes4;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint128;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tuples.generated.Tuple5;
import org.fisco.bcos.web3j.tuples.generated.Tuple7;
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
public class AuctionUnfixedPrice extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50612326806100206000396000f3fe6080604052600436106100865760003560e01c8063b4598d8211610059578063b4598d8214610136578063c73d16ae14610174578063cdbd4d01146101b1578063dd768922146101da578063ea28a5ae1461021757610086565b80630f41ba4b1461008b57806331f6f50e146100a75780637bed7232146100e4578063916765761461010d575b600080fd5b6100a560048036038101906100a09190611d78565b61025a565b005b3480156100b357600080fd5b506100ce60048036038101906100c99190611b7b565b6107e6565b6040516100db919061209c565b60405180910390f35b3480156100f057600080fd5b5061010b60048036038101906101069190611c76565b6107fb565b005b34801561011957600080fd5b50610134600480360381019061012f9190611d01565b610d67565b005b34801561014257600080fd5b5061015d60048036038101906101589190611c76565b61118b565b60405161016b929190612132565b60405180910390f35b34801561018057600080fd5b5061019b60048036038101906101969190611bfb565b61146a565b6040516101a8919061209c565b60405180910390f35b3480156101bd57600080fd5b506101d860048036038101906101d39190611c76565b61147e565b005b3480156101e657600080fd5b5061020160048036038101906101fc9190611cb2565b6117b3565b60405161020e9190612117565b60405180910390f35b34801561022357600080fd5b5061023e60048036038101906102399190611c76565b6117e5565b6040516102519796959493929190611ff1565b60405180910390f35b60008060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600084815260200190815260200160002090508060010160009054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168210156102ec57600080fd5b8060050160149054906101000a900460ff1661030757600080fd5b4281600201541161034d576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610344906120f7565b60405180910390fd5b60008090506000600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600086815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054111561050b578160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc633600160008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600088815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518363ffffffff1660e01b81526004016104d4929190611f6b565b600060405180830381600087803b1580156104ee57600080fd5b505af1158015610502573d6000803e3d6000fd5b50505050600190505b82600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600086815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6113330866040518463ffffffff1660e01b81526004016105fe93929190611f21565b600060405180830381600087803b15801561061857600080fd5b505af115801561062c573d6000803e3d6000fd5b5050505060008260060180549050141561069157828260040181905550338260050160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610749565b600060018360060180549050039050838360060182815481106106b057fe5b9060005260206000200154106106fb576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106f2906120b7565b60405180910390fd5b838360040181905550338360050160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505b806107df5781600701339080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816006018390806001815401808255809150506001900390600052602060002001600090919091909150555b5050505050565b60006331f6f50e60e01b905095945050505050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002090504281600201541115610895576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161088c906120d7565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146108f157600080fd5b8060050160149054906101000a900460ff1661090c57600080fd5b60008160050160146101000a81548160ff0219169083151502179055506000816006018054905014156109d1578273ffffffffffffffffffffffffffffffffffffffff1663d0e7d611308360000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16856040518463ffffffff1660e01b815260040161099a93929190611fa7565b600060405180830381600087803b1580156109b457600080fd5b505af11580156109c8573d6000803e3d6000fd5b50505050610d62565b8060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc68260000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1683600401546040518363ffffffff1660e01b8152600401610a58929190612060565b600060405180830381600087803b158015610a7257600080fd5b505af1158015610a86573d6000803e3d6000fd5b5050505060008090505b8160070180549050811015610ccd578160050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16826007018281548110610ae957fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610cc0578160030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc6836007018381548110610b7e57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008781526020019081526020016000206000866007018681548110610c0a57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518363ffffffff1660e01b8152600401610c8d929190612060565b600060405180830381600087803b158015610ca757600080fd5b505af1158015610cbb573d6000803e3d6000fd5b505050505b8080600101915050610a90565b508273ffffffffffffffffffffffffffffffffffffffff1663d0e7d611308360050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16856040518463ffffffff1660e01b8152600401610d2f93929190611fa7565b600060405180830381600087803b158015610d4957600080fd5b505af1158015610d5d573d6000803e3d6000fd5b505050505b505050565b600073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415610da157600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff161415610ddb57600080fd5b6000826fffffffffffffffffffffffffffffffff1611610dfa57600080fd5b60008111610e0757600080fd5b610e0f6118bd565b6040518061012001604052803373ffffffffffffffffffffffffffffffffffffffff168152602001846fffffffffffffffffffffffffffffffff1681526020018381526020018573ffffffffffffffffffffffffffffffffffffffff16815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600115158152602001600067ffffffffffffffff81118015610eb657600080fd5b50604051908082528060200260200182016040528015610ee55781602001602082028036833780820191505090505b508152602001600067ffffffffffffffff81118015610f0357600080fd5b50604051908082528060200260200182016040528015610f325781602001602082028036833780820191505090505b50815250905060003390508673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6118230896040518463ffffffff1660e01b8152600401610f7a93929190611fa7565b600060405180830381600087803b158015610f9457600080fd5b505af1158015610fa8573d6000803e3d6000fd5b50505050816000808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152","602001908152602001600020600088815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160010160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff1602179055506040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506080820151816004015560a08201518160050160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060c08201518160050160146101000a81548160ff02191690831515021790555060e082015181600601908051906020019061116092919061195f565b5061010082015181600701908051906020019061117e9291906119ac565b5090505050505050505050565b6000806111966118bd565b6000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000858152602001908152602001600020604051806101200160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160009054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600482015481526020016005820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016005820160149054906101000a900460ff16151515158152602001600682018054806020026020016040519081016040528092919081815260200182805480156113bb57602002820191906000526020600020905b8154815260200190600101908083116113a7575b505050505081526020016007820180548060200260200160405190810160405280929190818152602001828054801561144957602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116113ff575b505050505081525050905080608001518160a0015192509250509250929050565b600063c73d16ae60e01b9050949350505050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002090503373ffffffffffffffffffffffffffffffffffffffff168160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161461152d57600080fd5b8060050160149054906101000a900460ff1661154857600080fd5b60008160050160146101000a81548160ff021916908315150217905550600080600090505b8260070180549050811015611719578260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639bd9bbc68460070183815481106115cb57fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000888152602001908152602001600020600087600701868154811061165757fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518363ffffffff1660e01b81526004016116da929190612060565b600060405180830381600087803b1580156116f457600080fd5b505af1158015611708573d6000803e3d6000fd5b50505050808060010191505061156d565b508373ffffffffffffffffffffffffffffffffffffffff1663d0e7d611308460000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16866040518463ffffffff1660e01b815260040161177b93929190611fa7565b600060405180830381600087803b15801561179557600080fd5b505af11580156117a9573d6000803e3d6000fd5b5050505050505050565b600160205282600052604060002060205281600052604060002060205280600052604060002060009250925050505481565b6000602052816000526040600020602052806000526040600020600091509150508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010160009054906101000a90046fffffffffffffffffffffffffffffffff16908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060040154908060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060050160149054906101000a900460ff16905087565b604051806101200160405280600073ffffffffffffffffffffffffffffffffffffffff16815260200160006fffffffffffffffffffffffffffffffff16815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff16815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160608152602001606081525090565b82805482825590600052602060002090810192821561199b579160200282015b8281111561199a57825182559160200191906001019061197f565b5b5090506119a89190611a36565b5090565b828054828255906000526020600020908101928215611a25579160200282015b82811115611a245782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550916020019190600101906119cc565b5b509050611a329190611a5b565b5090565b611a5891905b80821115611a54576000816000905550600101611a3c565b5090565b90565b611a9b91905b80821115611a9757600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550600101611a61565b5090565b90565b600081359050611aad816122ab565b92915050565b60008083601f840112611ac557600080fd5b8235905067ffffffffffffffff811115611ade57600080fd5b602083019150836001820283011115611af657600080fd5b9250929050565b600082601f830112611b0e57600080fd5b8135611b21611b1c82612188565b61215b565b91508082526020830160208301858383011115611b3d57600080fd5b611b4883828461229c565b50505092915050565b600081359050611b60816122c2565b92915050565b600081359050611b75816122d9565b92915050565b600080600080600060808688031215611b9357600080fd5b6000611ba188828901611a9e565b9550506020611bb288828901611a9e565b9450506040611bc388828901611b66565b935050606086013567ffffffffffffffff811115611be057600080fd5b611bec88828901611ab3565b92509250509295509295909350565b60008060008060808587031215611c1157600080fd5b6000611c1f87828801611a9e565b9450506020611c3087828801611a9e565b9350506040611c4187828801611b66565b925050606085013567ffffffffffffffff811115611c5e57600080fd5b611c6a87828801611afd565b91505092959194509250565b60008060408385031215611c8957600080fd5b6000611c9785828601611a9e565b9250506020611ca885828601611b66565b9150509250929050565b600080600060608486031215611cc757600080fd5b6000611cd586828701611a9e565b9350506020611ce686828701611b66565b9250506040611cf786828701611a9e565b9150509250925092565b600080600080600060a08688031215611d1957600080fd5b6000611d2788828901611a9e565b9550506020611d3888828901611b66565b9450506040611d4988828901611a9e565b9350506060611d5a88828901611b51565b9250506080611d6b88828901611b66565b9150509295509295909350565b600080600060608486031215611d8d57600080fd5b6000611d9b86828701611a9e565b9350506020611dac86828701611b66565b9250506040611dbd86828701611b66565b9150509250925092565b611dd081612266565b82525050565b611ddf816121d6565b82525050565b611dee816121e8565b82525050565b611dfd816121f4565b82525050565b6000611e106027836121c5565b91507f43757272656e74206d61782062696420697320686967686572207468616e207960008301527f6f757220626964000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000611e766019836121c5565b91507f446561646c696e6520646964206e6f74207061737320796574000000000000006000830152602082019050919050565b6000611eb66000836121b4565b9150600082019050919050565b6000611ed06017836121c5565b91507f446561646c696e6520616c7265616479207061737365640000000000000000006000830152602082019050919050565b611f0c81612220565b82525050565b611f1b8161225c565b82525050565b6000608082019050611f366000830186611dc7565b611f436020830185611dd6565b611f506040830184611f12565b8181036060830152611f6181611ea9565b9050949350505050565b6000606082019050611f806000830185611dc7565b611f8d6020830184611f12565b8181036040830152611f9e81611ea9565b90509392505050565b6000608082019050611fbc6000830186611dd6565b611fc96020830185611dd6565b611fd66040830184611f12565b818103606083015261","1fe781611ea9565b9050949350505050565b600060e082019050612006600083018a611dd6565b6120136020830189611f03565b6120206040830188611f12565b61202d6060830187611dd6565b61203a6080830186611f12565b61204760a0830185611dd6565b61205460c0830184611de5565b98975050505050505050565b60006060820190506120756000830185611dd6565b6120826020830184611f12565b818103604083015261209381611ea9565b90509392505050565b60006020820190506120b16000830184611df4565b92915050565b600060208201905081810360008301526120d081611e03565b9050919050565b600060208201905081810360008301526120f081611e69565b9050919050565b6000602082019050818103600083015261211081611ec3565b9050919050565b600060208201905061212c6000830184611f12565b92915050565b60006040820190506121476000830185611f12565b6121546020830184611dd6565b9392505050565b6000604051905081810181811067ffffffffffffffff8211171561217e57600080fd5b8060405250919050565b600067ffffffffffffffff82111561219f57600080fd5b601f19601f8301169050602081019050919050565b600082825260208201905092915050565b600082825260208201905092915050565b60006121e18261223c565b9050919050565b60008115159050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b60006fffffffffffffffffffffffffffffffff82169050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b600061227182612278565b9050919050565b60006122838261228a565b9050919050565b60006122958261223c565b9050919050565b82818337600083830152505050565b6122b4816121d6565b81146122bf57600080fd5b50565b6122cb81612220565b81146122d657600080fd5b50565b6122e28161225c565b81146122ed57600080fd5b5056fea26469706673582212206fadba331398ab610f1e7e698bd29a7485af5cb32309016a37d9d09aa6874f1a64736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"_amount\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"bid\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"payable\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"name\":\"bids\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"cancelAution\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"_ftAssetAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_price\",\"type\":\"uint128\",\"type0\":null,\"indexed\":false},{\"name\":\"_duration\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"createNFTAssetAuction\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"executeSale\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"getNFTAssetAuctionDetails\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"nftAssetToAuction\",\"outputs\":[{\"name\":\"seller\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"price\",\"type\":\"uint128\",\"type0\":null,\"indexed\":false},{\"name\":\"duration\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"ftAssetAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"maxBid\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"maxBidUser\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"isActive\",\"type\":\"bool\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"onBAC001Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"onBAC002Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_BID = "bid";

    public static final String FUNC_BIDS = "bids";

    public static final String FUNC_CANCELAUTION = "cancelAution";

    public static final String FUNC_CREATENFTASSETAUCTION = "createNFTAssetAuction";

    public static final String FUNC_EXECUTESALE = "executeSale";

    public static final String FUNC_GETNFTASSETAUCTIONDETAILS = "getNFTAssetAuctionDetails";

    public static final String FUNC_NFTASSETTOAUCTION = "nftAssetToAuction";

    public static final String FUNC_ONBAC001RECEIVED = "onBAC001Received";

    public static final String FUNC_ONBAC002RECEIVED = "onBAC002Received";

    @Deprecated
    protected AuctionUnfixedPrice(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AuctionUnfixedPrice(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AuctionUnfixedPrice(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AuctionUnfixedPrice(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return BINARY;
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> bid(String _nft, BigInteger _nftAssetId, BigInteger _amount) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void bid(String _nft, BigInteger _nftAssetId, BigInteger _amount, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String bidSeq(String _nft, BigInteger _nftAssetId, BigInteger _amount) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple3<String, BigInteger, BigInteger> getBidInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_BID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple3<String, BigInteger, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public RemoteCall<BigInteger> bids(String param0, BigInteger param1, String param2) {
        final Function function = new Function(FUNC_BIDS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> cancelAution(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_CANCELAUTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void cancelAution(String _nft, BigInteger _nftAssetId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CANCELAUTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String cancelAutionSeq(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_CANCELAUTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, BigInteger> getCancelAutionInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CANCELAUTION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> createNFTAssetAuction(String _nft, BigInteger _nftAssetId, String _ftAssetAddress, BigInteger _price, BigInteger _duration) {
        final Function function = new Function(
                FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_ftAssetAddress), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void createNFTAssetAuction(String _nft, BigInteger _nftAssetId, String _ftAssetAddress, BigInteger _price, BigInteger _duration, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_ftAssetAddress), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String createNFTAssetAuctionSeq(String _nft, BigInteger _nftAssetId, String _ftAssetAddress, BigInteger _price, BigInteger _duration) {
        final Function function = new Function(
                FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_ftAssetAddress), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint128(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple5<String, BigInteger, String, BigInteger, BigInteger> getCreateNFTAssetAuctionInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint128>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple5<String, BigInteger, String, BigInteger, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> executeSale(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_EXECUTESALE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void executeSale(String _nft, BigInteger _nftAssetId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_EXECUTESALE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String executeSaleSeq(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_EXECUTESALE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, BigInteger> getExecuteSaleInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_EXECUTESALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public RemoteCall<Tuple2<BigInteger, String>> getNFTAssetAuctionDetails(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(FUNC_GETNFTASSETAUCTIONDETAILS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        return new RemoteCall<Tuple2<BigInteger, String>>(
                new Callable<Tuple2<BigInteger, String>>() {
                    @Override
                    public Tuple2<BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<Tuple7<String, BigInteger, BigInteger, String, BigInteger, String, Boolean>> nftAssetToAuction(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_NFTASSETTOAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint128>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple7<String, BigInteger, BigInteger, String, BigInteger, String, Boolean>>(
                new Callable<Tuple7<String, BigInteger, BigInteger, String, BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple7<String, BigInteger, BigInteger, String, BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, BigInteger, BigInteger, String, BigInteger, String, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (Boolean) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onBAC001Received(String param0, String param1, BigInteger param2, byte[] param3, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String onBAC001ReceivedSeq(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC001RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
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

    public RemoteCall<TransactionReceipt> onBAC002Received(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC002RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void onBAC002Received(String param0, String param1, BigInteger param2, byte[] param3, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ONBAC002RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String onBAC002ReceivedSeq(String param0, String param1, BigInteger param2, byte[] param3) {
        final Function function = new Function(
                FUNC_ONBAC002RECEIVED, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(param1), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.fisco.bcos.web3j.abi.datatypes.DynamicBytes(param3)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple4<String, String, BigInteger, byte[]> getOnBAC002ReceivedInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ONBAC002RECEIVED, 
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

    public Tuple1<byte[]> getOnBAC002ReceivedOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ONBAC002RECEIVED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    @Deprecated
    public static AuctionUnfixedPrice load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionUnfixedPrice(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AuctionUnfixedPrice load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionUnfixedPrice(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AuctionUnfixedPrice load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AuctionUnfixedPrice(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AuctionUnfixedPrice load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AuctionUnfixedPrice(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AuctionUnfixedPrice> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionUnfixedPrice.class, web3j, credentials, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AuctionUnfixedPrice> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionUnfixedPrice.class, web3j, credentials, gasPrice, gasLimit, getBinary(), "");
    }

    public static RemoteCall<AuctionUnfixedPrice> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionUnfixedPrice.class, web3j, transactionManager, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AuctionUnfixedPrice> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionUnfixedPrice.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), "");
    }
}
