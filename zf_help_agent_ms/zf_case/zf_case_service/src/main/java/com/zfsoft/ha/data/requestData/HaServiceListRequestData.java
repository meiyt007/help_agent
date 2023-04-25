package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * 获取帮代办服务的列表请求参数
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月09日 14:22:19
 */
@Data
@ToString
public class HaServiceListRequestData {

    /**
     * 身份证号码
     */
    private String cardNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String companyCode;
}
