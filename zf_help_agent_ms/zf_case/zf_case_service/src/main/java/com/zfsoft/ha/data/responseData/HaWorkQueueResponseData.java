package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //队列响应实体类
 * @Author: Wangyh
 * @Date: 2022/7/27 15:21
 */
@Data
@ToString
public class HaWorkQueueResponseData {
    /**
     * 人员编号
     */
    private Long id;
    /**
     * 分组编号
     */
    private long groupId;
    /**
     *  分组名称
     */
    private String groupName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 工号
     */
    private String workNumber;
    /**
     * 账户
     */
    private String account;

    /**
     * 1-快捷帮办，2-圆桌帮办
     */
    private String haType;
    /**
     * 状态
     */
    private String status;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String image;
    /**
     * 每个人平均时长
     */
    private Integer avgServiceTime;
    /**
     * 最大服务人数
     */
    private Integer maxServiceNum;
    /**
     * 当前服务人数
     */
    private Integer currentServiceNum;
    /**
     * 正在服务的人数
     */
    private Integer inServiceNum;
    /**
     * 等待的人数
     */
    private Integer waitingNum;
    /**
     * 预计等待时间
     */
    private Integer esuimateTime;
    /**
     * 今日已服务人数
     */
    private Integer todayServiceNum;

}
