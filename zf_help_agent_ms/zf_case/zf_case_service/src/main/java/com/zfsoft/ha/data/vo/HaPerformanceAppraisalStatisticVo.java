package com.zfsoft.ha.data.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @Description 绩效考核统计实体类（包括加分时长绩效和常规时长绩效）
 * @author dingsn
 * @date 2022/11/04  13:30
 */
@ApiModel
@Data
@ToString
public class HaPerformanceAppraisalStatisticVo {
    private Long id;
    private Long workUserOid;//帮办人主键
    private String workUserName;//帮办人姓名
    private String manageFactor;//帮办人员管理系数
    private String servicePeopleNum;//服务人数
    private Integer serviceDuration;//服务时长;单位：分钟
    private Long plusProjectOid;//加分项目id
    private String plusProjectName;//加分项目名称
    private Integer plusDuration;//加分时长;单位：分钟
    private String auditStatus;//审核状态;0-待审核，1-审核通过，2-审核不通过
    private String auditReason;//审核原因理由
    private Long groupLeaderId;//帮办人员组长id
    private String groupLeaderName;//帮办人员组长名字
    /**
     * 帮办人组长手机号
     */
    private String groupLeaderPhone;
    /**
     * 帮办人组长所在分组
     */
    private String groupName;
    /**
     * 组长未审核加分时长的总数
     */
    private Integer noAuditStatusSum;
    //季度工作量小计
    private Integer jiduSum;
    //季度累计积分
    private double leijiSum;
    //工作日天数
    private Integer fdWorkCount;
    //日工作量
    private double dayWorkSum;
    //工作效能百分比
    private double gzxnPercent;

    /**
     * 电话绩效时长
     */
    private Integer phoneDuration;

}
