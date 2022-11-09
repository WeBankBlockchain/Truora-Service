package com.webank.truora.bcos3runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fisco.bcos.sdk.v3.codec.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.v3.model.EventLog;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;

public class Bcos3ModelTools {

    public static ABIDefinition findAbiDefinition(String abi,String funcname) throws JsonProcessingException {
        ABIDefinition[] abiDefinition = (new ObjectMapper()).readValue(abi, ABIDefinition[].class);
        for (ABIDefinition definition : abiDefinition) {
            if (definition.getName() != null && definition.getName().equals(funcname)) {
                return definition;
            }
        }
        return null;
    }

    public static EventLog logToEventLog(TransactionReceipt.Logs receiptlog)
    {

        EventLog eventLog = new EventLog();
        eventLog.setAddress(receiptlog.getAddress());
        eventLog.setLogIndex("0");
        eventLog.setData(receiptlog.getData());
        eventLog.setTopics(receiptlog.getTopics());
        eventLog.setBlockNumber(receiptlog.getBlockNumber());
        //eventLog.setTransactionHash(receiptlog.getTransactionHash());
        eventLog.setTransactionIndex("0");
        return eventLog;
    }

}
