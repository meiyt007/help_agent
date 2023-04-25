package com.zfsoft.cases.data.vo;

import lombok.Data;

/**
 * 办件信息
 * @ClassName CaseRegisterInfoVo
 * @Author WangKe
 * @Date 2022/06/10
 **/
@Data
public class CaseRegisterInfoVo {

    /**
     * 办件编号
     */
    private String caseNumber;

    /**
     * 项目名称
     */
    private String applyProjectName;

    /**
     * 申报事项的实施编码或业务项编码
     */
    private String serviceCode;

    /**
     * 办件所属区划编码
     */
    private String districtCode;

    /**
     * 办件所属机构统一社会信用代码
     */
    private String organCode;

    /**
     * 登记人主键
     */
    private String createUserOid;

    /**
     * 登记时间
     */
    private String createDate;

    /**
     * 办件类型
     */
    private String regCaseType;

    /**
     * 办件来源
     */
    private String caseResource;

    /**
     * 受理具体地点
     */
    private String specificLocation;

    /**
     * 申请数量
     */
    private String applyNumber;

    /**
     * 申请人类型
     */
    private String applyUserType;

    /**
     * 当申请人为自然人时填申请人名称，当申请人为法人类型时填法人名称
     */
    private String applicantName;

    /**
     * 申请人证件类型
     */
    private String applicantCard;

    /**
     * 证件号
     */
    private String applicantCardNumber;

    /**
     * 申请人住址
     */
    private String applicantAddr;

    /**
     * 法人代表，当申请人为法人类型时必填
     */
    private String legalPersonName;

    /**
     * 联系人手机
     */
    private String applicantPhone;

    /**
     * 通讯地址
     */
    private String postalAddress;

    /**
     * 是否为代理人，0-是，1-否
     */
    private String useProxy;

    /**
     * 代理人名称，当为代理人是必填
     */
    private String agentName;

    /**
     * 代理人身份证号，当为代理人是必填
     */
    private String agentCardNumber;

    /**
     * 代理人手机，当为代理人是必填
     */
    private String agentPhone;

    /**
     * “一件事”对接联办编号（一件事对接时必填）
     */
    private String applyNo;

    /**
     * “一件事”对接办件流水号（一件事对接时必填）
     */
    private String caseNo;

    /**
     *  是否需要出库
     */
    private String outOfStockFlag = "Y";
}
