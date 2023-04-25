package com.zfsoft.single.dbaccess.data.ywbl;

import java.io.Serializable;
import java.util.Date;
/**
 * 办件收费表实体类
 *
 * @author wangwg
 * @since 2021-06-10
 */
public class DbSfCaseCharge {

    private String caseChargeOid;

    private String caseOid;

    private String caseNumber;

    private String applyUserName;


    private String paymentOrder;
    /**
     * 总金额
     */
    private Double sumMoney;
    /**
     * 减免总金额
     */
    private Double discountMoney;

    private String chargeUserOid;
    /**
     * 收费时间
     */
    private Date chargeTime;
    /**
     * 收费标识0未生成单号 1已生成单号 2已确认收费
     */
    private String chargeFlag;

    private String createUserOid;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 实收金额
     */
    private Double paidAmount;

    private String orderAttaOid;
    /**
     * 付款方式
     */
    private String payType;
    /**
     * 修改时间
     */
    private Date modifyDate;


    public String getCaseChargeOid() {
        return caseChargeOid;
    }

    public void setCaseChargeOid(String caseChargeOid) {
        this.caseChargeOid = caseChargeOid;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public String getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(String paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public Double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getChargeUserOid() {
        return chargeUserOid;
    }

    public void setChargeUserOid(String chargeUserOid) {
        this.chargeUserOid = chargeUserOid;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public String getCreateUserOid() {
        return createUserOid;
    }

    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getOrderAttaOid() {
        return orderAttaOid;
    }

    public void setOrderAttaOid(String orderAttaOid) {
        this.orderAttaOid = orderAttaOid;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }
}
