package com.zfsoft.outer.inter.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/9 10:34
 */
@Data
public class HaAttaPushRequestData {
    /**
     * 资源名称
     */
    @NotNull(message = "资源名称不能为空")
    private String attaName;
    /**
     * 附件地址
     */
    @NotNull(message = "附件地址不能为空")
    private String attaUrl;
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
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;
}
