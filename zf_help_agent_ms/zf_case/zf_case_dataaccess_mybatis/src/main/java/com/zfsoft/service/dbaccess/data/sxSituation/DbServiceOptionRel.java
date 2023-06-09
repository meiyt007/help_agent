package com.zfsoft.service.dbaccess.data.sxSituation;

import java.util.Date;

public class DbServiceOptionRel {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.OID
     *
     * @mbggenerated
     */
    private String oid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.SERVICE_OID
     *
     * @mbggenerated
     */
    private String serviceOid;

    /**
     * 选项标题oid集合
     */
    private String titleOids;

    /**
     * 选项值oid集合
     */
    private String valueOids;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_service_option_rel.DELETE_STATUS
     *
     * @mbggenerated
     */
    private Short deleteStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.OID
     *
     * @return the value of t_service_option_rel.OID
     *
     * @mbggenerated
     */
    public String getOid() {
        return oid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.OID
     *
     * @param oid the value for t_service_option_rel.OID
     *
     * @mbggenerated
     */
    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.ID
     *
     * @return the value of t_service_option_rel.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.ID
     *
     * @param id the value for t_service_option_rel.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.SERVICE_OID
     *
     * @return the value of t_service_option_rel.SERVICE_OID
     *
     * @mbggenerated
     */
    public String getServiceOid() {
        return serviceOid;
    }


    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.SERVICE_OID
     *
     * @param serviceOid the value for t_service_option_rel.SERVICE_OID
     *
     * @mbggenerated
     */
    public void setServiceOid(String serviceOid) {
        this.serviceOid = serviceOid == null ? null : serviceOid.trim();
    }

    public String getTitleOids() {
        return titleOids;
    }

    public void setTitleOids(String titleOids) {
        this.titleOids = titleOids == null ? null : titleOids.trim();
    }

    public String getValueOids() {
        return valueOids;
    }

    public void setValueOids(String valueOids) {
        this.valueOids = valueOids == null ? null : valueOids.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.MODIFY_DATE
     *
     * @return the value of t_service_option_rel.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.MODIFY_DATE
     *
     * @param modifyDate the value for t_service_option_rel.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_service_option_rel.DELETE_STATUS
     *
     * @return the value of t_service_option_rel.DELETE_STATUS
     *
     * @mbggenerated
     */
    public Short getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_service_option_rel.DELETE_STATUS
     *
     * @param deleteStatus the value for t_service_option_rel.DELETE_STATUS
     *
     * @mbggenerated
     */
    public void setDeleteStatus(Short deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}