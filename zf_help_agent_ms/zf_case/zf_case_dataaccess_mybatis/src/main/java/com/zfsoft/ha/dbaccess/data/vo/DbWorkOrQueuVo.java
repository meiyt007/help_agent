package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //检查排队情况接口返回实体类
 * @Author: Wangyh
 * @Date: 2023/3/14 17:27
 */
@Data
@ToString
public class DbWorkOrQueuVo {
    /**
     * 队列编号
     */
    private Long queueId;
    /**
     * 帮办人员工号
     */
    private String workNumber;
    /**
     * 服务工作人员编号
     */
    private Long serviceWorkUserId;
    /**
     * 服务人员姓名
     */
    private String serviceUserName;
    /**
     * 排队号
     */
    private String windowsNumber;
    /**
     * 分配状态
     * 1-指定人员，2-随机分配，3-窗口办理
     */
    private String distributeStatus;
    /**
     * 排队状态
     * 1-扫码排队中，2-系统已分配，3-超时未处理
     */
    private String queueStatus;
    /**
     * 服务状态
     * 1-等待服务，2-服务中，3-服务完成，4-超时未处理
     */
    private String serviceStatus;
    /**
     * 等待的人数
     */
    private Integer waitingNum;
    /**
     * 预计等待时间
     */
    private Integer esuimateTime;
    /**
     * 平局等待时间
     */
    private Integer avgServiceTime;

}
