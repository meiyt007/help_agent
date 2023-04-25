package com.zfsoft.service.sxConditionalRules.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Wangke
 */
@Data
@ToString
public class SxConditionalRules {

    /** 规则类型-条件预检 */
    public static final String RULETYPE_CONDITION = "1";

    /** 规则类型-秒批 */
    public static final String RULETYPE_APPROVAL = "2";

    /** 主键 */
    private Long id;

    /** 预检配置OID，业务主键 */
    private String rulesOid;

    /** 预检项名称 */
    private String rulesName;

    /** 事项主键 */
    private String serviceOid;

    /** 排序序号 */
    private String sort;

    /** 预检规则 调用其他服务的inter_api_code */
    private String interApiCode;

    /** 删除状态(0否、1是) */
    private Short delFlag;

    /** 数据创建时间 */
    private Date createDate;

    /** 数据修改时间 */
    private Date modifyDate;

    /** 1:条件预检2:秒批接口3:推送接口(2和3都属于秒批规则) */
    private String ruleType;

    /**核验说明*/
    private String rulesMemo;

    /**核验地址*/
    private String rulesAddress;

}
