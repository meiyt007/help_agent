package com.zfsoft.ha.data.requestData;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/23 9:52
 */
@Data
public class HaPerPhoneRequestData {
    /**
     * 来电号码
     */
    private String phone;
    /**
     * 呼叫结果
     */
    private String callResults;
    /**
     * 云客服工号
     */
    private String workNumber;

    /**
     * 筛查开始时间
     */

    private String beginTime;
    /**
     * 筛查结束时间
     */
    private String endTime;

    private Integer pageNumber;

    private Integer pageSize;
}
