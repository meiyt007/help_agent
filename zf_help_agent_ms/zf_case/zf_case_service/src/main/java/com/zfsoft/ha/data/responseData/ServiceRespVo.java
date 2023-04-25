package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，事项响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:07
 */
@Data
@ToString
public class ServiceRespVo {
    /**
     * 事项业务主键
     */
    private String serviceOid;
    /**
     * 区划名称
     */
    private String districtName;
    /**
     * 部门名称
     */
    private String organName;
    /**
     *事项名称
     */
    private String serviceName;
    /**
     * 事项类型
     */
    private String serviceTypeName;
    /**
     * 服务对象：1-自然人；2-企业法人；3-事业法人；4-社会组织法人；5-非法人企业；6-行政机关；9-其他组织
     */
    private String serviceObject;
    /**
     * 后台配置的服务对象名称，前端可直接使用
     */
    private String serviceObjectName;
    /**
     * 办件类型：1-即办件，2-承诺件
     */
    private Short caseType;
    /**
     * 办件类型名称
     */
    private String caseTypeName;
    /**
     * 办理形式：0-窗口办理，1-网上办理，2-快递申请，中间是逗号隔开
     */
    private String handleForm;
    /**
     * 是否支持预约，0-不支持，1-支持
     */
    private Short appointmentFlag;
    /**
     * 是否支持预约，
     */
    private String appointmentFlagName;
    /**
     * 是否有联办机构，0-否，1-是
     */
    private Short unionOrganFlag;
    /**
     * 是否有联办机构，后台配置值，前端可直接展示
     */
    private String unionOrganFlagName;
    /**
     * 是否有联办机构，如果配置的有联办机构需要展示“联办机构”
     */
    private String unionOrgan;
    /**
     * 网办深度
     */
    private String handleDepth;
    /**
     * 网办深度，前端可直接展示
     */
    private String handleDepthName;
    /**
     * 事项分类编号
     */
    private String subjectClassification;
    /**
     * 选择的事项分类名称
     */
    private String subjectClassificationName;
    /**
     * 事项分类
     */
    private String serviceGroupClassificationName;
    /**
     * 办理结果送达方式：2-公告，4-物流，6-自行取件，7直接送达
     */
    private String resultDeliveryWay;
    /**
     * 结果物送达方式名称
     */
    private String resultDeliveryWayName;
    /**
     * 是否有中介服务，0-否，1-是
     */
    private Short isZjfw;
    /**
     * 是否有中介服务，选择的名称，前端可直接展示
     */
    private String isZjfwName;
    /**
     * 是否含有特别程序，0-否，1-是
     */
    private Short isSpecial;
    /**
     * 是否含有特别程序，后台配置值前端可直接使用
     */
    private String isSpecialName;
    /**
     * 实施主体
     */
    private String hostOffices;
    /**
     *网办地址
     */
    private String onlineApplyLink;

    /**
     * 0-窗口咨询，1-网上咨询，2-邮件咨询，3-信函咨询，4-电话咨询
     */
    private String zxType;
    /**
     * 咨询名称
     */
    private String zxTypeName;
    /**
     * 大厅窗口咨询内容
     */
    private String zxCkText;
    /**
     * 网上咨询方式内容
     */
    private String zxWlText;
    /**
     * 电话咨询方式内容
     */
    private String zxDhText;
    /**
     * 信函咨询方式内容
     */
    private String zxXjText;
    /**
     * 邮件咨询方式内容
     */
    private String zxYjText;
    /**
     * 是否支持自助终端办理，0-否，1-是
     */
    private Short terminalProcessing;
    /**
     * 是否支持自助终端办理，0-否，1-是
     */
    private String terminalProcessingName;
    /**
     * 自助终端办理地址
     */
    private String terminalProcessingUrl;
    /**
     * 中介服务名称，当是否有中介服务，是，则展示中介服务名称
     */
    private String mediationName;
    /**
     * 填报须知
     */
    private String fillNotice;
    /**
     * 送达期限
     */
    private String deliverTerm;
    /**
     * 是否生成电子证照1-生成，0-不生成
     */
    private Short elecCertProduceFlag;
    /**
     * 日常用语
     */
    private String popularTerms;
    /**
     * 是否网办，0-否，1-是
     */
    private Short isOnlineHandle;

    /**
     * 审批对象
     */
    private String approvedBy;
    /**
     * 适用范围
     */
    private String applyRange;
    /**
     * 办理地点
     */
    private String handleAddress;
    /**
     * 是否支持容缺受理，0-不支持，1-支持
     */
    private Short isSupportTolerance;
    /**
     * 是否支持告知承诺0-不支持，1-支持
     */
    private Short isSupportInformCommitment;
    /**
     * 事项办理特殊程序
     */
    private String specialProcedure;

    /**
     * 承诺期限说明
     */
    private String promiseTimeDesc;

}
