package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @description: 评价结果请求参数
 * @author: kangax
 * @date: 2022-08-12 17:32
 **/
@Data
@ToString
public class HaResourcePushResponseData {
    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;
    /**
     * 身份证号码
     */
    @NotNull(message = "身份证号码不能为空")
    private String cardNo;
    /**
     * 统一-社会信用代码
     */
    private String companyCode;
    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 资源id
     */
    private Long resourceId;

    private HaUserResourceResponseVoData haUserResourceResponseVoData;

}
