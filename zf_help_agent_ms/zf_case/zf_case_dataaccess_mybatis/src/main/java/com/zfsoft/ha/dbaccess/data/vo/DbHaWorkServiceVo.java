package com.zfsoft.ha.dbaccess.data.vo;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/11 10:54
 */
public class DbHaWorkServiceVo {

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 服务人员编号
     */
    private Long workUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.WORK_QUEUE_ID
     *
     * @mbg.generated
     */
    private Long workQueueId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.SERVICE_TYPE
     *
     * @mbg.generated
     */
    private String serviceType;

    private String pushType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.SERVICE_MEMO
     *
     * @mbg.generated
     */
    private String serviceMemo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.SX_SERVICE_ID
     *
     * @mbg.generated
     */
    private String sxServiceId;


    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.QL_CASE_ID
     *
     * @mbg.generated
     */
    private String qlCaseId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.PUSH_MEMO
     *
     * @mbg.generated
     */
    private String pushMemo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_service.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;


    private Date serviceTime;

    private String serviceName;

    private String name;

    private String companyName;

    private String companyCode;

    /**
     * 转派id
     */
    private Long turnRecordId;
    /**
     * 转派id
     */
    private String serviceStatus;

    private String workUserName;

    private String caseNumber;

    private String  applyUserName;

    private String applyUserType;

    private String phone;

    public String getApplyUserType() {
        return applyUserType;
    }

    public void setApplyUserType(String applyUserType) {
        this.applyUserType = applyUserType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getWorkUserName() {
        return workUserName;
    }

    public void setWorkUserName(String workUserName) {
        this.workUserName = workUserName;
    }

    public Long getTurnRecordId() {
        return turnRecordId;
    }

    public void setTurnRecordId(Long turnRecordId) {
        this.turnRecordId = turnRecordId;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.ID
     *
     * @return the value of t_ha_work_service.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.ID
     *
     * @param id the value for t_ha_work_service.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.WORK_QUEUE_ID
     *
     * @return the value of t_ha_work_service.WORK_QUEUE_ID
     *
     * @mbg.generated
     */
    public Long getWorkQueueId() {
        return workQueueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.WORK_QUEUE_ID
     *
     * @param workQueueId the value for t_ha_work_service.WORK_QUEUE_ID
     *
     * @mbg.generated
     */
    public void setWorkQueueId(Long workQueueId) {
        this.workQueueId = workQueueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.SERVICE_TYPE
     *
     * @return the value of t_ha_work_service.SERVICE_TYPE
     *
     * @mbg.generated
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.SERVICE_TYPE
     *
     * @param serviceType the value for t_ha_work_service.SERVICE_TYPE
     *
     * @mbg.generated
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.SERVICE_MEMO
     *
     * @return the value of t_ha_work_service.SERVICE_MEMO
     *
     * @mbg.generated
     */
    public String getServiceMemo() {
        return serviceMemo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.SERVICE_MEMO
     *
     * @param serviceMemo the value for t_ha_work_service.SERVICE_MEMO
     *
     * @mbg.generated
     */
    public void setServiceMemo(String serviceMemo) {
        this.serviceMemo = serviceMemo == null ? null : serviceMemo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.SX_SERVICE_ID
     *
     * @return the value of t_ha_work_service.SX_SERVICE_ID
     *
     * @mbg.generated
     */
    public String getSxServiceId() {
        return sxServiceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.SX_SERVICE_ID
     *
     * @param sxServiceId the value for t_ha_work_service.SX_SERVICE_ID
     *
     * @mbg.generated
     */
    public void setSxServiceId(String sxServiceId) {
        this.sxServiceId = sxServiceId == null ? null : sxServiceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.QL_CASE_ID
     *
     * @return the value of t_ha_work_service.QL_CASE_ID
     *
     * @mbg.generated
     */
    public String getQlCaseId() {
        return qlCaseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.QL_CASE_ID
     *
     * @param qlCaseId the value for t_ha_work_service.QL_CASE_ID
     *
     * @mbg.generated
     */
    public void setQlCaseId(String qlCaseId) {
        this.qlCaseId = qlCaseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.PUSH_MEMO
     *
     * @return the value of t_ha_work_service.PUSH_MEMO
     *
     * @mbg.generated
     */
    public String getPushMemo() {
        return pushMemo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.PUSH_MEMO
     *
     * @param pushMemo the value for t_ha_work_service.PUSH_MEMO
     *
     * @mbg.generated
     */
    public void setPushMemo(String pushMemo) {
        this.pushMemo = pushMemo == null ? null : pushMemo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.CREATE_BY
     *
     * @return the value of t_ha_work_service.CREATE_BY
     *
     * @mbg.generated
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.CREATE_BY
     *
     * @param createBy the value for t_ha_work_service.CREATE_BY
     *
     * @mbg.generated
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.CREATE_DATE
     *
     * @return the value of t_ha_work_service.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.CREATE_DATE
     *
     * @param createDate the value for t_ha_work_service.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.UPDATE_BY
     *
     * @return the value of t_ha_work_service.UPDATE_BY
     *
     * @mbg.generated
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.UPDATE_BY
     *
     * @param updateBy the value for t_ha_work_service.UPDATE_BY
     *
     * @mbg.generated
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_work_service.UPDATE_DATE
     *
     * @return the value of t_ha_work_service.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_work_service.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_work_service.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getWorkUserId() {
        return workUserId;
    }

    public void setWorkUserId(Long workUserId) {
        this.workUserId = workUserId;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
