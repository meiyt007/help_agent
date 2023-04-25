package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/10 13:07
 */
@Data
@ToString
public class HaWorkQueueResponseData2 {
    private Long id;  //姓名
    private String name;  //姓名
    private String account;  //账号
    private String haType;
    private String status;
    private String phone;
    private String image;
    private Long groupId;
    private String groupName;
    private String workNumber;
    /*
     * 每个人平均服务时长
     * */
    private Integer avgServiceTime;
    /**
     * 当前服务人数
     */
    private Integer currentServiceNum;
    /**
     * 最大服务人数
     */
    private Integer maxServiceNum;

    private Integer inServiceNum;
    private Integer waitingNum;
    private Integer esuimateTime;
    private Integer todayServiceNum;


}
