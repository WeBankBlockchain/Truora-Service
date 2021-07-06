package com.webank.oracle.test.transaction.bac.auction.auctionFixedPrice;

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
 * <p>Generated with web3j version null.
 */
@SuppressWarnings("unchecked")
public class AuctionFixedPrice extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061105c806100206000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c806399d9f7811161005b57806399d9f781146100ff578063c73d16ae1461011b578063cdbd4d011461014b578063ea28a5ae146101675761007d565b806314edb1f11461008257806331f6f50e1461009e578063906aa2fd146100ce575b600080fd5b61009c60048036038101906100979190610c13565b61019b565b005b6100b860048036038101906100b39190610adc565b61041b565b6040516100c59190610e89565b60405180910390f35b6100e860048036038101906100e39190610bd7565b610430565b6040516100f6929190610e0d565b60405180910390f35b61011960048036038101906101149190610bd7565b610588565b005b61013560048036038101906101309190610b5c565b610794565b6040516101429190610e89565b60405180910390f35b61016560048036038101906101609190610bd7565b6107a8565b005b610181600480360381019061017c9190610bd7565b610927565b604051610192959493929190610e36565b60405180910390f35b600073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614156101d557600080fd5b600073ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff16141561020f57600080fd5b6000821161021c57600080fd5b6000811161022957600080fd5b6102316109b7565b6040518060a001604052803373ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018381526020018573ffffffffffffffffffffffffffffffffffffffff16815260200160011515815250905060003390508673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6118230896040518463ffffffff1660e01b81526004016102cb93929190610dc3565b600060405180830381600087803b1580156102e557600080fd5b505af11580156102f9573d6000803e3d6000fd5b50505050816000808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600088815260200190815260200160002060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550602082015181600101556040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160030160146101000a81548160ff02191690831515021790555090505050505050505050565b60006331f6f50e60e01b905095945050505050565b60008061043b6109b7565b6000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008581526020019081526020016000206040518060a00160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160018201548152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016003820160149054906101000a900460ff16151515158152505090508060600151816020015192509250509250929050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000838152602001908152602001600020905042816002015411610621576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161061890610ea4565b60405180910390fd5b8060030160149054906101000a900460ff1661063c57600080fd5b60008160030160146101000a81548160ff02191690831515021790555060008160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690506000826001015490508260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d0e7d6113384846040518463ffffffff1660e01b81526004016106ec93929190610d2f565b600060405180830381600087803b15801561070657600080fd5b505af115801561071a573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff1663d0e7d6113033876040518463ffffffff1660e01b815260040161075b93929190610d79565b600060405180830381600087803b15801561077557600080fd5b505af1158015610789573d6000803e3d6000fd5b505050505050505050565b600063c73d16ae60e01b9050949350505050565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600083815260200190815260200160002090503373ffffffffffffffffffffffffffffffffffffffff168160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161461085757600080fd5b8060030160149054906101000a900460ff1661087257600080fd5b60008160030160146101000a81548160ff0219169083151502179055508273ffffffffffffffffffffffffffffffffffffffff1663d0e7d611308360000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16856040518463ffffffff1660e01b81526004016108f093929190610dc3565b600060405180830381600087803b15801561090a57600080fd5b505af115801561091e573d6000803e3d6000fd5b50505050505050565b6000602052816000526040600020602052806000526040600020600091509150508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010154908060020154908060030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030160149054906101000a900460ff16905085565b6040518060a00160405280600073ffffffffffffffffffffffffffffffffffffffff1681526020016000815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff1681526020016000151581525090565b600081359050610a2381610ff8565b92915050565b60008083601f840112610a3b57600080fd5b8235905067ffffffffffffffff811115610a5457600080fd5b602083019150836001820283011115610a6c57600080fd5b9250929050565b600082601f830112610a8457600080fd5b8135610a97610a9282610ef1565b610ec4565b91508082526020830160208301858383011115610ab357600080fd5b610abe838284610fe9565b50505092915050565b600081359050610ad68161100f565b92915050565b600080600080600060808688031215610af457600080fd5b6000610b0288828901610a14565b9550506020610b1388828901610a14565b9450506040610b2488828901610ac7565b935050606086013567ffffffffffffffff811115610b4157600080fd5b610b4d88828901610a29565b92509250509295509295909350565b60008060008060808587031215610b7257600080fd5b6000610b8087828801610a14565b9450506020610b9187828801610a14565b9350506040610ba287828801610ac7565b925050606085013567ffffffffffffffff811115610bbf57600080fd5b610bcb87828801610a73565b91505092959194509250565b60008060408385031215610bea57600080fd5b6000610bf885828601610a14565b9250506020610c0985828601610ac7565b9150509250929050565b600080600080600060a08688031215610c2b57600080fd5b6000610c3988828901610a14565b9550506020610c4a88828901610ac7565b9450506040610c5b88828901610a14565b9350506060610c6c88828901610ac7565b9250506080610c7d88828901610ac7565b9150509295509295909350565b610c9381610fb3565b82525050565b610ca281610f3f565b82525050565b610cb181610f51565b82525050565b610cc081610f5d565b82525050565b6000610cd3600083610f1d565b9150600082019050919050565b6000610ced601783610f2e565b91507f446561646c696e6520616c7265616479207061737365640000000000000000006000830152602082019050919050565b610d2981610fa9565b82525050565b6000608082019050610d446000830186610c8a565b610d516020830185610c99565b610d5e6040830184610d20565b8181036060830152610d6f81610cc6565b9050949350505050565b6000608082019050610d8e6000830186610c99565b610d9b6020830185610c8a565b610da86040830184610d20565b8181036060830152610db981610cc6565b9050949350505050565b6000608082019050610dd86000830186610c99565b610de56020830185610c99565b610df26040830184610d20565b8181036060830152610e0381610cc6565b9050949350505050565b6000604082019050610e226000830185610c99565b610e2f6020830184610d20565b9392505050565b600060a082019050610e4b6000830188610c99565b610e586020830187610d20565b610e656040830186610d20565b610e726060830185610c99565b610e7f6080830184610ca8565b9695505050505050565b6000602082019050610e9e6000830184610cb7565b92915050565b60006020820190508181036000830152610ebd81610ce0565b9050919050565b6000604051905081810181811067ffffffffffffffff82111715610ee757600080fd5b8060405250919050565b600067ffffffffffffffff821115610f0857600080fd5b601f19601f8301169050602081019050919050565b600082825260208201905092915050565b600082825260208201905092915050565b6000610f4a82610f89565b9050919050565b60008115159050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000610fbe82610fc5565b9050919050565b6000610fd082610fd7565b9050919050565b6000610fe282610f","89565b9050919050565b82818337600083830152505050565b61100181610f3f565b811461100c57600080fd5b50565b61101881610fa9565b811461102357600080fd5b5056fea2646970667358221220fea8bbb255d6ddf8208a89e206f16a899d62341d13448da0e4b195b433cfe80f64736f6c634300060a0033"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"cancelAution\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"_ftAssetAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_price\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"_duration\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"createNFTAssetAuction\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":true,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"getNftAssetAuctionDetails\",\"outputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"nftAssetToAuction\",\"outputs\":[{\"name\":\"seller\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"price\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"duration\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"ftAssetAddress\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"isActive\",\"type\":\"bool\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"view\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"onBAC001Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false},{\"name\":\"\",\"type\":\"bytes\",\"type0\":null,\"indexed\":false}],\"name\":\"onBAC002Received\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes4\",\"type0\":null,\"indexed\":false}],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"},{\"constant\":false,\"inputs\":[{\"name\":\"_nft\",\"type\":\"address\",\"type0\":null,\"indexed\":false},{\"name\":\"_nftAssetId\",\"type\":\"uint256\",\"type0\":null,\"indexed\":false}],\"name\":\"purchaseNFTAsset\",\"outputs\":[],\"type\":\"function\",\"payable\":false,\"stateMutability\":\"nonpayable\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_CANCELAUTION = "cancelAution";

    public static final String FUNC_CREATENFTASSETAUCTION = "createNFTAssetAuction";

    public static final String FUNC_GETNFTASSETAUCTIONDETAILS = "getNftAssetAuctionDetails";

    public static final String FUNC_NFTASSETTOAUCTION = "nftAssetToAuction";

    public static final String FUNC_ONBAC001RECEIVED = "onBAC001Received";

    public static final String FUNC_ONBAC002RECEIVED = "onBAC002Received";

    public static final String FUNC_PURCHASENFTASSET = "purchaseNFTAsset";

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
        return BINARY;
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
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
