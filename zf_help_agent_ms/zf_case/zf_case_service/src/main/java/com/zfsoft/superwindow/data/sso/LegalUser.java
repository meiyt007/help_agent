package com.zfsoft.superwindow.data.sso;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-法人
 * @Date: 2022/5/31 21:46
 */
@Data
public class LegalUser extends SsoBaseUser {

    /**
     * 法人用户ID
     */
    @JsonProperty("CORPORATE_ID")
    private String corporateId;

    /**
     * 创建时间
     */
    @JsonProperty("CREATE_TIME")
    private Date createTime;

    /**
     * 法人名称
     */
    @JsonProperty("CORPORATE_NAME")
    private String corporateName;

    /**
     * 统一社会信用代码
     */
    @JsonProperty("CORPORATE_CODE")
    private String corporateCode;

    /**
     * 法定代表人姓名
     */
    @JsonProperty("REPRESENTATIVE_NAME")
    private String representativeName;

    /**
     * 法定代表人身份证号
     */
    @JsonProperty("REPRESENTATIVE_CARD_NO")
    private String representativeCardNo;

    /**
     * 联系电话
     */
    @JsonProperty("CORPORATE_PHONE")
    private String corporatePhone;

    /**
     * 注册地址
     */
    @JsonProperty("REGISTER_ADDRESS")
    private String registerAddress;

    /**
     * 审批机关
     */
    @JsonProperty("REGISTER_AGENCY")
    private String registerAgency;

    /**
     * 办理人姓名
     */
    @JsonProperty("AGENT_NAME")
    private String agentName;

    /**
     * 办理人手机
     */
    @JsonProperty("AGENT_MOBILE")
    private String agentMobile;

    /**
     * 办理人身份证号
     */
    @JsonProperty("AGENT_CARD_NO")
    private String agentCardNo;

    /**
     *  法人证照
     */
    @JsonProperty("CORPROATE_PICTURE")
    private String corproatePicture;

    /**
     * 法定代表人证件类型
     */
    @JsonProperty("REPRESENTATIVE_CARD_TYPE")
    private String representativeCardType;

}
