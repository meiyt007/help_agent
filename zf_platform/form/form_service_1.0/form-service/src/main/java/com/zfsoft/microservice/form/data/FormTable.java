package com.zfsoft.microservice.form.data;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName FormTable
 * @Description: 创建表的表字段
 * @Author wuxx
 * @Date 2021/8/6
 **/
@Data
public class FormTable {

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
    @NotNull(message = "id不能为空",groups = {FormTable.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormTable.UPDATE_GROUP.class})
    private String tableOid;

    /**
     * @COLUMN_EXPLAIN : 存储数据库主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String datasourceOid;

    /**
     * @COLUMN_EXPLAIN : 表名称
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "表名称不能为空",groups = {FormTable.UPDATE_GROUP.class})
    private String tableName;

    /**
     * @COLUMN_EXPLAIN : 列名称
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "列名称不能为空",groups = {FormTable.UPDATE_GROUP.class})
    private String columnName;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 最大长度
     */
    private String maxLength;
    /**
     * 备注
     */
    private String demo;

    /**
     * 是否创建索引 默认不创建 1创建
     */
    private Integer indexFlag;

    /**
     * 是否为空 默认为 1不为null
     */
    private Integer isNotNull;

    private Date createDate;
}
