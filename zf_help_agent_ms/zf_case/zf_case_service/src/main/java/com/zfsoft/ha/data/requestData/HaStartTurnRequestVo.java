package com.zfsoft.ha.data.requestData;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/3 11:45
 */
@Data
public class HaStartTurnRequestVo {

    /**
     * 队列编号
     */
    @NotNull(message = "办事队列id不能为空")
    private Long queueId;

    /**
     * 组别编号
     */
    @NotNull(message = "转派目标分组id不能为空")
    private Long groupId;

    /**
     * 转派原因
     */
    private String turnMemo;

    /**
     * 转派组别组长id  userId
     */
    @NotNull(message = "转派目标分组队长id不能为空")
    private String groupLeaderId;
}
