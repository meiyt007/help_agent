/*
 Navicat Premium Data Transfer

 Source Server         : 172.168.250.1
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 172.168.250.1:3306
 Source Schema         : new_platform_form_init

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 09/08/2021 15:18:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_form_atta
-- ----------------------------
DROP TABLE IF EXISTS `t_form_atta`;
CREATE TABLE `t_form_atta`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `ATTA_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件业务主键',
  `NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件名称',
  `ORIGIN_NAME` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件原始名称',
  `FILE_PATH` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `EXTENSION_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件扩展名',
  `UPLOAD_DATE` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `USER_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户业务主键',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `FILE_SIZE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `MODIFY_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `FASTDFS_UPLOAD_URL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'fastdfs上传url',
  `CAN_CHANGE_FLAG` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可交换标识，仅为Y时可交换',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `SA_IS_DELETE`(`IS_DELETE`) USING BTREE,
  INDEX `SA_OID`(`ID`) USING BTREE,
  INDEX `NAME`(`NAME`) USING BTREE,
  INDEX `MODIFY_DATE`(`MODIFY_DATE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_authorize
-- ----------------------------
DROP TABLE IF EXISTS `t_form_authorize`;
CREATE TABLE `t_form_authorize`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权key',
  `SYSTEM_NAME` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统名称',
  `TIME_TYPE` int(1) NULL DEFAULT NULL COMMENT '授权期限类型（0永久 1临时）',
  `START_TIME` datetime(0) NULL DEFAULT NULL COMMENT '授权开始时间',
  `END_TIME` datetime(0) NULL DEFAULT NULL COMMENT '授权结束时间',
  `REMARK` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '启禁用状态',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `MODIFY_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 120 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单访问授权' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_column
-- ----------------------------
DROP TABLE IF EXISTS `t_form_column`;
CREATE TABLE `t_form_column`  (
  `ID` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '主键',
  `COLUMN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `OBJECT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储对象的主键',
  `DATASOURCE_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库对象',
  `COLUMN_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `COLUMN_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `OBJECT_PROPERTY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对象的属性（唯一标识）',
  `NOT_NULL` int(1) NULL DEFAULT NULL COMMENT '必填  1必填 0非必填',
  `DEFAULT_VALUE` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `MAX_LENTH` varchar(20) NULL DEFAULT NULL COMMENT '最大长度',
  `DATA_TYPE` int(1) NULL DEFAULT NULL COMMENT '数据存储类型(0 字符串 1数组 2对象 默认字符串)',
  `SOURCE` int(1) NULL DEFAULT NULL COMMENT '来源（0 对象 1数据库）',
  `FOREIGN_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联外键',
  `FOREIGN_FORM` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表',
  `DEMO` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `EXTAND_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展对象主键',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 862 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表单元素列' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_component
-- ----------------------------
DROP TABLE IF EXISTS `t_form_component`;
CREATE TABLE `t_form_component`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `COMPONENT_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `COMPONENT_CODE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组件类型',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权key',
  `COMPONENT_CONFIG` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '组件配置',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间 ',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单组件分类授权表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_datasource
-- ----------------------------
DROP TABLE IF EXISTS `t_form_datasource`;
CREATE TABLE `t_form_datasource`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `DATASOURCE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `AUTHORIZE_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `MODULE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块主键',
  `DATASOURCE_NAME` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `CONNECTION_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接名',
  `HOST` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的主机',
  `PORT` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的端口',
  `TYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的类型',
  `SERVICE_NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名',
  `DRIVER_CLASS_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的去驱动名称',
  `USERNAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的登录名',
  `PASSWORD` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库的密码',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '是否启禁用',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单数据库配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_design
-- ----------------------------
DROP TABLE IF EXISTS `t_form_design`;
CREATE TABLE `t_form_design`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `DESIGN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `DESIGN_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单名称',
  `FORM_MAIN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属表单主键',
  `FORM_CONFIG` longblob NULL COMMENT '设计表单配置',
  `MODULE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块主键',
  `OBJECT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储对象主键',
  `VERSION` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `RELEASE_STATUS` int(1) NULL DEFAULT NULL COMMENT '发布状态 \'0\':\'未设计\',\'1\':\'草稿\',\'2\':\'发布\',\'3\':\'变更\'',
  `SAVE_DATA_TYPE` int(1) NULL DEFAULT NULL COMMENT '数据库存储类型  0本地  1API  2数据库',
  `API_URL` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'API请求url',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态 1已删除 0未删除',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `MODIFY_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `FORM_TEMPLATE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属模板',
  `SOURCE` int(1) NULL DEFAULT NULL COMMENT '来源 0设计，1模板，2导入',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 879274 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表单设计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_main
-- ----------------------------
DROP TABLE IF EXISTS `t_form_main`;
CREATE TABLE `t_form_main`  (
  `ID` bigint(20) NOT NULL COMMENT '业务主键',
  `FORM_MAIN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `FORM_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单名称',
  `FORM_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单编码',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `MODULE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块主键',
  `OBJECT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储对象主键',
  `DICT_TYPE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单所属类型',
  `FORM_STATUS` int(1) NULL DEFAULT NULL COMMENT '发布状态 \'0\':\'未设计\',\'1\':\'草稿\',\'2\':\'发布\',\'3\':\'变更\'',
  `SAVE_DATA_TYPE` int(1) NULL DEFAULT NULL COMMENT '数据库存储类型  0本地  1API  2数据库',
  `API_URL` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'API请求url',
  `VERSION` int(5) NULL DEFAULT NULL COMMENT '最新的版本号',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '启禁用',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态 1已删除 0未删除',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `IDX_FORM_MAIN_OID`(`FORM_MAIN_OID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 5461 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_member
-- ----------------------------
DROP TABLE IF EXISTS `t_form_member`;
CREATE TABLE `t_form_member`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `MEMBER_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `USER_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户oid',
  `USER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成员名称',
  `AUTHORIZE_KEY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入系统授权key',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接入系统的成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_module
-- ----------------------------
DROP TABLE IF EXISTS `t_form_module`;
CREATE TABLE `t_form_module`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `MODULE_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `AUTHORIZE_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权KEY',
  `MODULE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `DEMO` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '启禁用',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单的模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_object
-- ----------------------------
DROP TABLE IF EXISTS `t_form_object`;
CREATE TABLE `t_form_object`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `OBJECT_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `AUTHORIZE_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权key',
  `DATASOURCE_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储数据库主键',
  `MODULE_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块主键',
  `OBJECT_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储对象名称',
  `OBJECT_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储对象编码',
  `OBJECT_FORM` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象表名',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '启禁用状态',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `IS_SAVE` int(11) NULL DEFAULT 1 COMMENT '是否保存 0否 1是',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '存储对象表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_object_extand
-- ----------------------------
DROP TABLE IF EXISTS `t_form_object_extand`;
CREATE TABLE `t_form_object_extand`  (
  `ID` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '主键',
  `EXTAND_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `MAIN_OBJECT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主存储对象的主键',
  `SECONDARY_OBJECT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副存储对象的主键',
  `VARIABLE_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变量名',
  `FOREIGN_KEY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外键',
  `TYPE` int(11) NULL DEFAULT NULL COMMENT '类型',
  `DEMO` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态 1已删除 0未删除',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '存储对象扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_report
-- ----------------------------
DROP TABLE IF EXISTS `t_form_report`;
CREATE TABLE `t_form_report`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `REPORT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `FORM_MAIN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属表单主键',
  `DESIGN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属表单设计',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `BUSINESS_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联第三方的业务主键',
  `FORM_DATA` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据json数据',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单填报内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_report_log
-- ----------------------------
DROP TABLE IF EXISTS `t_form_report_log`;
CREATE TABLE `t_form_report_log`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `REPORT_LOG_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `REPORT_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '填报主键',
  `FORM_MAIN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属表单主键',
  `DESIGN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属表单设计',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `BUSINESS_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联第三方的业务主键',
  `FORM_DATA` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据json数据',
  `SAVE_DATA_TYPE` int(1) NULL DEFAULT NULL COMMENT '数据库存储类型  0本地  1API  2数据库',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 16384 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '填报数据日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_template
-- ----------------------------
DROP TABLE IF EXISTS `t_form_template`;
CREATE TABLE `t_form_template`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `TEMPLATE_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `TEMPLATE_NAME` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `TEMPLATE_CONFIG` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '模板设计代码',
  `DESIGN_OID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板来源oid',
  `TEMPLATE_ATTA` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板样本附件',
  `REMARK` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `IS_PUBLIC` int(1) NULL DEFAULT NULL COMMENT '是否公开（1公开 0不公开）',
  `IS_FIXED` int(1) NULL DEFAULT NULL COMMENT '是否固化（1固化 其他不固化）',
  `IS_ABLE` int(1) NULL DEFAULT NULL COMMENT '启禁用状态',
  `IS_DELETE` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AVG_ROW_LENGTH = 10240 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子表单模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_form_table
-- ----------------------------
DROP TABLE IF EXISTS `t_form_table`;
CREATE TABLE `t_form_table`  (
  `ID` bigint(0) NOT NULL COMMENT '主键',
  `TABLE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `DATASOURCE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源业务主键',
  `TABLE_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `COLUMN_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `COLUMN_TYPE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `MAX_LENGTH` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最大长度',
  `DEMO` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `INDEX_FLAG` int(0) NULL DEFAULT NULL COMMENT '是否创建索引 默认0 不创建 1创建',
  `IS_NOT_NULL` int(0) NULL DEFAULT NULL COMMENT '是否为空 默认为0   1不为null',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `t_docx_template`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `DOCX_TEMPLATE_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务主键',
  `DOCX_TEMPLATE_NAME` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `DOCX_ATTA_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'DOCX模板附件oid',
  `OBJECT_OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储对象的oid',
  `APPEND_OBJECT_OIDS` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '追加的存储对象的oid',
  `AUTHORIZE_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权key',
  `IS_DELETE` int(1) NOT NULL COMMENT '删除状态',
  `IS_ABLE` int(1) NOT NULL COMMENT '是否禁用',
  `CREATE_DATE` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
