package com.webank.oracle.contract;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.webank.oracle.base.enums.ContractTypeEnum;

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
@Table(name = "contract_deploy", schema = "truora",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chainId", "groupId", "contractType", "version"}),
                @UniqueConstraint(columnNames = {"chainId", "groupId", "contractAddress"})
        })

public class ContractDeploy {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    private Long id;

    /**
     * 链 ID
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private Integer chainId;

    /**
     * 群组 ID
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private Integer groupId = 1;

    /**
     * 合约类型：
     * 0: oracle core
     * 1: vrf
     */
    @ColumnDefault("0")
    @Column(unique = false, nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int contractType;


    /**
     * 部署的合约地址
     */
    @Column(unique = true, nullable = true, length = 64)
    private String contractAddress;

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime modifyTime;


    //********************** add in v1.1.0 **********************
    /**
     * 合约版本
     */
    @Column(length = 8, nullable = false)
    @ColumnDefault("'v1.0.0'")
    private String version = "v1.0.0";

    @Column(nullable = false)
    @ColumnDefault("1")
    private boolean enable = true;



    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public static ContractDeploy build(int chainId, int groupId, ContractTypeEnum contractType, String version ) {
        ContractDeploy contractDeploy = new ContractDeploy();
        contractDeploy.setChainId(chainId);
        contractDeploy.setGroupId(groupId);
        contractDeploy.setContractType(contractType.getId());
        contractDeploy.setVersion(version);
        return contractDeploy;
    }

}