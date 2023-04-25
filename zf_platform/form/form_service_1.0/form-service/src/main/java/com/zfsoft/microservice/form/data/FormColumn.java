package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @description: 表结构
 * @author: wuxx
 * @Date: 2021/4/2 10:26
 **/
@Data
@ToString
public class FormColumn {

    /**
     * 新增信息验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新信息验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * @COLUMN_EXPLAIN : 主键
     * @TABLE_COLUMN_TYPE : VARCHAR (64)
     */
    @NotNull(message = "id不能为空",groups = {FormColumn.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormColumn.UPDATE_GROUP.class})
    private String columnOid;

    /**
     * @COLUMN_EXPLAIN : 存储对象主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "存储对象主键不能为空",groups = {FormColumn.UPDATE_GROUP.class})
    private String objectOid;

    /**
     * @COLUMN_EXPLAIN : 存储数据库主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String datasourceOid;

    /**
     * @COLUMN_EXPLAIN : 对象属性（唯一标识）
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "对象属性主键不能为空",groups = {FormColumn.UPDATE_GROUP.class})
    @Length(min = 1,max = 50,message = "对象属性长度为1-50",groups = {FormColumn.INSERT_GROUP.class})
    @Pattern(message = "对象属性只能由字母、数字或下划线组成的非数字开头字符",regexp = "^[^0-9][A-Za-z0-9_]+$",groups = {FormColumn.INSERT_GROUP.class})
    private String objectProperty;

    /**
     * @COLUMN_EXPLAIN : 列类型
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String columnType;

    /**
     * @COLUMN_EXPLAIN : 列名称
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String columnName;

    /**
     * @COLUMN_EXPLAIN : 必填  1必填 0非必填
     */
    private Integer notNull;
    /**
     * @COLUMN_EXPLAIN : 来源（0 对象 1数据库）
     */
    private Integer source;
    /**
     * @COLUMN_EXPLAIN : 默认值
     */
    private String defaultValue;

    /**
     * @COLUMN_EXPLAIN : 最大长度
     */
    private String maxLenth;

    /**
     * @COLUMN_EXPLAIN : 数据存储类型(0 字符串 1数组 2对象 3文件类 默认字符串)
     * @TABLE_COLUMN_TYPE : Integer (1)
     */
    private Integer dataType;

    /**
     * @COLUMN_EXPLAIN : 关联表
     */
    private String foreignForm;

    /**
     * @COLUMN_EXPLAIN : 关联key
     */
    private String foreignKey;

    /**
     * @COLUMN_EXPLAIN : 备注
     */
    private String demo;

    /**
     * 扩展对象主键
     */
    private String extandOid;


}
