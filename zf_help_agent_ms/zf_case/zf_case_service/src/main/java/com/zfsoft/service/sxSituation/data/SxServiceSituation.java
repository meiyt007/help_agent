package com.zfsoft.service.sxSituation.data;

import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author wangns
 * @description 事项情形表
 * @date 2020/11/3 11:51
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxServiceSituation {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 情形名称
     */
    private String situationName;

    /**
     * 所属事项主键
     */
    private String serviceOid;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 情形、选项关系集合
     */
    private List<SxServiceSituationOption> sxServiceSituationOptions;

    /**
     * 情形、选项关系
     */
    private SxServiceSituationOption sxServiceSituationOption;

}
