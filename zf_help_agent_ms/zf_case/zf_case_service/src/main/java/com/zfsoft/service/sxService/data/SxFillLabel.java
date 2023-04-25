package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName SxFillLabel
 * @Description 表单字段标签实体
 **/
@Data
@ToString
public class SxFillLabel {


    private Long id;


    private String labelOid;


    private String labelName;


    private String labelCode;


    private Integer sort;


    private String typeOid;

    private String typeName;


    private String serviceOid;

    private String serviceName;


    private Integer delFlag;


    private Date createDate;


    private Date modifyDate;

    private Integer isMovingFlag;

}
