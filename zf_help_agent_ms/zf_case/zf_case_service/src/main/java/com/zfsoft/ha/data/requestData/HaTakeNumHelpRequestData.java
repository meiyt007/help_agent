package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description: 扫码填写帮代办信息接口请求参数类
 * @author: kangax
 * @date: 2022-08-10 12:22
 **/
@Data
@ToString
public class HaTakeNumHelpRequestData {
    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;

    /**
     * 证件类型
     *
     */
//    @NotNull(message = "证件类型不能为空")
    private String cardType;

    /**
     * 身份证号码
     *
     */
//    @NotNull(message = "身份证号码不能为空")
    private String cardNo;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
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
     * 工作人员编号
     */
    private String workUserIds;

    /**
     * 取号类型：1-扫码取号，2-预约取号，3-普通取号, 4手机取号 5 视频咨询取号
     */
    private String takeNumberType;

    /**
     * 帮办人员分组编号
     */
    private String groupId;

    /**
     * 预约id 逗号隔开
     */
    private String appointmentIds;

    /**
     * 帮代办类型，1-快捷帮办，2-圆桌帮办
     */
    private String haType;
//    /**
//     * sig
//     */
//    private String sig;



}
