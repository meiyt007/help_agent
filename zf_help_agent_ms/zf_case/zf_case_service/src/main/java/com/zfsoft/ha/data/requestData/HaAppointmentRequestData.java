package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 接收旗舰店的预约数据接口请求参数
 *
 * @author：yupeng
 * @version: 1.0
 * @date: 2022/7/28 16:35
 */
@Data
public class HaAppointmentRequestData {

    /**
     * 排班id
     */

    @NotNull(message = "排班id不能为空")
    private Long scheduleId;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;


    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    private String cardNo;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 企业名称
     */
    @NotNull(message = "企业名称不能为空")
    private String companyName;

    /**
     * 统一社会信用代码
     */
    @NotNull(message = "统一社会信用代码不能为空")
    private String companyCode;

    /**
     * 预约工作人员编号，对应t_ha_help_work_user中ID或CONNECT_USER_ID
     */
    @NotNull(message = "预约工作人员编号不能为空")
    private String appointmentWorkUserId;

    /**
     * 预约时间点
     */
    @Pattern(regexp = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$",
            message = "时间格式应为：yyyy-MM-dd")
    @NotNull(message = "预约时间点不能为空")
    private String appointmentTime;

    /**
     * 预约时间段  五个时段
     * 1--8：30~9：29、
     * 2--9：30~10:29、
     * 3--10：30~11：29、
     * 4--13：30~14：29、
     * 5--14：30~15：30
     */
    @Pattern(regexp = "[1-5]",
            message = "预约时间段应该为1-5 \n" +
                    "1--8：30~9：29\n" +
                    "2--9：30~10:29\n" +
                    "3--10：30~11：29\n" +
                    "4--13：30~14：29\n" +
                    "5--14：30~15：30")
    @NotNull(message = "预约时间段不能为空")
    private String appointmentTimePeriod;
    /**
     * 午别  1上午。2下午
     */
    @NotNull(message = "午别不能为空")
    private String amOrPm;
}
