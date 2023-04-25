package com.zfsoft.service.dbaccess.data.sxService;

import java.util.Date;

public class DbRefinedMaterial {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.OID
     *
     * @mbggenerated
     */
    private String oid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_OID
     *
     * @mbggenerated
     */
    private String materialOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.REFINED_MATERIAL_NAME
     *
     * @mbggenerated
     */
    private String refinedMaterialName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.NEED_STATUS
     *
     * @mbggenerated
     */
    private Short needStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_SOURCE
     *
     * @mbggenerated
     */
    private String materialSource;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.REVIEW_POINTS_OID
     *
     * @mbggenerated
     */
    private String reviewPointsOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_TYPE
     *
     * @mbggenerated
     */
    private Short materialType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_SAMPLE_OID
     *
     * @mbggenerated
     */
    private String materialSampleOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_SAMPLE_ADDR
     *
     * @mbggenerated
     */
    private String materialSampleAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.SERIAL_NUMBER
     *
     * @mbggenerated
     */
    private Long serialNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.BAIDU_TEMPLATE_IDS
     *
     * @mbggenerated
     */
    private String baiduTemplateIds;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    private String materialCatalogOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.DELETE_STATUS
     *
     * @mbggenerated
     */
    private Short deleteStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.SERVICE_OID
     *
     * @mbggenerated
     */
    private String serviceOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.LICENCE_OID
     *
     * @mbggenerated
     */
    private String licenceOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_refined_material.LICENCE_NAME
     *
     * @mbggenerated
     */
    private String licenceName;


    //关联一件事目录oid
    private String comboDirectoryOid;
    public String getComboDirectoryOid() {
        return comboDirectoryOid;
    }

    public void setComboDirectoryOid(String comboDirectoryOid) {
        this.comboDirectoryOid = comboDirectoryOid == null ? null : comboDirectoryOid.trim();
    }



    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.ID
     *
     * @return the value of t_refined_material.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.ID
     *
     * @param id the value for t_refined_material.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.OID
     *
     * @return the value of t_refined_material.OID
     *
     * @mbggenerated
     */
    public String getOid() {
        return oid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.OID
     *
     * @param oid the value for t_refined_material.OID
     *
     * @mbggenerated
     */
    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_OID
     *
     * @return the value of t_refined_material.MATERIAL_OID
     *
     * @mbggenerated
     */
    public String getMaterialOid() {
        return materialOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_OID
     *
     * @param materialOid the value for t_refined_material.MATERIAL_OID
     *
     * @mbggenerated
     */
    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid == null ? null : materialOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.REFINED_MATERIAL_NAME
     *
     * @return the value of t_refined_material.REFINED_MATERIAL_NAME
     *
     * @mbggenerated
     */
    public String getRefinedMaterialName() {
        return refinedMaterialName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.REFINED_MATERIAL_NAME
     *
     * @param refinedMaterialName the value for t_refined_material.REFINED_MATERIAL_NAME
     *
     * @mbggenerated
     */
    public void setRefinedMaterialName(String refinedMaterialName) {
        this.refinedMaterialName = refinedMaterialName == null ? null : refinedMaterialName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.NEED_STATUS
     *
     * @return the value of t_refined_material.NEED_STATUS
     *
     * @mbggenerated
     */
    public Short getNeedStatus() {
        return needStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.NEED_STATUS
     *
     * @param needStatus the value for t_refined_material.NEED_STATUS
     *
     * @mbggenerated
     */
    public void setNeedStatus(Short needStatus) {
        this.needStatus = needStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_SOURCE
     *
     * @return the value of t_refined_material.MATERIAL_SOURCE
     *
     * @mbggenerated
     */
    public String getMaterialSource() {
        return materialSource;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_SOURCE
     *
     * @param materialSource the value for t_refined_material.MATERIAL_SOURCE
     *
     * @mbggenerated
     */
    public void setMaterialSource(String materialSource) {
        this.materialSource = materialSource == null ? null : materialSource.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.REVIEW_POINTS_OID
     *
     * @return the value of t_refined_material.REVIEW_POINTS_OID
     *
     * @mbggenerated
     */
    public String getReviewPointsOid() {
        return reviewPointsOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.REVIEW_POINTS_OID
     *
     * @param reviewPointsOid the value for t_refined_material.REVIEW_POINTS_OID
     *
     * @mbggenerated
     */
    public void setReviewPointsOid(String reviewPointsOid) {
        this.reviewPointsOid = reviewPointsOid == null ? null : reviewPointsOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_TYPE
     *
     * @return the value of t_refined_material.MATERIAL_TYPE
     *
     * @mbggenerated
     */
    public Short getMaterialType() {
        return materialType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_TYPE
     *
     * @param materialType the value for t_refined_material.MATERIAL_TYPE
     *
     * @mbggenerated
     */
    public void setMaterialType(Short materialType) {
        this.materialType = materialType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_SAMPLE_OID
     *
     * @return the value of t_refined_material.MATERIAL_SAMPLE_OID
     *
     * @mbggenerated
     */
    public String getMaterialSampleOid() {
        return materialSampleOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_SAMPLE_OID
     *
     * @param materialSampleOid the value for t_refined_material.MATERIAL_SAMPLE_OID
     *
     * @mbggenerated
     */
    public void setMaterialSampleOid(String materialSampleOid) {
        this.materialSampleOid = materialSampleOid == null ? null : materialSampleOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_SAMPLE_ADDR
     *
     * @return the value of t_refined_material.MATERIAL_SAMPLE_ADDR
     *
     * @mbggenerated
     */
    public String getMaterialSampleAddr() {
        return materialSampleAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_SAMPLE_ADDR
     *
     * @param materialSampleAddr the value for t_refined_material.MATERIAL_SAMPLE_ADDR
     *
     * @mbggenerated
     */
    public void setMaterialSampleAddr(String materialSampleAddr) {
        this.materialSampleAddr = materialSampleAddr == null ? null : materialSampleAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.SERIAL_NUMBER
     *
     * @return the value of t_refined_material.SERIAL_NUMBER
     *
     * @mbggenerated
     */
    public Long getSerialNumber() {
        return serialNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.SERIAL_NUMBER
     *
     * @param serialNumber the value for t_refined_material.SERIAL_NUMBER
     *
     * @mbggenerated
     */
    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.BAIDU_TEMPLATE_IDS
     *
     * @return the value of t_refined_material.BAIDU_TEMPLATE_IDS
     *
     * @mbggenerated
     */
    public String getBaiduTemplateIds() {
        return baiduTemplateIds;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.BAIDU_TEMPLATE_IDS
     *
     * @param baiduTemplateIds the value for t_refined_material.BAIDU_TEMPLATE_IDS
     *
     * @mbggenerated
     */
    public void setBaiduTemplateIds(String baiduTemplateIds) {
        this.baiduTemplateIds = baiduTemplateIds == null ? null : baiduTemplateIds.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MATERIAL_CATALOG_OID
     *
     * @return the value of t_refined_material.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogOid() {
        return materialCatalogOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MATERIAL_CATALOG_OID
     *
     * @param materialCatalogOid the value for t_refined_material.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogOid(String materialCatalogOid) {
        this.materialCatalogOid = materialCatalogOid == null ? null : materialCatalogOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.CREATE_DATE
     *
     * @return the value of t_refined_material.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.CREATE_DATE
     *
     * @param createDate the value for t_refined_material.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.MODIFY_DATE
     *
     * @return the value of t_refined_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.MODIFY_DATE
     *
     * @param modifyDate the value for t_refined_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.DELETE_STATUS
     *
     * @return the value of t_refined_material.DELETE_STATUS
     *
     * @mbggenerated
     */
    public Short getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.DELETE_STATUS
     *
     * @param deleteStatus the value for t_refined_material.DELETE_STATUS
     *
     * @mbggenerated
     */
    public void setDeleteStatus(Short deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.SERVICE_OID
     *
     * @return the value of t_refined_material.SERVICE_OID
     *
     * @mbggenerated
     */
    public String getServiceOid() {
        return serviceOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.SERVICE_OID
     *
     * @param serviceOid the value for t_refined_material.SERVICE_OID
     *
     * @mbggenerated
     */
    public void setServiceOid(String serviceOid) {
        this.serviceOid = serviceOid == null ? null : serviceOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.LICENCE_OID
     *
     * @return the value of t_refined_material.LICENCE_OID
     *
     * @mbggenerated
     */
    public String getLicenceOid() {
        return licenceOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.LICENCE_OID
     *
     * @param licenceOid the value for t_refined_material.LICENCE_OID
     *
     * @mbggenerated
     */
    public void setLicenceOid(String licenceOid) {
        this.licenceOid = licenceOid == null ? null : licenceOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_refined_material.LICENCE_NAME
     *
     * @return the value of t_refined_material.LICENCE_NAME
     *
     * @mbggenerated
     */
    public String getLicenceName() {
        return licenceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_refined_material.LICENCE_NAME
     *
     * @param licenceName the value for t_refined_material.LICENCE_NAME
     *
     * @mbggenerated
     */
    public void setLicenceName(String licenceName) {
        this.licenceName = licenceName == null ? null : licenceName.trim();
    }
}