package com.zfsoft.service.scSign.data;


import java.util.List;

/**
 * @Author ChangSheng
 * @Date 9:57 2022/6/17
 * @Description 为了页面传输
 **/
public class SxSignDto {
    private String materialOid;

    private List<SxSign> signList;

    public String getMaterialOid() {
        return materialOid;
    }

    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid;
    }

    public List<SxSign> getSignList() {
        return signList;
    }

    public void setSignList(List<SxSign> signList) {
        this.signList = signList;
    }
}
