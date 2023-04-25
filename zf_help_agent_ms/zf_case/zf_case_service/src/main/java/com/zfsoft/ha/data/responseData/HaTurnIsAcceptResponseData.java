package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @description: 转派服务是否接收响应返回参数
 * @author: kangax
 * @date: 2022-08-03 09:48
 **/
@Data
@ToString
public class HaTurnIsAcceptResponseData {
    /**
     * 接收状态，1-待接收，2-接收，3-退回
     */
    private String turnIsVerify;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 退回原因
     */
    private String rollbackMemo;

    /**
     * 工作人员编号
     */
    private Long workUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String workNumber;

    /**
     * 服务位置
     */
    private String servicePosition;

}
