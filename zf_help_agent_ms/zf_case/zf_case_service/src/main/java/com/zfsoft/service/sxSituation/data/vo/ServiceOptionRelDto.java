package com.zfsoft.service.sxSituation.data.vo;


import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import lombok.Data;
import lombok.ToString;

import java.util.List;
/**
 * 选项关系
 * xldong
 */

@Data
@ToString
public class ServiceOptionRelDto {

    /**
     * @COLUMN_EXPLAIN : 主键
     * @TABLE_COLUMN_TYPE : varchar
     */
    private String oid;

    /**
     * 大综窗选项关联表
     */
    private ServiceOptionRel serviceOptionRel;

    /**
     * 情形结果list
     * 格式 ： 选项标题【选项值】
     */
    private List<String>  optionValueList;
    /**
     * 情形标题list
     * 格式 ： 选项标题
     */
    private List<String>  optionTitleList;
}
