package com.zfsoft.ha.data.responseData;

import lombok.Data;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/28 16:12
 */
@Data
public class HaWorkServiceResponseData {
    /**
     * 办事队列编号
     */
    private Long workQueueId;

    /**
     * 服务类型;1-咨询，2-材料准备，3-收件，4-一键推送
     */
    private String serviceType;

    /**
     * 服务内容
     */
    private String serviceMemo;

    /**
     * 服务事项编号
     */
    private String sxServiceId;

    /**
     * 办件编号
     */
    private Long qlCaseId;

    /**
     * 一键推送时，说明内容
     */
    private String pushMemo;

    /**
     * 服务时间
     */
    private Date serviceTime;

    /**
     * 事项名称
     */
    private String serviceName;

    /**
     * 事项名称
     */
    private String serviceStatus;
}
