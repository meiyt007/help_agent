package com.zfsoft.microservice.workflow.data;

import com.zfsoft.platform.common.data.BaseStaticParameter;

/**
 * @Description: 工作流的静态资源类
 * @Author wuxx
 * @Date 2021/2/25
 **/
public class WorkflowStaticParameter extends BaseStaticParameter {

    /**
     * 工作流状态-创建
     */
    public static final String WORK_FLOW_SRATUS_CREATED = "CREATED";

    /**
     * 工作流状态-正在进行
     */
    public static final String WORK_FLOW_SRATUS_RUNNING = "RUNNING";

    /**
     * 工作流状态-取消
     */
    public static final String WORK_FLOW_SRATUS_CANCELLED = "CANCELLED";

    /**
     * 工作流状态-完成
     */
    public static final String WORK_FLOW_SRATUS_COMPLETED = "COMPLETED";

    /**
     * 工作流状态-删除
     */
    public static final String WORK_FLOW_SRATUS_DELETED = "DELETED";

    /**
     * 工作流状态-挂起
     */
    public static final String WORK_FLOW_SRATUS_SUSPENDED = "SUSPENDED";

}
