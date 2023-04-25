package com.zfsoft.single.util.factorLable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GenDataFactorLableTreeUtil
 * @Description: 生成业务层级树数据的工具类
 * @Author wangxl
 * @Date 2020/10/29
 **/
public class GenDataElementTreeUtil {
    /**
     * 下拉树构建
     * @author wangxl
     * @date 2021/1/15
     * @param treeSelects 下拉树
     * @return
     */
    private static List<ElementTreeSelect> buildTree(List<ElementTreeSelect> treeSelects)
    {
        List<ElementTreeSelect> returnList = new ArrayList<ElementTreeSelect>();
        List<String> tempList = new ArrayList<String>();
        for (ElementTreeSelect treeSelect:treeSelects){
            tempList.add(treeSelect.getId());
        }
        for (Iterator<ElementTreeSelect> iterator = treeSelects.iterator(); iterator.hasNext();)
        {
            ElementTreeSelect t = (ElementTreeSelect) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (!tempList.contains(t.getParentId())){
                recursionFn(treeSelects, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = treeSelects;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static void recursionFn(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        // 得到子节点列表
        List<ElementTreeSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (ElementTreeSelect tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<ElementTreeSelect> it = childList.iterator();
                while (it.hasNext())
                {
                    ElementTreeSelect n = (ElementTreeSelect) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private static List<ElementTreeSelect> getChildList(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        List<ElementTreeSelect> tlist = new ArrayList<ElementTreeSelect>();
        Iterator<ElementTreeSelect> it = list.iterator();
        while (it.hasNext())
        {
            ElementTreeSelect n = (ElementTreeSelect) it.next();
            if (n.getParentId().equals(t.getId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    private static List<ElementTreeSelect> buildFactorTypeTree(List<ElementTreeSelect> treeSelects)
    {
        List<ElementTreeSelect> returnList = new ArrayList<ElementTreeSelect>();
        List<String> tempList = new ArrayList<String>();
        for (ElementTreeSelect treeSelect:treeSelects){
            tempList.add(treeSelect.getId());
        }
        for (Iterator<ElementTreeSelect> iterator = treeSelects.iterator(); iterator.hasNext();)
        {
            ElementTreeSelect t = (ElementTreeSelect) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (!tempList.contains(t.getParentId())){
                recursionFn2(treeSelects, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = treeSelects;
        }
        return returnList;
    }

    private static void recursionFn2(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        // 得到子节点列表
        List<ElementTreeSelect> childList = getChildList2(list, t);
        t.setChildren(childList);
        for (ElementTreeSelect tChild : childList)
        {
            if (hasChild2(list, tChild))
            {
                // 判断是否有子节点
                Iterator<ElementTreeSelect> it = childList.iterator();
                while (it.hasNext())
                {
                    ElementTreeSelect n = (ElementTreeSelect) it.next();
                    recursionFn2(list, n);
                }
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild2(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        return getChildList2(list, t).size() > 0 ? true : false;
    }

    private static List<ElementTreeSelect> getChildList2(List<ElementTreeSelect> list, ElementTreeSelect t)
    {
        List<ElementTreeSelect> tlist = new ArrayList<ElementTreeSelect>();
        Iterator<ElementTreeSelect> it = list.iterator();
        while (it.hasNext())
        {
            ElementTreeSelect n = (ElementTreeSelect) it.next();
            if (n.getParentId().equals(t.getId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 构建前端所需要业务要素下拉树结构
     *
     * @param treeSelects 树列表
     * @return 下拉树结构列表
     * @author: yuy
     * @Date: 2021/1/22
     **/
    public static List<ElementTreeSelect> buildSunElementTreeSelect(List<ElementTreeSelect> treeSelects) {
        treeSelects = buildTree(treeSelects);
        return treeSelects.stream().map(ElementTreeSelect::new).collect(Collectors.toList());
    }



}
