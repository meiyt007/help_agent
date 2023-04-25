package com.zfsoft.ha.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zfsoft.ha.data.HaWorkQueue;
import lombok.Data;

import java.util.Date;

@Data
public class HaDeskVo {

    /**
     * 圆桌主键
     */
    private Long id;
    /**
     * 预约队列id
     */
    private Long queueId;

    /**
     * 队列对象
     */
    private HaWorkQueueVo haWorkQueueVo;
    /**
     * 联办队列id
     */
    private Long deskQueueId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业统一社会信用代码
     */
    private String companyCode;
    /**
     * 状态 0未预约，1预约中，2已预约，3已发起一桌联办
     */
    private String deskStatus;
    /**
     * 预约时间
     */
    private Date appDate;
    /**
     * 帮办人员id
     */
    private Long workUserId;
    /**
     * 帮办人员姓名
     */
    private String workUserName;
    /**
     * 备注
     */
    private String desc;

    @JsonIgnore
    private String deleteStatus;
    @JsonIgnore
    private String createBy;
    @JsonIgnore
    private Date createDate;
    @JsonIgnore
    private String updateBy;
    @JsonIgnore
    private Date updateDate;

}