package com.zfsoft.ha.data.responseData;

import lombok.Data;


/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/28 15:04
 */
@Data
public class HaAppointmentResponseData {
    /**
     *主键
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号码
     */
    private String cardNo;
    /**
     *手机号
     */
    private String phone;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 统一社会信用代码
     */
    private String appointmentWorkUserId;
    /**
     * 预约时间;yyyy-MM-dd
     */
    private String appointmentTime;

    /**
     * 预约时间段  五个时段
     * 1--8：30~9：29、
     * 2--9：30~10:29、
     * 3--10：30~11：29、
     * 4--13：30~14：29、
     * 5--14：30~15：30
     */
    private String appointmentTimePeriod;
    /**
     * 预约状态;1-未处理，2-已处理
     */
    private String appointmentStatus;


}
