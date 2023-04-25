package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

/**
 * 办理时间表(TAveragePrescription)实体类
 *
 * @author makejava
 * @since 2021-07-16 11:07:53
 */
public class DbAveragePrescription {
    /**
     * 主键
     */
    private Long id;
    /**
     * 工作人员主键
     */
    private String userOid;
    /**
     * 服务时间
     */
    private Long processingTime;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 逻辑删除状态 0 未删除 1 删除
     */
    private Integer delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
