package com.zfsoft.microservice.settings.util;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GenDataTreeUtil
 * @Description: 生成树数据的工具类
 * @Author wuxx
 * @Date 2020/9/1
 **/
public class GenDataTreeUtil {

    /**
     * 生成数据字典下拉树树
     *
     * @param sysDictList 数据字典列表
     * @author wuxx
     * @date 2020/9/12 14:04
     */
    public static List<TreeSelect> buildSysDictTreeSelect(List<SysDict> sysDictList) {
        //数据字典列表
        List<TreeSelect> treeSelectList = sysDictList.stream().map(dict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(dict.getDictOid());
            treeSelect.setLabel(dict.getName());
            if(StrUtil.isEmpty(dict.getParentOid())){
                treeSelect.setParentId("");
            }else{
                treeSelect.setParentId(dict.getParentOid());
            }
            treeSelect.setChildren(new ArrayList<>());
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList=buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 生成参数配置下拉树树
     *
     * @param sysConfigList 参数配置列表
     * @author wuxx
     * @date 2020/9/12 14:04
     */
    public static List<TreeSelect> buildSysConfigTreeSelect(List<SysConfig> sysConfigList) {
        //参数配置列表
        List<TreeSelect> treeSelectList = sysConfigList.stream().map(config -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(config.getConfigOid());
            treeSelect.setLabel(config.getName());
            if(StrUtil.isEmpty(config.getParentOid())){
                treeSelect.setParentId("");
            }else{
                treeSelect.setParentId(config.getParentOid());
            }
            treeSelect.setChildren(new ArrayList<>());
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList=buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    //--------------------------------------------------分割线----------------------------------------

    /**
     * 下拉树构建
     *
     * @param treeSelects 下拉树
     * @return
     * @author zxx
     * @date 2020/9/11 4:05 下午
     */
    private static List<TreeSelect> buildTree(List<TreeSelect> treeSelects) {
        List<TreeSelect> returnList = new ArrayList<TreeSelect>();
        Map<String, TreeSelect> tempMap = new HashMap<>();
        List<String> pIdsList = new ArrayList<String>();
        for (TreeSelect treeSelect : treeSelects) {
            tempMap.put(treeSelect.getId(),treeSelect);
            if (StrUtil.isEmpty(treeSelect.getParentId())) {
                pIdsList.add(BaseStaticParameter.YES);
            }
        }
        returnList = createTree(treeSelects, tempMap);
//        for (Iterator<TreeSelect> iterator = treeSelects.iterator(); iterator.hasNext(); ) {
//            TreeSelect t = (TreeSelect) iterator.next();
//            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
//            if (!tempList.contains(t.getParentId())) {
//                recursionFn(treeSelects, t);
//                returnList.add(t);
//            }
//        }
        if (returnList.isEmpty()) {
            returnList = treeSelects;
        }
        //如果父类id不为空，排除
//        if (null != returnList && returnList.size() > 1) {
//            Iterator it = returnList.iterator();
//            while(it.hasNext()){
//                TreeSelect select = (TreeSelect)it.next();
//                if ((null!=pIdsList && pIdsList.contains(BaseStaticParameter.YES)) && StrUtil.isNotEmpty(select.getParentId())) {
//                    it.remove(); //移除该对象
//                }
//
//            }
//        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static void recursionFn(List<TreeSelect> list, TreeSelect t) {
        // 得到子节点列表
        List<TreeSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TreeSelect tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<TreeSelect> it = childList.iterator();
                while (it.hasNext()) {
                    TreeSelect n = (TreeSelect) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<TreeSelect> getChildList(List<TreeSelect> list, TreeSelect t) {
        List<TreeSelect> tlist = new ArrayList<TreeSelect>();
        Iterator<TreeSelect> it = list.iterator();
        while (it.hasNext()) {
            TreeSelect n = (TreeSelect) it.next();
            if (t.getId().equals(n.getParentId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<TreeSelect> list, TreeSelect t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    private static List<TreeSelect> createTree(List<TreeSelect> dataList, Map<String, TreeSelect> parentMap) {
        Map<String, List<TreeSelect>> tempMap = new HashMap<>();
        List<TreeSelect> menuList = new ArrayList<>();
        dataList.stream().forEach(node -> {
            String id = node.getId();
            String parentId = node.getParentId();
            //父主键不存在，说明是顶级
            if (parentMap.get(parentId)==null) {
                menuList.add(node);
            }else {
                List<TreeSelect> tempList = tempMap.get(parentId);
                if (tempList == null) {
                    tempList = new ArrayList<>();
                    tempMap.put(parentId, tempList);
                }
                tempList.add(node);
            }
            List<TreeSelect> childList = tempMap.get(id);
            if (childList == null) {
                childList = new ArrayList<>();
                tempMap.put(id, childList);
            }
            node.setChildren(childList);
        });

        return menuList;
    }

}
