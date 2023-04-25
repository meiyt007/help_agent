package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 存储对象扩展
 */
@Data
@ToString
public class FormObjectExtand {

    /**
     * 新增信息验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新信息验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /**
     * 逻辑主键
     */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private String extandOid;


    /**
     * 主存储对象
     */
    @NotNull(message = "主存储对象不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String mainObjectOid;


    /**
     * 副存储对象
     */
    private String secondaryObjectOid;

    /**
     * 变量名
     */
    private String variableName;

    /**
     * 外键
     */
    private String foreignKey;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String demo;

    /**
     * 删除状态
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 对象表结构的属性集合
     */
    private List<FormColumn> columnList;

}
