package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

public class DbHaWorkQueue {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.NAME
     *
     * @mbg.generated
     */
    private String name;

    private String cardType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.CARD_NO
     *
     * @mbg.generated
     */
    private String cardNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.PHONE
     *
     * @mbg.generated
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.COMPANY_NAME
     *
     * @mbg.generated
     */
    private String companyName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.COMPANY_CODE
     *
     * @mbg.generated
     */
    private String companyCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.QUEUE_STATUS
     *
     * @mbg.generated
     */
    private String queueStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.DISTRIBUTE_STATUS
     *
     * @mbg.generated
     */
    private String distributeStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.DISTRIBUTE_TIME
     *
     * @mbg.generated
     */
    private Date distributeTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.SERVICE_STATUS
     *
     * @mbg.generated
     */
    private String serviceStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.SERVICE_WORK_USER_ID
     *
     * @mbg.generated
     */
    private Long serviceWorkUserId;


    /**
     * 服务开始时间：真正为办事人服务的开始时间，转派后，是转派后接待的时间
     */
    private Date serviceBeginTime;

    /**
     * 第一次服务开始时间， 第一个为办事人服务的时间
     */
    private Date firstServiceBeginTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.SERVICE_END_TIME
     *
     * @mbg.generated
     */
    private Date serviceEndTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.SERVICE_DURATION
     *
     * @mbg.generated
     */
    private Integer serviceDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.NEXT_SERVICE_ADVISE
     *
     * @mbg.generated
     */
    private String nextServiceAdvise;


    /**
     * 分配建议
     */
    private String distributionAdvise;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.ADVISE_MEMO
     *
     * @mbg.generated
     */
    private String adviseMemo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_queue.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;


    private Long distributeUserId;

    /**
     * 排队号
     */
    private String windowsNumber;

    /**
     * 帮取号
     */
    private String agentTakeNumber;

    /**
     * 帮取号
     */
    private String takeNumberType;


    /**
     * DETECTS_SERVICE_TIME 检测服务时间
     * @return
     */
    private Date detectsServiceTime ;

    public Date getDetectsServiceTime() {
        return detectsServiceTime;
    }

    public void setDetectsServiceTime(Date detectsServiceTime) {
        this.detectsServiceTime = detectsServiceTime;
    }

    public String getTakeNumberType() {
        return takeNumberType;
    }

    public void setTakeNumberType(String takeNumberType) {
        this.takeNumberType = takeNumberType;
    }

    public String getAgentTakeNumber() {
        return agentTakeNumber;
    }

    public void setAgentTakeNumber(String agentTakeNumber) {
        this.agentTakeNumber = agentTakeNumber;
    }

    public String getWindowsNumber() {
        return windowsNumber;
    }

    public void setWindowsNumber(String windowsNumber) {
        this.windowsNumber = windowsNumber;
    }

    public Long getDistributeUserId() {
        return distributeUserId;
    }

    public void setDistributeUserId(Long distributeUserId) {
        this.distributeUserId = distributeUserId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.ID
     *
     * @return the value of t_ha_work_queue.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.ID
     *
     * @param id the value for t_ha_work_queue.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.NAME
     *
     * @return the value of t_ha_work_queue.NAME
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.NAME
     *
     * @param name the value for t_ha_work_queue.NAME
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.CARD_NO
     *
     * @return the value of t_ha_work_queue.CARD_NO
     *
     * @mbg.generated
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.CARD_NO
     *
     * @param cardNo the value for t_ha_work_queue.CARD_NO
     *
     * @mbg.generated
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.PHONE
     *
     * @return the value of t_ha_work_queue.PHONE
     *
     * @mbg.generated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.PHONE
     *
     * @param phone the value for t_ha_work_queue.PHONE
     *
     * @mbg.generated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.COMPANY_NAME
     *
     * @return the value of t_ha_work_queue.COMPANY_NAME
     *
     * @mbg.generated
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.COMPANY_NAME
     *
     * @param companyName the value for t_ha_work_queue.COMPANY_NAME
     *
     * @mbg.generated
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.COMPANY_CODE
     *
     * @return the value of t_ha_work_queue.COMPANY_CODE
     *
     * @mbg.generated
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.COMPANY_CODE
     *
     * @param companyCode the value for t_ha_work_queue.COMPANY_CODE
     *
     * @mbg.generated
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.QUEUE_STATUS
     *
     * @return the value of t_ha_work_queue.QUEUE_STATUS
     *
     * @mbg.generated
     */
    public String getQueueStatus() {
        return queueStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.QUEUE_STATUS
     *
     * @param queueStatus the value for t_ha_work_queue.QUEUE_STATUS
     *
     * @mbg.generated
     */
    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus == null ? null : queueStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.DISTRIBUTE_STATUS
     *
     * @return the value of t_ha_work_queue.DISTRIBUTE_STATUS
     *
     * @mbg.generated
     */
    public String getDistributeStatus() {
        return distributeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.DISTRIBUTE_STATUS
     *
     * @param distributeStatus the value for t_ha_work_queue.DISTRIBUTE_STATUS
     *
     * @mbg.generated
     */
    public void setDistributeStatus(String distributeStatus) {
        this.distributeStatus = distributeStatus == null ? null : distributeStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.DISTRIBUTE_TIME
     *
     * @return the value of t_ha_work_queue.DISTRIBUTE_TIME
     *
     * @mbg.generated
     */
    public Date getDistributeTime() {
        return distributeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.DISTRIBUTE_TIME
     *
     * @param distributeTime the value for t_ha_work_queue.DISTRIBUTE_TIME
     *
     * @mbg.generated
     */
    public void setDistributeTime(Date distributeTime) {
        this.distributeTime = distributeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.SERVICE_STATUS
     *
     * @return the value of t_ha_work_queue.SERVICE_STATUS
     *
     * @mbg.generated
     */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.SERVICE_STATUS
     *
     * @param serviceStatus the value for t_ha_work_queue.SERVICE_STATUS
     *
     * @mbg.generated
     */
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus == null ? null : serviceStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.SERVICE_WORK_USER_ID
     *
     * @return the value of t_ha_work_queue.SERVICE_WORK_USER_ID
     *
     * @mbg.generated
     */
    public Long getServiceWorkUserId() {
        return serviceWorkUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.SERVICE_WORK_USER_ID
     *
     * @param serviceWorkUserId the value for t_ha_work_queue.SERVICE_WORK_USER_ID
     *
     * @mbg.generated
     */
    public void setServiceWorkUserId(Long serviceWorkUserId) {
        this.serviceWorkUserId = serviceWorkUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.SERVICE_BEGIN_TIME
     *
     * @return the value of t_ha_work_queue.SERVICE_BEGIN_TIME
     *
     * @mbg.generated
     */
    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.SERVICE_BEGIN_TIME
     *
     * @param serviceBeginTime the value for t_ha_work_queue.SERVICE_BEGIN_TIME
     *
     * @mbg.generated
     */
    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.SERVICE_END_TIME
     *
     * @return the value of t_ha_work_queue.SERVICE_END_TIME
     *
     * @mbg.generated
     */
    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.SERVICE_END_TIME
     *
     * @param serviceEndTime the value for t_ha_work_queue.SERVICE_END_TIME
     *
     * @mbg.generated
     */
    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.SERVICE_DURATION
     *
     * @return the value of t_ha_work_queue.SERVICE_DURATION
     *
     * @mbg.generated
     */
    public Integer getServiceDuration() {
        return serviceDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.SERVICE_DURATION
     *
     * @param serviceDuration the value for t_ha_work_queue.SERVICE_DURATION
     *
     * @mbg.generated
     */
    public void setServiceDuration(Integer serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.NEXT_SERVICE_ADVISE
     *
     * @return the value of t_ha_work_queue.NEXT_SERVICE_ADVISE
     *
     * @mbg.generated
     */
    public String getNextServiceAdvise() {
        return nextServiceAdvise;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.NEXT_SERVICE_ADVISE
     *
     * @param nextServiceAdvise the value for t_ha_work_queue.NEXT_SERVICE_ADVISE
     *
     * @mbg.generated
     */
    public void setNextServiceAdvise(String nextServiceAdvise) {
        this.nextServiceAdvise = nextServiceAdvise == null ? null : nextServiceAdvise.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.ADVISE_MEMO
     *
     * @return the value of t_ha_work_queue.ADVISE_MEMO
     *
     * @mbg.generated
     */
    public String getAdviseMemo() {
        return adviseMemo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.ADVISE_MEMO
     *
     * @param adviseMemo the value for t_ha_work_queue.ADVISE_MEMO
     *
     * @mbg.generated
     */
    public void setAdviseMemo(String adviseMemo) {
        this.adviseMemo = adviseMemo == null ? null : adviseMemo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.CREATE_BY
     *
     * @return the value of t_ha_work_queue.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.CREATE_BY
     *
     * @param createBy the value for t_ha_work_queue.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.CREATE_DATE
     *
     * @return the value of t_ha_work_queue.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.CREATE_DATE
     *
     * @param createDate the value for t_ha_work_queue.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.UPDATE_BY
     *
     * @return the value of t_ha_work_queue.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.UPDATE_BY
     *
     * @param updateBy the value for t_ha_work_queue.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_queue.UPDATE_DATE
     *
     * @return the value of t_ha_work_queue.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_queue.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_work_queue.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getFirstServiceBeginTime() {
        return firstServiceBeginTime;
    }

    public void setFirstServiceBeginTime(Date firstServiceBeginTime) {
        this.firstServiceBeginTime = firstServiceBeginTime;
    }

    public String getDistributionAdvise() {
        return distributionAdvise;
    }

    public void setDistributionAdvise(String distributionAdvise) {
        this.distributionAdvise = distributionAdvise;
    }
}