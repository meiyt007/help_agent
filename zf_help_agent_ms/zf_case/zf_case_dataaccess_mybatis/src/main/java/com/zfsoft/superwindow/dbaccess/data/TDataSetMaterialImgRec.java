package com.zfsoft.superwindow.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/21 13:46
 */
public class TDataSetMaterialImgRec implements Serializable {
    private static final long serialVersionUID = -91849781682000887L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 事项id
     */
    private String serviceId;
    /**
     * 细化材料id
     */
    private String refinedMaterialId;
    /**
     * 证照oid
     */
    private String licenceOid;
    /**
     * 证照名称
     */
    private String licenceName;
    /**
     * 附件地址（文件平台）
     */
    private String attachmentAddress;
    /**
     * 是否空表
     */
    private Integer isEmpty;
    /**
     * 上传时间
     */
    private Date loadTime;
    /**
     * 创建人id
     */
    private String createBy;
    /**
     * 事项材料id
     */
    private String materialId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRefinedMaterialId() {
        return refinedMaterialId;
    }

    public void setRefinedMaterialId(String refinedMaterialId) {
        this.refinedMaterialId = refinedMaterialId;
    }

    public String getLicenceOid() {
        return licenceOid;
    }

    public void setLicenceOid(String licenceOid) {
        this.licenceOid = licenceOid;
    }

    public String getLicenceName() {
        return licenceName;
    }

    public void setLicenceName(String licenceName) {
        this.licenceName = licenceName;
    }

    public String getAttachmentAddress() {
        return attachmentAddress;
    }

    public void setAttachmentAddress(String attachmentAddress) {
        this.attachmentAddress = attachmentAddress;
    }

    public Integer getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Integer isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
