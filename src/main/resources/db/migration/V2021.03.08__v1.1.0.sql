SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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


-- VRF seed
ALTER TABLE `req_history`
ADD COLUMN `input_seed` varchar(128) NULL DEFAULT '' COMMENT '用户输入种子, VRF 使用',
ADD COLUMN `actual_seed` varchar(128) NULL DEFAULT '' COMMENT '实际计算种子，VRF 使用';


SET FOREIGN_KEY_CHECKS = 1;
