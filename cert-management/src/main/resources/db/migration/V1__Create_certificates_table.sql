-- 创建证书信息表
CREATE TABLE `certificates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '证书ID',
  `domain_name` varchar(255) NOT NULL COMMENT '关联域名',
  `sans` text DEFAULT NULL COMMENT '备用名称(Subject Alternative Names)',
  `issuer` varchar(255) NOT NULL COMMENT '证书颁发机构',
  `expiration_date` datetime NOT NULL COMMENT '证书到期时间',
  `owner` varchar(255) NOT NULL COMMENT '证书负责人',
  `notes` text DEFAULT NULL COMMENT '备注信息',
  `status` varchar(20) NOT NULL DEFAULT 'Normal' COMMENT '证书状态: Normal, ExpiringSoon, Expired',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_domain_name` (`domain_name`),
  INDEX `idx_expiration_date` (`expiration_date`),
  INDEX `idx_status` (`status`),
  INDEX `idx_owner` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书信息表';