package com.zfsoft.ha.wandaResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //材料对象
 * @Author: Wangyh
 * @Date: 2022/8/23 16:16
 */
@Data
@ToString
public class CommitListVo {
    /**
     * 材料id（stStuffId）
     */
    private String rowguid;
    /**
     * 情形id （statusID）
     */
    private String itemguid;
    /**
     * 材料必要性（是否必须）
     */
    private String materialnecessity;
    /**
     * 材料名称
     */
    private String materialname;
    /**
     * 备注
     */
    private String baknote;
    /**
     * 电子证照（复选）
     */
    private String auditlicencematerial;
    /**
     * 电子材料目录编码
     */
    private String priStCatalogCode;
    /**
     * 空白表
     */
    private String materialemptytable;
    /**
     * 材料样张ID（材料样张）
     */
    private String materialexampletable;
    /**
     * 原件数
     */
    private String nmOriginal;
    /**
     * 复印件数
     */
    private String nmCopy;
}
