package com.zfsoft.superwindow.controller.clzs.ser;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: Treeselect树结构实体类
 * @author: wangxl
 * @Date: 2020/10/29
 **/
public class MaterialCatalogTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MaterialCatalogTreeSelect> children;

    /**
     * 父节点
     */
    private String parentId;

    public MaterialCatalogTreeSelect()
    {

    }

    /**
     * TreeSelect
     * @param treeSelect
     */
    public MaterialCatalogTreeSelect(MaterialCatalogTreeSelect treeSelect)
    {
        this.id = treeSelect.getId();
        this.label = treeSelect.getLabel();
        this.parentId = treeSelect.getParentId();
        this.children =treeSelect.getChildren().stream().map(MaterialCatalogTreeSelect::new).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<MaterialCatalogTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<MaterialCatalogTreeSelect> children)
    {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
