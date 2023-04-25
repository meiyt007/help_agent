package com.zfsoft.superwindow.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @description: Treeselect树结构实体类
 * @author: wangxl
 * @Date: 2020/10/29
 **/
@Data
@ToString
public class ElectronicLicenseTree implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;
    private String value;


    /** 节点名称 */
    private String label;

    private String code;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ElectronicLicenseTree> children;

    /**
     * 父节点
     */
    private String parentId;

}
