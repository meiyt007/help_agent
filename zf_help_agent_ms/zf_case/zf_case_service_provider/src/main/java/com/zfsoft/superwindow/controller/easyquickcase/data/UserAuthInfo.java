package com.zfsoft.superwindow.controller.easyquickcase.data;

import lombok.Data;

@Data
public class UserAuthInfo {

    // 用户姓名（实名）
    private String realName;

    // 身份证号
    private String idCard;

    //
    private String openId;

    //
    private String checkPhone;

    // 注册手机号
    private String mobilePhone;

    // 统一社会信用代码
    private String creditCode;

    // 企业名称
    private String creditName;
}
