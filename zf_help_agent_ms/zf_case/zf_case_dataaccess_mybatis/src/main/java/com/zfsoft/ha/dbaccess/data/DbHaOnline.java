package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaOnline {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.WORK_USER_ID
     *
     * @mbg.generated
     */
    private Long workUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.LOGIN_TIME
     *
     * @mbg.generated
     */
    private Date loginTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.LOGOUT_TIME
     *
     * @mbg.generated
     */
    private Date logoutTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.ONLINE_TIME
     *
     * @mbg.generated
     */
    private Long onlineTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.LOGOUT_TYPE
     *
     * @mbg.generated
     */
    private String logoutType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.LOGIN_TOKEN
     *
     * @mbg.generated
     */
    private String loginToken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.LOGIN_IP
     *
     * @mbg.generated
     */
    private String loginIp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_user_online.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     *  登录类型
     */
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.ID
     *
     * @return the value of t_ha_user_online.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.ID
     *
     * @param id the value for t_ha_user_online.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.WORK_USER_ID
     *
     * @return the value of t_ha_user_online.WORK_USER_ID
     *
     * @mbg.generated
     */
    public Long getWorkUserId() {
        return workUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.WORK_USER_ID
     *
     * @param workUserId the value for t_ha_user_online.WORK_USER_ID
     *
     * @mbg.generated
     */
    public void setWorkUserId(Long workUserId) {
        this.workUserId = workUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.LOGIN_TIME
     *
     * @return the value of t_ha_user_online.LOGIN_TIME
     *
     * @mbg.generated
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.LOGIN_TIME
     *
     * @param loginTime the value for t_ha_user_online.LOGIN_TIME
     *
     * @mbg.generated
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.LOGOUT_TIME
     *
     * @return the value of t_ha_user_online.LOGOUT_TIME
     *
     * @mbg.generated
     */
    public Date getLogoutTime() {
        return logoutTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.LOGOUT_TIME
     *
     * @param logoutTime the value for t_ha_user_online.LOGOUT_TIME
     *
     * @mbg.generated
     */
    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.ONLINE_TIME
     *
     * @return the value of t_ha_user_online.ONLINE_TIME
     *
     * @mbg.generated
     */
    public Long getOnlineTime() {
        return onlineTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.ONLINE_TIME
     *
     * @param onlineTime the value for t_ha_user_online.ONLINE_TIME
     *
     * @mbg.generated
     */
    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.LOGOUT_TYPE
     *
     * @return the value of t_ha_user_online.LOGOUT_TYPE
     *
     * @mbg.generated
     */
    public String getLogoutType() {
        return logoutType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.LOGOUT_TYPE
     *
     * @param logoutType the value for t_ha_user_online.LOGOUT_TYPE
     *
     * @mbg.generated
     */
    public void setLogoutType(String logoutType) {
        this.logoutType = logoutType == null ? null : logoutType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.LOGIN_TOKEN
     *
     * @return the value of t_ha_user_online.LOGIN_TOKEN
     *
     * @mbg.generated
     */
    public String getLoginToken() {
        return loginToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.LOGIN_TOKEN
     *
     * @param loginToken the value for t_ha_user_online.LOGIN_TOKEN
     *
     * @mbg.generated
     */
    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken == null ? null : loginToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.LOGIN_IP
     *
     * @return the value of t_ha_user_online.LOGIN_IP
     *
     * @mbg.generated
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.LOGIN_IP
     *
     * @param loginIp the value for t_ha_user_online.LOGIN_IP
     *
     * @mbg.generated
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.CREATE_BY
     *
     * @return the value of t_ha_user_online.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.CREATE_BY
     *
     * @param createBy the value for t_ha_user_online.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.CREATE_DATE
     *
     * @return the value of t_ha_user_online.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.CREATE_DATE
     *
     * @param createDate the value for t_ha_user_online.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.UPDATE_BY
     *
     * @return the value of t_ha_user_online.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.UPDATE_BY
     *
     * @param updateBy the value for t_ha_user_online.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_user_online.UPDATE_DATE
     *
     * @return the value of t_ha_user_online.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_user_online.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_user_online.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
