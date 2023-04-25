package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaDeskDep {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.DESK_ID
     *
     * @mbg.generated
     */
    private Long deskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.GROUP_ID
     *
     * @mbg.generated
     */
    private Long groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.DESK_QUEUE_ID
     *
     * @mbg.generated
     */
    private Long deskQueueId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.CONFIRM_FLAG
     *
     * @mbg.generated
     */
    private String confirmFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.CON_WORK_USER_ID
     *
     * @mbg.generated
     */
    private Long conWorkUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.CON_DATE
     *
     * @mbg.generated
     */
    private Date conDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.DESC
     *
     * @mbg.generated
     */
    private String desc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.DELETE_STATUS
     *
     * @mbg.generated
     */
    private String deleteStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_desk_dep.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.ID
     *
     * @return the value of t_ha_desk_dep.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.ID
     *
     * @param id the value for t_ha_desk_dep.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.DESK_ID
     *
     * @return the value of t_ha_desk_dep.DESK_ID
     *
     * @mbg.generated
     */
    public Long getDeskId() {
        return deskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.DESK_ID
     *
     * @param deskId the value for t_ha_desk_dep.DESK_ID
     *
     * @mbg.generated
     */
    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.GROUP_ID
     *
     * @return the value of t_ha_desk_dep.GROUP_ID
     *
     * @mbg.generated
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.GROUP_ID
     *
     * @param groupId the value for t_ha_desk_dep.GROUP_ID
     *
     * @mbg.generated
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.DESK_QUEUE_ID
     *
     * @return the value of t_ha_desk_dep.DESK_QUEUE_ID
     *
     * @mbg.generated
     */
    public Long getDeskQueueId() {
        return deskQueueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.DESK_QUEUE_ID
     *
     * @param deskQueueId the value for t_ha_desk_dep.DESK_QUEUE_ID
     *
     * @mbg.generated
     */
    public void setDeskQueueId(Long deskQueueId) {
        this.deskQueueId = deskQueueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.CONFIRM_FLAG
     *
     * @return the value of t_ha_desk_dep.CONFIRM_FLAG
     *
     * @mbg.generated
     */
    public String getConfirmFlag() {
        return confirmFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.CONFIRM_FLAG
     *
     * @param confirmFlag the value for t_ha_desk_dep.CONFIRM_FLAG
     *
     * @mbg.generated
     */
    public void setConfirmFlag(String confirmFlag) {
        this.confirmFlag = confirmFlag == null ? null : confirmFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.CON_WORK_USER_ID
     *
     * @return the value of t_ha_desk_dep.CON_WORK_USER_ID
     *
     * @mbg.generated
     */
    public Long getConWorkUserId() {
        return conWorkUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.CON_WORK_USER_ID
     *
     * @param conWorkUserId the value for t_ha_desk_dep.CON_WORK_USER_ID
     *
     * @mbg.generated
     */
    public void setConWorkUserId(Long conWorkUserId) {
        this.conWorkUserId = conWorkUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.CON_DATE
     *
     * @return the value of t_ha_desk_dep.CON_DATE
     *
     * @mbg.generated
     */
    public Date getConDate() {
        return conDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.CON_DATE
     *
     * @param conDate the value for t_ha_desk_dep.CON_DATE
     *
     * @mbg.generated
     */
    public void setConDate(Date conDate) {
        this.conDate = conDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.DESC
     *
     * @return the value of t_ha_desk_dep.DESC
     *
     * @mbg.generated
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.DESC
     *
     * @param desc the value for t_ha_desk_dep.DESC
     *
     * @mbg.generated
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.DELETE_STATUS
     *
     * @return the value of t_ha_desk_dep.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_desk_dep.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.CREATE_BY
     *
     * @return the value of t_ha_desk_dep.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.CREATE_BY
     *
     * @param createBy the value for t_ha_desk_dep.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.CREATE_DATE
     *
     * @return the value of t_ha_desk_dep.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.CREATE_DATE
     *
     * @param createDate the value for t_ha_desk_dep.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.UPDATE_BY
     *
     * @return the value of t_ha_desk_dep.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.UPDATE_BY
     *
     * @param updateBy the value for t_ha_desk_dep.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_desk_dep.UPDATE_DATE
     *
     * @return the value of t_ha_desk_dep.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_desk_dep.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_desk_dep.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}