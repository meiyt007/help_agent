package com.zfsoft.superwindow.controller.clzs.ser;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;

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
public class GenDataMaterialCatalogTreeUtil {
    /**
     * 下拉树构建
     * @author wangxl
     * @date 2020/10/29
     * @param treeSelects 下拉树
     * @return
     */
    private static List<MaterialCatalogTreeSelect> buildTree(List<MaterialCatalogTreeSelect> treeSelects)
    {
        List<MaterialCatalogTreeSelect> returnList = new ArrayList<MaterialCatalogTreeSelect>();
        List<String> tempList = new ArrayList<String>();
        for (MaterialCatalogTreeSelect treeSelect:treeSelects){
            tempList.add(treeSelect.getId());
        }
        for (Iterator<MaterialCatalogTreeSelect> iterator = treeSelects.iterator(); iterator.hasNext();)
        {
            MaterialCatalogTreeSelect t = (MaterialCatalogTreeSelect) iterator.next();
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
    private static void recursionFn(List<MaterialCatalogTreeSelect> list, MaterialCatalogTreeSelect t)
    {
        // 得到子节点列表
        List<MaterialCatalogTreeSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MaterialCatalogTreeSelect tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<MaterialCatalogTreeSelect> it = childList.iterator();
                while (it.hasNext())
                {
                    MaterialCatalogTreeSelect n = (MaterialCatalogTreeSelect) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private static List<MaterialCatalogTreeSelect> getChildList(List<MaterialCatalogTreeSelect> list, MaterialCatalogTreeSelect t)
    {
        List<MaterialCatalogTreeSelect> tlist = new ArrayList<MaterialCatalogTreeSelect>();
        Iterator<MaterialCatalogTreeSelect> it = list.iterator();
        while (it.hasNext())
        {
            MaterialCatalogTreeSelect n = (MaterialCatalogTreeSelect) it.next();
            if (n.getParentId().equals(t.getId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }


    /**
     * 构建前端所需要套餐主题下拉树结构
     *
     * @param materialCatalogs 目录列表
     * @return 下拉树结构列表
     * @author: wangxl
     * @Date: 2020/10/29
     **/
    public static List<MaterialCatalogTreeSelect> buildMaterialCatalogTreeSelect(List<MaterialCatalog> materialCatalogs) {
        List<MaterialCatalogTreeSelect> treeSelectList = materialCatalogs.stream().map(materialCatalog -> {
            MaterialCatalogTreeSelect treeSelect = new MaterialCatalogTreeSelect();
            treeSelect.setId(materialCatalog.getMaterialCatalogOid());
            treeSelect.setLabel(materialCatalog.getCatalogName());
            treeSelect.setParentId(StrUtil.isEmpty(materialCatalog.getMaterialParentOid()) ? "" : materialCatalog.getMaterialParentOid());
            treeSelect.setChildren(new ArrayList<>());
            return treeSelect;
        }).collect(Collectors.toList());
        //添加顶级
        MaterialCatalogTreeSelect topMaterialCatalog = new MaterialCatalogTreeSelect();
        topMaterialCatalog.setId("0");
        topMaterialCatalog.setLabel("目录分类");
        topMaterialCatalog.setParentId("");
        topMaterialCatalog.setChildren(new ArrayList<>());
        treeSelectList.add(topMaterialCatalog);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(MaterialCatalogTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<MaterialCatalogTreeSelect> list, MaterialCatalogTreeSelect t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}