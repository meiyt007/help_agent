package com.zfsoft.microservice.form.util;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormAuthorize;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.web.TreeSelect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GenDataTreeUtil
 * @Description: 生成树数据的工具类
 * @Author wuxx
 * @Date 2020/9/1
 **/
public class GenDataTreeUtil {

    /**
     * 表单的前缀
     **/
    public final static String TYPE_FORM="FORM-";

    /**
     * 业务系统的前缀
     **/
    public final static String TYPE_AUTHORIZE="AUTHORIZE-";

    /**
     * 表单的ID(默认32个0)
     **/
    public final static String TYPE_FORM_ID="00000000000000000000000000000000";

    /**
     *  默认的tree结构（包含模板和表单）
     **/
    private static List<TreeSelect> defaultTreeList = new ArrayList<>();

    static {
        //初始化默认的tree
        TreeSelect fromTree = new TreeSelect();
        fromTree.setId(TYPE_FORM + TYPE_FORM_ID);
        fromTree.setValue(TYPE_FORM + TYPE_FORM_ID);
        fromTree.setLabel("接入系统列表");
        fromTree.setParentId(null);
        fromTree.setChildren(new ArrayList<>());
        fromTree.setIdentity(TYPE_FORM.replace("-",""));
        fromTree.setDisabled(false);
        defaultTreeList.add(fromTree);
    }

    /**
     * @description:  构建接入系统的tree
     * @param formAuthorizeList 业务系统
     * @author: wuxx
     * @Date: 2021/4/7 13:48
     **/
    public static List<TreeSelect> buildFormAuthorizeTreeSelect(List<FormAuthorize> formAuthorizeList){
        List<TreeSelect> treeSelectList=new ArrayList<>();
        //业务系统列表
        List<TreeSelect> authorizeTreeSelectList = null != formAuthorizeList ? formAuthorizeList.stream().map(formAuthorize -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_AUTHORIZE + formAuthorize.getAuthorizeKey());
            treeSelect.setValue((TYPE_AUTHORIZE + formAuthorize.getAuthorizeKey()));
            treeSelect.setLabel(formAuthorize.getSystemName());
            treeSelect.setParentId(TYPE_FORM + TYPE_FORM_ID);
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity(TYPE_AUTHORIZE.replace("-",""));
            treeSelect.setDisabled(false);
            return treeSelect;
        }).collect(Collectors.toList()):null;
        //增加默认的数据
        //初始化默认的tree
        treeSelectList.addAll(defaultTreeList);
        if(null!=authorizeTreeSelectList && authorizeTreeSelectList.size()>0){
            treeSelectList.addAll(authorizeTreeSelectList);
        }
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    //--------------------------------------------------分割线----------------------------------------

    /**
     * 下拉树构建
     *
     * @param treeSelects 下拉树
     * @author zxx
     */
    private static List<TreeSelect> buildTree(List<TreeSelect> treeSelects) {
        List<TreeSelect> returnList = new ArrayList<TreeSelect>();
        List<String> tempList = new ArrayList<String>();
        List<String> pIdsList = new ArrayList<String>();
        for (TreeSelect treeSelect : treeSelects) {
            tempList.add(treeSelect.getId());
            if(StrUtil.isEmpty(treeSelect.getParentId())){
                pIdsList.add(BaseStaticParameter.YES);
            }
        }
        for (Iterator<TreeSelect> iterator = treeSelects.iterator(); iterator.hasNext(); ) {
            TreeSelect t = (TreeSelect) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (!tempList.contains(t.getParentId())) {
                recursionFn(treeSelects, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty()) {
            returnList = treeSelects;
        }
        //如果父类id不为空，排除
        if (null != returnList && returnList.size() > 1) {
            Iterator it = returnList.iterator();
            while(it.hasNext()){
                TreeSelect select = (TreeSelect)it.next();
                if ((null!=pIdsList && pIdsList.contains(BaseStaticParameter.YES)) && StrUtil.isNotEmpty(select.getParentId())) {
                    it.remove(); //移除该对象
                }

            }
        }
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

}
