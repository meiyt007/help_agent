package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description: 表单设计详细版本表
 * @author: wuxx
 * @Date: 2021/4/16 10:26
 **/
@Data
@ToString
public class FormDesign {

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
    @NotNull(message = "id不能为空",groups = {FormDesign.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormDesign.UPDATE_GROUP.class})
    private String designOid;

    /**
     *  所属表单主键
     */
    @NotNull(message = "所属表单不能为空",groups = {FormDesign.INSERT_GROUP.class, FormDesign.UPDATE_GROUP.class})
    private String formMainOid;

    /**
     * @COLUMN_EXPLAIN : 存储对象主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String objectOid;

    /**
     *  所属表单名称
     */
    private String designName;

    /**
     * 所属模块
     */
    @NotNull(message = "所属模块不能为空",groups = {INSERT_GROUP.class})
    private String moduleOid;

    /**
     *  表单配置
     */
    private String formConfig;

    /**
     * @COLUMN_EXPLAIN : 版本
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private Integer version;

    /**
     * 发布状态 {'0':'未设计','1':'草稿','2':'发布','3':'变更'},
     */
    private Integer releaseStatus;

    /**
     * 数据库存储类型  0本地  1API  2数据库
     */
    private Integer saveDataType;

    /**
     * api接口地址
     */
    private String apiUrl;
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
     *  模板主键
     */
    private String formTemplateOid;

    /**
     * 来源 0设计，1模板，2导入
     */
    private Integer source;

    /**
     *  是否发布标识 true是
     */
    private Boolean isPublish;
}
