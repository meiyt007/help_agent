package com.zfsoft.superwindow.data.sso;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-用户基础信息
 * @Date: 2022/5/31 22:22
 */
@Data
public class SsoBaseUser {

    /**
     *登录账号
     */
    @JsonProperty("LOGIN_NAME")
    private String loginName;

    /**
     * 登录密码
     */
    @JsonProperty("LOGIN_PWD")
    private String loginPwd;

    /**
     * 状态（0:无效 1:有效）
     */
    @JsonProperty("STATUS")
    private String status;

    /**
     * 排序
     */
    @JsonProperty("SORT_ORDER")
    private String sortOrder;

    /**
     * 邮箱
     */
    @JsonProperty("EMAIL")
    private String email;

    /**
     * 手机号
     */
    @JsonProperty("MOBILE")
    private String mobile;

    /**
     * 密码加密类型
     * 1：MD5
     * 2：SHA-1
     * 3：SM3
     */
    @JsonProperty("ENCRYPTIONTY")
    private String encryptionty;

    /**
     * 创建者ID
     */
    @JsonProperty("CREATOR")
    private String creator;

    /**
     * 自然人：
     * 11	实名认证类型（以逗号分隔）
     * 0：未认证
     * 1:电信运营商实名认证
     * 2:银联卡实名认证
     * 3:社保卡实名认证
     * 4:公积金账号实名认证
     * 5:驾驶证实名认证
     * 6:身份证认证
     * 7:人脸核验
     * 9:实体大厅现场审核验证
     *
     * 法人：
     * 0：未认证
     * 1: 网上实名认证
     * 9: 线下材料审核验证
     */
    @JsonProperty("REAL_AUTH")
    private String realAuth;

    /**
     *注册来源
     * 0：网上自主注册
     * 1：实体大厅现场注册
     */
    @JsonProperty("REGISTER_TYPE")
    private String registerType;
}
