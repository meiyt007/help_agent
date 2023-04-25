package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/17 14:02
 */
@Data
public class OnekeyPushVo {
    @NotNull(message = "办件编号不能为空")
    private String caseOid;
    private String serviceOid;
    @NotNull(message = "办件类型不能为空")
    private String pushType;
    private String name;
    @NotNull(message = "身份证号码不能为空")
    private String cardNo;
    private String companyName;
    private String companyCode;
}
