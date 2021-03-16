package com.webank.oracle.event.vo;

import java.math.BigInteger;

import org.fisco.bcos.web3j.tx.txdecode.LogResult;

import com.webank.oracle.base.enums.SourceTypeEnum;
import com.webank.oracle.history.ReqHistory;

/**
 *
 */

public interface LogEvent {

    /**
     * @param logResult
     */
    public void parse(LogResult logResult);


    /**
     * @return
     */
    public ReqHistory convert(int chainId, int groupId, BigInteger blockNumber, String coreContractVersion, SourceTypeEnum sourceTypeEnum);

}
