package com.zfsoft.ha.dbaccess.data;

import java.util.Date;

/**
 * 事项授权或帮代办人员授权db
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月03日 15:02:44
 */
public class DbHaUserService {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 工作人员编号;对应t_ha_help_work_user表主键
     */
    private Long workUserId;

    /**
     * 事项编号
     */
    private String serviceId;

    /**
     * 服务类型;CLZB-材料准备，SJ-收件
     */
    private String serviceType;

    /**
     * 服务状态;1-有权限，2-无权限
     */
    private String serviceStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkUserId() {
        return workUserId;
    }

    public void setWorkUserId(Long workUserId) {
        this.workUserId = workUserId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
