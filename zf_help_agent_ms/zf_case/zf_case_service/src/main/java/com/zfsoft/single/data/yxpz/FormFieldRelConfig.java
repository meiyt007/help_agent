package com.zfsoft.single.data.yxpz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class FormFieldRelConfig {

    /**
     * 新增模板，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新模板，验证规则组
     */
    public interface UPDATE_GROUP{};
    private Long id;

    private String oid;

    @NotNull(message = "事项主键不能为空")
    private String serviceOid;

    /**
   OCR识别字段
     */
    private String ocrFieldOid;

    /**
     电子表单字段
     */
    private String elecLiecenseFieldOid;

    /**
     基础表单主键
     */
    private String basicFormFieldOid;

    @NotNull(message = "表单字段不能为空")
    private String fillFieldOid;

    /**
     * /0填充基础表单  1 填充电子表单
     */

    private Integer fillType;

    private Integer delFlag;

    private Date createDate;

    private Date modifyDate;

    private Integer fillSource;

    private String tempFieldOid;

    private String fillFieldName;
    private String temFieldName;

    /** 预检规则OID */
    private String interApiId;
    private String interApiText;

    /** 预检规则返回值 */
    private String interApiValId;
    private String interApiValText;

    /** 电子证照类型 */
    private String licenceType;

}
