package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class SxDocuTemplate {
    /**
     * 新增模板，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新模板，验证规则组
     */
    public interface UPDATE_GROUP{};
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.DOCU_TEMPLATE_OID
     *
     * @mbggenerated
     */
    private String docuTemplateOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.SERVICE_OID
     *
     * @mbggenerated
     */
    private String serviceOid;

    /* 文书类型*/
    private String docuTypeOid;

//    /**
    private String docuTemplateName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.ATTA_OID
     *
     * @mbggenerated
     */
    private String attaOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.GLOBAL_TEMPLATE
     *
     * @mbggenerated
     */
    private String globalTemplate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.ENABLED_FLAG
     *
     * @mbggenerated
     */
    private Integer enabledFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.DEL_FLAG
     *
     * @mbggenerated
     */
    private Integer delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_docu_template.USER_OID
     *
     * @mbggenerated
     */
    private String userOid;


    private String applicationType;

   }