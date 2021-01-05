ALTER TABLE req_history ADD COLUMN block_number bigint(20) COMMENT '当前块高' DEFAULT '0' ;
ALTER TABLE req_history ADD COLUMN need_proof tinyint(1) COMMENT '需要proof验证' DEFAULT '0' ;

-- change text to varchar(32)
ALTER TABLE req_history MODIFY COLUMN times_amount varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL;

