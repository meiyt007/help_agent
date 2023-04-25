alter table t_form_column  add EXTAND_OID varchar(64) comment '扩展对象主键';
CREATE TABLE `t_form_object_extand` (
  `ID` bigint(20) unsigned zerofill NOT NULL COMMENT '主键',
  `EXTAND_OID` varchar(64) NOT NULL COMMENT '业务主键',
  `MAIN_OBJECT_OID` varchar(64) DEFAULT NULL COMMENT '主存储对象的主键',
  `SECONDARY_OBJECT_OID` varchar(64) DEFAULT NULL COMMENT '副存储对象的主键',
  `VARIABLE_NAME` varchar(100) DEFAULT NULL COMMENT '变量名',
  `FOREIGN_KEY` varchar(100) DEFAULT NULL COMMENT '外键',
  `TYPE` int(11) DEFAULT NULL COMMENT '类型',
  `DEMO` varchar(512) DEFAULT NULL COMMENT '备注',
  `IS_DELETE` int(1) DEFAULT NULL COMMENT '删除状态 1已删除 0未删除',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储对象扩展表';
