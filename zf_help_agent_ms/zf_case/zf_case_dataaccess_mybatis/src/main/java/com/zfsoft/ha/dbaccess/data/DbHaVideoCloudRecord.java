package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaVideoCloudRecord {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.ROOM_OID
     *
     * @mbg.generated
     */
    private Long roomOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.ROOM_NUMBER
     *
     * @mbg.generated
     */
    private Long roomNumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    private String videoNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.TASK_ID
     *
     * @mbg.generated
     */
    private String taskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.START_DATE
     *
     * @mbg.generated
     */
    private Date startDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.STOP_DATE
     *
     * @mbg.generated
     */
    private Date stopDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.DOWNLOAD_STATUS
     *
     * @mbg.generated
     */
    private Integer downloadStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    private String downloadUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_video_cloud_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.ID
     *
     * @return the value of t_ha_video_cloud_record.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.ID
     *
     * @param id the value for t_ha_video_cloud_record.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.ROOM_OID
     *
     * @return the value of t_ha_video_cloud_record.ROOM_OID
     *
     * @mbg.generated
     */
    public Long getRoomOid() {
        return roomOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.ROOM_OID
     *
     * @param roomOid the value for t_ha_video_cloud_record.ROOM_OID
     *
     * @mbg.generated
     */
    public void setRoomOid(Long roomOid) {
        this.roomOid = roomOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.ROOM_NUMBER
     *
     * @return the value of t_ha_video_cloud_record.ROOM_NUMBER
     *
     * @mbg.generated
     */
    public Long getRoomNumber() {
        return roomNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.ROOM_NUMBER
     *
     * @param roomNumber the value for t_ha_video_cloud_record.ROOM_NUMBER
     *
     * @mbg.generated
     */
    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.VIDEO_NUM
     *
     * @return the value of t_ha_video_cloud_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    public String getVideoNum() {
        return videoNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.VIDEO_NUM
     *
     * @param videoNum the value for t_ha_video_cloud_record.VIDEO_NUM
     *
     * @mbg.generated
     */
    public void setVideoNum(String videoNum) {
        this.videoNum = videoNum == null ? null : videoNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.TASK_ID
     *
     * @return the value of t_ha_video_cloud_record.TASK_ID
     *
     * @mbg.generated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.TASK_ID
     *
     * @param taskId the value for t_ha_video_cloud_record.TASK_ID
     *
     * @mbg.generated
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.START_DATE
     *
     * @return the value of t_ha_video_cloud_record.START_DATE
     *
     * @mbg.generated
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.START_DATE
     *
     * @param startDate the value for t_ha_video_cloud_record.START_DATE
     *
     * @mbg.generated
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.STOP_DATE
     *
     * @return the value of t_ha_video_cloud_record.STOP_DATE
     *
     * @mbg.generated
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.STOP_DATE
     *
     * @param stopDate the value for t_ha_video_cloud_record.STOP_DATE
     *
     * @mbg.generated
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.DOWNLOAD_STATUS
     *
     * @return the value of t_ha_video_cloud_record.DOWNLOAD_STATUS
     *
     * @mbg.generated
     */
    public Integer getDownloadStatus() {
        return downloadStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.DOWNLOAD_STATUS
     *
     * @param downloadStatus the value for t_ha_video_cloud_record.DOWNLOAD_STATUS
     *
     * @mbg.generated
     */
    public void setDownloadStatus(Integer downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.DOWNLOAD_URL
     *
     * @return the value of t_ha_video_cloud_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.DOWNLOAD_URL
     *
     * @param downloadUrl the value for t_ha_video_cloud_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.CREATE_BY
     *
     * @return the value of t_ha_video_cloud_record.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.CREATE_BY
     *
     * @param createBy the value for t_ha_video_cloud_record.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.CREATE_DATE
     *
     * @return the value of t_ha_video_cloud_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.CREATE_DATE
     *
     * @param createDate the value for t_ha_video_cloud_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.UPDATE_BY
     *
     * @return the value of t_ha_video_cloud_record.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.UPDATE_BY
     *
     * @param updateBy the value for t_ha_video_cloud_record.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_cloud_record.UPDATE_DATE
     *
     * @return the value of t_ha_video_cloud_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_cloud_record.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_video_cloud_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}