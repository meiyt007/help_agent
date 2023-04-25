package com.zfsoft.ha.data.responseData;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/17 13:24
 */
@Data
public class HaQlCaseAppayResponseData {
    /**
     * 主键
     */
    private Long id;

    /**
     * 申请人名称
     */
    private String applyUserName;
    /**
     * 证件类型
     */
    private String credentialType;
    /**
     * 证件号码
     */
    private String credentialNumber;
}
