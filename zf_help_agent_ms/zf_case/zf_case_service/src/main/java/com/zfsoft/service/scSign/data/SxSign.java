package com.zfsoft.service.scSign.data;

import java.util.Date;

/**
 * @Author ChangSheng
 * @Date 14:12 2022/6/16
 * @Description 签名实体类
 **/
public class SxSign {
    //id
    private Long id;

    //角色名
    private String roleName;

    //是否多人
    private String manyPeople;

    //签章类型
    private String type;

    //排序
    private int sort;

    //材料oid
    private String materialOid;

    //短信内容
    private String message;

    //是否删除
    private String deleteStatus;

    //创建时间
    private Date createDate;

    //修改时间
    private Date modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getManyPeople() {
        return manyPeople;
    }

    public void setManyPeople(String manyPeople) {
        this.manyPeople = manyPeople;
    }

    public String getMaterialOid() {
        return materialOid;
    }

    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
