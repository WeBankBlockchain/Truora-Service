package com.webank.truora.bcos2runner.oracle;

import com.webank.truora.base.enums.ReturnTypeEnum;
import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;

import java.math.BigInteger;
import java.util.List;

/**
 *
 */

@Slf4j
@Getter
@Setter
public class OracleCoreLogResult extends BaseLogResult {

    private static final String LOG_CALLBACK_ADDR = "callbackAddr";
    private static final String LOG_URL = "url";
    private static final String LOG_TIMES_AMOUNT = "timesAmount";
    private static final String LOG_EXPIRATION = "expiration";
    private static final String NEED_PROOF = "needProof";
    private static final String RETURN_TYPE = "returnType";

    private String callbackAddress;
    private String url;
    private BigInteger expiration;

    /**
     * Multiply the result by 1000000000000000000 to remove decimals
     */
    private BigInteger timesAmount;
    private boolean needProof;
    private ReturnTypeEnum returnType;

    public OracleCoreLogResult(LogResult logResult) {
        super(logResult);
    }

    @Override
    public void parse(LogResult logResult) {
        List rawLogResults = logResult.getLogParams();
        callbackAddress = CommonUtils.getStringFromEventLog(rawLogResults, LOG_CALLBACK_ADDR);
        url = CommonUtils.getStringFromEventLog(rawLogResults, LOG_URL);
        timesAmount = CommonUtils.getBigIntegerFromEventLog(rawLogResults, LOG_TIMES_AMOUNT);
        expiration = CommonUtils.getBigIntegerFromEventLog(rawLogResults, LOG_EXPIRATION);
        needProof = CommonUtils.getBooleanFromEventLog(rawLogResults, NEED_PROOF);
        returnType = ReturnTypeEnum.get(CommonUtils.getBigIntegerFromEventLog(rawLogResults, RETURN_TYPE));
    }

    @Override
    public DBReqHistory convert(String chainId, String groupId, BigInteger blockNumber, String oracleCoreVersion, SourceTypeEnum sourceTypeEnum) {
        return DBReqHistory.build(chainId, groupId, blockNumber, requestId, callbackAddress, oracleCoreVersion, sourceTypeEnum, url, timesAmount.toString(10));
    }
}