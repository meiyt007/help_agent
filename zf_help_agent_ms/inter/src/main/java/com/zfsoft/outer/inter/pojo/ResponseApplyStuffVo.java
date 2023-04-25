package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //增加材料响应类
 * @Author: Wangyh
 * @Date: 2022/9/20 11:03
 */
@Data
@ToString
public class ResponseApplyStuffVo {
    /**
     * 材料Id
     */
    private String stuffID;
    /**
     * 材料名称
     */
    private String stuffName;
    /**
     * 材料的办事指南Id
     */
    private String stuffsourceId;
    /**
     * 原件数量
     */
    private String nmOriginal;
    /**
     * 复印件数量
     */
    private String nmCopy;
    /**
     * 办件id
     */
    private String applyId;
    /**
     * 情形材料主键
     */
    private String rowguid;
}
