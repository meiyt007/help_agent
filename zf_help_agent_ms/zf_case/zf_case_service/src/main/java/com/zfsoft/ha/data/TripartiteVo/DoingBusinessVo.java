package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //0+N个营商服务点的在线状态及视频通讯
 * @Author: Wangyh
 * @Date: 2023/4/11 11:36
 */
@Data
@ToString
public class DoingBusinessVo {
    /**
     * 名称
     */
    private String name;

    /**
     * 营商服务点
     */
    private String businessServicesSddress;
    /**
     * 是否在线
     */
    private String onlineStatus;
    /**
     * 是否视频在线 1-在线 2-不在线
     */
    private String videoOnlineStatus;
    /**
     * 视频房间编号
     */
    private Long roomId;
    /**
     * 视频房间sig
     */
    private String videoSig;
    /**
     *视频应用编号
     */
    private String videoNum;
    /**
     * 用户标识
     */
    private String videoAppId;
    /**
     * 房间号
     */
    private String roomNumber;
}
