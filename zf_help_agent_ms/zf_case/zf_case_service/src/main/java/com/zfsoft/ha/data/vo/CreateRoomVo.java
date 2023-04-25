package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/3/14 11:12
 */
@Data
@ToString
public class CreateRoomVo {
    /**
     * 队列主键
     */
    private  Long  queueId;
    /**
     * 房间主键
     */
    private  Long roomId;
    /**
     * 房间号
     */
    private  String roomNumber;
    /**
     * 房间名称
     */
    private  Long roomName;
    /**
     * 房间状态
     */
    private  String roomStatus;
    /**
     * 当前房间人数
     */
    private  String roomNum;
    /**
     * 工号
     */
    private  String workNumber;
    /**
     * 帮办人员主键
     */
    private  Long workUserId;
    /**
     * 帮办人员姓名
     */
    private  String worlUserName;
    /**
     *  排队号
     */
    private  String windowsNumber;
    /**
     * sig
     */
    private String sig;
    /**
     * Appid
     */
    private String appId;
    /**
     * 用户标识
     */
    private Long videoNum;

    /**
     * 房间出入记录表主键
     */
    private Long accessId;
    /**
     * 屏幕分享用户标识
     */
    private Long screenVideoNum;
    /**
     * 屏幕分享用户sign值
     */
    private String screenVideoSig;
}
