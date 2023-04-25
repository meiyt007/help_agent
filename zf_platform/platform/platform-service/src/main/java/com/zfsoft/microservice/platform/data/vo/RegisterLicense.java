package com.zfsoft.microservice.platform.data.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @description:  注册文件
 * @author: wuxx
 * @Date: 2020/11/3 10:43
 **/
@Data
public class RegisterLicense implements Serializable {

    private static final long serialVersionUID = 553380243490605784L;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 警告日期
     */
    private Date warnDate;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 授权到期日期
     */
    private Date expireDate;
    /**
     * 检测code
     */
    private String checkCode;
    /**
     * 注册编码
     */
    private String licenseCode;
    /**
     * 注册授权文件地址
     */
    private String attaOid;
    /**
     * 申请oid
     */
    private String applyOid;

}
