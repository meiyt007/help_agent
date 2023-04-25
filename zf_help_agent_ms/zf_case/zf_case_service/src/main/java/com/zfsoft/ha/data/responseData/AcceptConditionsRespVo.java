package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，受理条件响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:01
 */
@Data
@ToString
public class AcceptConditionsRespVo {
    /**
     * 业务主键
     */
    private String conditionOid;
    /**
     * 所属事项主键
     */
    private String serviceOid;
    /**
     * 受理内容
     */
    private String conditionText;

}
