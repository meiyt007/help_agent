package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaMessage {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.TITLE
     *
     * @mbg.generated
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.CONTENT
     *
     * @mbg.generated
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.URL
     *
     * @mbg.generated
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.SEND_USER_ID
     *
     * @mbg.generated
     */
    private Long sendUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.SEND_TIME
     *
     * @mbg.generated
     */
    private Date sendTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.RECEIVE_USER_ID
     *
     * @mbg.generated
     */
    private Long receiveUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.READ_STATUS
     *
     * @mbg.generated
     */
    private String readStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.READ_TIME
     *
     * @mbg.generated
     */
    private Date readTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.DELETE_STATUS
     *
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_message.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.ID
     *
     * @return the value of t_ha_message.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.ID
     *
     * @param id the value for t_ha_message.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.TITLE
     *
     * @return the value of t_ha_message.TITLE
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.TITLE
     *
     * @param title the value for t_ha_message.TITLE
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.CONTENT
     *
     * @return the value of t_ha_message.CONTENT
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.CONTENT
     *
     * @param content the value for t_ha_message.CONTENT
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.URL
     *
     * @return the value of t_ha_message.URL
     *
     * @mbg.generated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.URL
     *
     * @param url the value for t_ha_message.URL
     *
     * @mbg.generated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.SEND_USER_ID
     *
     * @return the value of t_ha_message.SEND_USER_ID
     *
     * @mbg.generated
     */
    public Long getSendUserId() {
        return sendUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.SEND_USER_ID
     *
     * @param sendUserId the value for t_ha_message.SEND_USER_ID
     *
     * @mbg.generated
     */
    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.SEND_TIME
     *
     * @return the value of t_ha_message.SEND_TIME
     *
     * @mbg.generated
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.SEND_TIME
     *
     * @param sendTime the value for t_ha_message.SEND_TIME
     *
     * @mbg.generated
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.RECEIVE_USER_ID
     *
     * @return the value of t_ha_message.RECEIVE_USER_ID
     *
     * @mbg.generated
     */
    public Long getReceiveUserId() {
        return receiveUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.RECEIVE_USER_ID
     *
     * @param receiveUserId the value for t_ha_message.RECEIVE_USER_ID
     *
     * @mbg.generated
     */
    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.READ_STATUS
     *
     * @return the value of t_ha_message.READ_STATUS
     *
     * @mbg.generated
     */
    public String getReadStatus() {
        return readStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.READ_STATUS
     *
     * @param readStatus the value for t_ha_message.READ_STATUS
     *
     * @mbg.generated
     */
    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus == null ? null : readStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.READ_TIME
     *
     * @return the value of t_ha_message.READ_TIME
     *
     * @mbg.generated
     */
    public Date getReadTime() {
        return readTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.READ_TIME
     *
     * @param readTime the value for t_ha_message.READ_TIME
     *
     * @mbg.generated
     */
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.DELETE_STATUS
     *
     * @return the value of t_ha_message.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_message.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.CREATE_BY
     *
     * @return the value of t_ha_message.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.CREATE_BY
     *
     * @param createBy the value for t_ha_message.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.CREATE_DATE
     *
     * @return the value of t_ha_message.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.CREATE_DATE
     *
     * @param createDate the value for t_ha_message.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.UPDATE_BY
     *
     * @return the value of t_ha_message.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.UPDATE_BY
     *
     * @param updateBy the value for t_ha_message.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_message.UPDATE_DATE
     *
     * @return the value of t_ha_message.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_message.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_message.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}