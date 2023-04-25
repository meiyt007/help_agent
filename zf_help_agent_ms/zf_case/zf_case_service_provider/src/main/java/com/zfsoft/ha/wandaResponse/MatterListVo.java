package com.zfsoft.ha.wandaResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //事项列表响应类-万达
 * @Author: Wangyh
 * @Date: 2022/8/23 9:52
 */
@Data
@ToString
public class MatterListVo {
    /**
     * 事项唯一编码
     */
    private String itemID;
    /**
     * 事项基本编码（12位，上海事项库catalog_code）
     */
    private String itemNo;
    /**
     * 事项名称（上海事项库ql_name）
     */
    private String itemName;
    /**
     * 部门编码（实施主体编码，上海事项库org_code）
     */
    private String organCode;
    /**
     * 委托部门名称（上海事项库entrust_deptname）
     */
    private String organName;
    /**
     * 事项库名称
     */
    private String dbName;
    /**
     * 上海市部门编码（SH00XX）
     */
    private String defaultOrganCode;
}
