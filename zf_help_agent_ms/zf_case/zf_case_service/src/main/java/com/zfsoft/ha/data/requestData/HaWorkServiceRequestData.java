package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 帮代办服务记录相关请求参数实体类
 * @author: kangax
 * @date: 2022-07-27 17:47
 **/
@Data
@ToString
public class HaWorkServiceRequestData {
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
    private String qlCaseId;

    /**
     * 一键推送时，说明内容
     */
    private String pushMemo;


    /**
     * 一键推送时，推送类型
     */
    private String pushType;

    /**
     * 下次服务建议;1-待服务、2-窗口取号、3-完结
     */
    private String nextServiceAdvise;

    /**
     * 分配建议;1-随机分配帮办人员、2-手动分配帮办人员
     */
    private String distributionAdvise;

    /**
     * 建议内容
     */
    private String adviseMemo;

    /**
     * 办件编号（流水号）
     */
    private String caseNumber;

    /**
     * 服务状态
     */
    private String serviceStatus;

}
