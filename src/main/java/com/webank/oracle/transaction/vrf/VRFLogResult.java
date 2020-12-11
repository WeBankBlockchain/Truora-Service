package com.webank.oracle.transaction.vrf;

import java.math.BigInteger;

import org.fisco.bcos.web3j.tx.txdecode.LogResult;

import com.webank.oracle.base.enums.OracleVersionEnum;
import com.webank.oracle.base.enums.SourceTypeEnum;
import com.webank.oracle.base.utils.CommonUtils;
import com.webank.oracle.event.vo.BaseLogResult;
import com.webank.oracle.history.ReqHistory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@Getter
@Setter
public class VRFLogResult extends BaseLogResult {

    private static final String LOG_KEY_HASH = "keyHash";
    private static final String LOG_SEED = "seed";
    private static final String LOG_BLOCK_NUMBER = "blockNumber";
    private static final String LOG_SENDER = "sender";
    private static final String LOG_SEED_BLOCK_NUM = "seedAndBlockNum";

    private String keyHash;
    private BigInteger seed;
    private BigInteger blockNumber;
    private String sender;
    private String seedAndBlockNum;

    public VRFLogResult(LogResult logResult) {
        super(logResult);
    }

    @Override
    public void parse(LogResult logResult) {
        keyHash = CommonUtils.byte32LogToString(logResult.getLogParams(), LOG_KEY_HASH);
        seed = CommonUtils.getBigIntegerFromEventLog(logResult.getLogParams(), LOG_SEED);
        blockNumber = CommonUtils.getBigIntegerFromEventLog(logResult.getLogParams(), LOG_BLOCK_NUMBER);
        sender = CommonUtils.getStringFromEventLog(logResult.getLogParams(), LOG_SENDER);
        seedAndBlockNum = CommonUtils.byte32LogToString(logResult.getLogParams(), LOG_SEED_BLOCK_NUM);
    }

    @Override
    public ReqHistory convert(int chainId, int groupId, OracleVersionEnum oracleVersionEnum, SourceTypeEnum sourceTypeEnum) {
        return ReqHistory.build(chainId, groupId, requestId, sender, oracleVersionEnum, sourceTypeEnum, seedAndBlockNum, null, null);
    }
}
