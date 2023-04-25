package com.zfsoft.service.sxService.data;

import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 实施清单-扩展信息
 */
@Data
@ToString
public class SxServiceExtend {
    /**
     * 主健
     */

    private Long id;

    /**
     * 业务主健
     */
    private String extendOid;

    /**
     * 所属事项类型
     */
    private String serviceOid;
    /**
     * 设定依据
     */
    private String setAccord;
    /**
     * 权限划分
     */
    private String powerModular;
    /**
     * 行使内容
     */
    private String exerciseContent;

    /**
     * 法定时限
     */
    private Long legalLimit;

    /**
     * 法定时限类型
     */
    private String legalLimitType;
    private String legalLimitTypeName;

    /**
     * 法定时限说明
     */
    private String legalLimitTest;

    /**
     * 承诺时限
     */
    private Long promiseLimit;

    /**
     * 承诺时限类型
     */
    private String promiseLimitType;

    /**
     * 办理流程图
     */
    private String handleFlow;
    /**
     * 办理流程说明
     */
    private String handleFlowInstruction;

    /**
     * 收费依据(附件)
     */
    private String chargeEvidence;

    /**
     * 限制数量
     */
    private Long numberLimit;

    /**
     * 是否有数量限制(0否、1是)
     */
    private Short numberLimitType;

    /**办理流程图
     * 结果名称
     */
    private String resultName;

    /**
     * 结果样本
     */
    private String resultSampleAddr;

    /**
     * 结果样本类型0证照 1批文 2其它
     */
    private Short resultSampleType;
    /**
     * 收费标准
     */
    private String chargeStandard;

    /**
     * 收费依据
     */
    private String chargeAccord;

    /**
     * 通办范围【全国、跨省、跨市、跨县】
     */
    private String handleScope;

    /**
     * 运行系统 【国家级、省级、市级】
     */
    private String runSystem;

    /**
     * 目录类型
     */
    private Short directoryType;
    /**
     * 审查标准
     */
    private String censorStandard;

    /**
     * 是否长期有效(0否、1是)
     */
    private Short expipyFlag;

    /**
     * 有效期时间
     */
    private String expipyDate;

    /**
     * 预约渠道
     */
    private String bookChannel;

    /**
     * 预约网址
     */
    private String bookUrl;

    /**
     * 预约地点
     */
    private String bookSite;

    /**
     * 是否权限划分(0否、1是)
     */
    private Short isPermisionDivid;
    /**
     * 权限划分标准
     */
    private String dividStandard;
    /**
     * 数量限制说明
     */
    private String numlimitContent;

    /**
     * 数量限制依据
     */
    private String numlimitDesc;
    /**
     * 年审年检
     */
    private String auditChannel;

    /**
     * 阶段性办理0否1是
     */
    private Short isStageHandle;

    /**
     * 收件凭证送达渠道
     */
    private String recipientVoucherChannel;
    /**
     * 收件凭证送达相关要求
     */
    private String recipientVoucherRequirement;

    /**
     * 来源
     */
    private Short dataSource;

    /**
     * 是否进驻政务大厅(0否、1是)
     */
    private Short isEntryCenter;

    /**
     * 乡镇街道名称
     */
    private String townName;

    /**
     * 乡镇街道代码
     */
    private String townCode;

    /**
     * 村居社区名称
     */
    private String villageName;

    /**
     * 村居社区代码
     */
    private String villageCode;

    /**
     * 实施机构编码
     */
    private String organCode;
    /**
     * 委托部门
     */
    private String entrustOrgan;

    /**
     * 计划生效时间
     */
    private Date planEffectiveDate;

    /**
     * 计划取消日期
     */
    private Date planCancelDate;
    /**
     * 处罚的行为、种类、幅度
     */
    private String punishMentMethod;

    /**
     * 是否涉及征收(税)费减免的审批(0否、1是)
     */
    private Short isPermitReduce;

    /**
     * 征收种类
     */
    private Short collectType;

    /**
     * 面向自然人地方特色主题分类
     */
    private String naturalCharacteristic;

    /**
     * 面向法人地方特色主题分类
     */
    private String legalCharacteristic;

    /**
     * 预约电话
     */
    private String bookPhone;

    /**
     * 办理时间段
     */
    private String hanleTimeRange;
    /**
     * 备注
     */
    private String remark;
    /**
     * 事项咨询人主键
     */
    private String consultUserOid;

    private SxSysAtta resultSampleAddrFile;//结果样本附件信息

    private SxSysAtta handleFlowFile;//办理流程图附件信息

    private SxSysAtta chargeEvidenceFile;//收费附件信息

    //更新时间 modify by shimh 2021-08-18 添加更新时间
    private Date modifyDate;

    // 秒批通过文案
    private String mpPass;
    // 秒批不通过文案
    private String mpNoPass;
}
