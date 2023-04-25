package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TVirtualBusinessRecord)实体类
 *
 * @author makejava
 * @since 2021-08-12 15:53:19
 */
public class DbVirtualBusinessRecord {

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String oid;
    /**
     * 登陆人主键
     */
    private String createBy;
    /**
     * 窗口信息
     */
    private String windowInfo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 办件信息id
     */
    private String handleOid;
    /**
     * 是否删除 0 -- 否 1 -- 是
     */
    private Integer isDelete;
    /**
     * 登陆人名字
     */
    private String createName;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 办事人员名字
     */
    private String caseUserName;
    /**
     * 录音地址
     */
    private String audioAddress;


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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getWindowInfo() {
        return windowInfo;
    }

    public void setWindowInfo(String windowInfo) {
        this.windowInfo = windowInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHandleOid() {
        return handleOid;
    }

    public void setHandleOid(String handleOid) {
        this.handleOid = handleOid;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
    }

    public String getAudioAddress() {
        return audioAddress;
    }

    public void setAudioAddress(String audioAddress) {
        this.audioAddress = audioAddress;
    }

}
