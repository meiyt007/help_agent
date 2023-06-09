package com.zfsoft.ha.data.vo;

import java.util.Date;

public class HaVideoChatRecordVo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.ROOM_OID
     *
     * @mbg.generated
     */
    private Long roomOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    private Long videoNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.USER_NAME
     *
     * @mbg.generated
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.ACCESS_OID
     *
     * @mbg.generated
     */
    private Long accessOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.CONTENT_TYPE
     *
     * @mbg.generated
     */
    private String contentType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.FILE_TYPE
     *
     * @mbg.generated
     */
    private String fileType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.SURE_TYPE
     *
     * @mbg.generated
     */
    private String sureType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.SEND_CONTENT
     *
     * @mbg.generated
     */
    private String sendContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_chat_record.SEND_DATE
     *
     * @mbg.generated
     */
    private Date sendDate;

    /**
     * 确认状态  信息状态 0-没问题 1-有问题 存到redis中，key为 checkCode:id 且只有在内容类型为4的时候，该字段有值
     */
    private String checkCode;


    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.ID
     *
     * @return the value of t_ha_video_chat_record.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.ID
     *
     * @param id the value for t_ha_video_chat_record.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.ROOM_OID
     *
     * @return the value of t_ha_video_chat_record.ROOM_OID
     *
     * @mbg.generated
     */
    public Long getRoomOid() {
        return roomOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.ROOM_OID
     *
     * @param roomOid the value for t_ha_video_chat_record.ROOM_OID
     *
     * @mbg.generated
     */
    public void setRoomOid(Long roomOid) {
        this.roomOid = roomOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.VIDEO_NUM
     *
     * @return the value of t_ha_video_chat_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    public Long getVideoNum() {
        return videoNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.VIDEO_NUM
     *
     * @param videoNum the value for t_ha_video_chat_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    public void setVideoNum(Long videoNum) {
        this.videoNum = videoNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.USER_NAME
     *
     * @return the value of t_ha_video_chat_record.USER_NAME
     *
     * @mbg.generated
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.USER_NAME
     *
     * @param userName the value for t_ha_video_chat_record.USER_NAME
     *
     * @mbg.generated
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.ACCESS_OID
     *
     * @return the value of t_ha_video_chat_record.ACCESS_OID
     *
     * @mbg.generated
     */
    public Long getAccessOid() {
        return accessOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.ACCESS_OID
     *
     * @param accessOid the value for t_ha_video_chat_record.ACCESS_OID
     *
     * @mbg.generated
     */
    public void setAccessOid(Long accessOid) {
        this.accessOid = accessOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.CONTENT_TYPE
     *
     * @return the value of t_ha_video_chat_record.CONTENT_TYPE
     *
     * @mbg.generated
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.CONTENT_TYPE
     *
     * @param contentType the value for t_ha_video_chat_record.CONTENT_TYPE
     *
     * @mbg.generated
     */
    public void setContentType(String contentType) {
        this.contentType = contentType == null ? null : contentType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.FILE_TYPE
     *
     * @return the value of t_ha_video_chat_record.FILE_TYPE
     *
     * @mbg.generated
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.FILE_TYPE
     *
     * @param fileType the value for t_ha_video_chat_record.FILE_TYPE
     *
     * @mbg.generated
     */
    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.SURE_TYPE
     *
     * @return the value of t_ha_video_chat_record.SURE_TYPE
     *
     * @mbg.generated
     */
    public String getSureType() {
        return sureType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.SURE_TYPE
     *
     * @param sureType the value for t_ha_video_chat_record.SURE_TYPE
     *
     * @mbg.generated
     */
    public void setSureType(String sureType) {
        this.sureType = sureType == null ? null : sureType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.SEND_CONTENT
     *
     * @return the value of t_ha_video_chat_record.SEND_CONTENT
     *
     * @mbg.generated
     */
    public String getSendContent() {
        return sendContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.SEND_CONTENT
     *
     * @param sendContent the value for t_ha_video_chat_record.SEND_CONTENT
     *
     * @mbg.generated
     */
    public void setSendContent(String sendContent) {
        this.sendContent = sendContent == null ? null : sendContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_chat_record.SEND_DATE
     *
     * @return the value of t_ha_video_chat_record.SEND_DATE
     *
     * @mbg.generated
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_chat_record.SEND_DATE
     *
     * @param sendDate the value for t_ha_video_chat_record.SEND_DATE
     *
     * @mbg.generated
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

}
