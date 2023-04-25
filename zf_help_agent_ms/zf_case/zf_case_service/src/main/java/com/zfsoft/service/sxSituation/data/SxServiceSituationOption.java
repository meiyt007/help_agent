package com.zfsoft.service.sxSituation.data;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wangns
 * @description 情形、选项关系表
 * @date 2020/11/3 13:22
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxServiceSituationOption {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String situationOid;

    /**
     * 事项选项主键
     */
    private String optionOid;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;


    /**
     * 选项标题集合
     */
    private List<SxServiceOptionTitle> sxServiceOptionTitles;

}
