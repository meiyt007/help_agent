package com.zfsoft.platform.common.workflow;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 任务结果
 */
@Data
@ToString
public class TaskResult {

    /**
     * 流程实例ID
     */
    private String id;
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 启动时间
     */
    private Date startDate;

    /**
     * 状态
     */
    private String status;

    /**
     * 任务信息列表
     */
    private List<TaskInfo> taskInfoList;
}
