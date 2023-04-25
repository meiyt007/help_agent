package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ClassName RepeatField
 * @Author xiayj
 * @Date 2021/7/20 10:01
 **/
@Data
@ToString
public class RepeatFieldConfig {


    private Long id;

    private String configOid;

    private String masterField;

    private String slaveField;

    private Integer isAble;

    private Integer delFlag;

    private Date createDate;

    private Date modifyDate;

    private List<String> slaveFieldList;
}
