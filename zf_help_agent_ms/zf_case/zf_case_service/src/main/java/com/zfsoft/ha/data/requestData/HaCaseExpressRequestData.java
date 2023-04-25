package com.zfsoft.ha.data.requestData;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* Description: 办件快递实体类

* @author zhaobf
* date: 2023/4/7 15:15
* @copyright 版权由上海卓繁信息技术股份有限公司拥有
*/
@Data
public class HaCaseExpressRequestData {

    /**
     * 办件id
     */
    @NotNull(message = "办件id不能为空")
    private String qlCaseId;

    /**
     * 快递编号
     */
    @NotNull(message = "快递编号不能为空")
    private String caseExpressNum;

}