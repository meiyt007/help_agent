package com.zfsoft.ha.data.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author hut
 * @date 2023/3/22
 */
@Data
public class HaVideoAccessVo {
    /** 主键 */
    private Long id;
    /** 房间主键 */
    private Long roomOid;
    /** 工作人员编号;对应帮代办工作人员表 */
    private Long workUserId;
    /** 视频咨询的用户编号;必须是数字   或 从10000开始递增 */
    private Long videoNum;
    /** 用户名称 */
    private String userName;
    /** sig值 */
    private String sig;
    /** 房间类型;1-帮办人员，2-帮办组长，3-委办局老师，4-办事人 没有委办局老师的类别，需要新增类别 */
    private String userType;
    /** 人员在房间的状态 1-呼叫中、2-接通、3-拒绝、4-退出、5-超时 */
    private String existStatus;
    /** 进房间时间 */
    private Date inDate;
    /** 出房间时间 */
    private Date outDate;
    /** 百家云appId */
    private String videoAppId;
    /** 房间编号 */
    private String roomNumber ;
    /** 队列编号 */
    private Long queueId ;
    /** 呼叫人主键 */
    private Long callUserId;
    /** 呼叫人名称 */
    private String callUserName;
    /**
     * 屏幕分享用户标识
     */
    private Long screenVideoNum;
    /**
     * 屏幕分享用户sign值
     */
    private String screenVideoSig;
}
