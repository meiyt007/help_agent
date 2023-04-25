package com.zfsoft.ha.data.requestData;

import com.zfsoft.ha.data.vo.HaPolicyPushVo;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HaPolicyPushRequestData {


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
     * 政策
     */
    @NotNull(message = "政策不能为空")
    private HaPolicyPushVo policyBase;


}