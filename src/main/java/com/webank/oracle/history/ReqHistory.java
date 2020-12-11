package com.webank.oracle.history;

import static com.webank.oracle.base.properties.ConstantProperties.MAX_ERROR_LENGTH;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webank.oracle.base.enums.OracleVersionEnum;
import com.webank.oracle.base.enums.SourceTypeEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
//@DynamicUpdate
@Table(name = "req_history", schema = "trustoracle",
        indexes = {
                @Index(columnList = "chainId,groupId")
        }
)
public class ReqHistory {

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
     * 链 Id
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private int chainId;

    /**
     * 群组 Id
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private int groupId;

    /**
     * Oracle 合约版本号，默认 1
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private int oracleVersion = 1;

    /**
     * 数据来源，0. url。默认0
     */
    @ColumnDefault("0")
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int sourceType;

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
    @Column(columnDefinition = "TEXT")
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


    public static ReqHistory build(int chainId, int groupId,String reqId, String userContract,
                                   OracleVersionEnum oracleVersionEnum,
                                   SourceTypeEnum sourceTypeEnum,
                                   String reqQuery, String timesAmount) {
        return build(chainId,groupId,reqId, userContract, oracleVersionEnum, sourceTypeEnum, reqQuery, null, timesAmount);
    }

    public static ReqHistory build(int chainId, int groupId,String reqId, String userContract,
                                   OracleVersionEnum oracleVersionEnum,
                                   SourceTypeEnum sourceTypeEnum,
                                   String reqQuery,
                                   String serviceIdList,
                                   String timesAmount) {
        ReqHistory reqHistory = new ReqHistory();
        reqHistory.setReqId(reqId);
        reqHistory.setChainId(chainId);
        reqHistory.setGroupId(groupId);
        reqHistory.setOracleVersion(oracleVersionEnum.getId());
        reqHistory.setUserContract(userContract);
        reqHistory.setSourceType(sourceTypeEnum.getId());
        reqHistory.setReqQuery(reqQuery);
        reqHistory.setServiceIdList(serviceIdList);
        reqHistory.setTimesAmount(timesAmount);
        return reqHistory;
    }
}