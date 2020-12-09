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
  `NUM` decimal(18,0)  DEFAULT NULL COMMENT '数量',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`),
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='企上云数量';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'UserID',
  `NAME` varchar(32) DEFAULT NULL COMMENT '姓名',
  `TELEPHONE` varchar(16) DEFAULT NULL COMMENT '住宅电话',
  `USERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(255) DEFAULT NULL COMMENT '密码',
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

-- 安全生产码
DROP TABLE IF EXISTS `safecode`;
CREATE TABLE `safecode` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `GREEN` decimal(18,0) DEFAULT NULL COMMENT '绿码数量',
  `YELLOW` decimal(18,0) DEFAULT NULL COMMENT '黄码数量',
  `ORANGE` decimal(18,0) DEFAULT NULL COMMENT '橙码数量',
  `RED` decimal(18,0) DEFAULT NULL COMMENT '红码数量',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

-- 安全隐患
DROP TABLE IF EXISTS `hidden_danger`;
CREATE TABLE `hidden_danger` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ENAME` varchar(1000) DEFAULT NULL COMMENT '企业名称',
  `CONTENT` varchar(1000) DEFAULT NULL COMMENT '隐患内容',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

-- 指标监测
DROP TABLE IF EXISTS `target_monitor`;
CREATE TABLE `target_monitor` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ECOUNT` decimal(18,0) DEFAULT NULL COMMENT '企业总数',
  `HASCHECK` decimal(18,0) DEFAULT NULL COMMENT '已巡检',
  `NOTCHECK` decimal(18,0) DEFAULT NULL COMMENT '未巡检',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

-- 智能预警
DROP TABLE IF EXISTS `intelligent_tips`;
CREATE TABLE `intelligent_tips` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ENAME` varchar(200) DEFAULT NULL COMMENT '企业名称',
  `SDANGER` varchar(1000) DEFAULT NULL COMMENT '安全隐患',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '记录首次创建的时间。',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '记录每次修改的时间。',
  `REMARK` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '记录备注。',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;

-- 隐患分类分析
DROP TABLE IF EXISTS `hid_danger_class`;
CREATE TABLE `hid_danger_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hid_danger_type` varchar(200) DEFAULT NULL COMMENT '隐患类型',
  `hid_danger_num` decimal(10,0) DEFAULT NULL COMMENT '隐患数量',
  `proportion` decimal(10,0) DEFAULT NULL COMMENT '比例',
  `CREATE_DATE` datetime DEFAULT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  `REMARK` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 风险等级分析
DROP TABLE IF EXISTS `risk_level_analy`;
CREATE TABLE `risk_level_analy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ENTERPRISE_NAME` varchar(300) DEFAULT NULL COMMENT '公司名称',
  `risk_level` varchar(200) DEFAULT NULL COMMENT '风险等级',
  `CREATE_DATE` datetime DEFAULT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  `REMARK` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--隐患整改分析
DROP TABLE IF EXISTS `hid_danger_change`;
CREATE TABLE `hid_danger_change` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `treatment` varchar(200) DEFAULT NULL COMMENT '处理情况',
  `hid_danger_num` decimal(10,0) DEFAULT NULL COMMENT '隐患数量',
  `proportion` decimal(10,0) DEFAULT NULL COMMENT '比例',
  `CREATE_DATE` datetime DEFAULT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  `REMARK` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
