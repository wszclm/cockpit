-- sql 表创建脚本--

--  企业数据模型
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
  `ID` varchar(35) COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `ENTERPRISE_NAME` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业名称',
  `CONTACT` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '法人姓名',
  `CONTACT_NUM` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联系电话',
  `PORDUCT` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '主要产品',
  `DETAIL_TONS` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '所在乡镇',
  `DETAIL_ADDRESS` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '详细地址',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`),
  KEY `idx_enterprise_name_createtime` (`ENTERPRISE_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='企业信息';

DROP TABLE IF EXISTS `cloud_enterprise`;
CREATE TABLE `cloud_enterprise` (
  `ID` varchar(35) COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `TOWN` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '所在乡镇',
  `NUM` decimal(18,0) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '数量',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`),
  KEY `idx_ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='企上云数量';


CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'UserID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `telephone` varchar(16) DEFAULT NULL COMMENT '住宅电话',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

