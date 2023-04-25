package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/3/13 16:24
 */
@Data
@ToString
public class HaCRoomTakeRequestData {
    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;

    /**
     * 身份证号码
     *
     */
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
     * 取号类型：1-扫码取号，2-预约取号，3-普通取号, 4手机取号
     */
    private String takeNumberType;

    /**
     * 帮办人员分组编号
     */
    private String groupId;

    /**
     * 帮代办类型，1-快捷帮办，2-圆桌帮办
     */
    private String haType;
}
