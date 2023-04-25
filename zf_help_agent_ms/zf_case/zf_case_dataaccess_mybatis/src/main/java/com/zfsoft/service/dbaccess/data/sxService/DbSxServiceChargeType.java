package com.zfsoft.service.dbaccess.data.sxService;

import java.io.Serializable;
import java.util.Date;

/**
 * 收费种类表实体类
 *
 * @author wangwg
 * @since 2021-06-10
 */
public class DbSxServiceChargeType {

    private String chargeTypeOid;

    private String type;

    private String unit;
    /**
     * 禁用状态
     */
    private String enabledFlag;
    /**
     * 删除状态
     */
    private String delFlag;
    /**
     * 创建时间
     */
    private Date createDate;

    private String createUser;
    /**
     * 分段状态 0是 1 否
     */
    private String subFlag;
    /**
     * 计费方式 0单价 1费率
     */
    private String chargeWay;
    /**
     * 浮动状态 0是 1否
     */
    private String floatFlag;
    /**
     * 修改时间
     */
    private Date modifyDate;


    public String getChargeTypeOid() {
        return chargeTypeOid;
    }

    public void setChargeTypeOid(String chargeTypeOid) {
        this.chargeTypeOid = chargeTypeOid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSubFlag() {
        return subFlag;
    }

    public void setSubFlag(String subFlag) {
        this.subFlag = subFlag;
    }

    public String getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(String chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getFloatFlag() {
        return floatFlag;
    }

    public void setFloatFlag(String floatFlag) {
        this.floatFlag = floatFlag;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

}
