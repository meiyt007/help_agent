package com.zfsoft.service.dbaccess.data.sxSituation;

import java.util.Date;

public class DbSxServiceSituationOption {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.SITUATION_OID
     *
     * @mbggenerated
     */
    private String situationOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.OPTION_OID
     *
     * @mbggenerated
     */
    private String optionOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sx_service_situation_option.DELETE_STATUS
     *
     * @mbggenerated
     */
    private Short deleteStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.ID
     *
     * @return the value of t_sx_service_situation_option.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.ID
     *
     * @param id the value for t_sx_service_situation_option.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.SITUATION_OID
     *
     * @return the value of t_sx_service_situation_option.SITUATION_OID
     *
     * @mbggenerated
     */
    public String getSituationOid() {
        return situationOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.SITUATION_OID
     *
     * @param situationOid the value for t_sx_service_situation_option.SITUATION_OID
     *
     * @mbggenerated
     */
    public void setSituationOid(String situationOid) {
        this.situationOid = situationOid == null ? null : situationOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.OPTION_OID
     *
     * @return the value of t_sx_service_situation_option.OPTION_OID
     *
     * @mbggenerated
     */
    public String getOptionOid() {
        return optionOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.OPTION_OID
     *
     * @param optionOid the value for t_sx_service_situation_option.OPTION_OID
     *
     * @mbggenerated
     */
    public void setOptionOid(String optionOid) {
        this.optionOid = optionOid == null ? null : optionOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.CREATE_DATE
     *
     * @return the value of t_sx_service_situation_option.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.CREATE_DATE
     *
     * @param createDate the value for t_sx_service_situation_option.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.MODIFY_DATE
     *
     * @return the value of t_sx_service_situation_option.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.MODIFY_DATE
     *
     * @param modifyDate the value for t_sx_service_situation_option.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sx_service_situation_option.DELETE_STATUS
     *
     * @return the value of t_sx_service_situation_option.DELETE_STATUS
     *
     * @mbggenerated
     */
    public Short getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sx_service_situation_option.DELETE_STATUS
     *
     * @param deleteStatus the value for t_sx_service_situation_option.DELETE_STATUS
     *
     * @mbggenerated
     */
    public void setDeleteStatus(Short deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}