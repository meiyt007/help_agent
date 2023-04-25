package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 根据转派编号获取办事队列信息接口响应类
 * @author: kangax
 * @date: 2022-08-04 09:58
 **/
@Data
@ToString
public class HaGetWorkQueueByTurnIdResponseData {
    /**
     * 转派记录编号
     */
    private Long turnRecordId;

    /**
     * 办事队列编号
     */
    private Long queueId;

    /**
     * 原服务人员编号
     */
    private Long oldServiceWorkUserId;

    /**
     * 原服务人员姓名
     */
    private String oldServiceWorkUserName;

    /**
     * 转派状态，1-待接收，2-接收，3-退回
     */
    private String turnStatus;

    /**
     * 转派原因
     */
    private String turnMemo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
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


}
