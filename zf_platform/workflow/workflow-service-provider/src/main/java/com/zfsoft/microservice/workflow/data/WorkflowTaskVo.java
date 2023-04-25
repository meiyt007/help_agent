package com.zfsoft.microservice.workflow.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName WorkflowTaskVo
 * @Description: 流程任务vo
 * @Author wuxx
 * @Date 2021/1/29
 **/
@Data
public class WorkflowTaskVo implements Serializable {

    /**
     *  流程任务节点id
     **/
    private String activityId;

    /**
     *  流程任务id
     **/
    private String taskId;

    /**
     *  流程实例id
     **/
    private String processInstanceId;

    /**
     * 开始时间
     **/
    private Date startDate;
    /**
     * 结束时间
     **/
    private Date endDate;

    /**
     * 预警时间
     **/
    private Date warnDate;

    /**
     * 限制结束时间
     **/
    private Date limitDate;

    /**
     * 办理人主键
     **/
    private String userId;

    /**
     * 办理人名称
     **/
    private String userName;
    /**
     * 当前环节状态 （1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停 8终止流程,9 驳回退回 10审核不通过）
     **/
    private String handleStatus;

    /**
     * 办理状态 0未办理  1审核通过  2审核不通过 3驳回 4退回 5跳过
     **/
    private String handleResult;

    /** 环节时限 */
    private Integer timeLimit;

    /** 环节时限小时 */
    private Integer timeLimitHour;

    /** 环节时限单位 */
    private String timeUnit;

    /** 办理人员名称 */
    private String handleUserNames;

    /** 办理岗位名称 */
    private String handlePostNames;


    /** 办理角色名称 */
    private String handleRoleNames;


    /** 办理机构名称 */
    private String handleOrganNames;

    /** 删除原因 */
    private String deleteReason;
}
