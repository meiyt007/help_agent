package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 记录办件信息与选择材料情形选型关系表(QlCaseSituationTitleValRelation)实体类
 *
 * @author wangwg
 * @date  2020-11-30
 */
public class DbQlCase implements Serializable {
    private static final long serialVersionUID = -66685586795784584L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String caseOid;
    /**
     * 所属事项
     */
    private String serviceOid;
    /**
     * 办件编号
     */
    private String caseNumber;
    /**
     * 所属机构
     */
    private String organOid;
    /**
     * 办件状态
     */
    private Integer caseStatus;
    /**
     * 是否删除（否0、是1）
     */
    private Integer delFlag;
    /**
     * 受理日期
     */
    private Date acceptanceDate;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人主键
     */
    private String createUserOid;
    /**
     * 办结时间
     */
    private Date concludeDate;
    /**
     * 办件类型
     */
    private String caseType;
    /**
     * 应办结时间
     */
    private Date shouldConcludeDate;



    private String startDate;

    private String endDate;
    /**
     * 业务项名称/实施清单名称
     */
    private String serviceName;
    /**
     * 事项类型
     */
    private String serviceType;
    /**
     * 事项类型名称
     */
    private String serviceTypeName;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 套餐办件编号
     */
    private String packageCaseOid;
    /**
     * 所属应用(1综窗、5运行平台、3不动产登记、4工改系统)
     */
    private Integer sourceApp;

    /**
     * 所属来源(1  窗口申请
     * 2  网上申请
     * 3  其他方式
     * 4  自助终端
     * 5  工作台
     * 6  无人受理站
     * 7  微信公众号
     * 8  移动app)
     */
    private Integer sourceType;

    /**
     * 容缺补正到期时间
     */
    private Date rqbzDueDate;

    private String applyUserType;

    private String applyUserName;

    private String credentialNumber;

    private String applyUserPhone;

    private String applyUserAddress;

    private String addresseeDetailAddress;

    private String credentialType;

    //add  排队号
    private String  queueNum;

    private String  formOids;

    private Date modifyDate;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 终端主键
     */
    private String terminalOid;

    /**
     * 终端编号
     */
    private String terminalCode;

    // 是否已预填
    private int prepFill;

    /**
     * 材料类型 1:材料准备,2:收件
     */
    private String  materialType;
    /**
     *万达-发证方式：自取，物流，无结果物
     */
    private String  certWay;
    /**
     * 万达-配送公司，取值为EMS、顺丰速递、其他
     */
    private String  expressCompany;

    public String getCertWay() { return certWay; }

    public void setCertWay(String certWay) { this.certWay = certWay; }

    public String getExpressCompany() { return expressCompany; }

    public void setExpressCompany(String expressCompany) { this.expressCompany = expressCompany; }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public int getPrepFill() {
        return prepFill;
    }

    public void setPrepFill(int prepFill) {
        this.prepFill = prepFill;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTerminalOid() {
        return terminalOid;
    }

    public void setTerminalOid(String terminalOid) {
        this.terminalOid = terminalOid;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(String queueNum) {
        this.queueNum = queueNum;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public String getServiceOid() {
        return serviceOid;
    }

    public void setServiceOid(String serviceOid) {
        this.serviceOid = serviceOid;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getOrganOid() {
        return organOid;
    }

    public void setOrganOid(String organOid) {
        this.organOid = organOid;
    }

    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserOid() {
        return createUserOid;
    }

    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid;
    }

    public Date getConcludeDate() {
        return concludeDate;
    }

    public void setConcludeDate(Date concludeDate) {
        this.concludeDate = concludeDate;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public Date getShouldConcludeDate() {
        return shouldConcludeDate;
    }

    public void setShouldConcludeDate(Date shouldConcludeDate) {
        this.shouldConcludeDate = shouldConcludeDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPackageCaseOid() {
        return packageCaseOid;
    }

    public void setPackageCaseOid(String packageCaseOid) {
        this.packageCaseOid = packageCaseOid;
    }

    public Integer getSourceApp() {
        return sourceApp;
    }

    public void setSourceApp(Integer sourceApp) {
        this.sourceApp = sourceApp;
    }

    public Date getRqbzDueDate() {
        return rqbzDueDate;
    }

    public void setRqbzDueDate(Date rqbzDueDate) {
        this.rqbzDueDate = rqbzDueDate;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getApplyUserType() {
        return applyUserType;
    }

    public void setApplyUserType(String applyUserType) {
        this.applyUserType = applyUserType;
    }

    public String getApplyUserPhone() {
        return applyUserPhone;
    }

    public void setApplyUserPhone(String applyUserPhone) {
        this.applyUserPhone = applyUserPhone;
    }

    public String getApplyUserAddress() {
        return applyUserAddress;
    }

    public void setApplyUserAddress(String applyUserAddress) {
        this.applyUserAddress = applyUserAddress;
    }

    public String getAddresseeDetailAddress() {
        return addresseeDetailAddress;
    }

    public void setAddresseeDetailAddress(String addresseeDetailAddress) {
        this.addresseeDetailAddress = addresseeDetailAddress;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFormOids() {
        return formOids;
    }

    public void setFormOids(String formOids) {
        this.formOids = formOids;
    }
}
