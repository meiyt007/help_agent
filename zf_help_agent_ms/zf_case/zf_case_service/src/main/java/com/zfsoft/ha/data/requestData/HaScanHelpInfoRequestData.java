package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 扫码填写帮代办信息接口请求参数类
 * @author: kangax
 * @date: 2022-08-10 12:22
 **/
@Data
@ToString
public class HaScanHelpInfoRequestData {
    /**
     * 姓名
     */

    private String name;
    /**
     * 证件类型
     *
     */
    private String cardType;

    /**
     * 身份证号码
     *
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
     * 工作人员编号
     */
    private Long workUserId;

    /**
     * 取号类型：1-扫码取号，2-预约取号，3-普通取号
     */
    private String takeNumberType;

    /**
     * 帮办人员分组编号
     */
    private long groupId;

    /**
     * 帮代办类型，1-快捷帮办，2-圆桌帮办
     */
    private long haType;
}
