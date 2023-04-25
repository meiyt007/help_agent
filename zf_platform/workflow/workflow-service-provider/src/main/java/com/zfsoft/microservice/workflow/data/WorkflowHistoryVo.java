package com.zfsoft.microservice.workflow.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: kkfan
 * @create: 2021-02-01 13:49:40
 * @description: 历史
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowHistoryVo {
    /** 历史记录id */
    private String id;

    private Long workflowInfoId;

    private String workflowInfoOid;

    private String modelId;

    /** 流程名称 */
    private String name;

    /** 流程版本 */
    private Integer version;

    /** 流程key */
    private String processKey;

    /** deploymentId */
    private String deploymentId;

    /** 部署状态 */
    private String deployStatus;

    /** 部署时间 */
    private String deployTime;

    private String processDefId;

}
