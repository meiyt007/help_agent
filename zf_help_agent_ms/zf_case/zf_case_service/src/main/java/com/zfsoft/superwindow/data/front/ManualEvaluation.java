package com.zfsoft.superwindow.data.front;

import java.util.Date;

public class ManualEvaluation {
    /**
     * ID
     */
    private Long id;
    /**
     * 手动评价ID
     */
    private String oid;
    /**
     * 服务评价
     */
    private String serviceEvaluation;
    /**
     * 虚拟业务表编号
     */
    private String virtualBusinessNum;
    /**
     * 办件编号
     */
    private String caseNumber;
    /**
     * 具体评价项
     */
    private String evaluationItem;
    /**
     * 具体评价项code
     */
    private String evaluationCode;
    /**
     * 办事人员名字
     */
    private String caseUserName;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 满意度分数
     */
    private String satisfactionNum;
    /**
     * 情绪评价
     */
    private String emotionEvaluation;

    private String createUserOid;

    private String createUserName;
    /**
     * 是否推送
     */
    private Integer pushFlag;
    /**
     * 鏄惁鍒犻櫎
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 是否评价
     */
    private Integer evaluateFlag;
    /**
     * 评价来源
     */
    private Integer evaluateSource;

    private String  caseOid;//办件表主键

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getServiceEvaluation() {
        return serviceEvaluation;
    }

    public void setServiceEvaluation(String serviceEvaluation) {
        this.serviceEvaluation = serviceEvaluation;
    }

    public String getVirtualBusinessNum() {
        return virtualBusinessNum;
    }

    public void setVirtualBusinessNum(String virtualBusinessNum) {
        this.virtualBusinessNum = virtualBusinessNum;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getEvaluationItem() {
        return evaluationItem;
    }

    public void setEvaluationItem(String evaluationItem) {
        this.evaluationItem = evaluationItem;
    }

    public String getEvaluationCode() { return evaluationCode; }

    public void setEvaluationCode(String evaluationCode) { this.evaluationCode = evaluationCode; }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSatisfactionNum() {
        return satisfactionNum;
    }

    public void setSatisfactionNum(String satisfactionNum) {
        this.satisfactionNum = satisfactionNum;
    }

    public String getEmotionEvaluation() {
        return emotionEvaluation;
    }

    public void setEmotionEvaluation(String emotionEvaluation) {
        this.emotionEvaluation = emotionEvaluation;
    }

    public String getCreateUserOid() {
        return createUserOid;
    }

    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(Integer pushFlag) {
        this.pushFlag = pushFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getEvaluateFlag() {
        return evaluateFlag;
    }

    public void setEvaluateFlag(Integer evaluateFlag) { this.evaluateFlag = evaluateFlag; }

    public Integer getEvaluateSource() {
        return evaluateSource;
    }

    public void setEvaluateSource(Integer evaluateSource) { this.evaluateSource = evaluateSource; }
}
