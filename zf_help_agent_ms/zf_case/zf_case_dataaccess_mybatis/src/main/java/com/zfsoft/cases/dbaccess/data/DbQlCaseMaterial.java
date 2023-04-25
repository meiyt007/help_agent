package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 办件材料表(QlCaseMaterial)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlCaseMaterial implements Serializable {
    private static final long serialVersionUID = -95599495866361141L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String caseMaterialOid;
    /**
     * 所属事项材料
     */
    private String materialOid;
    /**
     * 所属事项材料名称
     */
    private String materialName;
    /**
     * 所属办件
     */
    private String caseOid;
    /**
     * 材料是否已收取（否0、是1）
     */
    private Integer collectionFlag;
    /**
     * 收取方式
     */
    private String collectionType;
    /**
     * 收取数量
     */
    private Integer collectionNumber;
    /**
     * 证照主键
     */
    private String elemLicenseOid;
    /**
     * 证照编码
     */
    private String elemNumber;
    /**
     * 收取时间
     */
    private Date collectionDate;
    /**
     * 删除状态（否0、是1）
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private String createUserOid;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;

    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;

    /**
     * 预览样表id
     */
    private String materialSampleAddr;
    /**
     * 预览样表地址
     */
    private String materialSampleAddrYl;

    /**
     * 审核类型
     */
    private String  auditType;

    /**
    *审核结果状态
    */
    private String  resultStatus;
    /**
    *确认状态
    */
    private String  confirmStatus;

   /**
   * 证照名称
   */
    private String elecLicenName;

    private String elecLicenNumber;

    private Date modifyDate;

    private Short mustFlag;
    private String elecBillOid;

    private String electronicResult;

    private String materialType;

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getElecBillOid() {
        return elecBillOid;
    }
    public void setElecBillOid(String elecBillOid) {
        this.elecBillOid = elecBillOid == null ? null : elecBillOid.trim();
    }

    public String getElectronicResult() {
        return electronicResult;
    }

    public void setElectronicResult(String electronicResult) {
        this.electronicResult = electronicResult;
    }

    public Short getMustFlag() {
        return mustFlag;
    }

    public void setMustFlag(Short mustFlag) {
        this.mustFlag = mustFlag;
    }


    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getElecLicenName() {
        return elecLicenName;
    }

    public void setElecLicenName(String elecLicenName) {
        this.elecLicenName = elecLicenName == null ? null : elecLicenName.trim();
    }

    public String getElecLicenNumber() {
        return elecLicenNumber;
    }

    public void setElecLicenNumber(String elecLicenNumber) {
        this.elecLicenNumber = elecLicenNumber == null ? null : elecLicenNumber.trim();
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus == null ? null : resultStatus.trim();
    }


    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus == null ? null : confirmStatus.trim();
    }



    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType == null ? null : auditType.trim();
    }


    public String getMaterialSampleAddr() {
        return materialSampleAddr;
    }

    public void setMaterialSampleAddr(String materialSampleAddr) {
        this.materialSampleAddr = materialSampleAddr == null ? null : materialSampleAddr.trim();
    }


    public String getMaterialSampleAddrYl() {
        return materialSampleAddrYl;
    }

    public void setMaterialSampleAddrYl(String materialSampleAddrYl) {
        this.materialSampleAddrYl = materialSampleAddrYl == null ? null : materialSampleAddrYl.trim();
    }

    public String getBaiduTemplateIds() {
        return baiduTemplateIds;
    }

    public void setBaiduTemplateIds(String baiduTemplateIds) {
        this.baiduTemplateIds = baiduTemplateIds == null ? null : baiduTemplateIds.trim();
    }

    public String getMaterialCatalogOid() {
        return materialCatalogOid;
    }

    public void setMaterialCatalogOid(String materialCatalogOid) {
        this.materialCatalogOid = materialCatalogOid == null ? null : materialCatalogOid.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseMaterialOid() {
        return caseMaterialOid;
    }

    public void setCaseMaterialOid(String caseMaterialOid) {
        this.caseMaterialOid = caseMaterialOid;
    }

    public String getMaterialOid() {
        return materialOid;
    }

    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public Integer getCollectionFlag() {
        return collectionFlag;
    }

    public void setCollectionFlag(Integer collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public Integer getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(Integer collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
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

    public String getCreateUserOid() {
        return createUserOid;
    }

    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid;
    }

    public String getElemLicenseOid() {
        return elemLicenseOid;
    }

    public void setElemLicenseOid(String elemLicenseOid) {
        this.elemLicenseOid = elemLicenseOid;
    }

    public String getElemNumber() {
        return elemNumber;
    }

    public void setElemNumber(String elemNumber) {
        this.elemNumber = elemNumber;
    }

}