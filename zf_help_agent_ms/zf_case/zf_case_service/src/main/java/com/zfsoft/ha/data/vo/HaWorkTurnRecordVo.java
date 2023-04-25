package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
public class HaWorkTurnRecordVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 队列表主键
     */
    private Long workQueueId;

    /**
     * 服务转派人员编号
     */
    private Long handleWorkUserId;

    /**
     * 服务人员编号
     */

    private Long serviceWorkUserId;

    /**
     * 转派组长人员编号 VERIFY_WORK_USER_ID
     */
    private Long verifyWorkUserId;


    /**
     * 原服务人员编号
     */
    private Long oldServiceWorkUserId;

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
     * 服务开始时间
     */
    private Date serviceBeginTime;

    /**
     * 服务结束时间
     */
    private Date serviceEndTime;

    /**
     * 服务时长
     */
    private Integer serviceDuration;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    private String name;

    private String serviceWorkUserName;

    private String handleWorkUserName;

    private String companyName;

    private String companyCode;

    private String cardNo;

    private String phone;

    private String serviceName;

}