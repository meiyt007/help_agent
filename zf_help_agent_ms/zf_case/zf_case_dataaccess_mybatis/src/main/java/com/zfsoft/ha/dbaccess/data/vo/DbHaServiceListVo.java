package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 帮代办服务数据vo
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月09日 17:08:54
 */
@Data
@ToString
public class DbHaServiceListVo {

    /**
     * 服务编号
     */
    private String serviceId;


    /**
     * 服务类型;1-咨询，2-材料准备，3-收件，4-一键推送
     */
    private String serviceType;


    /**
     * 服务内容;填写咨询过程
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
     * 服务的状态;3-收件：1-已完成，2-暂存（需要后续提交）服务的状态;3-收件：1-已完成，2-暂存（需要后续提交）
     */
    private String serviceStatus;


    /**
     * 推送说明
     */
    private String pushMemo;

    /**
     * 服务名称
     */
    private String serviceName;
}
