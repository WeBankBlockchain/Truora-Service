package com.webank.truora.bcos2runner.base;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.database.DBReqHistory;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;

import java.math.BigInteger;

/**
 *
 */

public interface TruoraLogEvent {

    /**
     * @param logResult
     */
    public void parse(LogResult logResult);


    /**
     * @return
     */
    public DBReqHistory convert(String chainId, String groupId, BigInteger blockNumber, String coreContractVersion, SourceTypeEnum sourceTypeEnum);

}
