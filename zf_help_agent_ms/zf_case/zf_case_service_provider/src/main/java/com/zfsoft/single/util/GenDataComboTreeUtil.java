package com.zfsoft.single.util;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GenDataComboTreeUtil
 * @Description: 生成树数据的工具类
 * @Author wangxl
 * @Date 2020/10/29
 **/
public class GenDataComboTreeUtil {
    /**
     * 下拉树构建
     * @author wangxl
     * @date 2020/10/29
     * @param treeSelects 下拉树
     * @return
     */
    private static List<ComboTreeSelect> buildTree(List<ComboTreeSelect> treeSelects)
    {
        List<ComboTreeSelect> returnList = new ArrayList<ComboTreeSelect>();
        List<String> tempList = new ArrayList<String>();
        for (ComboTreeSelect treeSelect:treeSelects){
            tempList.add(treeSelect.getId());
        }
        for (Iterator<ComboTreeSelect> iterator = treeSelects.iterator(); iterator.hasNext();)
        {
            ComboTreeSelect t = (ComboTreeSelect) iterator.next();
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
    private static void recursionFn(List<ComboTreeSelect> list, ComboTreeSelect t)
    {
        // 得到子节点列表
        List<ComboTreeSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (ComboTreeSelect tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<ComboTreeSelect> it = childList.iterator();
                while (it.hasNext())
                {
                    ComboTreeSelect n = (ComboTreeSelect) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private static List<ComboTreeSelect> getChildList(List<ComboTreeSelect> list, ComboTreeSelect t)
    {
        List<ComboTreeSelect> tlist = new ArrayList<ComboTreeSelect>();
        Iterator<ComboTreeSelect> it = list.iterator();
        while (it.hasNext())
        {
            ComboTreeSelect n = (ComboTreeSelect) it.next();
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
    private static boolean hasChild(List<ComboTreeSelect> list, ComboTreeSelect t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}