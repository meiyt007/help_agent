package com.zfsoft.ha.data.responseData;

import com.zfsoft.ha.data.vo.HaVideoAccessVo;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 接待下一位返回数据
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/21 上午11:32
 */
@Data
@ToString
public class NextServiceResponseData {
    /*
     * 办事队列主键
     * */
    private Long queueId;

    /**
     * 姓名
     */
    private String name;
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
     * 服务状态：1-等待服务，2-服务中，3-服务完成
     */
    private String serviceStatus;

    /**
     * 窗口号
     */
    private String windowsNumber;

    /**
     * 来访时间
     */
    private Date createDate;
    /**
     * 第一次服务开始时间
     */
    private Date firstServiceBeginTime;

    /**
     * 服务开始时间
     */
    private Date serviceBeginTime;

    /**
     * 取号类型
     */
    private String takeNumberType;

    private HaVideoAccessVo haVideoAccess;
}
