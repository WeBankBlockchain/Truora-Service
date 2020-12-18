ALTER TABLE req_history ADD COLUMN block_number bigint(20) COMMENT '当前块高' DEFAULT '0' ;

ALTER TABLE req_history ADD COLUMN need_proof tinyint(1) COMMENT '需要proof验证' DEFAULT '0' ;
