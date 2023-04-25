package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 办件审批环节信息(QlCaseLinkResult)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlCaseLinkResult implements Serializable {
    private static final long serialVersionUID = 801948385598217725L;

    private Long id;

    private String linkResultOid;

    private String caseOid;

    private String personOid;

    private String personName;

    private String linkCode;

    private String linkName;

    private Integer finalStatus;

    private String finalOpinion;

    private String finalOpinionDesc;

    private Date finalDate;

    private Date createDate;

    private String attaOid;

    private Date modifyDate;

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkResultOid() {
        return linkResultOid;
    }

    public void setLinkResultOid(String linkResultOid) {
        this.linkResultOid = linkResultOid;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public String getPersonOid() {
        return personOid;
    }

    public void setPersonOid(String personOid) {
        this.personOid = personOid;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Integer getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(Integer finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getFinalOpinion() {
        return finalOpinion;
    }

    public void setFinalOpinion(String finalOpinion) {
        this.finalOpinion = finalOpinion;
    }

    public String getFinalOpinionDesc() {
        return finalOpinionDesc;
    }

    public void setFinalOpinionDesc(String finalOpinionDesc) {
        this.finalOpinionDesc = finalOpinionDesc;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAttaOid() {
        return attaOid;
    }

    public void setAttaOid(String attaOid) {
        this.attaOid = attaOid;
    }
}