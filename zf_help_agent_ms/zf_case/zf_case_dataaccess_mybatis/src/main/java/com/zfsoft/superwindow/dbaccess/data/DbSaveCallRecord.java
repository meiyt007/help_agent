package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;
import java.io.Serializable;

/**
 * (DbSaveCallRecord)实体类
 *
 * @author makejava
 * @since 2021-08-16 11:38:01
 */
public class DbSaveCallRecord {

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String oid;
    /**
     * 叫号信息
     */
    private String callInfo;
    /**
     * 叫号号码
     */
    private String callNum;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 时长秒值
     */
    private Long timeLength;
    /**
     * 登陆人主键
     */
    private String createBy;
    /**
     * 创建此记录时间
     */
    private Date createTime;
    /**
     * 虚拟业务记录表主键
     */
    private String virtualBusinessOid;
    /**
     * 是否删除 0 -- 否 1 -- 是
     */
    private Integer isDelete;
    /**
     * 鍔炰簨浜哄憳鍚嶅瓧
     */
    private String caseUserName;

    //证件号
    private String  cardNo;

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

    public String getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(String callInfo) {
        this.callInfo = callInfo;
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Long timeLength) {
        this.timeLength = timeLength;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVirtualBusinessOid() {
        return virtualBusinessOid;
    }

    public void setVirtualBusinessOid(String virtualBusinessOid) {
        this.virtualBusinessOid = virtualBusinessOid;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
