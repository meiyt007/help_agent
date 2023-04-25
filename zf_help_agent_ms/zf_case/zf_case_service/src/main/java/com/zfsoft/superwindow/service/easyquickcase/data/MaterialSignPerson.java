package com.zfsoft.superwindow.service.easyquickcase.data;

import com.zfsoft.superwindow.data.yxpz.PbpjManage;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author ChangSheng
 * @Date 11:13 2022/6/20
 * @Description 签章配置
 **/
public class MaterialSignPerson {
    //id
    @NotNull(message = "id不能为空",groups = {PbpjManage.UPDATE_GROUP.class})
    private Long id;

    //主表oid
    private String signOid;

    //签章流程id
    private String flowId;

    //办件oid
    private String caseOid;

    //材料oid
    private String materialOid;

    //签章角色oid
    private String roleId;

    //签章角色名
    private String roleName;

    //签名人姓名
    private String name;

    //签名人手机号
    private String phone;

    //企业名称
    private String companyName;

    //统一社会信用代码
    private String socialCode;

    //签章地址
    private String signatureUrl;

    //短信通知状态
    private String mailStatus;

    //是否删除
    private String deleteStatus;

    //创建时间
    private Date createDate;

    //修改时间
    private Date modifyDate;


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialOid() {
        return materialOid;
    }

    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
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
}
