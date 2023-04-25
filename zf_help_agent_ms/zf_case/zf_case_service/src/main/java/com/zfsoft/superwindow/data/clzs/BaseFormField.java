package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;

@Data
public class BaseFormField {
    //id
    private Long id;
    //业务主键
    private String oid;
    //字段编码
    private String fieldKey;
    //字段名称
    private String fieldName;
    //字段类型
    private String fieldType;
    //删除标识
    private String deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;

}