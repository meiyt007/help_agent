package com.zfsoft.microservice.workflow.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 工作流流程的驳回退回记录表
 * @author: wuxx
 * @Date: 2021/5/8 14:43
 **/
@Data
@ToString
public class WorkflowFallback {

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
    private String fallbackOid;

    /** 类型 0驳回  1退回 2 跳过*/
    private Integer fallbackType;

    /** 任务节点id */
    @NotNull(message = "任务节点id不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String activityId;

    /** 环节oid */
    private String stepOid;

    /** 任务id */
    private String taskId;

    /** 创建时间 */
    private Date createDate;

    /** 删除状态 */
    private Integer isDelete;

}
