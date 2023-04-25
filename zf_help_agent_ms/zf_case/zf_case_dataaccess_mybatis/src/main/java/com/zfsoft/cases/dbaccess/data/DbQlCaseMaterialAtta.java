package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 材料附件表(QlCaseMaterialAtta)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlCaseMaterialAtta implements Serializable {
    private static final long serialVersionUID = -37282222983864852L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String materialAttaOid;
    /**
     * 所属办件材料
     */
    private String caseMaterialOid;

    public String getTemplatePdfUrl() {
        return templatePdfUrl;
    }

    public void setTemplatePdfUrl(String templatePdfUrl) {
        this.templatePdfUrl = templatePdfUrl;
    }

    /**
     * 存储位置
     */
    private String attaOid;
    /**
     * 创建时间
     */
    private Date createDate;

    private String materialCatalogOid;

    /**
     * 精细化材料oid
     */
    private  String refinedMaterialOid;

    /**
     * 材料去黑边修改附件地址
     */
    private  String modifyBeforeAttaOid;

    private Date modifyDate;

    private Integer serialNumber;

    private String signaturePdfUrl;

    private String templatePdfUrl;

    // 智能生成材料类型：1-签章，2-上传
    private Integer autoType;

    public Integer getAutoType() {
        return autoType;
    }

    public void setAutoType(Integer autoType) {
        this.autoType = autoType;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBeforeAttaOid() {
        return modifyBeforeAttaOid;
    }

    public void setModifyBeforeAttaOid(String modifyBeforeAttaOid) {
        this.modifyBeforeAttaOid = modifyBeforeAttaOid;
    }

    public String getRefinedMaterialOid() {
        return refinedMaterialOid;
    }

    public void setRefinedMaterialOid(String refinedMaterialOid) {
        this.refinedMaterialOid = refinedMaterialOid;
    }

    public String getSignaturePdfUrl() {
        return signaturePdfUrl;
    }

    public void setSignaturePdfUrl(String signaturePdfUrl) {
        this.signaturePdfUrl = signaturePdfUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialAttaOid() {
        return materialAttaOid;
    }

    public void setMaterialAttaOid(String materialAttaOid) {
        this.materialAttaOid = materialAttaOid;
    }

    public String getCaseMaterialOid() {
        return caseMaterialOid;
    }

    public void setCaseMaterialOid(String caseMaterialOid) {
        this.caseMaterialOid = caseMaterialOid;
    }

    public String getAttaOid() {
        return attaOid;
    }

    public void setAttaOid(String attaOid) {
        this.attaOid = attaOid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMaterialCatalogOid() {
        return materialCatalogOid;
    }

    public void setMaterialCatalogOid(String materialCatalogOid) {
        this.materialCatalogOid = materialCatalogOid;
    }
}