package com.zfsoft.microservice.workflow.util;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.web.TreeSelect;

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
     * 区划的前缀
     **/
    private final static String TYPE_DISTRICT="DISTRICT-";
    /**
     * 组织机构的前缀
     **/
    private final static String TYPE_ORGAN="ORGAN-";
    /**
     * 流程的前缀
     **/
    public final static String TYPE_FLOW="FLOW-";

    /**
     * 构建区划、机构、流程
     * @author wuxx
     * @date 2021/02/2 2:07 下午
     * @param sysDistrictList 区划列表
     * @param sysOrganList 机构列表
     * @param workflowList 流程列表
     * @return
     */
    public static List<TreeSelect> buildDistrictAndOrganAndWorkflowTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList,List<WorkflowBussInfo> workflowList){
        List<TreeSelect> treeSelectList=new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setValue(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysDistrict.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            treeSelect.setDisabled(true);
            return treeSelect;
        }).collect(Collectors.toList());
        //机构列表
        List<TreeSelect> organTreeSelectList = sysOrganList.stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setValue(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysOrgan.getDistrictOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ORGAN");
            treeSelect.setDisabled(true);
            return treeSelect;
        }).collect(Collectors.toList());
        //流程列表
        List<TreeSelect> flowTreeSelectList = workflowList.stream().map(workflowBussInfo -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_FLOW + workflowBussInfo.getInfoOid());
            treeSelect.setValue(TYPE_FLOW + workflowBussInfo.getInfoOid());
            treeSelect.setLabel(workflowBussInfo.getWorkflowName());
            treeSelect.setParentId(TYPE_ORGAN + workflowBussInfo.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("FLOW");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(flowTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    //------------------------------------分割线-----------------------------------

    //------------------------------------去除最底层数据分割线-----------------------------------

    /**
     * @description: 去除设置类型的tree数据
     * @param treeSelects tree数据
     * @param type 类型
     * @author: wuxx
     * @Date: 2021/2/26 11:35
     **/
    public static List<TreeSelect> buildRemoveLastTree(List<TreeSelect> treeSelects,String type) {
        if(treeSelects.size()>0){
            Iterator it = treeSelects.iterator();
            while(it.hasNext()){
                TreeSelect next = (TreeSelect) it.next();
                if(null!=next.getChildren() && next.getChildren().size()>0){
                    buildRemoveLastTree(next.getChildren(),type);
                }else{
                    if(!next.getId().contains(type)){
                        it.remove();
                    }
                }
            }
        }
        return  treeSelects;
    }

    /**
     * @description: 设置类型的tree数据无最下级则设置为不可选中
     * @param treeSelects tree数据
     * @param type 类型
     * @author: wuxx
     * @Date: 2021/2/26 11:35
     **/
    public static List<TreeSelect> buildDisabledLastTree(List<TreeSelect> treeSelects,String type) {
        if(treeSelects.size()>0){
            Iterator it = treeSelects.iterator();
            while(it.hasNext()){
                TreeSelect next = (TreeSelect) it.next();
                if(null!=next.getChildren() && next.getChildren().size()>0){
                    buildDisabledLastTree(next.getChildren(),type);
                }else{
                    if(!next.getId().contains(type)){
                        next.setIsDisabled(true);
                        next.setDisabled(true);
                    }
                }
            }
        }
        return  treeSelects;
    }

    //------------------------------------去除最底层数据分割线-----------------------------------


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
        Map<String,TreeSelect> tempMap = new HashMap<>();
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

    private static List<TreeSelect> createTree(List<TreeSelect> dataList, Map<String,TreeSelect> parentMap) {
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
