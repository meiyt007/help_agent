package com.zfsoft.microservice.form.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description: 表单设计主表表
 * @author: wuxx
 * @Date: 2021/4/16 10:26
 **/
@Data
@ToString
public class FormReport {

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
    @NotNull(message = "id不能为空",groups = {FormReport.UPDATE_GROUP.class})
    private Long id;

    /**
     * @COLUMN_EXPLAIN : 业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "业务主键不能为空",groups = {FormReport.UPDATE_GROUP.class})
    private String reportOid;

    /**
     * @COLUMN_EXPLAIN : 表单
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    @NotNull(message = "表单不能为空",groups = {FormReport.UPDATE_GROUP.class})
    private String formMainOid;

    /**
     *  授权KEY
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormReport.INSERT_GROUP.class, FormReport.UPDATE_GROUP.class})
    private String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 设计表业务主键
     * @TABLE_COLUMN_TYPE : VARCHAR (32)
     */
    private String designOid;

    /**
     * @COLUMN_EXPLAIN : 与第三方关联的业务主键
     */
    private String businessKey;

    /**
     * 表单设计的数据
     */
    private String formData;

    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

    /**
     *  所属表单名称
     */
    private String formName;

    /**
     *  所属表单版本
     */
    private Integer version;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
}
