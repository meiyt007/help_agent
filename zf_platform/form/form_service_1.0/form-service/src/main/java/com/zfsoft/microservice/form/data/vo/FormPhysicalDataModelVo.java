package com.zfsoft.microservice.form.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 表单设计主表表  针对 综窗 表单物理数据模型 的回传信息
 * @author: zje
 **/
@Data
@ToString
public class FormPhysicalDataModelVo {

    /**
     * @COLUMN_EXPLAIN : 主键
     * @TABLE_COLUMN_TYPE : VARCHAR (64)
     */
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String formMainOid;

    /**
     *  授权KEY
     */
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 表单名称
     * @TABLE_COLUMN_TYPE : VARCHAR (100)
     */
    private String formName;

    /**
     * @COLUMN_EXPLAIN : 表单编码
     * @TABLE_COLUMN_TYPE : VARCHAR (30)
     */
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


    /**
     * 接入系统关联存储对象的标识码(暂为综窗所设置)，
     * 保存后返回存储对象Oid和标识码的对应关系如："relationObjectBusinessId":"业务1Oid:储存对象1Oid,业务2Oid:储存对象2Oid"
     */
    private String relationObjectBusinessId;

}
