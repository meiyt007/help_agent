package com.zfsoft.single.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/22 13:20
 */
public class TDataSetZipRec implements Serializable {
    private static final long serialVersionUID = -86735999141907002L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 事项ids(事项id逗号隔开)
     */
    private String serviceIds;
    /**
     * zip文件地址
     */
    private String zipFileLocation;
    /**
     * 打包时间
     */
    private Date packingTime;
    /**
     * 发布时间
     */
    private Date publishTime;
    /**
     * 是否删除(0否，1是)
     */
    private Integer deleteStatus;
    /**
     * 打包人
     */
    private String createBy;
    /**
     * 是否发布(0否，1是)
     */
    private Integer isPublish;
    /**
     * 打包说明
     */
    private String publishExplain;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getZipFileLocation() {
        return zipFileLocation;
    }

    public void setZipFileLocation(String zipFileLocation) {
        this.zipFileLocation = zipFileLocation;
    }

    public Date getPackingTime() {
        return packingTime;
    }

    public void setPackingTime(Date packingTime) {
        this.packingTime = packingTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Integer isPublish) {
        this.isPublish = isPublish;
    }

    public String getPublishExplain() {
        return publishExplain;
    }

    public void setPublishExplain(String publishExplain) {
        this.publishExplain = publishExplain;
    }
}

