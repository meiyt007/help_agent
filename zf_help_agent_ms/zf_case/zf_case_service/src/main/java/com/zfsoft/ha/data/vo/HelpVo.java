package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //视频咨询公共部分
 * @Author: Wangyh
 * @Date: 2023/3/13 15:00
 */
@Data
@ToString
public class HelpVo {
    /**
     *当前服务人数
     */
    private Integer currentServiceNum;
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
