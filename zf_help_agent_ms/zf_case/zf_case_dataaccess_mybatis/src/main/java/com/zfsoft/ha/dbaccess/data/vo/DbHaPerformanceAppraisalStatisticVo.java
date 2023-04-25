package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 绩效考核统计实体类（包括加分时长绩效和常规时长绩效）
 * @author dingsn
 * @date 2022/11/04  16:30
 */
@Data
@ToString
public class DbHaPerformanceAppraisalStatisticVo {
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
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

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

    private List<String> ids;

    private String groupSplitId;

    /**
     * 电话绩效时长
     */
    private Integer phoneDuration;

}
