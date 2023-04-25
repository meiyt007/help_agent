package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //提交办件对象
 * @Author: Wangyh
 * @Date: 2022/9/16 11:12
 */
@Data
@ToString
public class WorkUnionLogisticsVo {
    /**
     * 办件Id
     */
    private String applyId;

    /**
     * 办件提交类型（已收件、当场办结、当场退件）
     */
    private String applySubmitType;

    /**
     * 发证方式：自取，物流，无结果物
     */
     private String certWay;

    /**
     * 业务办理对象（公司、个人）
     */
    private String applicant;

    /**
     * 寄件人姓名（经办人）
     */
    private String receiver;
    /**
     * 申请人地址
     */
    private String revAddress;
    /**
     * 申请人地址所在（省、区、市）
     */
    private String revProvince;
    /**
     * 申请人地址所在（城市）
     */
    private String revCity;
    /**
     * 申请人地址所在（区）
     */
    private String revArea;
    /**
     * 申请人邮编
     */
    private String revZipcode;
    /**
     * 申请人电话
     */
    private String revPhone;
    /**
     * 运费支付方式，取值为在线支付、当面付款、货到付款、包邮、其他
     */
    private String feePayMethod;
    /**
     * 运费支付方
     */
    private String feePay;
    /**
     * 运费金额，单位为人民币元，免费为0
     */
    private String fee;
    /**
     * 运单号，承运单位（快递公司）提供的本地快递服务的快递单号，可用来查询快递进度
     */
    private String expressNo;
    /**
     * 服务类型，取值为标准快递、加急快递、其他
     */
    private String serviceType;
    /**
     * 配送范围，取值为上海同城、外地
     */
    private String expressArea;

    /**
     * 配送公司，取值为EMS、顺丰速递、其他
     */
    private String expressCompany;
    /**
     * 办件窗口受理开始时间，即窗口收件环节的开始时间
     */
    private Date startTime;

}
