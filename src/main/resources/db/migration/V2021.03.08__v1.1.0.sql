SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- change oracle version type to string
ALTER TABLE `req_history`
MODIFY COLUMN `oracle_version` varchar(8) NOT NULL DEFAULT 'v1.0.0' AFTER `modify_time`;
-- update oracle version to default v1.0.0
update `req_history` SET `oracle_version` = 'v1.0.0';

-- VRF seed
ALTER TABLE `req_history`
ADD COLUMN `input_seed` varchar(128) NULL DEFAULT '' COMMENT '用户输入种子, VRF 使用',
ADD COLUMN `actual_seed` varchar(128) NULL DEFAULT '' COMMENT '实际计算种子，VRF 使用';

-- add version and enable fields to contract
ALTER TABLE `contract_deploy`
ADD COLUMN `version` varchar(8) NOT NULL DEFAULT 'v1.0.0' COMMENT '合约版本号，格式：vx.x.x默认: v1.0.0' AFTER `modify_time`,
ADD COLUMN `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用监听，true，监听；false，不监听。默认：true，监听' AFTER `version`,
DROP INDEX `UK80cfrlmgu023crlr3c0f750hm`,
ADD UNIQUE INDEX `UK80cfrlmgu023crlr3c0f750hm`(`chain_id`, `group_id`, `contract_type`, `version`) USING BTREE;



-- create table lib_config
CREATE TABLE `lib_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `chain_id` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '链 ID，如果是全局配置，为 0',
  `group_id` int(11) unsigned NOT NULL DEFAULT '1' COMMENT '群组 ID，如果是全局配置，为 0',
  `config_type` varchar(512) NOT NULL COMMENT '配置类型，字符串',
  `config_value` varchar(1024) DEFAULT NULL COMMENT '配置值',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKf1mrkpnxd702s4epjeii75fh3` (`chain_id`,`group_id`,`config_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



SET FOREIGN_KEY_CHECKS = 1;
