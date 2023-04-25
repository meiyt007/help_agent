package com.zfsoft.service.sxService.data;

import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实施清单-主要信息
 */
@Data
@ToString
public class SxService {

    private Long id;
    /**
     * 业务主健
     */
    private String serviceOid;
    /**
     * 所属区划
     */
    private String districtOid;

    private String districtName;

    /**
     * 实施机构
     */
    private String organOid;
    /**
     * 多部门OIDS
     */
    private String organOids;

    private String organName;

    /**
     * 实施主体性质
     */
    private String implementOrganProperty;

    private String implementOrganPropertyName;

    /**
     * 基本编码
     */
    private String basicCode;

    /**
     * 实施编码
     */
    private String implementCode;

    /**
     * 业务项名称
     */
    private String serviceName;

    /**
     * 所属事项类型
     */
    private String serviceTypeOid;
    private String serviceTypeName;

    /**
     * 事项状态【3发布4暂停5取消】
     */
    private Short serviceStatus;
    private String serviceStatusName;

    /**
     * 事项状态_备份
     */
    private Short serviceStatusBak;

    /**
     * 行使层级
     */
    private String levelName;

    /**
     * 行使层级字典值
     */
    private String levelDicts;

    /**
     * 是否收费(0否、1是)
     */
    private Short chargeFlag;
    private String chargeFlagName;

    /**
     * 服务对象
     */
    private String serviceObject;
    private String serviceObjectName;

    /**
     * 办件类型
     */
    private Short caseType;
    private String caseTypeName;

    /**
     * 办理形式
     */
    private String handleForm;

    private String handleFormName;

    /**
     * 是否支持网上支付(0否、1是)
     */
    private Short onlinePayFlag;
    private String onlinePayFlagName;

    /**
     * 是否支持在线预约(0否、1是)
     */
    private Short appointmentFlag;
    private String appointmentFlagName;

    /**
     * 是否支持物流快递(0否、1是)
     */
    private Short expressFlag;
    private String expressFlagName;

    /**
     * 是否联合办理(0否、1是)
     */
    private Short unionOrganFlag;
    private String unionOrganFlagName;

    /**
     * 联合办理机构
     */
    private String unionOrgan;

    /**
     * 有效流程状态(0否、1是)
     */
    private Short effectiveFlow;

    /**
     * 权力更新类型
     */
    private Short powerUpdateType;

    /**
     * 行政管辖地
     */
    private String adminJurisdiction;
    private String adminJurisdictionName;

    /**
     * 办理深度
     */
    private String handleDepth;
    private String handleDepthName;

    /**
     * 事项性质
     */
    private String serviceCharacter;
    private String serviceCharacterName;

    /**
     *版本号
     */
    private Long versionNumber;

    /**
     * 版本生效时间
     */
    private Date versionEffectiveDate;
    /**
     * 主题分类
     */
    private String subjectClassification;
    /**
     * 事项分类
     */
    private String subjectClassificationName;

    /**
     * 生命周期分类
     */
    private String lifeCycleClassification;
    private String lifeCycleClassificationName;

    /**
     * 办事群体分类
     */
    private String serviceGroupClassification;
    private String serviceGroupClassificationName;

    /**
     * 公开方式
     */
    private Short openWay;
    private String openWayName;

    /**
     * 公开渠道
     */
    private Short openChannel;
    private String openChannelName;
    /**
     * 公开内容
     */
    private String openContent;
    /**
     * 公开文书内容
     */
    private String openDocumentContent;

    /**
     * 办理结果送达方式
     */
    private String resultDeliveryWay;
    private String resultDeliveryWayName;

    /**
     * 收费环节
     */
    private String chargeLinkOid;
    private String chargeLinkName;

    /**
     * 所属目录
     */
    private String directoryOid;
    private String directoryName;

    /**
     * 事项来源
     */
    private String origin;
    private String originName;

    /**
     * 所属是否有中介服务事项(0否、1是)
     */
    private Short isZjfw;
    private String isZjfwName;

    /**
     * 是否特殊程序(0否、1是)
     */
    private Short isSpecial;
    private String isSpecialName;

    /**
     * 主办处室
     */
    private String hostOffices;

    /**
     * 承诺期限说明
     */
    private String promiseTimeDesc;

    /**
     * 网站或系统名称
     */
    private String webName;

    /**
     * 网上申请链接
     */
    private String onlineApplyLink;
    /**
     * 邮递地址
     */
    private String postAddr;

    /**
     * 收件人
     */
    private String postAcceptName;

    /**
     * 是否支持人证核身标识
     */
    private Short showFlag;
    private String showFlagName;

    /**
     * 移动端是否对接单点登录(0否、1是)
     */
    private Short appIssingleLogin;
    private String appIssingleLoginName;

    /**
     * 移动端办理地址
     */
    private String mobileTerminalUrl;

    /**
     * 计算机端是否对接单点登录(0否、1是)
     */
    private String isSingleLogin;
    private String isSingleLoginName;

    /**
     * 计算机端在线办理跳转地址
     */
    private String linkAddr;

    /**
     * 是否配置填报系统(0否、1是)
     */
    private String formFlag;
    private String formFlagName;

    /**
     * 是否存在子项(0否、1是)
     */
    private String existChildItem;
    private String existChildItemName;

    /**
     * 父级主键（若为办理项，该字段不能为空）
     */
    private String serviceParentOid;
    private String serviceParentName;

    /**
     * 投诉方式
     */
    private String tsType;
    private String tsTypeName;

    /**
     * 咨询方式
     */
    private String zxType;
    private String zxTypeName;

    /**
     * 大厅窗口咨询方式
     */
    private String zxCkText;

    /**
     * 网络咨询方式
     */
    private String zxWlText;

    /**
     * 电话咨询方式
     */
    private String zxDhText;

    /**
     * 信件咨询方式
     */
    private String zxXjText;

    /**
     * 邮件咨询方式
     */
    private String zxYjText;

    /**
     * 大厅窗口咨询方式
     */
    private String tsCkText;

    /**
     * 网络咨询方式
     */
    private String tsWlText;

    /**
     * 电话咨询方式
     */
    private String tsDhText;

    /**
     * 信件咨询方式
     */
    private String tsXjText;

    /**
     * 邮件咨询方式
     */
    private String tsYjText;

    /**
     * 是否支持终端办理(0否、1是)
     */
    private Short terminalProcessing;
    private String terminalProcessingName;
    /**
     * 自助端办理地址
     */
    private String terminalProcessingUrl;

    /**
     * 到现场次数
     */
    private String countToScence;

    /**
     * 必须到现场办理原因说明
     */
    private String reasonToScence;

    /**
     * 中介服务名称
     */
    private String mediationName;

    /**
     * 是否支持人证核身标识
     */
    private Short showRzhs;
    private String showRzhsName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;
    private String createUserName;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 是否已关联目录
     */
    private Integer ifCombo;
    /**
     * 事项扩展信息【事项结果】
     */
    private SxServiceExtend sxServiceExtend = new SxServiceExtend();

    //事项材料
    private List<SxServiceMaterial> materials = new ArrayList<>();

    //颗粒化材料列表
    private List<ServiceMaterial> serviceMaterialList = new ArrayList<>();

    private String autoClassificationStatus;

    private String classifierId;

    private Integer sjStatus;

    //用于材料分类器配置
    private List<SxServiceMaterial> serviceMaterList = new ArrayList<>();

    //细化材料个数
    private Integer refinedMaterialNum;
    //更新时间 modify by shimh 2021-08-18 添加更新时间
    private Date modifyDate;
    /**
     * 办理类型[1好办易办 2秒批秒办 3标准]
     */
    private String handleType;
    private String handleTypeName;
    /**
     * 关联推荐事项集合RECOMMENDED_SERVICE_OIDS
     */
    private String recommendedServiceOids;

    /**
     * 填报须知
     */
    private String fillNotice;

    /**
     * 送达期限
     */
    private String deliverTerm;
    /**
     * 是否生成电子证照
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
     * 联办机构
     */
    private String joinOrgan;

    /**
     *审批对象
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
     * 网办深度
     */
    private String onlineHandleDepth;

    /**
     * 事项办理特殊程序
     */
    private String specialProcedure;

    /**
     * 万达-12位事项编码
     * @return
     */
    private String itemNo;
    /**
     * 情形33位编码
     */
    private String stateNo;
    /**
     * 是否是主题事项 0-否，1-是
     */
    private String isScene;
    /**
     * 万达-事项唯一编码
     */
    private String itemId;
    /**
     * 万达-排序
     */
    private String wdOrder;
    /**
     * 是否屏蔽 Y-屏蔽 N未屏蔽
     */
    private String isShield;


}
