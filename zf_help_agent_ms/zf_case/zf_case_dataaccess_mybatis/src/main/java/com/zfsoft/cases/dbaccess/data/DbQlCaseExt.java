package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 办件信息表(QlCaseExt)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlCaseExt implements Serializable {
    private static final long serialVersionUID = -69527965049869639L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 办件主键
     */
    private String caseOid;
    /**
     * 投资项目编号
     */
    private String investProjectCode;
    /**
     * 投资项目名称
     */
    private String investProjecName;
    /**
     * 是否需要制证（否0、是1）
     */
    private Integer needLincese;
    /**
     * 所属单位
     */
    private String belongsystem;
    /**
     * 查询密码
     */
    private String projpwd;
    /**
     * 办件来源二级类型
     */
    private String sourceType;
    /**
     * 寄送日期
     */
    private Date sendDate;
    /**
     * 业务办理项编码
     */
    private String taskHandleItem;
    /**
     * 地方实施编码
     */
    private String localTaskCode;
    /**
     * 受理文书编号
     */
    private String acceptDocNo;
    /**
     * 办件摘要
     */
    private String projectAbstract;
    /**
     * 跨地区办理目标部门
     */
    private String targeOrgName;
    /**
     * 是否已经补齐补正（否0、是1）
     */
    private Integer isBqbz;
    /**
     * 事项性质
     */
    private String serviceCharacter;
    /**
     * 送达方式
     */
    private String resultDeliveryWay;
    /**
     * 送达状态（否0、是1）
     */
    private Integer deliverFlag;

    /**
     * 是否是代理人（否0、是1）
     */
    private String proxyFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    private Date modifyDate;

    // 纸质材料提交方式，1-邮寄，2-窗口提交
    private int paperSubmitType;

    public int getPaperSubmitType() {
        return paperSubmitType;
    }

    public void setPaperSubmitType(int paperSubmitType) {
        this.paperSubmitType = paperSubmitType;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
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

    public String getInvestProjectCode() {
        return investProjectCode;
    }

    public void setInvestProjectCode(String investProjectCode) {
        this.investProjectCode = investProjectCode;
    }

    public String getInvestProjecName() {
        return investProjecName;
    }

    public void setInvestProjecName(String investProjecName) {
        this.investProjecName = investProjecName;
    }

    public Integer getNeedLincese() {
        return needLincese;
    }

    public void setNeedLincese(Integer needLincese) {
        this.needLincese = needLincese;
    }

    public String getBelongsystem() {
        return belongsystem;
    }

    public void setBelongsystem(String belongsystem) {
        this.belongsystem = belongsystem;
    }

    public String getProjpwd() {
        return projpwd;
    }

    public void setProjpwd(String projpwd) {
        this.projpwd = projpwd;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getTaskHandleItem() {
        return taskHandleItem;
    }

    public void setTaskHandleItem(String taskHandleItem) {
        this.taskHandleItem = taskHandleItem;
    }

    public String getLocalTaskCode() {
        return localTaskCode;
    }

    public void setLocalTaskCode(String localTaskCode) {
        this.localTaskCode = localTaskCode;
    }

    public String getAcceptDocNo() {
        return acceptDocNo;
    }

    public void setAcceptDocNo(String acceptDocNo) {
        this.acceptDocNo = acceptDocNo;
    }

    public String getProjectAbstract() {
        return projectAbstract;
    }

    public void setProjectAbstract(String projectAbstract) {
        this.projectAbstract = projectAbstract;
    }

    public String getTargeOrgName() {
        return targeOrgName;
    }

    public void setTargeOrgName(String targeOrgName) {
        this.targeOrgName = targeOrgName;
    }

    public Integer getIsBqbz() {
        return isBqbz;
    }

    public void setIsBqbz(Integer isBqbz) {
        this.isBqbz = isBqbz;
    }

    public String getServiceCharacter() {
        return serviceCharacter;
    }

    public void setServiceCharacter(String serviceCharacter) {
        this.serviceCharacter = serviceCharacter;
    }

    public String getResultDeliveryWay() {
        return resultDeliveryWay;
    }

    public void setResultDeliveryWay(String resultDeliveryWay) {
        this.resultDeliveryWay = resultDeliveryWay;
    }

    public Integer getDeliverFlag() {
        return deliverFlag;
    }

    public String getProxyFlag() {
        return proxyFlag;
    }

    public void setProxyFlag(String proxyFlag) {
        this.proxyFlag = proxyFlag;
    }

    public void setDeliverFlag(Integer deliverFlag) {
        this.deliverFlag = deliverFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}