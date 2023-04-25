package com.zfsoft.superwindow.data.sso;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: qiaol
 * @Description:  统一身份认证-自然人
 * @Date: 2022/5/31 21:45
 */
@Data
public class NatureUser extends SsoBaseUser {

    /**
     *用户ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     *用户ID
     */
    @JsonProperty("USER_ID")
    private String userId;

    /**
     * 创建时间
     */
    @JsonProperty("CREATE_TIME")
    private Date createTime;

    /**
     * 用户姓名
     */
    @JsonProperty("USER_NAME")
    private String userName;

    /**
     * 证件号码
     */
    @JsonProperty("CARD_NO")
    private String cardNo;

    /**
     * 性别
     * 0：女
     * 1：男
     */
    @JsonProperty("SEX")
    private String sex;

    /**
     * 出生日期
     */
    @JsonProperty("BIRTHDAY")
    private String birthday;

    /**
     * 银行卡号
     */
    @JsonProperty("BANK_NO")
    private String bankNo;

    /**
     * 支付宝账号
     */
    @JsonProperty("ALIPAY_NO")
    private String alipayNo;


    /**
     * 社保卡号
     */
    @JsonProperty("SOCIAL_CARD")
    private String socialCard;

    /**
     * 公积金账号
     */
    @JsonProperty("FUND_CARD")
    private String fundCard;

    /**
     * 证件类型
     */
    @JsonProperty("CARD_TYPE")
    private String cardType;

}
