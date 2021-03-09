package com.webank.oracle.config;

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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */

@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
//@DynamicUpdate
@Table(name = "lib_config", schema = "truora",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"chainId", "groupId", "configType"})
})
public class LibConfig {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    private Long id;

    /**
     * 链 ID，如果是全局配置, chainId = 0
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private Integer chainId;

    /**
     * 群组 ID，如果是全局配置, groupId = 0
     */
    @Column(nullable = false, columnDefinition = "INT(11) UNSIGNED")
    @ColumnDefault("1")
    private Integer groupId = 1;

    /**
     * 配置类型
     *
     */
    @Column(length = 512, nullable = false)
    private String configType;

    /**
     * 部署的合约地址
     */
    @Column(nullable = true, length = 1024)
    private String configValue;

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime modifyTime;

    public static LibConfig initGlobalConfig(String configType, String configValue){
        return initChainGroupConfig(0,0,configType,configValue);
    }

    public static LibConfig initChainGroupConfig(int chainId,int groupId,String configType, String configValue){
        LibConfig config =  new LibConfig();
        config.setChainId(chainId);
        config.setGroupId(groupId);
        config.setConfigType(configType);
        config.setConfigValue(configValue);
        return config;

    }
}