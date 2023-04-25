package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

/**
 * 办件签名记录实体类
 *
 * @author wangwg
 * @date 2021-08-16
 */
public class DbCaseSignRecord {

    private Long id;
    /**
     * 签名主键
     */
    private String signOid;
    /**
     * 办件主键
     */
    private String caseOid;
    /**
     * 申请人证件号
     */
    private String applyCarno;
    /**
     * 签名路径
     */
    private String signUrl;
    /**
     * 签名时间
     */
    private Date createDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignOid() {
        return signOid;
    }

    public void setSignOid(String signOid) {
        this.signOid = signOid;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public String getApplyCarno() {
        return applyCarno;
    }

    public void setApplyCarno(String applyCarno) {
        this.applyCarno = applyCarno;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
