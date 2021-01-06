ALTER TABLE `contract_deploy` DROP INDEX `UK_mru8e0c49ymnl9gdsof1rcok6`;
ALTER TABLE `contract_deploy` ADD UNIQUE INDEX `UK_mru8e0c49ymnl9gdsof1rcok6`(`chain_id`, `group_id`,`contract_address`);

