package com.zfsoft.service.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: Treeselect树结构实体类
 * @author: wuxx
 * @Date: 2020/9/9 15:15
 **/
public class TreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * 标识
     */
    private String identity;

    /**
     * 是否多选
     */
    private Boolean disabled;

    public TreeSelect()
    {

    }

    /**
     * TreeSelect
     * @param treeSelect
     */
    public TreeSelect(TreeSelect treeSelect)
    {
        this.id = treeSelect.getId();
        this.label = treeSelect.getLabel();
        this.parentId = treeSelect.getParentId();
        this.children =treeSelect.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
        this.identity = treeSelect.getIdentity();
        this.disabled = treeSelect.getDisabled();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
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

    public List<TreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelect> children)
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
