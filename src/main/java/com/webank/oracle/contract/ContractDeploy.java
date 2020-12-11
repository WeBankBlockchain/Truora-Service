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
@Table(name = "contract_deploy", schema = "trustoracle",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chainId", "groupId", "contractType"})
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
     * 请求编号，唯一
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private Integer chainId;

    /**
     * Oracle 合约版本号，默认 1
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

    /**
     * @param chainId
     * @param groupId
     * @return
     */
    public static ContractDeploy build(int chainId, int groupId, ContractTypeEnum contractType) {
        ContractDeploy contractDeploy = new ContractDeploy();
        contractDeploy.setChainId(chainId);
        contractDeploy.setGroupId(groupId);
        contractDeploy.setContractType(contractType.getId());
        return contractDeploy;
    }

}