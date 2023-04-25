package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ElectronicLicense {
    //id
    private Long id;
    //业务主键
    private String oid;
    //证照oid
    private String electronicLicenseOid;
    //证照编码
    private String electronicLicenseCode;
    //证照名称
    private String electronicLicenseName;
    //删除标识
    private Integer deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;

    //类型
    private String type;

    private Integer pageNum;

    private Integer pageSize;

    //目录元素
    private List<ElectronicLicenseElement> subList;

}