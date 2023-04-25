package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/4/11 11:36
 */
@Data
@ToString
public class ZwWindowStatusVo {
    /**
     * 名称
     */
    private String name;

    /**
     * 位置
     */
    private String districtName;
    /**
     * 是否在线
     */
    private String onlineStatus;
    /**
     * 服务位置
     */
    private String servicePostion;
    /**
     * 是否视频在线
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
     *视频咨询appId
     */
    private String videoAppId;

    /**
     * 房间号
     */
    private String roomNumber;
}
