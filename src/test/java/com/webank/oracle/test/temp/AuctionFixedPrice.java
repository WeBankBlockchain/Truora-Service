package com.webank.oracle.test.temp;

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
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tuples.generated.Tuple5;
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
public class AuctionFixedPrice extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610f8b806100206000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806314edb1f11461008857806331f6f50e146100b1578063906aa2fd146100ee57806399d9f7811461012c578063c73d16ae14610155578063cdbd4d0114610192578063ea28a5ae146101bb575b600080fd5b34801561009457600080fd5b506100af60048036036100aa9190810190610c62565b6101fc565b005b3480156100bd57600080fd5b506100d860048036036100d39190810190610bab565b6104a0565b6040516100e59190610e22565b60405180910390f35b3480156100fa57600080fd5b5061011560048036036101109190810190610c26565b6104d0565b604051610123929190610da6565b60405180910390f35b34801561013857600080fd5b50610153600480360361014e9190810190610c26565b610629565b005b34801561016157600080fd5b5061017c60048036036101779190810190610bab565b610870565b6040516101899190610e22565b60405180910390f35b34801561019e57600080fd5b506101b960048036036101b49190810190610c26565b6108a0565b005b3480156101c757600080fd5b506101e260048036036101dd9190810190610c26565b610a3f565b6040516101f3959493929190610dcf565b60405180910390f35b610204610acf565b60008073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415151561024157600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168773ffffffffffffffffffffffffffffffffffffffff161415151561027d57600080fd5b60008411151561028c57600080fd5b60008311151561029b57600080fd5b60a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018581526020018481526020018673ffffffffffffffffffffffffffffffffffffffff1681526020016001151581525091503390508673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6118230896040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161035093929190610d5c565b600060405180830381600087803b15801561036a57600080fd5b505af115801561037e573d6000803e3d6000fd5b50505050816000808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600088815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550602082015181600101556040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160030160146101000a81548160ff02191690831515021790555090505050505050505050565b60006331f6f50e7c0100000000000000000000000000000000000000000000000000000000029050949350505050565b6000806104db610acf565b6000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600085815260200190815260200160002060a060405190810160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160018201548152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016003820160149054906101000a900460ff16151515158152505090508060600151816020015192509250509250929050565b60008060008060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600085815260200190815260200160002092504283600201541115156106c7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106be90610e3d565b60405180910390fd5b8260030160149054906101000a900460ff1615156106e457600080fd5b60008360030160146101000a81548160ff0219169083151502179055508260000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169150826001015490508260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6113384846040518463ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016107ac93929190610d5c565b600060405180830381600087803b1580156107c657600080fd5b505af11580156107da573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff1663d0e7d6113033876040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161083793929190610d5c565b600060405180830381600087803b15801561085157600080fd5b505af1158015610865573d6000803e3d6000fd5b505050505050505050565b600063c73d16ae7c0100000000000000000000000000000000000000000000000000000000029050949350505050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002090503373ffffffffffffffffffffffffffffffffffffffff168160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561095157600080fd5b8060030160149054906101000a900460ff16151561096e57600080fd5b60008160030160146101000a81548160ff0219169083151502179055508273ffffffffffffffffffffffffffffffffffffffff1663d0e7d611308360000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401610a0893929190610d5c565b600060405180830381600087803b158015610a2257600080fd5b505af1158015610a36573d6000803e3d6000fd5b50505050505050565b6000602052816000526040600020602052806000526040600020600091509150508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010154908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030160149054906101000a900460ff16905085565b60a060405190810160405280600073ffffffffffffffffffffffffffffffffffffffff1681526020016000815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff1681526020016000151581525090565b6000610b398235610f18565b905092915050565b600082601f8301121515610b5457600080fd5b8135610b67610b6282610e8a565b610e5d565b91508082526020830160208301858383011115610b8357600080fd5b610b8e838284610f42565b50505092915050565b6000610ba38235610f38565b905092915050565b60008060008060808587031215610bc157600080fd5b6000610bcf87828801610b2d565b9450506020610be087828801610b2d565b9350506040610bf187828801610b97565b925050606085013567ffffffffffffffff811115610c0e57600080fd5b610c1a87828801610b41565b91505092959194509250565b60008060408385031215610c3957600080fd5b6000610c4785828601610b2d565b9250506020610c5885828601610b97565b9150509250929050565b600080600080600060a08688031215610c7a57600080fd5b6000610c8888828901610b2d565b9550506020610c9988828901610b97565b9450506040610caa88828901610b2d565b9350506060610cbb88828901610b97565b9250506080610ccc88828901610b97565b9150509295509295909350565b610ce281610eb6565b82525050565b610cf181610ed6565b82525050565b610d0081610ee2565b82525050565b6000808252602082019050919050565b6000601782527f446561646c696e6520616c7265616479207061737365640000000000000000006020830152604082019050919050565b610d5681610f0e565b82525050565b6000608082019050610d716000830186610cd9565b610d7e6020830185610cd9565b610d8b6040830184610d4d565b8181036060830152610d9c81610d06565b9050949350505050565b6000604082019050610dbb6000830185610cd9565b610dc86020830184610d4d565b9392505050565b600060a082019050610de46000830188610cd9565b610df16020830187610d4d565b610dfe6040830186610d4d565b610e0b6060830185610cd9565b610e186080830184610ce8565b9695505050505050565b6000602082019050610e376000830184610cf7565b92915050565b60006020820190508181036000830152610e5681610d16565b9050919050565b6000604051905081810181811067ffffffffffffffff82111715610e8057600080fd5b8060405250919050565b600067ffffffffffffffff821115610ea157600080fd5b601f19601f8301169050602081019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b828183376000838301525050505600a265627a7a72305820c35d2a47af051eae686d667232fa977f3e7c5356120f7186ac23ad7d61979d046c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\"},{\"name\":\"_nftAssetId\",\"type\":\"uint256\"},{\"name\":\"_ftAssetAddress\",\"type\":\"address\"},{\"name\":\"_price\",\"type\":\"uint256\"},{\"name\":\"_duration\",\"type\":\"uint256\"}],\"name\":\"createNFTAssetAuction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"bytes\"}],\"name\":\"onBAC002Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\"},{\"name\":\"_nftAssetId\",\"type\":\"uint256\"}],\"name\":\"getNftAssetAuctionDetails\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\"},{\"name\":\"_nftAssetId\",\"type\":\"uint256\"}],\"name\":\"purchaseNFTAsset\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"bytes\"}],\"name\":\"onBAC001Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\"},{\"name\":\"_nftAssetId\",\"type\":\"uint256\"}],\"name\":\"cancelAution\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"nftAssetToAuction\",\"outputs\":[{\"name\":\"seller\",\"type\":\"address\"},{\"name\":\"price\",\"type\":\"uint256\"},{\"name\":\"duration\",\"type\":\"uint256\"},{\"name\":\"ftAssetAddress\",\"type\":\"address\"},{\"name\":\"isActive\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610f8b806100206000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630aa78678146100885780630c74d421146100c55780634cab2860146101035780634db0bfc714610140578063599122d21461016957806377fc040114610192578063d1a773e6146101d3575b600080fd5b34801561009457600080fd5b506100af60048036036100aa9190810190610bab565b6101fc565b6040516100bc9190610e22565b60405180910390f35b3480156100d157600080fd5b506100ec60048036036100e79190810190610c26565b61022c565b6040516100fa929190610da6565b60405180910390f35b34801561010f57600080fd5b5061012a60048036036101259190810190610bab565b610385565b6040516101379190610e22565b60405180910390f35b34801561014c57600080fd5b5061016760048036036101629190810190610c26565b6103b5565b005b34801561017557600080fd5b50610190600480360361018b9190810190610c26565b610554565b005b34801561019e57600080fd5b506101b960048036036101b49190810190610c26565b61079b565b6040516101ca959493929190610dcf565b60405180910390f35b3480156101df57600080fd5b506101fa60048036036101f59190810190610c62565b61082b565b005b6000630aa786787c0100000000000000000000000000000000000000000000000000000000029050949350505050565b600080610237610acf565b6000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600085815260200190815260200160002060a060405190810160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160018201548152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016003820160149054906101000a900460ff16151515158152505090508060600151816020015192509250509250929050565b6000634cab28607c0100000000000000000000000000000000000000000000000000000000029050949350505050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002090503373ffffffffffffffffffffffffffffffffffffffff168160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561046657600080fd5b8060030160149054906101000a900460ff16151561048357600080fd5b60008160030160146101000a81548160ff0219169083151502179055508273ffffffffffffffffffffffffffffffffffffffff166380b30b3f308360000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16856040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161051d93929190610d5c565b600060405180830381600087803b15801561053757600080fd5b505af115801561054b573d6000803e3d6000fd5b50505050505050565b60008060008060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600085815260200190815260200160002092504283600201541115156105f2576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016105e990610e3d565b60405180910390fd5b8260030160149054906101000a900460ff16151561060f57600080fd5b60008360030160146101000a81548160ff0219169083151502179055508260000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169150826001015490508260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166380b30b3f3384846040518463ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004016106d793929190610d5c565b600060405180830381600087803b1580156106f157600080fd5b505af1158015610705573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff166380b30b3f3033876040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161076293929190610d5c565b600060405180830381600087803b15801561077c57600080fd5b505af1158015610790573d6000803e3d6000fd5b505050505050505050565b6000602052816000526040600020602052806000526040600020600091509150508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010154908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030160149054906101000a900460ff16905085565b610833610acf565b60008073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415151561087057600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168773ffffffffffffffffffffffffffffffffffffffff16141515156108ac57600080fd5b6000841115156108bb57600080fd5b6000831115156108ca57600080fd5b60a0604051908101604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018581526020018481526020018673ffffffffffffffffffffffffffffffffffffffff1681526020016001151581525091503390508673ffffffffffffffffffffffffffffffffffffffff166380b30b3f8230896040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040161097f93929190610d5c565b600060405180830381600087803b15801561099957600080fd5b505af11580156109ad573d6000803e3d6000fd5b50505050816000808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600088815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550602082015181600101556040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160030160146101000a81548160ff02191690831515021790555090505050505050505050565b60a060405190810160405280600073ffffffffffffffffffffffffffffffffffffffff1681526020016000815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff1681526020016000151581525090565b6000610b398235610f18565b905092915050565b600082601f8301121515610b5457600080fd5b8135610b67610b6282610e8a565b610e5d565b91508082526020830160208301858383011115610b8357600080fd5b610b8e838284610f42565b50505092915050565b6000610ba38235610f38565b905092915050565b60008060008060808587031215610bc157600080fd5b6000610bcf87828801610b2d565b9450506020610be087828801610b2d565b9350506040610bf187828801610b97565b925050606085013567ffffffffffffffff811115610c0e57600080fd5b610c1a87828801610b41565b91505092959194509250565b60008060408385031215610c3957600080fd5b6000610c4785828601610b2d565b9250506020610c5885828601610b97565b9150509250929050565b600080600080600060a08688031215610c7a57600080fd5b6000610c8888828901610b2d565b9550506020610c9988828901610b97565b9450506040610caa88828901610b2d565b9350506060610cbb88828901610b97565b9250506080610ccc88828901610b97565b9150509295509295909350565b610ce281610eb6565b82525050565b610cf181610ed6565b82525050565b610d0081610ee2565b82525050565b6000808252602082019050919050565b6000601782527f446561646c696e6520616c7265616479207061737365640000000000000000006020830152604082019050919050565b610d5681610f0e565b82525050565b6000608082019050610d716000830186610cd9565b610d7e6020830185610cd9565b610d8b6040830184610d4d565b8181036060830152610d9c81610d06565b9050949350505050565b6000604082019050610dbb6000830185610cd9565b610dc86020830184610d4d565b9392505050565b600060a082019050610de46000830188610cd9565b610df16020830187610d4d565b610dfe6040830186610d4d565b610e0b6060830185610cd9565b610e186080830184610ce8565b9695505050505050565b6000602082019050610e376000830184610cf7565b92915050565b60006020820190508181036000830152610e5681610d16565b9050919050565b6000604051905081810181811067ffffffffffffffff82111715610e8057600080fd5b8060405250919050565b600067ffffffffffffffff821115610ea157600080fd5b601f19601f8301169050602081019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60008115159050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b6000819050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b828183376000838301525050505600a265627a7a723058200534b8ce2582eea875aea7f424a12d4eadd6ec1efe4a23a94ebd8e3d4d1e7e226c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_CREATENFTASSETAUCTION = "createNFTAssetAuction";

    public static final String FUNC_ONBAC002RECEIVED = "onBAC002Received";

    public static final String FUNC_GETNFTASSETAUCTIONDETAILS = "getNftAssetAuctionDetails";

    public static final String FUNC_PURCHASENFTASSET = "purchaseNFTAsset";

    public static final String FUNC_ONBAC001RECEIVED = "onBAC001Received";

    public static final String FUNC_CANCELAUTION = "cancelAution";

    public static final String FUNC_NFTASSETTOAUCTION = "nftAssetToAuction";

    @Deprecated
    protected AuctionFixedPrice(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AuctionFixedPrice(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AuctionFixedPrice(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AuctionFixedPrice(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> createNFTAssetAuction(String _nft, BigInteger _nftAssetId, String _ftAssetAddress, BigInteger _price, BigInteger _duration) {
        final Function function = new Function(
                FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId), 
                new org.fisco.bcos.web3j.abi.datatypes.Address(_ftAssetAddress), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
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
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
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
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_price), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_duration)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple5<String, BigInteger, String, BigInteger, BigInteger> getCreateNFTAssetAuctionInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATENFTASSETAUCTION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple5<String, BigInteger, String, BigInteger, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue()
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

    public RemoteCall<Tuple2<String, BigInteger>> getNftAssetAuctionDetails(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(FUNC_GETNFTASSETAUCTIONDETAILS, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<String, BigInteger>>(
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> purchaseNFTAsset(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_PURCHASENFTASSET, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void purchaseNFTAsset(String _nft, BigInteger _nftAssetId, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_PURCHASENFTASSET, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String purchaseNFTAssetSeq(String _nft, BigInteger _nftAssetId) {
        final Function function = new Function(
                FUNC_PURCHASENFTASSET, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(_nft), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(_nftAssetId)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<String, BigInteger> getPurchaseNFTAssetInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PURCHASENFTASSET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
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

    public RemoteCall<Tuple5<String, BigInteger, BigInteger, String, Boolean>> nftAssetToAuction(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_NFTASSETTOAUCTION, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(param0), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple5<String, BigInteger, BigInteger, String, Boolean>>(
                new Callable<Tuple5<String, BigInteger, BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple5<String, BigInteger, BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, BigInteger, BigInteger, String, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
                    }
                });
    }

    @Deprecated
    public static AuctionFixedPrice load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFixedPrice(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AuctionFixedPrice load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AuctionFixedPrice(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AuctionFixedPrice load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AuctionFixedPrice(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AuctionFixedPrice load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AuctionFixedPrice(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AuctionFixedPrice> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionFixedPrice.class, web3j, credentials, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AuctionFixedPrice> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionFixedPrice.class, web3j, credentials, gasPrice, gasLimit, getBinary(), "");
    }

    public static RemoteCall<AuctionFixedPrice> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AuctionFixedPrice.class, web3j, transactionManager, contractGasProvider, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<AuctionFixedPrice> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AuctionFixedPrice.class, web3j, transactionManager, gasPrice, gasLimit, getBinary(), "");
    }
}
