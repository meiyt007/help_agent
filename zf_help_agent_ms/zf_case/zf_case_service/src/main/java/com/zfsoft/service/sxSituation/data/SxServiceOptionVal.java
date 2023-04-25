package com.zfsoft.service.sxSituation.data;

import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wangns
 * @description 选项值表
 * @date 2020/11/3 11:26
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxServiceOptionVal {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 标题名称
     */
    private String name;

    /**
     * 选项主键
     */
    private String titleOid;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;

    /**
     * 排序号
     */
    private Short sort;

    /**
     * 是否默认 1-是 0-不是
     */
    private Short defaultFlag;

    /**
     * 细化材料选项值关系集合
     */
    private List<SxServiceMateOptRel> sxServiceMateOptRels;

    /**
     * 是否已被选中 0-未被选中  1-已被选中  默认 0-未被选中
     */
    private String isSelected = "0";

    private List<SxServiceMaterial> sxServiceMaterials;
    //标题名称（用于关系配置）
    private String titleName;

    // 配置情形关系时使用：单选框选项id
    public String radioValOid;

    // 配置情形关系时使用：多选框选项ids
    private List<String> valOidList;

}
