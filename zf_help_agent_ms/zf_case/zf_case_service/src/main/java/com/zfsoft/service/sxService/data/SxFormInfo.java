package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ClassName SxFormInfo
 * @Author xiayj
 * @Date 2021/7/21 15:30
 **/
@Data
@ToString
public class SxFormInfo {


    private Long id;

    private String formOid;

    private String formName;

    private String serviceOid;

    private String serviceName;

    private String fieldTypeOid;

    private String fieldTypeName;

    private Integer status;

    private String authorizeKey;

    private String formCode;

    private Integer delFlag;

    private Integer isAble;

    private Date createDate;

    private Date modifyDate;

    private String designOid;

    private String formMainOid;

    private String childFormName;

    private Integer sort;

    private String logicFormName;

    private String relationObjectBusinessId;

    private List<SxFillField> fieldList;

}
