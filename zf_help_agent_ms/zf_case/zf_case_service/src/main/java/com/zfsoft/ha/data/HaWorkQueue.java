package com.zfsoft.ha.data;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description //队列dto
 * @Author: Wangyh
 * @Date: 2022/7/20 10:46
 */
@Data
@ToString
public class HaWorkQueue {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     *姓名
     */
    private String name;
    /**
     * 证件类型
     */
    private String cardType;
    /**
     * 身份证号码
     */
    private String cardNo;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 统一社会信用代码
     */
    private String companyCode;
    /**
     * 排队状态1-扫码排队中，2-导服已分配
     */
    private String queueStatus;
    /**
     * 分配状态1-指定人员，2-随机分配，3-窗口办理
     */
    private String distributeStatus;
    /**
     * 分配时间
     */
    private Date distributeTime;
    /**
     * 服务状态：1-等待服务，2-服务中，3-服务完成
     */
    private String serviceStatus;
    /**
     * 服务工作人员编号对应帮代办工作人员表
     */
    private Long serviceWorkUserId;
    /**
     * 服务开始时间
     */
    private Date serviceBeginTime;
    /**
     * 服务结束时间：最终的服务结束时间，如果有转派，那就是转派人员的结束时间
     */
    private Date serviceEndTime;
    /**
     * 服务时长：单位：秒
     */
    private Integer serviceDuration;
    /**
     * 下次服务建议：1-待服务、2-随机分配帮办人员、3-手动分配帮办人员、4-窗口取号、5-完结
     */
    private String nextServiceAdvise;
    /**
     * 建议内容
     */
    private String adviseMemo;

    /**
     * 排队号
     */
    private String windowsNumber;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;

    /**
     *更新时间
     */
    private Date updateDate;

    /**
     * 第一次服务开始时间， 第一个为办事人服务的时间
     */
    private Date firstServiceBeginTime;



    /**
     * DETECTS_SERVICE_TIME 检测服务时间
     * @return
     */
    private Date detectsServiceTime ;

    /**
     * 视频录制结果列表
     */
    private JSONArray videoRecordList ;

}
