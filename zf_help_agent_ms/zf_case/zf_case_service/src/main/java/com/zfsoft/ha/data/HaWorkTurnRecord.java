package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办事转派记录表
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 上午10:58
 */
@Data
@ToString
public class HaWorkTurnRecord {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     *队列表主键
     */
    private Long workQueueId;

    /**
     * 服务转派人员编号
     */
    private Long handleWorkUserId;

    /**
     *服务人员编号
     */
    private String serviceWorkUserId;

    /**
     *原服务人员编号
     */
    private String oldServiceWorkUserId;

    /**
     * 转派组长人员编号 VERIFY_WORK_USER_ID
     */
    private Long verifyWorkUserId;
    /**
     * 转派时间
     */
    private Date turnTime;

    /**
     * 转派状态
     */
    private String turnStatus;

    /**
     * 服务转派原因
     */
    private String turnMemo;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 退回原因
     */
    private String rollbackMemo;

    /**
     *服务开始时间
     */
    private Date serviceBeginTime;

    /**
     *服务结束时间
     */
    private Date serviceEndTime;

    /**
     *服务时长
     */
    private Integer serviceDuration;

    /**
     *创建人
     */
    private String createBy;

    /**
     *创建时间
     */
    private Date createDate;

    /**
     *更新人
     */
    private String updateBy;

    /**
     *更新时间
     */
    private Date updateDate;

}
