package com.zfsoft.microservice.workflow.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办理过程实例
 */
@Data
@ToString
public class ProProcessExample {
    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP {
    }

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP {
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String processOid;

    /**
     * 办件编号
     */
    private String projectNo;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 任务主键
     */
    private String taskId;
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程实例标识父类
     */
    private String superProcessInstanceId;
    /**
     * 申请人
     */
    private String applyerName;


    /**
     * 业务动作 通过，退回，驳回、退回
     */
    private String eventName;

    /**
     * 环节名称
     */
    private String processName;

    /**
     * 办理人姓名
     */
    private String handleUserName;
    /**
     * 办理意见
     */
    private String handleExplain;
    /**
     * 环节开始时间
     */
    private String eventStartTime;
    /**
     * 环节结束时间
     */
    private String eventEndTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 办结时间
     */
    private Date endDate;

    /**
     * 删除状态
     */
    private Integer isDelete;
    /**
     * 办理参数
     */
    private String handleParamKey;
    /**
     * 办理参数
     */
    private String handleParamValue;

    /** 驳回节点用户任务xml的id */
    private String rejectTaskId;

    /** 退回节点用户任务xml的id */
    private String fallbackTaskId;
}
