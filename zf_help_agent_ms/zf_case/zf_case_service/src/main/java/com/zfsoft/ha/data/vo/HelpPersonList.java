package com.zfsoft.ha.data.vo;

import lombok.Data;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/3/13 14:53
 */
@Data
public class HelpPersonList {
    /**
     * 帮办人员 编号
     */
    private Long id;
    /**
     * 帮办人员姓名
     */
    private String name;
    /** 工号 */
    private String workNumber ;
    /** 头像 */
    private String image;
    /** 备注 */
    private String memo;
    /**
     * 分组编号
     */
    private Long groupId;
    /**
     * 状态
     */
    private String status;
    /**
     * 帮代办类型，1-快捷帮办，2-圆桌帮办
     */
    private String haType;
    /**
     * 当前服务人数
     * 线上
     */
    private  Integer currentServiceNumOnline;
    /**
     * 当前服务人数
     * 线下
     */
    private  Integer currentServiceNumOffline;

    /**
     * 最大服务人数
     */
    private Integer maxServiceNum;
    /**
     * 每个人平均服务时长
     */
    private Integer avgServiceTime;
    /**
     * 等待的人数
     */
    private int waitingNum;
    /**
     * 预计等待时间
     */
    private Integer esuimateTime;
}
