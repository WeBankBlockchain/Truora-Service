package com.webank.truora.database;

import com.webank.truora.base.enums.ContractEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 *
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
//@DynamicUpdate
@Table(name = "contract_deploy", schema = "truora", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chainId", "groupId", "contractType", "version"}),
        @UniqueConstraint(columnNames = {"chainId", "groupId", "contractAddress"})
})

public class DBContractDeploy {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    private Long id;

    /**
     * 平台名，可以包含版本，如fiscobcos2,fiscobcos3等
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    @ColumnDefault("1")
    private String platform="fiscobcos";


    /**
     * 链 ID
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    @ColumnDefault("1")
    private String chainId="1";

    /**
     * 群组 ID
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    @ColumnDefault("1")
    private String groupId = "1";

    /**
     * 合约类型：
     * 0: oracle core
     * 1: vrf
     */
    @ColumnDefault("0")
    @Column(unique = false, nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int contractType;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    @ColumnDefault("1")
    private String contractName="";

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
    public static DBContractDeploy build(String platform, String chainId, String groupId, ContractEnum contractType, String contractName, String version ) {
        DBContractDeploy contractDeploy = new DBContractDeploy();
        contractDeploy.setPlatform(platform);
        contractDeploy.setChainId(chainId);
        contractDeploy.setGroupId(groupId);
        contractDeploy.setContractType(contractType.getType());
        contractDeploy.setContractName(contractName);
        contractDeploy.setVersion(version);
        return contractDeploy;
    }

}