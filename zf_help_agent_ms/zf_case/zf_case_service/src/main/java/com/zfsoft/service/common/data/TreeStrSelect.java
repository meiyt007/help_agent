package com.zfsoft.service.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TreeStrSelect树结构实体类
 * @author: yuy
 * @Date: 2021/1/21
 **/
public class TreeStrSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeStrSelect> children;

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

    public TreeStrSelect()
    {

    }

    /**
     * TreeSelect
     * @param treeSelect
     */
    public TreeStrSelect(TreeStrSelect treeSelect)
    {
        this.id = treeSelect.getId();
        this.label = treeSelect.getLabel();
        this.parentId = treeSelect.getParentId();
        this.children =treeSelect.getChildren().stream().map(TreeStrSelect::new).collect(Collectors.toList());
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

    public List<TreeStrSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeStrSelect> children)
    {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
}
