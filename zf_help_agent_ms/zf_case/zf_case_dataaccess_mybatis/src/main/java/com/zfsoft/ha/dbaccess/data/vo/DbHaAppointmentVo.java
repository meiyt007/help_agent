package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;

/**
 * 预约信息vo
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月12日 10:23:42
 */
@Data
public class DbHaAppointmentVo {

    private Long id;

    private Long scheduleId;

    /**
     * 预约人姓名
     */
    private String name;

    /**
     * 预约的工作人员姓名
     */
    private String workUserName;
    /**
     * 预约的工作人员id
     */
    private String appointmentWorkUserId;
    /**
     * 预约人身份证号
     */
    private String cardNo;

    private String phone;

    /**
     * 预约的公司
     */
    private String companyName;

    /**
     * 公司社会信用代码
     */
    private String companyCode;

    /**
     * 预约时间
     */
    private String appointmentTime;

    /**
     * 预约状态
     */
    private String appointmentStatus;

    private String status;

    /**
     * 预约时间段
     */
    private String appointmentTimePeriod;

    /**
     * 午别  1上午。2下午
     */
    private String amOrPm;

    private Long groupId;

    private String groupName;
}
