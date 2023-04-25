package com.zfsoft.service.sxSituation.data;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wangns
 * @description 选项标题表
 * @date 2020/11/3 11:04
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxServiceOptionTitle {

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
     * 标题名称
     */
    private String name;

    /**
     * 是否必填 1-是 0-不是
     */
    private Short fillFlag;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 来源渠道
     */
    private String optionSource;

    /**
     * 是否多选
     */
    private Short moreStatus;

    /**
     * 选项值集合
     */
    private List<SxServiceOptionVal> sxServiceOptionVals;

    /**
     * 是否有关联选项值的关联关系 0-没有  1-有  默认 0 - 没有
     */
    private String isHavingCorrelation = "0";

    // 情形选项值
    private String radioValOid;

    private Integer isSelected;


    /**
     * 是否在告知单中展示该情形    1-展示（默认）  0  不展示
     */
    private Short noticeFormFlag;
}
