package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 模块管理
 * @author: wuxx
 * @Date: 2021/4/2 10:26
 **/
@Data
@ToString
public class FormModule {

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
    @NotNull(message = "id不能为空",groups = {FormModule.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormModule.UPDATE_GROUP.class})
    private String moduleOid;

    /**
     * @COLUMN_EXPLAIN : 授权authorizeKey
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormModule.INSERT_GROUP.class})
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 模块名称
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    @NotNull(message = "模块名称不能为空",groups = {FormModule.INSERT_GROUP.class})
    @Length(min = 1,max = 20,message = "模块名称长度为1-20",groups = {INSERT_GROUP.class})
    private String moduleName;

    /**
     * @COLUMN_EXPLAIN : 启禁用状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private Integer isAble;

    /**
     * @COLUMN_EXPLAIN : 删除状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private Integer isDelete;

    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

    /**
     * @COLUMN_EXPLAIN : 备注
     * @TABLE_COLUMN_TYPE : VARCHAR (200)
     */
    private String demo;

}
