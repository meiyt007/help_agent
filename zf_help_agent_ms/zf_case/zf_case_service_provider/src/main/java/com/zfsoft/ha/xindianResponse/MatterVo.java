package com.zfsoft.ha.xindianResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description //新点材料类
 * @Author: Wangyh
 * @Date: 2023/1/28 15:06
 */
@Data
@ToString
public class MatterVo {
    /**
     * 事项主键
     */
    private String rowguid;
    /**
     * 基本表主键
     */
    private String itemguid;
    /**
     * 版本唯一标识
     */
    private String materialid;
    /**
     * 材料名称
     */
    private String materialname;
    /**
     * 材料必要性(见9.19材料必要性)
     */
    private String materialnecessity;

    /**
     * 材料类型(见9.20材料形式)
     */
    private Short materialformat;

    /**
     * 来源渠道(见9.21来源渠道)
     */
    private short file_source;
    /**
     * 材料形式(见9.22材料提交方式)
     */
    private String submittype;
    /**
     * 来源渠道说明
     */
    private String file_source_explain;
    /**
     * 空表模板
     */
    private MaterialemptytableVo materialemptytable;

    /**
     * 样表模板
     */
    private MaterialexampletableVo materialexampletable;


    /**
     * 纸质材料份数
     */
    private String page_num;
    /**
     * 要求（备注）
     */
    private String baknote;
    /**
     * 免交方式
     */
    private String exemption_form;
    /**
     * 无法免交说明
     */
    private String exemption_form_explain;
    /**
     * 落地情况标识
     */
    private String isconfirm;
    /**
     * 排序号
     */
    private String ordernumber;
    /**
     * 要求提供材料的依据
     */
    private String by_law;
}
