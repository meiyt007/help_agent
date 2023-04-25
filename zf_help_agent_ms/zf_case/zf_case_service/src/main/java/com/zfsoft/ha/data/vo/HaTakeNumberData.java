package com.zfsoft.ha.data.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/1 11:23
 */
@Data
public class HaTakeNumberData {
    /**
     * {
     * 	"hallCode": "",
     * 	"groupId": "861afddf-4511-444c-af8e-7de8e168007d",
     * 	"cateGoryId": "82074911-349d-4333-8478-d8eb8452f39d",
     * 	"username": "帮办测试",
     * 	"mobile": "17111111111",
     * 	"certno": "330411199805010635",
     * 	"clintId": ""
     * }
     */

    private String hallCode;

    @NotNull(message = "组别id不能为空")
    private String groupId;

    @NotNull(message = "目录id不能为空")
    private String cateGoryId;

    @NotNull(message = "姓名不能为空")
    private String username;

    @NotNull(message = "手机号码不能为空")
    private String mobile;

    @NotNull(message = "身份证号码不能为空")
    private String certno;

    private String clintId;
}
