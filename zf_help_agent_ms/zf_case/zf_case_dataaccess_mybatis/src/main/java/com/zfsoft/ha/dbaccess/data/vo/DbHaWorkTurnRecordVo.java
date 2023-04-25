package com.zfsoft.ha.dbaccess.data.vo;

import java.util.Date;

public class DbHaWorkTurnRecordVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 队列表主键
     */
    private Long workQueueId;

    /**
     * 服务转派人员编号
     */
    private Long handleWorkUserId;

    /**HaWorkTurnRecordVo
     * 服务人员编号
     */

    private Long serviceWorkUserId;
    /**
     * 转派组长人员编号 VERIFY_WORK_USER_ID
     */
    private Long verifyWorkUserId;


    /**
     * 原服务人员编号
     */
    private Long oldServiceWorkUserId;

    /**
     * 转派时间
     */
    private Date turnTime;

    /**
     * 转派状态
     */
    private String turnStatus;

    /**
     * 服务转派原因
     */
    private String turnMemo;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 退回原因
     */
    private String rollbackMemo;

    /**
     * 服务开始时间
     */
    private Date serviceBeginTime;

    /**
     * 服务结束时间
     */
    private Date serviceEndTime;

    /**
     * 服务时长
     */
    private Integer serviceDuration;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    private String name;

    private String serviceWorkUserName;

    private String handleWorkUserName;

    private String companyName;

    private String companyCode;

    private String cardNo;

    private String phone;

    private String serviceName;
    /**
     * 当天开始时间
     */
    private Date beginADay;

    /**
     * 当天结束时间
     */
    private Date endADay;

    public Date getBeginADay() {
        return beginADay;
    }

    public void setBeginADay(Date beginADay) {
        this.beginADay = beginADay;
    }

    public Date getEndADay() {
        return endADay;
    }

    public void setEndADay(Date endADay) {
        this.endADay = endADay;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHandleWorkUserName() {
        return handleWorkUserName;
    }

    public void setHandleWorkUserName(String handleWorkUserName) {
        this.handleWorkUserName = handleWorkUserName;
    }

    public Long getVerifyWorkUserId() {
        return verifyWorkUserId;
    }

    public void setVerifyWorkUserId(Long verifyWorkUserId) {
        this.verifyWorkUserId = verifyWorkUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceWorkUserName() {
        return serviceWorkUserName;
    }

    public void setServiceWorkUserName(String serviceWorkUserName) {
        this.serviceWorkUserName = serviceWorkUserName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkQueueId() {
        return workQueueId;
    }

    public void setWorkQueueId(Long workQueueId) {
        this.workQueueId = workQueueId;
    }

    public Long getServiceWorkUserId() {
        return serviceWorkUserId;
    }

    public void setServiceWorkUserId(Long serviceWorkUserId) {
        this.serviceWorkUserId = serviceWorkUserId;
    }

    public Long getOldServiceWorkUserId() {
        return oldServiceWorkUserId;
    }

    public void setOldServiceWorkUserId(Long oldServiceWorkUserId) {
        this.oldServiceWorkUserId = oldServiceWorkUserId;
    }

    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }


    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public Integer getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(Integer serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getHandleWorkUserId() {
        return handleWorkUserId;
    }

    public void setHandleWorkUserId(Long handleWorkUserId) {
        this.handleWorkUserId = handleWorkUserId;
    }

    public Date getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(Date turnTime) {
        this.turnTime = turnTime;
    }

    public String getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(String turnStatus) {
        this.turnStatus = turnStatus;
    }

    public String getTurnMemo() {
        return turnMemo;
    }

    public void setTurnMemo(String turnMemo) {
        this.turnMemo = turnMemo;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getRollbackMemo() {
        return rollbackMemo;
    }

    public void setRollbackMemo(String rollbackMemo) {
        this.rollbackMemo = rollbackMemo;
    }
}