package com.zfsoft.ha.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description //队列Vo
 * @Author: Wangyh
 * @Date: 2022/7/20 11:50
 */
@Data
@ApiModel(description = "内层数据响应封装")
public class HaWorkQueueVo {
    /**
     * 主键
     */
    @ApiModelProperty(value = "id", required = false)
    private Long id;
    /**
     *姓名
     */
    @ApiModelProperty(value = "name", required = false)
    private String name;

    /**
     * 证件类型
     */
    @ApiModelProperty(value = "cardType", required = false)
    private String cardType;
    /**
     * 身份证号码（证件号）
     */
    @ApiModelProperty(value = "cardNo", required = false)
    private String cardNo;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "phone", required = false)
    private String phone;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "companyName", required = false)
    private String companyName;
    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "companyCode", required = false)
    private String companyCode;
    /**
     * 排队状态1-扫码排队中，2-导服已分配
     */
    @ApiModelProperty(value = "queueStatus", required = false)
    private String queueStatus;
    /**
     * 分配状态1-指定人员，2-随机分配，3-窗口办理
     */
    @ApiModelProperty(value = "distributeStatus", required = false)
    private String distributeStatus;
    /**
     * 服务状态：1-等待服务，2-服务中，3-服务完成
     */
    @ApiModelProperty(value = "serviceStatus", required = false)
    private String serviceStatus;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "serviceWorkUserName", required = false)
    private String serviceWorkUserName;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "serviceWorkUserId", required = false)
    private Long serviceWorkUserId;
    /**
     * 服务开始时间
     */
    @ApiModelProperty(value = "serviceBeginTime", required = false)
    private Date serviceBeginTime;
    /**
     * 服务结束时间：最终的服务结束时间，如果有转派，那就是转派人员的结束时间
     */
    @ApiModelProperty(value = "serviceEndTime", required = false)
    private Date serviceEndTime;
    /**
     * 服务时长：单位：秒
     */
    @ApiModelProperty(value = "serviceDuration", required = false)
    private Integer serviceDuration;

    /**
     * 服务开始时间
     */
    @ApiModelProperty(value = "createDate", required = false)
    private Date createDate;

    /**
     * 第一次服务开始时间， 第一个为办事人服务的时间
     */
    private Date firstServiceBeginTime;



    private Long groupId;

    private String groupName;

    private Integer serviceingNum;

    private Integer finishServiceNum;



    private String serviceType;

    private String pushType;

    private String serviceMemo;

    private String sxServiceId;

    private String sxServiceName;

    private String qlCaseId;

    private String waitTime;

    private String agentTakeNumber;

    /**
     * 建议内容
     */
    private String adviseMemo;

    /**
     * 视频帮办房间信息
     */
    private HaVideoAccessVo haVideoAccess;

    /**
     * 取号类型：1-扫码取号，2-预约取号，3-普通取号, 4手机取号
     */
    private String takeNumberType;

}
