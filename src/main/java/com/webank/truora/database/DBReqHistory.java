package com.webank.truora.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webank.truora.base.enums.SourceTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

import static com.webank.truora.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

/**
 *
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
//@DynamicUpdate
@Table(name = "req_history", schema = "truora",
        indexes = {
                @Index(columnList = "chainId,groupId")
        }
)
public class DBReqHistory {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    private Long id;

    /**
     * 请求编号，唯一
     */
    @Column(unique = true, nullable = false, length = 66)
    @EqualsAndHashCode.Include
    private String reqId;

    /**
     * 平台名，可以包含版本，如fiscobcos2,fiscobcos3等
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    @ColumnDefault("1")
    private String platform="fiscobcos";

    /**
     * 链 Id
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    @ColumnDefault("1")
    private String chainId;

    /**
     * 群组 Id
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(256)")
    @ColumnDefault("1")
    private String groupId;

    /**
     * Oracle 合约版本号，默认 v1.0.0
     */
    @Column(nullable = false, length = 8)
    @ColumnDefault("'v1.0.0'")
    private String oracleVersion = "v1.0.0";

    /**
     * 数据来源，0. url。默认0
     */
    @ColumnDefault("0")
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int sourceType;

    @Column(nullable = false, columnDefinition = "BIGINT(20) UNSIGNED")
    private BigInteger blockNumber;

    /**
     * 请求地址格式
     */
    @Column(nullable = false, length = 512)
    private String reqQuery;

    /**
     * 是否需要证明，仅链下 API 有效
     */
    @Column(nullable = false)
    private boolean needProof;

    /**
     * 请求状态, 0. 请求中；1. 请求失败；2. 请求成功。默认 0
     */
    @ColumnDefault("0")
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int reqStatus;


    /**
     * 来源合约地址
     */
    @Column(nullable = false, length = 128)
    private String userContract;

    /**
     * 请求处理时长，默认 0
     */
    @ColumnDefault("0")
    @Column(nullable = false, columnDefinition = "BIGINT(20) UNSIGNED")
    private long processTime;

    /**
     * 请求结果
     */
    @Column(columnDefinition = "TEXT")
    private String result;

    /**
     * 放大倍数，防止出现小数。
     */
    @Column(length = 32)
    private String timesAmount;

    /**
     * 请求失败是错误信息
     */
    @Column(length = MAX_ERROR_LENGTH)
    private String error;

    /**
     * 证明类型, 0. 无证明；1. 签名认证。默认0
     */
    @ColumnDefault("0")
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private int proofType;

    /**
     * 证明
     */
    @Column(columnDefinition = "TEXT")
    private String proof;

    /**
     * 处理请求的 oracle service id 列表
     */
    @Column(length = 256)
    private String serviceIdList;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    // ************************* Add in v1.1.0 VRF *************************
    /**
     *  用户输入种子
     */
    @Column(length = 128)
    private String inputSeed="";
    /**
     *  实际计算种子
     */
    @Column(length = 128)
    private String actualSeed="";

    public static DBReqHistory build(String chainId, String groupId, BigInteger blockNumber, String reqId, String userContract,
                                     String coreContractVersion,
                                     SourceTypeEnum sourceTypeEnum,
                                     String reqQuery, String timesAmount) {
        return build(chainId,groupId,blockNumber,reqId, userContract, coreContractVersion, sourceTypeEnum, reqQuery, null, timesAmount, "");
    }

    public static DBReqHistory build(String chainId, String groupId, BigInteger blockNumber, String reqId, String userContract,
                                     String coreContractVersion,
                                     SourceTypeEnum sourceTypeEnum,
                                     String reqQuery,
                                     String serviceIdList,
                                     String timesAmount,
                                     String inputSeed) {
        DBReqHistory reqHistory = new DBReqHistory();
        reqHistory.setReqId(reqId);
        reqHistory.setChainId(chainId);
        reqHistory.setGroupId(groupId);
        reqHistory.setBlockNumber(blockNumber);
        reqHistory.setOracleVersion(coreContractVersion);
        reqHistory.setUserContract(userContract);
        reqHistory.setSourceType(sourceTypeEnum.getId());
        reqHistory.setReqQuery(reqQuery);
        reqHistory.setServiceIdList(serviceIdList);
        reqHistory.setTimesAmount(timesAmount);
        reqHistory.setInputSeed(inputSeed);
        return reqHistory;
    }
}