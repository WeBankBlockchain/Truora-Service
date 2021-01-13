
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contract_deploy
-- ----------------------------
CREATE TABLE `contract_deploy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `chain_id` int(11) unsigned NOT NULL DEFAULT '1',
  `contract_address` varchar(64) DEFAULT NULL,
  `contract_type` int(11) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `group_id` int(11) unsigned NOT NULL DEFAULT '1',
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK80cfrlmgu023crlr3c0f750hm` (`chain_id`,`group_id`,`contract_type`),
  UNIQUE KEY `UK_mru8e0c49ymnl9gdsof1rcok6` (`chain_id`,`group_id`,`contract_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for req_history
-- ----------------------------
CREATE TABLE `req_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `chain_id` int(11) unsigned NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  `error` varchar(512) DEFAULT NULL,
  `group_id` int(11) unsigned NOT NULL DEFAULT '1',
  `modify_time` datetime DEFAULT NULL,
  `oracle_version` int(11) unsigned NOT NULL DEFAULT '1',
  `process_time` bigint(20) unsigned NOT NULL DEFAULT '0',
  `proof` text,
  `proof_type` int(11) unsigned DEFAULT '0',
  `req_id` varchar(66) NOT NULL,
  `req_query` varchar(512) NOT NULL,
  `req_status` int(11) unsigned NOT NULL DEFAULT '0',
  `result` text,
  `service_id_list` varchar(256) DEFAULT NULL,
  `source_type` int(11) unsigned NOT NULL DEFAULT '0',
  `times_amount` varchar(32) DEFAULT NULL,
  `user_contract` varchar(128) NOT NULL,
  `block_number` bigint(20) DEFAULT '0' COMMENT '当前块高',
  `need_proof` tinyint(1) DEFAULT '0' COMMENT '需要proof验证',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_e4v85484s96rgrfl7u0m4sp9t` (`req_id`),
  KEY `IDX2lf6ws09wkc6nnamfw3cxw9sg` (`chain_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
