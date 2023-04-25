package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
* Description: 短信接口
* @author zhaobf
* date: 2023/1/6 10:26
*/
@ConfigurationProperties(prefix = "sms")
@Component
@Data
public class SmsProperties {
    /**
     * 短信接口地址
     */
    private String url;
    /**
     * token
     */
    private String token;
//    /**
//     * 短信发送手机号
//     */
//    private String phone;
//    /**
//     * 短信内容
//     */
//    private String message;
//    /**
//     * 短信接口调用方联系人
//     */
//    private String displayName;
    /**
     * 短信接口调用方单位
     */
    private String displayOu;
    /**
     * 调用主体单位代码项值
     */
    public String unit;
    /**
     * 短信用途代码项值
     */
    public String msgusage;
}
