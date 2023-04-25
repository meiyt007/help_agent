package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 存储对象表
 * @author: wuxx
 * @Date: 2021/4/13 10:26
 **/
@Data
@ToString
public class FormObject {

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
    @NotNull(message = "id不能为空",groups = {FormObject.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormObject.UPDATE_GROUP.class})
    private String objectOid;

    /**
     *  授权KEY
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormObject.INSERT_GROUP.class, FormObject.UPDATE_GROUP.class})
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
    @NotNull(message = "所属模块不能为空",groups = {INSERT_GROUP.class})
    private String moduleOid;

    /**
     * @COLUMN_EXPLAIN : 对象名称
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    @NotNull(message = "存储对象名称不能为空",groups = {FormObject.INSERT_GROUP.class})
    @Length(min = 1,max = 50,message = "存储对象名称长度为1-50",groups = {FormObject.INSERT_GROUP.class})
    private String objectName;

    /**
     * @COLUMN_EXPLAIN : 对象编码
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    //@NotNull(message = "存储对象编码不能为空",groups = {FormObject.INSERT_GROUP.class})
    //@Length(min = 1,max = 30,message = "存储对象编码长度为1-20",groups = {FormObject.INSERT_GROUP.class})
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
}
