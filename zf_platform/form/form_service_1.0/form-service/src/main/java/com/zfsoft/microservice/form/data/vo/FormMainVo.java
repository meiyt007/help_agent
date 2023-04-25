package com.zfsoft.microservice.form.data.vo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 表单设计主表表Vo
 * @author: wuxx
 * @Date: 2021/5/16 10:26
 **/
@Data
@ToString
public class FormMainVo {

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
     * 表单状态 {'0':'未设计','1':'草稿','2':'发布','3':'变更'},
     */
    private Integer formStatus;

    /**
     * @COLUMN_EXPLAIN : 版本
     * @TABLE_COLUMN_TYPE : VARCHAR (20)
     */
    private Integer version;

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
