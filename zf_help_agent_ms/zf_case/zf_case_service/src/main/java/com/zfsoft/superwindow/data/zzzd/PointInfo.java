package com.zfsoft.superwindow.data.zzzd;

import com.zfsoft.superwindow.data.yxpz.PbpjManage;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author ChangSheng
 * @Date 17:47 2022/5/27
 * @Description 点位信息实体类
 **/
public class PointInfo {
    @NotNull(message = "id不能为空",groups = {PbpjManage.UPDATE_GROUP.class})
    private Long id;

    //点位名称
    private String name;

    //经纬度
    private String lonLat;

    //所属区划
    private String districtOid;

    //地址
    private String address;

    //创建时间
    private Date createDate;

    //修改时间
    private Date modifyDate;

    //删除状态
    private String deleteStatus;

    //区划名称
    private String districtName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLonLat() {
        return lonLat;
    }

    public void setLonLat(String lonLat) {
        this.lonLat = lonLat;
    }

    public String getDistrictOid() {
        return districtOid;
    }

    public void setDistrictOid(String districtOid) {
        this.districtOid = districtOid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
