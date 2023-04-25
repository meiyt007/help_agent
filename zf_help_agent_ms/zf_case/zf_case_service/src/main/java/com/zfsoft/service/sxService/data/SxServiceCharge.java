package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实施清单-收费信息
 */
@Data
@ToString
public class SxServiceCharge {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String chargeOid;

    /**
     * 所属收费种类
     */
    private String chargeTypeOid;

    /**
     * 所属事项
     */
    private String serviceOid;
    /**
     * 收费项目名称
     */
    private String chargeName;

    /**
     * 收费标准
     */
    private String chargeStandard;

    /**
     * 减免说明
     */
    private String discountDescription;

    /**
     * 必须收费状态
     */
    private Short mustChargeFlag;

    /**
     * 是否删除(0否、1是)
     */
    private Short delFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 单价/费率参数
     */
    private BigDecimal priceParam;

    /**
     * 最大单价/费率参数
     */
    private BigDecimal maxPriceParam;

    /**
     * 支付方式
     */
    private Short payType;

    /**
     * 转账账号
     */
    private String transferAccount;

    /**
     * 刷卡支持银行
     */
    private String supportBank;

    /**
     * 缴款银行
     */
    private String payBank;

    /**
     * 网上支付方式
     */
    private String webpayType;

    /**
     * 支付宝账号
     */
    private String alipayNumber;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 执收项目编码
     */
    private String receiptProjectCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否允许减免
     */
    private Short allowableRelief;

    /**
     * 收费依据
     */
    private String chargeAccord;

    private List<SxServiceChargeParam> chargeParamList = new ArrayList<SxServiceChargeParam>();

    private SxServiceChargeType chargeType = new SxServiceChargeType();

}
