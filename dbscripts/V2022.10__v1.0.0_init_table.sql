
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contract_deploy
-- ----------------------------
CREATE TABLE `contract_deploy` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`platform` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '平台名,如fiscobcos,fiscobcos3' COLLATE 'utf8mb4_general_ci',
	`chain_id` VARCHAR(100) NOT NULL DEFAULT '1' COLLATE 'utf8mb4_general_ci',
	`group_id` VARCHAR(100) NOT NULL DEFAULT '1' COLLATE 'utf8mb4_general_ci',
	`contract_address` VARCHAR(64) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`contract_type` INT(11) UNSIGNED NOT NULL DEFAULT '0',
	`contract_name` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '字符串类型的合约名，增加可读性' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT NULL,
	`version` VARCHAR(8) NOT NULL DEFAULT 'v1.0.0' COMMENT '合约版本号，格式：vx.x.x默认: v1.0.0' COLLATE 'utf8mb4_general_ci',
	`enable` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否启用监听，true，监听；false，不监听。默认：true，监听',
	`modify_time` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK80cfrlmgu023crlr3c0f750hm` (`platform`,`chain_id`, `group_id`, `contract_type`, `version`) USING BTREE,
	UNIQUE INDEX `UK_mru8e0c49ymnl9gdsof1rcok6` (`platform`,`chain_id`, `group_id`, `contract_address`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;



-- create table lib_config
CREATE TABLE `lib_config` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	
	`chain_id` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '链 ID，如果是全局配置，为 0' COLLATE 'utf8mb4_general_ci',
	`group_id` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '群组 ID，如果是全局配置，为 0' COLLATE 'utf8mb4_general_ci',
	`config_type` VARCHAR(512) NOT NULL COMMENT '配置类型，字符串' COLLATE 'utf8mb4_general_ci',
	
	`config_value` VARCHAR(1024) NULL DEFAULT NULL COMMENT '配置值' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT NULL,
	`modify_time` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UKf1mrkpnxd702s4epjeii75fh3` (`chain_id`, `group_id`, `config_type`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


-- ----------------------------
-- Table structure for req_history
-- ----------------------------
CREATE TABLE `req_history` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`platform` VARCHAR(100) NOT NULL DEFAULT '1' COMMENT '平台名,如fiscobcos,fiscobcos3' COLLATE 'utf8mb4_general_ci',
	`chain_id` VARCHAR(100) NOT NULL DEFAULT '1' COLLATE 'utf8mb4_general_ci',
	`group_id` VARCHAR(100) NOT NULL DEFAULT '1' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT NULL,
	`error` VARCHAR(512) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`modify_time` DATETIME NULL DEFAULT NULL,
	`oracle_version` VARCHAR(8) NOT NULL DEFAULT 'v1.0.0' COLLATE 'utf8mb4_general_ci',
	`process_time` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
	`proof` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`proof_type` INT(11) UNSIGNED NULL DEFAULT '0',
	`req_id` VARCHAR(66) NOT NULL COLLATE 'utf8mb4_general_ci',
	`req_query` VARCHAR(512) NOT NULL COLLATE 'utf8mb4_general_ci',
	`req_status` INT(11) UNSIGNED NOT NULL DEFAULT '0',
	`result` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`service_id_list` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`source_type` INT(11) UNSIGNED NOT NULL DEFAULT '0',
	`times_amount` VARCHAR(32) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`user_contract` VARCHAR(128) NOT NULL COLLATE 'utf8mb4_general_ci',
	`block_number` BIGINT(20) NULL DEFAULT '0' COMMENT '当前块高',
	`need_proof` TINYINT(1) NULL DEFAULT '0' COMMENT '需要proof验证',
	`input_seed` VARCHAR(128) NULL DEFAULT '' COMMENT '用户输入种子, VRF 使用' COLLATE 'utf8mb4_general_ci',
	`actual_seed` VARCHAR(128) NULL DEFAULT '' COMMENT '实际计算种子，VRF 使用' COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK_e4v85484s96rgrfl7u0m4sp9t` (`req_id`) USING BTREE,
	INDEX `IDX2lf6ws09wkc6nnamfw3cxw9sg` (`chain_id`, `group_id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


SET FOREIGN_KEY_CHECKS = 1;
