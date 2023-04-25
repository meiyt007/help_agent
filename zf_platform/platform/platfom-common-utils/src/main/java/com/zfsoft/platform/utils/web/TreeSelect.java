package com.zfsoft.platform.utils.web;

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

    /** 节点value */
    private String value;

    /** 节点名称 */
    private String label;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * el-tree多选按钮启禁用  true禁用  false启用
     */
    private boolean disabled;

    /**
     * treeselect 多选按钮启禁用  true禁用  false启用
     */
    private boolean isDisabled;

    /**
     * 标识
     */
    private String identity;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    /**
     * 叶子 true 叶子 ，false 不是叶子
     */
    private boolean leaf;

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
        this.parentId=treeSelect.getParentId();
        this.identity=treeSelect.getIdentity();
        this.disabled = treeSelect.getDisabled();
        this.isDisabled = treeSelect.getIsDisabled();
        this.leaf=treeSelect.isLeaf();
        this.value = treeSelect.getValue();
        this.children =treeSelect.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelect> children)
    {
        this.children = children;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

}
