package com.zfsoft.service.util;

import com.zfsoft.service.common.data.TreeStrSelect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName GenDataStrTreeUtil
 * @Description: 生成树数据的工具类
 * @Author yuy
 * @Date 2021/1/21
 **/
public class GenDataStrTreeUtil {

    //--------------------------------------------------分割线----------------------------------------

    /**
     * 下拉树构建
     * @Author yuy
     * @Date 2021/1/21
     * @param treeSelects 下拉树
     * @return
     */
    public static List<TreeStrSelect> buildTree(List<TreeStrSelect> treeSelects)
    {
        List<TreeStrSelect> returnList = new ArrayList<TreeStrSelect>();
        List<String> tempList = new ArrayList<String>();
        for (TreeStrSelect treeSelect:treeSelects){
            tempList.add(treeSelect.getId());
        }
        for (Iterator<TreeStrSelect> iterator = treeSelects.iterator(); iterator.hasNext();)
        {
            TreeStrSelect t = (TreeStrSelect) iterator.next();
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
    private static void recursionFn(List<TreeStrSelect> list, TreeStrSelect t)
    {
        // 得到子节点列表
        List<TreeStrSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TreeStrSelect tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<TreeStrSelect> it = childList.iterator();
                while (it.hasNext())
                {
                    TreeStrSelect n = (TreeStrSelect) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private static List<TreeStrSelect> getChildList(List<TreeStrSelect> list, TreeStrSelect t)
    {
        List<TreeStrSelect> tlist = new ArrayList<TreeStrSelect>();
        Iterator<TreeStrSelect> it = list.iterator();
        while (it.hasNext())
        {
            TreeStrSelect n = (TreeStrSelect) it.next();
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
    private static boolean hasChild(List<TreeStrSelect> list, TreeStrSelect t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}
