package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 表单设计主表表
 * @author: wuxx
 * @Date: 2021/4/16 10:26
 **/
@Data
@ToString
public class FormMain {

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
    @NotNull(message = "id不能为空",groups = {FormMain.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormMain.UPDATE_GROUP.class})
    private String formMainOid;

    /**
     *  授权KEY
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormMain.INSERT_GROUP.class, FormMain.UPDATE_GROUP.class})
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 表单名称
     * @TABLE_COLUMN_TYPE : VARCHAR (100)
     */
    @NotNull(message = "表单名称不能为空",groups = {FormMain.UPDATE_GROUP.class,FormMain.UPDATE_GROUP.class})
    @Length(min = 1,max = 30,message = "表单名称长度为1-30",groups = {FormMain.INSERT_GROUP.class,FormMain.UPDATE_GROUP.class})
    private String formName;

    /**
     * @COLUMN_EXPLAIN : 表单编码
     * @TABLE_COLUMN_TYPE : VARCHAR (30)
     */
    @NotNull(message = "表单编码不能为空",groups = {FormMain.UPDATE_GROUP.class,FormMain.UPDATE_GROUP.class})
    @Length(min = 1,max = 50,message = "表单编码长度为1-50",groups = {FormMain.INSERT_GROUP.class,FormMain.UPDATE_GROUP.class})
    private String formCode;

    /**
     * @COLUMN_EXPLAIN : 存储对象主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String objectOid;

    /**
     * @COLUMN_EXPLAIN : 存储对象名称
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String objectName;

    /**
     * @COLUMN_EXPLAIN : 表单类型主键
     */
    private String dictTypeOid;

    /**
     * 所属模块
     */
    @NotNull(message = "所属模块不能为空",groups = {INSERT_GROUP.class})
    private String moduleOid;

    /**
     * 所属模块名称
     */
    private String moduleName;

    /**
     * 表单状态 {'0':'未设计','1':'草稿','2':'发布','3':'变更'},
     */
    private Integer formStatus;

    /**
     * 数据库存储类型  0本地  1API  2数据库
     */
    private Integer saveDataType;

    /**
     * api接口地址
     */
    private String apiUrl;


    /**
     * @COLUMN_EXPLAIN : 版本
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private Integer version;

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
     * @COLUMN_EXPLAIN : 设计业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String designOid;

}
