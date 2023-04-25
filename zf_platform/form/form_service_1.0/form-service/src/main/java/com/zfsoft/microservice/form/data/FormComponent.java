package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @ClassName FormComponent
 * @Description: 电子表单组件配置表
 * @Author wuxx
 * @Date 2021/4/12
 **/
@Data
@ToString
public class FormComponent {

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
    @NotNull(message = "id不能为空",groups = {FormComponent.UPDATE_GROUP.class})
    private java.lang.Long id;

    /**
     * @COLUMN_EXPLAIN : 授权authorizeKey
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormComponent.UPDATE_GROUP.class})
    private java.lang.String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormComponent.UPDATE_GROUP.class})
    private java.lang.String componentOid;

    /**
     * @COLUMN_EXPLAIN : 组件类型
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    @NotNull(message = "组件类型不能为空",groups = {FormComponent.INSERT_GROUP.class})
    private String componentCode;

    /**
     * @COLUMN_EXPLAIN : 配置
     * @TABLE_COLUMN_TYPE : VARCHAR (2000)
     */
    private String componentConfig;

    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

}
