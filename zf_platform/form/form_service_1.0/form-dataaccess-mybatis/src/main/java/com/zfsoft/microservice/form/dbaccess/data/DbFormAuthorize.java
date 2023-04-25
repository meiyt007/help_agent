package com.zfsoft.microservice.form.dbaccess.data;

import java.util.Date;

public class DbFormAuthorize {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    private String authorizeKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.SYSTEM_NAME
     *
     * @mbggenerated
     */
    private String systemName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.TIME_TYPE
     *
     * @mbggenerated
     */
    private Integer timeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.START_TIME
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.END_TIME
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.REMARK
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.IS_ABLE
     *
     * @mbggenerated
     */
    private Integer isAble;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_form_authorize.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.ID
     *
     * @return the value of t_form_authorize.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.ID
     *
     * @param id the value for t_form_authorize.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.AUTHORIZE_KEY
     *
     * @return the value of t_form_authorize.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    public String getAuthorizeKey() {
        return authorizeKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.AUTHORIZE_KEY
     *
     * @param authorizeKey the value for t_form_authorize.AUTHORIZE_KEY
     *
     * @mbggenerated
     */
    public void setAuthorizeKey(String authorizeKey) {
        this.authorizeKey = authorizeKey == null ? null : authorizeKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.SYSTEM_NAME
     *
     * @return the value of t_form_authorize.SYSTEM_NAME
     *
     * @mbggenerated
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.SYSTEM_NAME
     *
     * @param systemName the value for t_form_authorize.SYSTEM_NAME
     *
     * @mbggenerated
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.TIME_TYPE
     *
     * @return the value of t_form_authorize.TIME_TYPE
     *
     * @mbggenerated
     */
    public Integer getTimeType() {
        return timeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.TIME_TYPE
     *
     * @param timeType the value for t_form_authorize.TIME_TYPE
     *
     * @mbggenerated
     */
    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.START_TIME
     *
     * @return the value of t_form_authorize.START_TIME
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.START_TIME
     *
     * @param startTime the value for t_form_authorize.START_TIME
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.END_TIME
     *
     * @return the value of t_form_authorize.END_TIME
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.END_TIME
     *
     * @param endTime the value for t_form_authorize.END_TIME
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.REMARK
     *
     * @return the value of t_form_authorize.REMARK
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.REMARK
     *
     * @param remark the value for t_form_authorize.REMARK
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.IS_ABLE
     *
     * @return the value of t_form_authorize.IS_ABLE
     *
     * @mbggenerated
     */
    public Integer getIsAble() {
        return isAble;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.IS_ABLE
     *
     * @param isAble the value for t_form_authorize.IS_ABLE
     *
     * @mbggenerated
     */
    public void setIsAble(Integer isAble) {
        this.isAble = isAble;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.IS_DELETE
     *
     * @return the value of t_form_authorize.IS_DELETE
     *
     * @mbggenerated
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.IS_DELETE
     *
     * @param isDelete the value for t_form_authorize.IS_DELETE
     *
     * @mbggenerated
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.CREATE_DATE
     *
     * @return the value of t_form_authorize.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.CREATE_DATE
     *
     * @param createDate the value for t_form_authorize.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_form_authorize.MODIFY_DATE
     *
     * @return the value of t_form_authorize.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_form_authorize.MODIFY_DATE
     *
     * @param modifyDate the value for t_form_authorize.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}