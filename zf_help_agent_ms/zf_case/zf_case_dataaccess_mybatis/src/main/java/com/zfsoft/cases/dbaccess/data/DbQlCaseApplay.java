package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权利办件申请信息(QlCaseApplay)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbQlCaseApplay implements Serializable {
    private static final long serialVersionUID = 181606430928439781L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 办件主键
     */
    private String caseOid;
    /**
     * 申请数量
     */
    private Integer applyNumber;
    /**
     * 申请数量限制
     */
    private String applyNumberLimit;
    /**
     * 申请人名称
     */
    private String applyUserName;
    /**
     * 申请人类型
     */
    private String applyUserType;
    /**
     * 证件类型
     */
    private String credentialType;
    /**
     * 证件号码
     */
    private String credentialNumber;
    /**
     * 申请人地址
     */
    private String applyUserAddress;
    /**
     * 申请人电话
     */
    private String applyUserTel;
    /**
     * 申请人手机
     */
    private String applyUserPhone;
    /**
     * 邮政编码
     */
    private String applyPostCode;
    /**
     * 法定代表人
     */
    private String legalPersonName;
    /**
     * 收件人姓名
     */
    private String addresseeName;
    /**
     * 收件人邮编
     */
    private String addresseePostCode;
    /**
     * 收件人手机
     */
    private String addresseePhone;
    /**
     * 收件人电话
     */
    private String addresseeTel;
    /**
     * 收件人地址
     */
    private String addresseeAddress;

    /**
     * 收件人地址选择
     */
    private String chooseAddress;
    /**
     * 收件人详细地址
     */
    private String addresseeDetailAddress;
    /**
     * 联系人名称
     */
    private String contactUserName;
    /**
     * 联系人身份证号码
     */
    private String contactCredentialNumber;
    /**
     * 联系人手机
     */
    private String contactUserPhone;
    /**
     * 联系人电话
     */
    private String contactUserTel;
    /**
     * 联系人邮件
     */
    private String contactEmail;
    /**
     * 联系人备注
     */
    private String contactRemark;
    /**
     * 所属业务管辖地
     */
    private String bussVenueDistrictOid;
    /**
     * 受理具体地点
     */
    private String specificLocation;
    /**
     * 创建时间
     */
    private Date createDate;

    private Date modifyDate;

    private String netUserId;

    public String getNetUserId() {
        return netUserId;
    }

    public void setNetUserId(String netUserId) {
        this.netUserId = netUserId;
    }

    public String getApplyNumberLimit() {
        return applyNumberLimit;
    }

    public void setApplyNumberLimit(String applyNumberLimit) {
        this.applyNumberLimit = applyNumberLimit;
    }

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

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public Integer getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(Integer applyNumber) {
        this.applyNumber = applyNumber;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserType() {
        return applyUserType;
    }

    public void setApplyUserType(String applyUserType) {
        this.applyUserType = applyUserType;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getApplyUserTel() {
        return applyUserTel;
    }

    public void setApplyUserTel(String applyUserTel) {
        this.applyUserTel = applyUserTel;
    }

    public String getApplyUserPhone() {
        return applyUserPhone;
    }

    public void setApplyUserPhone(String applyUserPhone) {
        this.applyUserPhone = applyUserPhone;
    }

    public String getApplyPostCode() {
        return applyPostCode;
    }

    public void setApplyPostCode(String applyPostCode) {
        this.applyPostCode = applyPostCode;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getAddresseePostCode() {
        return addresseePostCode;
    }

    public void setAddresseePostCode(String addresseePostCode) {
        this.addresseePostCode = addresseePostCode;
    }

    public String getAddresseePhone() {
        return addresseePhone;
    }

    public void setAddresseePhone(String addresseePhone) {
        this.addresseePhone = addresseePhone;
    }

    public String getAddresseeTel() {
        return addresseeTel;
    }

    public void setAddresseeTel(String addresseeTel) {
        this.addresseeTel = addresseeTel;
    }

    public String getAddresseeAddress() {
        return addresseeAddress;
    }

    public void setAddresseeAddress(String addresseeAddress) {
        this.addresseeAddress = addresseeAddress;
    }

    public String getAddresseeDetailAddress() {
        return addresseeDetailAddress;
    }

    public void setAddresseeDetailAddress(String addresseeDetailAddress) {
        this.addresseeDetailAddress = addresseeDetailAddress;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public String getContactCredentialNumber() {
        return contactCredentialNumber;
    }

    public void setContactCredentialNumber(String contactCredentialNumber) {
        this.contactCredentialNumber = contactCredentialNumber;
    }

    public String getContactUserPhone() {
        return contactUserPhone;
    }

    public void setContactUserPhone(String contactUserPhone) {
        this.contactUserPhone = contactUserPhone;
    }

    public String getContactUserTel() {
        return contactUserTel;
    }

    public void setContactUserTel(String contactUserTel) {
        this.contactUserTel = contactUserTel;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactRemark() {
        return contactRemark;
    }

    public void setContactRemark(String contactRemark) {
        this.contactRemark = contactRemark;
    }

    public String getBussVenueDistrictOid() {
        return bussVenueDistrictOid;
    }

    public void setBussVenueDistrictOid(String bussVenueDistrictOid) {
        this.bussVenueDistrictOid = bussVenueDistrictOid;
    }

    public String getSpecificLocation() {
        return specificLocation;
    }

    public void setSpecificLocation(String specificLocation) {
        this.specificLocation = specificLocation;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getApplyUserAddress() {
        return applyUserAddress;
    }

    public void setApplyUserAddress(String applyUserAddress) {
        this.applyUserAddress = applyUserAddress;
    }

    public String getChooseAddress() {
        return chooseAddress;
    }

    public void setChooseAddress(String chooseAddress) {
        this.chooseAddress = chooseAddress;
    }
}