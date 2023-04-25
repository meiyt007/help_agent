package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，条件预检响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:29
 */
@Data
@ToString
public class ConditionalrulesRespVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String rulesOid;
    /**
     * 预检项名称
     */
    private String rulesName;
    /**
     * 事项主键
     */
    private String serviceOid;
    /**
     * 核验说明
     */
    private String rulesMemo;
    /**
     * 核验地址
     */
    private String rulesAddress;
}
