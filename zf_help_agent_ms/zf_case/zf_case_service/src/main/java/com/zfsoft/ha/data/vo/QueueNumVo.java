package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 办事队列中等待人数
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 下午1:15
 */
@Data
@ToString
public class QueueNumVo {
    /**
     * 正在服务的人数
     */
    private Integer inServiceNum;
    /**
     * 等待的人数
     */
    private Integer waitIngNum;

    /**
     * 今日已服务人数
     */
    private Integer todayServiceNum;

    /**
     * 正在服务的人数（组内）
     */
    private Integer inServiceGroupNum;
    /**
     * 等待的人数（组内）
     */
    private Integer waitIngGroupNum;

    /**
     * 今日已服务人数（组内）
     */
    private Integer todayServiceGroupNum;
}
