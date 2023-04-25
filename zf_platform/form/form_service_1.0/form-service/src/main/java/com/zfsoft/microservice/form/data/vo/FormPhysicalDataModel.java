package com.zfsoft.microservice.form.data.vo;

import com.zfsoft.microservice.form.data.FormColumn;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.microservice.form.data.FormObjectExtand;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 表单物理数据模型
 */
@Data
@ToString
public class FormPhysicalDataModel {


    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String objectOid;

    /**
     *  授权KEY
     */
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 存储数据库主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String datasourceOid;

    /**
     * @COLUMN_EXPLAIN : 数据源对象名称
     */
    private String datasourceName;

    /**
     * 所属模块
     */
    private String moduleOid;

    /**
     * @COLUMN_EXPLAIN : 对象名称
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String objectName;

    /**
     * @COLUMN_EXPLAIN : 对象编码
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String objectCode;

    /**
     * @COLUMN_EXPLAIN : 对象表名
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String objectForm;

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
     * @description: 对象表结构的属性集合
     * @author: wuxx
     * @Date: 2021/4/14 17:02
     **/
    private List<FormColumn> columnList;

    /**
     * 扩展对象集合
     */
    private List<FormObjectExtand> extandList;

    /**
     * 对象扩展信息
     */
    private FormObjectExtand   formObjectExtand;

    /**
     * 存储类型 {'0':'存储对象','1':'逻辑对象'},
     */
    private String saveType;

    /**
     * 是否保存
     */
    private Integer isSave;

    /**
     * @COLUMN_EXPLAIN : 表单编码
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private String formCode;


    /**
     * 是否是新建 默认为0新建   1修改
     */
    private Integer isCreate;

    /**
     * 主键是否varchar类型  ——目前只针对综窗的mysql  1是  其他不是
     */
    private String idIsVarchar;

    /**
     * 接入系统关联存储对象的标识码(暂为综窗所设置)，
     * 保存后返回存储对象Oid和标识码的对应关系如："relationObjectBusinessId":"业务1Oid:储存对象1Oid,业务2Oid:储存对象2Oid"
     */
    private String relationObjectBusinessId;

    private List<FormPhysicalDataModel> children;

    /**
     * 是否数据库字段名(传过去的)和表单字段名 是否不用转换  1不用转换  其他需要转换  默认需要转换
     */
    private Integer isNotChangeFiledName;
}
