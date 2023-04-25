/*
 Navicat Premium Data Transfer

 Source Server         : 172.168.250.1
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 172.168.250.1:3306
 Source Schema         : zfsoft_form_v1.0

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 08/12/2021 10:12:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_docx_template
-- ----------------------------
DROP TABLE IF EXISTS `t_docx_template`;
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
