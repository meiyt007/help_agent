package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 实施清单-收费分段参数配置
 */
@Data
@ToString
public class SxServiceChargeParam {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String paramOid;

    /**
     * 所属事项收费项目
     */
    private String chargeOid;

    /**
     * 分割区间参数始
     */
    private BigDecimal subParam;

    /**
     * 分割区间参数止
     */
    private BigDecimal maxSubParam;

    /**
     * 单价/费率参数值
     */
    private BigDecimal priceParam;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;
}