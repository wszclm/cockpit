-- sql 表创建脚本--

--  上云企业数据模型
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
  `ID` varchar(35) COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `ENTERPRISE_NAME` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '企业名称',
  `CONTACT` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '法人姓名',
  `CONTACT_NUM` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联系电话',
  `PORDUCT` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '主要产品',
  `DETAIL_TONS` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '所在乡镇',
  `DETAIL_ADDRESS` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '详细地址',
  `SAFE_CODE` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '安全码：1234（红，橙，黄，绿）',
  `IS_CLOUD` datetime DEFAULT NULL COMMENT '上云企业',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`),
  KEY `idx_enterprise_name_createtime` (`ENTERPRISE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='记录企业国税数据资源数据信息';
