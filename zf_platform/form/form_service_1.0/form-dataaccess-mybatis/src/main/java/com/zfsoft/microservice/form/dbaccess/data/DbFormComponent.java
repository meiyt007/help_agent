package com.zfsoft.microservice.form.dbaccess.data;

import java.util.Date;

public class DbFormComponent {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.COMPONENT_OID
     *
     * @mbggenerated
     */
    private String componentOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.COMPONENT_CODE
     *
     * @mbggenerated
     */
    private String componentCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    private String authorizeKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_component.COMPONENT_CONFIG
     *
     * @mbggenerated
     */
    private String componentConfig;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.ID
     *
     * @return the value of t_form_component.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.ID
     *
     * @param id the value for t_form_component.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.COMPONENT_OID
     *
     * @return the value of t_form_component.COMPONENT_OID
     *
     * @mbggenerated
     */
    public String getComponentOid() {
        return componentOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.COMPONENT_OID
     *
     * @param componentOid the value for t_form_component.COMPONENT_OID
     *
     * @mbggenerated
     */
    public void setComponentOid(String componentOid) {
        this.componentOid = componentOid == null ? null : componentOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.COMPONENT_CODE
     *
     * @return the value of t_form_component.COMPONENT_CODE
     *
     * @mbggenerated
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.COMPONENT_CODE
     *
     * @param componentCode the value for t_form_component.COMPONENT_CODE
     *
     * @mbggenerated
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode == null ? null : componentCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.AUTHORIZE_KEY
     *
     * @return the value of t_form_component.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    public String getAuthorizeKey() {
        return authorizeKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.AUTHORIZE_KEY
     *
     * @param authorizeKey the value for t_form_component.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    public void setAuthorizeKey(String authorizeKey) {
        this.authorizeKey = authorizeKey == null ? null : authorizeKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.CREATE_DATE
     *
     * @return the value of t_form_component.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.CREATE_DATE
     *
     * @param createDate the value for t_form_component.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_component.COMPONENT_CONFIG
     *
     * @return the value of t_form_component.COMPONENT_CONFIG
     *
     * @mbggenerated
     */
    public String getComponentConfig() {
        return componentConfig;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_component.COMPONENT_CONFIG
     *
     * @param componentConfig the value for t_form_component.COMPONENT_CONFIG
     *
     * @mbggenerated
     */
    public void setComponentConfig(String componentConfig) {
        this.componentConfig = componentConfig == null ? null : componentConfig.trim();
    }
}
