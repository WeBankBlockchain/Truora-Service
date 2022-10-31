package com.webank.truora.bcos3runner.vrf;

import com.webank.truora.base.enums.SourceTypeEnum;
import com.webank.truora.base.utils.CommonUtils;
import com.webank.truora.database.DBReqHistory;
import com.webank.truora.bcos2runner.base.BaseLogResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;

import java.math.BigInteger;

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
    private static final String LOG_CONSUMER_SEED = "consumerSeed";

    private String keyHash;
    private BigInteger seed;
    private BigInteger blockNumber;
    private String sender;
    private String seedAndBlockNum;
    private BigInteger consumerSeed;

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
        consumerSeed = CommonUtils.getBigIntegerFromEventLog(logResult.getLogParams(), LOG_CONSUMER_SEED);
    }

    @Override
    public DBReqHistory convert(String chainId, String groupId, BigInteger blockNumber,
                                String vrfCoreVersion, SourceTypeEnum sourceTypeEnum) {
        return DBReqHistory.build(chainId, groupId, blockNumber,requestId, sender, vrfCoreVersion, sourceTypeEnum,"", null, null, consumerSeed.toString(16));
    }
}
