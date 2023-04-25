package com.zfsoft.microservice.workflow.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @ClassName WorkflowBussFlowStep
 * @Description: 工作流业务信息流程环节表
 * @Author wuxx
 * @Date 2021/1/25
 **/
@Data
@ToString
public class WorkflowBussFlowStep {

    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP{};


    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = {UPDATE_GROUP.class})
    private String stepOid;

    /** 步骤编号 */
    private String activityId;

    /** 流程设计的主键 */
    private String infoOid;

    /** 对应activiti的act_re_procdef 表ID */
    private String processDefId;

    /** 环节名称 */
    private String name;

    /** 环节linkOid 对应 WorkflowLink*/
    private String linkOid;

    /** 环节时限 */
    private Integer timeLimit;

    /** 环节时限小时 */
    private Integer timeLimitHour;

    /** 环节时限单位 */
    private String timeUnit;

    /** 环节办理类型 */
    private String handleType;

    /** 下一环节人员设定类型 */
    private String handleUserType;

    /** 驳回节点用户任务xml的id */
    private String rejectTaskId;

    /** 退回节点用户任务xml的id */
    private String fallbackTaskId;

    /** 办理人员 */
    private String handleUserOids;

    /** 办理人员名称 */
    private String handleUserNames;

    /** 办理岗位 */
    private String handlePostOids;

    /** 办理岗位名称 */
    private String handlePostNames;

    /** 内部流程编号 */
    private String innerFlowOid;

    /** Activiti环节节点编号 */
    private String activitiNodeId;

    /**
     * 节点类型，begin-开始节点，end-结束节点，其他为普通节点
     */
    private String nodeType;

    /**
     * @description: 表单的key
     * @author: wuxx
     * @Date: 2021/1/26 13:17
     **/
    private String formKey;
    /**
     *  驳回节点坐标
     */
    private String rejectWayPoints;
    /**
     * 退回节点坐标
     */
    private String fallbackWayPoints;

    /** 办理角色 */
    private String handleRoleOids;

    /** 办理角色名称 */
    private String handleRoleNames;

    /** 办理机构 */
    private String handleOrganOids;

    /** 办理机构名称 */
    private String handleOrganNames;
}
