package com.zfsoft.superwindow.data.zzzd;

import com.zfsoft.superwindow.data.yxpz.PbpjManage;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author ChangSheng
 * @Date 15:59 2022/5/24
 * @Description 自助终端实体类
 **/
public class TerminalInfo {

    @NotNull(message = "id不能为空",groups = {PbpjManage.UPDATE_GROUP.class})
    private Long id;

    //终端编号
    private String terminalCode;

    //终端类型
    private String terminalType;

    //所属区划
    private String districtOid;

    //所属点位
    private String pointOid;

    //所属单位
    private String unit;

    //外设
    private String peripheral;

    //创建时间
    private Date createDate;

    //修改时间
    private Date modifyDate;

    //删除状态
    private String deleteStatus;

    //状态
    private String status;

    //区划名称
    private String districtName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getDistrictOid() {
        return districtOid;
    }

    public void setDistrictOid(String districtOid) {
        this.districtOid = districtOid;
    }

    public String getPointOid() {
        return pointOid;
    }

    public void setPointOid(String pointOid) {
        this.pointOid = pointOid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPeripheral() {
        return peripheral;
    }

    public void setPeripheral(String peripheral) {
        this.peripheral = peripheral;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
