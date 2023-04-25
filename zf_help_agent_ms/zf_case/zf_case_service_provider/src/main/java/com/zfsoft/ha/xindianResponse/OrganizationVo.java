package com.zfsoft.ha.xindianResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //新点组织机构接口响应实体类
 * @Author: Wangyh
 * @Date: 2023/3/16 11:21
 */
@Data
@ToString
public class OrganizationVo {
    /**
     * 部门全称
     */
    private String ouname;
    /**
     * 部门简称
     */
    private String oushortname;
    /**
     * 部门编码
     */
    private String oucode;
    /**
     * 统一社会信用代码
     */
    private String org_code;
    /**
     * 区划编码
     */
    private String areacode;
    /**
     * 部门唯一标识
     */
    private String ouguid;
    /**
     * 部门是否禁用(1为禁用)
     */
    private String isnouse;
    /**
     * 部门行使层级
     */
    private String dept_level;
    /**
     * 上海部门编码
     */
    private String sh_code;
    /**
     * 是否为部门
     */
    private String isindependence;
    /**
     * 父部门标识
     */
    private String parentouguid;
    /**
     * 条线编码
     */
    private String businesscodes;
    /**
     * 市级部门ouguid（为区模板部门时该字段才有值）
     */
    private String cityouguid;
    /**
     * 排序号
     */
    private String ordernumber;
}
