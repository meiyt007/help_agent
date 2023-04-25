package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 收费种类表实体类
 *
 * @author wangwg
 * @since 2021-06-10
 */
@Data
@ToString
public class SxServiceChargeType {

    private String chargeTypeOid;

    private String type;

    private String unit;
    /**
     * 禁用状态
     */
    private String enabledFlag;
    /**
     * 删除状态
     */
    private String delFlag;
    /**
     * 创建时间
     */
    private Date createDate;

    private String createUser;
    /**
     * 分段状态 0是 1 否
     */
    private String subFlag;
    /**
     * 计费方式 0单价 1费率
     */
    private String chargeWay;
    /**
     * 浮动状态 0是 1否
     */
    private String floatFlag;
    /**
     * 修改时间
     */
    private Date modifyDate;

}
