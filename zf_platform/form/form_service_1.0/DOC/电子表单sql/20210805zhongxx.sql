-- 主存储对象 不进行保存标识。
alter table t_form_object add `IS_SAVE` int(11) DEFAULT '1' COMMENT '是否保存 0否 1是';
