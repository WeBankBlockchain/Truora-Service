package com.webank.truora.base.utils;

import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.InputAndOutputResult;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoderFactory;

import java.util.Collections;
import java.util.List;

/**
 * 解析合约执行的返回output
 *
 * @author darwindu
 * @date 2019/8/14
 **/
public class DecodeOutputUtils {

    /**
     * 0x16回执TransactionReceipt解析
     *
     * @param input
     * @return
     */
    public static String decodeOutputReturnString0x16(String input) {

        Function function =
                new Function(
                        "Error",
                        Collections.<Type>emptyList(),
                        Collections.singletonList(new TypeReference<Utf8String>() {
                        }));

        List<Type> r = FunctionReturnDecoder.decode(
                input.substring(10),
                function.getOutputParameters());
        return r.get(0).toString();
    }

    /**
     * 非0x16回执TransactionReceipt解析
     *
     * @param abi    合约abi
     * @param input  TransactionReceipt.input
     * @param output TransactionReceipt.output
     * @return
     */
//    public static InputAndOutputResult decodeOutputReturn(String abi, String input, String output) {
//
//        TransactionDecoder txDecodeSampleDecoder = TransactionDecoderFactory
//            .buildTransactionDecoder(abi, CommonConstant.EMPTY);
//        try {
//            return txDecodeSampleDecoder.decodeOutputReturnObject(input, output);
//        } catch (TransactionException | BaseException e) {
//            throw new SdkException(ErrorCode.DECODE_OUTPUT_ERROR, e);
//        }
//    }

    /**
     * 解码合约输出
     * @param abi
     * @param output
     * @return
     */
    public static InputAndOutputResult decoderOutPut(String abi,String input, String output) {
        TransactionDecoder decoder = TransactionDecoderFactory.buildTransactionDecoder(abi, "");
        try {
            InputAndOutputResult decodeResult = decoder.decodeOutputReturnObject( input,  output);
            return decodeResult;
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(decodeOutputReturnString0x16("0x08c379a0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000186e6f20636f72726573706f6e64696e6720726571756573740000000000000000"));
    }
}
