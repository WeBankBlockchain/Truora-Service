package com.webank.truora.contract.bcos3.simplevrf;

import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.*;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
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
public class LotteryOracleUseVrf extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516109013803806109018339818101604052602081101561003357600080fd5b810190808051906020019092919050505080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060028190555060016000806101000a81548160ff021916908360018111156100ab57fe5b021790555050610841806100c06000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80635d495aea1461005c57806381447999146100a6578063af440aad146100d2578063e580f47b1461018a578063f71d96cb146101a8575b600080fd5b610064610216565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100ae610525565b604051808260018111156100be57fe5b60ff16815260200191505060405180910390f35b610188600480360360208110156100e857600080fd5b810190808035906020019064010000000081111561010557600080fd5b82018360208201111561011757600080fd5b8035906020019184602083028401116401000000008311171561013957600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050610537565b005b6101926106d7565b6040518082815260200191505060405180910390f35b6101d4600480360360208110156101be57600080fd5b81019080803590602001909291905050506106dd565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000801515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c75f695a6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561029057600080fd5b505afa1580156102a4573d6000803e3d6000fd5b505050506040513d60208110156102ba57600080fd5b8101908080519060200190929190505050151514610323576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806107e76025913960400191505060405180910390fd5b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16632dff0d0d6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561039a57600080fd5b505afa1580156103ae573d6000803e3d6000fd5b505050506040513d60208110156103c457600080fd5b81019080805190602001909291905050509050600060018054905082816103e757fe5b0690506000600182815481106103f957fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600067ffffffffffffffff8111801561043e57600080fd5b5060405190808252806020026020018201604052801561046d5781602001602082028036833780820191505090505b5060019080519060200190610483929190610719565b5060016000806101000a81548160ff021916908360018111156104a257fe5b02179055507f1c2ae1f7e99dbbbfe25aac964f3889ea68259d88b74709f53037a6fb930bb02a6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b6000809054906101000a900460ff1681565b60018081111561054357fe5b6000809054906101000a900460ff16600181111561055d57fe5b146105d0576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b60008060006101000a81548160ff021916908360018111156105ee57fe5b02179055508060019080519060200190610609929190610719565b50600260008154809291906001019190505550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b37217a46002546040518263ffffffff1660e01b815260040180828152602001915050602060405180830381600087803b15801561069357600080fd5b505af11580156106a7573d6000803e3d6000fd5b505050506040513d60208110156106bd57600080fd5b810190808051906020019092919050505060048190555050565b60025481565b600181815481106106ea57fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b828054828255906000526020600020908101928215610792579160200282015b828111156107915782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555091602001919060010190610739565b5b50905061079f91906107a3565b5090565b6107e391905b808211156107df57600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055506001016107a9565b5090565b9056fe206f7261636c6520717565727920686173206e6f74206265656e2066756c66696c6c656421a2646970667358221220165acced6049158430a50d007884eacd788dc44f03a11db88d788952f918fe7464736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506040516109013803806109018339818101604052602081101561003357600080fd5b810190808051906020019092919050505080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600060028190555060016000806101000a81548160ff021916908360018111156100ab57fe5b021790555050610841806100c06000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c806301fbc5721461005c57806386e053e314610114578063975fcaa014610182578063d865927d146101cc578063e5501039146101ea575b600080fd5b6101126004803603602081101561007257600080fd5b810190808035906020019064010000000081111561008f57600080fd5b8201836020820111156100a157600080fd5b803590602001918460208302840111640100000000831117156100c357600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050610216565b005b6101406004803603602081101561012a57600080fd5b81019080803590602001909291905050506103b6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61018a6103f2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101d4610701565b6040518082815260200191505060405180910390f35b6101f2610707565b6040518082600181111561020257fe5b60ff16815260200191505060405180910390f35b60018081111561022257fe5b6000809054906101000a900460ff16600181111561023c57fe5b146102af576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f63616e27742073746172742061206e6577206c6f74746572792079657400000081525060200191505060405180910390fd5b60008060006101000a81548160ff021916908360018111156102cd57fe5b021790555080600190805190602001906102e8929190610719565b50600260008154809291906001019190505550600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f724a2796002546040518263ffffffff1660e01b815260040180828152602001915050602060405180830381600087803b15801561037257600080fd5b505af1158015610386573d6000803e3d6000fd5b505050506040513d602081101561039c57600080fd5b810190808051906020019092919050505060048190555050565b600181815481106103c357fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000801515600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166356ddd8ed6004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561046c57600080fd5b505afa158015610480573d6000803e3d6000fd5b505050506040513d602081101561049657600080fd5b81019080805190602001909291905050501515146104ff576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260258152602001806107e76025913960400191505060405180910390fd5b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663b791ee546004546040518263ffffffff1660e01b81526004018082815260200191505060206040518083038186803b15801561057657600080fd5b505afa15801561058a573d6000803e3d6000fd5b505050506040513d60208110156105a057600080fd5b81019080805190602001909291905050509050600060018054905082816105c357fe5b0690506000600182815481106105d557fe5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050600067ffffffffffffffff8111801561061a57600080fd5b506040519080825280602002602001820160405280156106495781602001602082028036833780820191505090505b506001908051906020019061065f929190610719565b5060016000806101000a81548160ff0219169083600181111561067e57fe5b02179055507f16f7a6a62614f6f8f716bc3e58eb0bc352ca0fe22427017ad411d8256eaa900b6002548285604051808481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a180935050505090565b60025481565b6000809054906101000a900460ff1681565b828054828255906000526020600020908101928215610792579160200282015b828111156107915782518260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555091602001919060010190610739565b5b50905061079f91906107a3565b5090565b6107e391905b808211156107df57600081816101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055506001016107a9565b5090565b9056fe206f7261636c6520717565727920686173206e6f74206265656e2066756c66696c6c656421a26469706673582212204883d4c0670e2bb2adf0c356addf899bcd8adc2ef2b6eb0e2aeb3ca171847a5464736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"randomOracle\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"lotteryId\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"winner\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"ramdomness\",\"type\":\"uint256\"}],\"name\":\"Winner\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"lotteryId\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"lottery_state\",\"outputs\":[{\"internalType\":\"enum LotteryOracleUseVrf.LOTTERY_STATE\",\"name\":\"\",\"type\":\"uint8\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"pickWinner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"players\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address[]\",\"name\":\"_players\",\"type\":\"address[]\"}],\"name\":\"start_new_lottery\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_LOTTERYID = "lotteryId";

    public static final String FUNC_LOTTERY_STATE = "lottery_state";

    public static final String FUNC_PICKWINNER = "pickWinner";

    public static final String FUNC_PLAYERS = "players";

    public static final String FUNC_START_NEW_LOTTERY = "start_new_lottery";

    public static final Event WINNER_EVENT = new Event("Winner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    protected LotteryOracleUseVrf(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<WinnerEventResponse> getWinnerEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WINNER_EVENT, transactionReceipt);
        ArrayList<WinnerEventResponse> responses = new ArrayList<WinnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WinnerEventResponse typedResponse = new WinnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lotteryId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ramdomness = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public BigInteger lotteryId() throws ContractException {
        final Function function = new Function(FUNC_LOTTERYID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger lottery_state() throws ContractException {
        final Function function = new Function(FUNC_LOTTERY_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt pickWinner() {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String pickWinner(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForPickWinner() {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<String> getPickWinnerOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public String players(BigInteger param0) throws ContractException {
        final Function function = new Function(FUNC_PLAYERS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public TransactionReceipt start_new_lottery(List<String> _players) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray<org.fisco.bcos.sdk.v3.codec.datatypes.Address>(
                        org.fisco.bcos.sdk.v3.codec.datatypes.Address.class,
                        org.fisco.bcos.sdk.v3.codec.Utils.typeMap(_players, org.fisco.bcos.sdk.v3.codec.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String start_new_lottery(List<String> _players, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray<org.fisco.bcos.sdk.v3.codec.datatypes.Address>(
                        org.fisco.bcos.sdk.v3.codec.datatypes.Address.class,
                        org.fisco.bcos.sdk.v3.codec.Utils.typeMap(_players, org.fisco.bcos.sdk.v3.codec.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForStart_new_lottery(List<String> _players) {
        final Function function = new Function(
                FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray<org.fisco.bcos.sdk.v3.codec.datatypes.Address>(
                        org.fisco.bcos.sdk.v3.codec.datatypes.Address.class,
                        org.fisco.bcos.sdk.v3.codec.Utils.typeMap(_players, org.fisco.bcos.sdk.v3.codec.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public Tuple1<List<String>> getStart_new_lotteryInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_START_NEW_LOTTERY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<List<String>>(

                convertToNative((List<Address>) results.get(0).getValue())
                );
    }

    public static LotteryOracleUseVrf load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new LotteryOracleUseVrf(contractAddress, client, credential);
    }

    public static LotteryOracleUseVrf deploy(Client client, CryptoKeyPair credential,
            String randomOracle) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(randomOracle)));
        return deploy(LotteryOracleUseVrf.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
    }

    public static class WinnerEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger lotteryId;

        public String winner;

        public BigInteger ramdomness;
    }
}
