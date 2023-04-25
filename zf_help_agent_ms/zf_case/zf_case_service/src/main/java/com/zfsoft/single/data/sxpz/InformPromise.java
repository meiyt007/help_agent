package com.zfsoft.single.data.sxpz;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
@Data
@ToString
public class InformPromise {

    private Long id;

    /**
    业务主键
     */
    private String promiseOid;

    /**
     事项主键
     */
    private String serviceOid;

    /**
     事项名称
     */
    private String serviceName;

    /**
     实施编码
     */
    private String implementCode;
    private String organOid;

    /**
     机构名称
     */
    private String organName;

    private String serviceTypeOid;
    /**
     事项类型名称
     */
    private String serviceTypeName;
    private String districtOid;

    private String districtName;

    /**
     是否删除0否1是
     */
    private Integer delFlag;

    /**
     修改时间
     */
    private Date modifyDate;
}