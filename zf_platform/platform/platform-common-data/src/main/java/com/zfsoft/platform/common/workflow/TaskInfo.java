package com.zfsoft.platform.common.workflow;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务信息
 */
@Data
@ToString
public class TaskInfo {

    /**
     * 任务标识
     */
    private String id;

    /**
     * 任务定义key
     */
    private String taskDefinitionKey;
    /**
     * 任务名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date createTime;

    /**
     * 应办结时间
     */
    private Date dueTime;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 进程id
     */
    private String executionId;

    /**
     * 定义唯一标识
     */
    private String processDefinitionId;

    /**
     * 流程类型环节唯一标识
     */
    private String linkOid;

    /**
     * 环节名称
     */
    private String linkName;

    /**
     * 环节编码
     */
    private String typeCode;

    /**
     * 环节操作名称
     */
    private String actionName;

    /**
     * 驳回节点用户任务xml的id Map
     */
    private Map<String,String> rejectTaskIdMap;

    /**
     * 退回节点用户任务xml的id Map
     */
    private Map<String,String> fallbackTaskMap;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 流程infoOid
     */
    private String infoOid;


    /** 下一环节人员设定类型 */
    private String handleType;

    /**
     * 用户oids
     */
    private String userOids;

    /**
     * 岗位oids
     */
    private String postOids;

    /**
     * 环节办理人
     */
    private String assignee;
}
