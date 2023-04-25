package com.zfsoft.service.sxSituation.data;

import lombok.Data;
import lombok.ToString;

/**
 * @author wangns
 * @description 大综窗选项关联表
 * @date 2020/11/3 10:41
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class ServiceOptionRel {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 事项基本信息表业务主健
     */
    private String serviceOid;

    /**
     * 选项主键集合
     */
    private String titleOids;

    /**
     * 选项值主键集合
     */
    private String valueOids;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;

}
