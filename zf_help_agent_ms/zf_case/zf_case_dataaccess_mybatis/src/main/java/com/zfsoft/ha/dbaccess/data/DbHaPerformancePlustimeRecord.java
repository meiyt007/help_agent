package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaPerformancePlustimeRecord {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.WORK_USER_OID
     *
     * @mbg.generated
     */
    private Long workUserOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.WORK_USER_NAME
     *
     * @mbg.generated
     */
    private String workUserName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.PLUS_PROJECT_OID
     *
     * @mbg.generated
     */
    private Long plusProjectOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.PLUS_DURATION
     *
     * @mbg.generated
     */
    private Integer plusDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.ATTA_OID
     *
     * @mbg.generated
     */
    private String attaOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.FASTDFS_UPLOAD_URL
     *
     * @mbg.generated
     */
    private String fastdfsUploadUrl;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.FASTDFS_NGINX_URL
     *
     * @mbg.generated
     */
    private String fastdfsNginxUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.BONUS_NOTES
     *
     * @mbg.generated
     */
    private String bonusNotes;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.AUDIT_STATUS
     *
     * @mbg.generated
     */
    private String auditStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.AUDIT_REASON
     *
     * @mbg.generated
     */
    private String auditReason;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.GROUP_LEADER_ID
     *
     * @mbg.generated
     */
    private Long groupLeaderId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.GROUP_LEADER_NAME
     *
     * @mbg.generated
     */
    private String groupLeaderName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.DELETE_STATUS
     *
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_performance_plustime_record.AUDIT_DATE
     *
     * @mbg.generated
     */
    private Date auditDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.ID
     *
     * @return the value of t_ha_performance_plustime_record.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.ID
     *
     * @param id the value for t_ha_performance_plustime_record.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.WORK_USER_OID
     *
     * @return the value of t_ha_performance_plustime_record.WORK_USER_OID
     *
     * @mbg.generated
     */
    public Long getWorkUserOid() {
        return workUserOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.WORK_USER_OID
     *
     * @param workUserOid the value for t_ha_performance_plustime_record.WORK_USER_OID
     *
     * @mbg.generated
     */
    public void setWorkUserOid(Long workUserOid) {
        this.workUserOid = workUserOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.WORK_USER_NAME
     *
     * @return the value of t_ha_performance_plustime_record.WORK_USER_NAME
     *
     * @mbg.generated
     */
    public String getWorkUserName() {
        return workUserName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.WORK_USER_NAME
     *
     * @param workUserName the value for t_ha_performance_plustime_record.WORK_USER_NAME
     *
     * @mbg.generated
     */
    public void setWorkUserName(String workUserName) {
        this.workUserName = workUserName == null ? null : workUserName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.PLUS_PROJECT_OID
     *
     * @return the value of t_ha_performance_plustime_record.PLUS_PROJECT_OID
     *
     * @mbg.generated
     */
    public Long getPlusProjectOid() {
        return plusProjectOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.PLUS_PROJECT_OID
     *
     * @param plusProjectOid the value for t_ha_performance_plustime_record.PLUS_PROJECT_OID
     *
     * @mbg.generated
     */
    public void setPlusProjectOid(Long plusProjectOid) {
        this.plusProjectOid = plusProjectOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.PLUS_DURATION
     *
     * @return the value of t_ha_performance_plustime_record.PLUS_DURATION
     *
     * @mbg.generated
     */
    public Integer getPlusDuration() {
        return plusDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.PLUS_DURATION
     *
     * @param plusDuration the value for t_ha_performance_plustime_record.PLUS_DURATION
     *
     * @mbg.generated
     */
    public void setPlusDuration(Integer plusDuration) {
        this.plusDuration = plusDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.ATTA_OID
     *
     * @return the value of t_ha_performance_plustime_record.ATTA_OID
     *
     * @mbg.generated
     */
    public String getAttaOid() {
        return attaOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.ATTA_OID
     *
     * @param attaOid the value for t_ha_performance_plustime_record.ATTA_OID
     *
     * @mbg.generated
     */
    public void setAttaOid(String attaOid) {
        this.attaOid = attaOid == null ? null : attaOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.FASTDFS_UPLOAD_URL
     *
     * @return the value of t_ha_performance_plustime_record.FASTDFS_UPLOAD_URL
     *
     * @mbg.generated
     */
    public String getFastdfsUploadUrl() {
        return fastdfsUploadUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.FASTDFS_UPLOAD_URL
     *
     * @param fastdfsUploadUrl the value for t_ha_performance_plustime_record.FASTDFS_UPLOAD_URL
     *
     * @mbg.generated
     */
    public void setFastdfsUploadUrl(String fastdfsUploadUrl) {
        this.fastdfsUploadUrl = fastdfsUploadUrl == null ? null : fastdfsUploadUrl.trim();
    }
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.FASTDFS_NGINX_URL
     *
     * @return the value of t_ha_performance_plustime_record.FASTDFS_NGINX_URL
     *
     * @mbg.generated
     */
    public String getFastdfsNginxUrl() {
        return fastdfsNginxUrl;
    }
    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.FASTDFS_NGINX_URL
     *
     * @param fastdfsNginxUrl the value for t_ha_performance_plustime_record.FASTDFS_NGINX_URL
     *
     * @mbg.generated
     */
    public void setFastdfsNginxUrl(String fastdfsNginxUrl) {
        this.fastdfsNginxUrl = fastdfsNginxUrl == null ? null : fastdfsNginxUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.BONUS_NOTES
     *
     * @return the value of t_ha_performance_plustime_record.BONUS_NOTES
     *
     * @mbg.generated
     */
    public String getBonusNotes() {
        return bonusNotes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.BONUS_NOTES
     *
     * @param bonusNotes the value for t_ha_performance_plustime_record.BONUS_NOTES
     *
     * @mbg.generated
     */
    public void setBonusNotes(String bonusNotes) {
        this.bonusNotes = bonusNotes == null ? null : bonusNotes.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.AUDIT_STATUS
     *
     * @return the value of t_ha_performance_plustime_record.AUDIT_STATUS
     *
     * @mbg.generated
     */
    public String getAuditStatus() {
        return auditStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.AUDIT_STATUS
     *
     * @param auditStatus the value for t_ha_performance_plustime_record.AUDIT_STATUS
     *
     * @mbg.generated
     */
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus == null ? null : auditStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.AUDIT_REASON
     *
     * @return the value of t_ha_performance_plustime_record.AUDIT_REASON
     *
     * @mbg.generated
     */
    public String getAuditReason() {
        return auditReason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.AUDIT_REASON
     *
     * @param auditReason the value for t_ha_performance_plustime_record.AUDIT_REASON
     *
     * @mbg.generated
     */
    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason == null ? null : auditReason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.GROUP_LEADER_ID
     *
     * @return the value of t_ha_performance_plustime_record.GROUP_LEADER_ID
     *
     * @mbg.generated
     */
    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.GROUP_LEADER_ID
     *
     * @param groupLeaderId the value for t_ha_performance_plustime_record.GROUP_LEADER_ID
     *
     * @mbg.generated
     */
    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.GROUP_LEADER_NAME
     *
     * @return the value of t_ha_performance_plustime_record.GROUP_LEADER_NAME
     *
     * @mbg.generated
     */
    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.GROUP_LEADER_NAME
     *
     * @param groupLeaderName the value for t_ha_performance_plustime_record.GROUP_LEADER_NAME
     *
     * @mbg.generated
     */
    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName == null ? null : groupLeaderName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.DELETE_STATUS
     *
     * @return the value of t_ha_performance_plustime_record.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_performance_plustime_record.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.CREATE_BY
     *
     * @return the value of t_ha_performance_plustime_record.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.CREATE_BY
     *
     * @param createBy the value for t_ha_performance_plustime_record.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.CREATE_DATE
     *
     * @return the value of t_ha_performance_plustime_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.CREATE_DATE
     *
     * @param createDate the value for t_ha_performance_plustime_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.UPDATE_BY
     *
     * @return the value of t_ha_performance_plustime_record.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.UPDATE_BY
     *
     * @param updateBy the value for t_ha_performance_plustime_record.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_performance_plustime_record.UPDATE_DATE
     *
     * @return the value of t_ha_performance_plustime_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_performance_plustime_record.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_performance_plustime_record.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }
}