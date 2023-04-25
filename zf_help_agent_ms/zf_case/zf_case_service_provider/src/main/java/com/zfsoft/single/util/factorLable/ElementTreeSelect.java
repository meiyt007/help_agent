package com.zfsoft.single.util.factorLable;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: ElementSelect树结构实体类
 * @author: wangxl
 * @Date: 2021/1/15
 **/
public class ElementTreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ElementTreeSelect> children;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * 顶级节点
     */
    private String topOid;

    public ElementTreeSelect()
    {

    }

    /**
     * TreeSelect
     * @param treeSelect
     */
    public ElementTreeSelect(ElementTreeSelect treeSelect)
    {
        this.id = treeSelect.getId();
        this.label = treeSelect.getLabel();
        this.parentId = treeSelect.getParentId();
        this.topOid = treeSelect.getTopOid();
        this.children =treeSelect.getChildren().stream().map(ElementTreeSelect::new).collect(Collectors.toList());
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

    public List<ElementTreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<ElementTreeSelect> children)
    {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTopOid() {
        return topOid;
    }

    public void setTopOid(String topOid) {
        this.topOid = topOid;
    }
}
