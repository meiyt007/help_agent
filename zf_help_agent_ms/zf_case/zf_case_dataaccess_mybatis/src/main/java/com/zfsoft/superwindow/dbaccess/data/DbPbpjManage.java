package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

public class DbPbpjManage {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.RUN_CODE
     *
     * @mbggenerated
     */
    private String runCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.NAME
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.IP
     *
     * @mbggenerated
     */
    private String ip;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.STATUS
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.USER_CODE
     *
     * @mbggenerated
     */
    private String userCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.USER_NAME
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.DATE_TIME
     *
     * @mbggenerated
     */
    private Date dateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.REMARK
     *
     * @mbggenerated
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pbpj_manage.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * 评价类型  0 平板评价 1 智能评价
     */
    private Integer pbpjType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.ID
     *
     * @return the value of t_pbpj_manage.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.ID
     *
     * @param id the value for t_pbpj_manage.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.RUN_CODE
     *
     * @return the value of t_pbpj_manage.RUN_CODE
     *
     * @mbggenerated
     */
    public String getRunCode() {
        return runCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.RUN_CODE
     *
     * @param runCode the value for t_pbpj_manage.RUN_CODE
     *
     * @mbggenerated
     */
    public void setRunCode(String runCode) {
        this.runCode = runCode == null ? null : runCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.NAME
     *
     * @return the value of t_pbpj_manage.NAME
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.NAME
     *
     * @param name the value for t_pbpj_manage.NAME
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.IP
     *
     * @return the value of t_pbpj_manage.IP
     *
     * @mbggenerated
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.IP
     *
     * @param ip the value for t_pbpj_manage.IP
     *
     * @mbggenerated
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.STATUS
     *
     * @return the value of t_pbpj_manage.STATUS
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.STATUS
     *
     * @param status the value for t_pbpj_manage.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.USER_CODE
     *
     * @return the value of t_pbpj_manage.USER_CODE
     *
     * @mbggenerated
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.USER_CODE
     *
     * @param userCode the value for t_pbpj_manage.USER_CODE
     *
     * @mbggenerated
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.USER_NAME
     *
     * @return the value of t_pbpj_manage.USER_NAME
     *
     * @mbggenerated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.USER_NAME
     *
     * @param userName the value for t_pbpj_manage.USER_NAME
     *
     * @mbggenerated
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.DATE_TIME
     *
     * @return the value of t_pbpj_manage.DATE_TIME
     *
     * @mbggenerated
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.DATE_TIME
     *
     * @param dateTime the value for t_pbpj_manage.DATE_TIME
     *
     * @mbggenerated
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.REMARK
     *
     * @return the value of t_pbpj_manage.REMARK
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.REMARK
     *
     * @param remark the value for t_pbpj_manage.REMARK
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pbpj_manage.IS_DELETE
     *
     * @return the value of t_pbpj_manage.IS_DELETE
     *
     * @mbggenerated
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pbpj_manage.IS_DELETE
     *
     * @param isDelete the value for t_pbpj_manage.IS_DELETE
     *
     * @mbggenerated
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getPbpjType() {
        return pbpjType;
    }

    public void setPbpjType(Integer pbpjType) {
        this.pbpjType = pbpjType;
    }
}