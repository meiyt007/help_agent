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
public class QueueAllNumVo {
    /**
     * 正在服务的人数
     */
    private Integer inServiceAllNum;

    /**
     * 今日正在服务的人数（快捷）
     */
    private Integer inServiceKJAllNum;
    /**
     * 今日正在服务的人数（圆桌）
     */
    private Integer inServiceYZAllNum;
    /**
     * 等待的人数
     */
    private Integer waitIngAllNum;
    /**
     * 今日等待的人数（快捷）
     */
    private Integer waitIngKJAllNum;
    /**
     * 今日等待的人数（圆桌）
     */
    private Integer waitIngYZAllNum;
    /**
     * 今日已服务人数
     */
    private Integer todayServiceAllNum;
    /**
     * 今日已服务人数（快捷）
     */
    private Integer todayServiceKJAllNum;
    /**
     * 今日已服务人数（圆桌）
     */
    private Integer todayServiceYZAllNum;
}
