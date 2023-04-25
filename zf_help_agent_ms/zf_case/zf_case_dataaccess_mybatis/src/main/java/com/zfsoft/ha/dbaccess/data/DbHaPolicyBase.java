package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaPolicyBase {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.TITLE
     *
     * @mbg.generated
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.NAME
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.DISTRICT_OID
     *
     * @mbg.generated
     */
    private String organId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.POLICY_LINK
     *
     * @mbg.generated
     */
    private String policyLink;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.SERVICE_NAME
     *
     * @mbg.generated
     */
    private String serviceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.DECLARE_LINK
     *
     * @mbg.generated
     */
    private String declareLink;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.EXPERIENCE_NAME
     *
     * @mbg.generated
     */
    private String experienceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.EXPERIENCE_LINK
     *
     * @mbg.generated
     */
    private String experienceLink;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.SORT
     *
     * @mbg.generated
     */
    private String sort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.DELETE_STATUS
     *
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_policy_base.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.ID
     *
     * @return the value of t_ha_policy_base.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.ID
     *
     * @param id the value for t_ha_policy_base.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.TITLE
     *
     * @return the value of t_ha_policy_base.TITLE
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.TITLE
     *
     * @param title the value for t_ha_policy_base.TITLE
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.NAME
     *
     * @return the value of t_ha_policy_base.NAME
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.NAME
     *
     * @param name the value for t_ha_policy_base.NAME
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.DISTRICT_OID
     *
     * @return the value of t_ha_policy_base.DISTRICT_OID
     *
     * @mbg.generated
     */
    public String getOrganId() {
        return organId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.DISTRICT_OID
     *
     * @param organId the value for t_ha_policy_base.DISTRICT_OID
     *
     * @mbg.generated
     */
    public void setOrganId(String organId) {
        this.organId = organId == null ? null : organId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.POLICY_LINK
     *
     * @return the value of t_ha_policy_base.POLICY_LINK
     *
     * @mbg.generated
     */
    public String getPolicyLink() {
        return policyLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.POLICY_LINK
     *
     * @param policyLink the value for t_ha_policy_base.POLICY_LINK
     *
     * @mbg.generated
     */
    public void setPolicyLink(String policyLink) {
        this.policyLink = policyLink == null ? null : policyLink.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.SERVICE_NAME
     *
     * @return the value of t_ha_policy_base.SERVICE_NAME
     *
     * @mbg.generated
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.SERVICE_NAME
     *
     * @param serviceName the value for t_ha_policy_base.SERVICE_NAME
     *
     * @mbg.generated
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.DECLARE_LINK
     *
     * @return the value of t_ha_policy_base.DECLARE_LINK
     *
     * @mbg.generated
     */
    public String getDeclareLink() {
        return declareLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.DECLARE_LINK
     *
     * @param declareLink the value for t_ha_policy_base.DECLARE_LINK
     *
     * @mbg.generated
     */
    public void setDeclareLink(String declareLink) {
        this.declareLink = declareLink == null ? null : declareLink.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.EXPERIENCE_NAME
     *
     * @return the value of t_ha_policy_base.EXPERIENCE_NAME
     *
     * @mbg.generated
     */
    public String getExperienceName() {
        return experienceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.EXPERIENCE_NAME
     *
     * @param experienceName the value for t_ha_policy_base.EXPERIENCE_NAME
     *
     * @mbg.generated
     */
    public void setExperienceName(String experienceName) {
        this.experienceName = experienceName == null ? null : experienceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.EXPERIENCE_LINK
     *
     * @return the value of t_ha_policy_base.EXPERIENCE_LINK
     *
     * @mbg.generated
     */
    public String getExperienceLink() {
        return experienceLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.EXPERIENCE_LINK
     *
     * @param experienceLink the value for t_ha_policy_base.EXPERIENCE_LINK
     *
     * @mbg.generated
     */
    public void setExperienceLink(String experienceLink) {
        this.experienceLink = experienceLink == null ? null : experienceLink.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.SORT
     *
     * @return the value of t_ha_policy_base.SORT
     *
     * @mbg.generated
     */
    public String getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.SORT
     *
     * @param sort the value for t_ha_policy_base.SORT
     *
     * @mbg.generated
     */
    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.DELETE_STATUS
     *
     * @return the value of t_ha_policy_base.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_policy_base.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.CREATE_BY
     *
     * @return the value of t_ha_policy_base.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.CREATE_BY
     *
     * @param createBy the value for t_ha_policy_base.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.CREATE_DATE
     *
     * @return the value of t_ha_policy_base.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.CREATE_DATE
     *
     * @param createDate the value for t_ha_policy_base.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.UPDATE_BY
     *
     * @return the value of t_ha_policy_base.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.UPDATE_BY
     *
     * @param updateBy the value for t_ha_policy_base.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_policy_base.UPDATE_DATE
     *
     * @return the value of t_ha_policy_base.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_policy_base.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_policy_base.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}