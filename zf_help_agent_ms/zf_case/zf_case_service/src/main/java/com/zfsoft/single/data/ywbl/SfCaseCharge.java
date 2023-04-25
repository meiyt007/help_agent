package com.zfsoft.single.data.ywbl;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 办件收费表实体类
 *
 * @author wangwg
 * @since 2021-06-10
 */
@Data
@ToString
public class SfCaseCharge {

    private String caseChargeOid;

    private String caseOid;

    private String caseNumber;

    private String applyUserName;

    private String paymentOrder;
    /**
     * 总金额
     */
    private Double sumMoney;
    /**
     * 减免总金额
     */
    private Double discountMoney;

    private String chargeUserOid;
    /**
     * 收费时间
     */
    private Date chargeTime;
    /**
     * 收费标识0未生成单号 1已生成单号 2已确认收费
     */
    private String chargeFlag;

    private String createUserOid;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 实收金额
     */
    private Double paidAmount;

    private String orderAttaOid;
    /**
     * 付款方式
     */
    private String payType;
    /**
     * 修改时间
     */
    private Date modifyDate;

}
