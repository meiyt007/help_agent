package com.zfsoft.microservice.platform.util;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.*;
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
    public final static String TYPE_DISTRICT = "DISTRICT-";
    /**
     * 组织机构的前缀
     **/
    public final static String TYPE_ORGAN = "ORGAN-";
    /**
     * 用户的前缀
     **/
    public final static String TYPE_USER = "USER-";
    /**
     * 岗位的前缀
     **/
    public final static String TYPE_POST = "POST-";
    /**
     * 应用的前缀
     **/
    public final static String TYPE_APP = "APP-";
    /**
     * 权限的前缀
     **/
    public final static String TYPE_PERMISSION = "PERMISSION-";

    /**
     * 角色的前缀
     **/
    public final static String TYPE_ROLE = "ROLE-";


    /**
     * 构建前端所需要下拉树结构
     *
     * @param sysDistricts 区划列表
     * @return 下拉树结构列表
     * @author: wuxx
     * @Date: 2020/9/9 15:15
     **/
    public static List<TreeSelect> buildSysDistrictTreeSelect(List<SysDistrict> sysDistricts) {
        List<TreeSelect> treeSelectList = sysDistricts.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            String parentOid = StrUtil.isNotEmpty(sysDistrict.getParentOid()) ? sysDistrict.getParentOid() : "";
            treeSelect.setParentId(TYPE_DISTRICT + parentOid);
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");

            treeSelect.setDisabled(sysDistrict.getDisabled() != null ? sysDistrict.getDisabled() : false);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建前端所需要下拉树结构--组织机构
     *
     * @param sysOrgans 组织机构列表
     * @return 下拉树结构列表
     * @author: wuxx
     * @Date: 2020/9/9 18:15
     **/
    public static List<TreeSelect> buildSysOrganTreeSelect(List<SysOrgan> sysOrgans) {
        List<TreeSelect> treeSelectList = sysOrgans.stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            treeSelect.setParentId(TYPE_ORGAN + sysOrgan.getParentOid());
            treeSelect.setIdentity("ORGAN");
            treeSelect.setChildren(new ArrayList<>());
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建权限树
     *
     * @param sysPermissionList 权限列表
     * @return
     * @author zxx
     * @date 2020/9/22 5:10 下午
     */
    public static List<TreeSelect> buildPermissionTreeSelect(List<SysPermission> sysPermissionList, boolean disable) {
        //权限列表
        List<TreeSelect> perTreeSelectList = sysPermissionList.stream().map(sysPermission -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_PERMISSION + sysPermission.getPermissionOid());
            treeSelect.setLabel(sysPermission.getName());
            treeSelect.setParentId(TYPE_PERMISSION + sysPermission.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("PERMISSION");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        perTreeSelectList = buildTree(perTreeSelectList);
        return perTreeSelectList;
    }

    /**
     * 生成应用权限下拉树
     *
     * @param sysAppList        应用列表
     * @param sysPermissionList 权限列表
     * @return
     * @author zxx
     * @date 2020/9/11 4:04 下午
     */
    public static List<TreeSelect> buildAppAndPermissionTreeSelect(List<SysApp> sysAppList, List<SysPermission> sysPermissionList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //应用列表
        List<TreeSelect> appTreeSelectList = sysAppList.stream().map(sysApp -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_APP + sysApp.getAppOid());
            treeSelect.setLabel(sysApp.getName());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("APP");
            return treeSelect;
        }).collect(Collectors.toList());
        //权限列表
        List<TreeSelect> perTreeSelectList = sysPermissionList.stream().map(sysPermission -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_PERMISSION + sysPermission.getPermissionOid());
            treeSelect.setLabel(sysPermission.getName());
            treeSelect.setParentId(StrUtil.isEmpty(sysPermission.getParentOid()) ? TYPE_APP + sysPermission.getAppOid() : TYPE_PERMISSION + sysPermission.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("PERMISSION");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(appTreeSelectList);
        treeSelectList.addAll(perTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建应用、角色树
     *
     * @param sysAppList  应用
     * @param sysRoleList 角色
     * @param disable     多选启禁用
     * @return
     * @author zxx
     * @date 2020/9/24 1:59 下午
     */
    public static List<TreeSelect> buildAppAndRoleTreeSelect(List<SysApp> sysAppList, List<SysRole> sysRoleList, boolean disable) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //应用列表
        List<TreeSelect> appTreeSelectList = sysAppList.stream().map(sysApp -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_APP + sysApp.getAppOid());
            treeSelect.setLabel(sysApp.getName());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("APP");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        //权限列表
        List<TreeSelect> roleTreeSelectList = sysRoleList.stream().map(sysRole -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ROLE + sysRole.getRoleOid());
            treeSelect.setLabel(sysRole.getName());
            treeSelect.setParentId(TYPE_APP + sysRole.getAppOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ROLE");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(appTreeSelectList);
        treeSelectList.addAll(roleTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建区划、机构、用户数
     *
     * @param sysDistrictList 区划列表
     * @param sysOrganList    机构列表
     * @param sysUserList     用户列表
     * @param disable         多选启禁用
     * @return
     * @author zxx
     * @date 2020/9/24 2:07 下午
     */
    public static List<TreeSelect> buildDistrictAndOrganAndUserTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList, List<SysUser> sysUserList, boolean disable) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setValue(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysDistrict.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            treeSelect.setDisabled(disable);
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
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        //用户列表
        List<TreeSelect> userTreeSelectList = sysUserList.stream().map(sysUser -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_USER + sysUser.getUserOid());
            treeSelect.setValue(TYPE_USER + sysUser.getUserOid());
            treeSelect.setLabel(sysUser.getName());
            treeSelect.setParentId(TYPE_ORGAN + sysUser.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("USER");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(userTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建区划、机构、办理岗位
     *
     * @param sysDistrictList 区划列表
     * @param sysOrganList    机构列表
     * @param sysPostList     岗位列表
     * @return
     * @author wuxx
     * @date 2021/1/26 2:07 下午
     */
    public static List<TreeSelect> buildDistrictAndOrganAndPostTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList, List<SysPost> sysPostList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setValue(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysDistrict.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
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
            return treeSelect;
        }).collect(Collectors.toList());
        //岗位列表
        List<TreeSelect> userTreeSelectList = sysPostList.stream().map(sysPost -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_POST + sysPost.getPostOid());
            treeSelect.setValue(TYPE_POST + sysPost.getPostOid());
            treeSelect.setLabel(sysPost.getName());
            treeSelect.setParentId(TYPE_ORGAN + sysPost.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("POST");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(userTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }


    /**
     * 构建区划机构树
     *
     * @param sysDistrictList 区划列表
     * @param sysOrganList    机构列表
     * @return
     * @author zxx
     * @date 2020/9/25 3:18 下午
     */
    public static List<TreeSelect> buildDistrictAndOrganTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            String parentOid = StrUtil.isNotEmpty(sysDistrict.getParentOid()) ? sysDistrict.getParentOid() : "";
            treeSelect.setParentId(TYPE_DISTRICT + parentOid);
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            return treeSelect;
        }).collect(Collectors.toList());
        //机构列表
        List<TreeSelect> organTreeSelectList = sysOrganList.stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysOrgan.getDistrictOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ORGAN");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建区划机构树
     *
     * @param sysDistrictList 区划列表
     * @param sysOrganList    机构列表
     * @return
     * @author wuxx
     * @date 2021/1/4
     */
    public static List<TreeSelect> buildOnlyDistrictAndOrganTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_DISTRICT + sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            String parentOid = StrUtil.isNotEmpty(sysDistrict.getParentOid()) ? sysDistrict.getParentOid() : "0";
            treeSelect.setParentId(TYPE_DISTRICT + parentOid);
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            return treeSelect;
        }).collect(Collectors.toList());
        //机构列表
        List<TreeSelect> organTreeSelectList = sysOrganList.stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            String districtOid = StrUtil.isNotEmpty(sysOrgan.getDistrictOid()) ? sysOrgan.getDistrictOid() : "0";
            treeSelect.setParentId(TYPE_DISTRICT + districtOid);
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ORGAN");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建机构用户列表
     *
     * @param sysOrganList 机构列表
     * @param sysUserList  用户列表
     * @param disable
     * @return
     * @author zxx
     * @date 2020/9/25 3:19 下午
     */
    public static List<TreeSelect> buildOrganAndUserTreeSelect(List<SysOrgan> sysOrganList, List<SysUser> sysUserList, boolean disable) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //机构列表
        List<TreeSelect> organTreeSelectList = sysOrganList.stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_ORGAN + sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            treeSelect.setParentId(TYPE_DISTRICT + sysOrgan.getDistrictOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ORGAN");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        //用户列表
        List<TreeSelect> userTreeSelectList = sysUserList.stream().map(sysUser -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_USER + sysUser.getUserOid());
            treeSelect.setLabel(sysUser.getName());
            treeSelect.setParentId(TYPE_ORGAN + sysUser.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("USER");
            treeSelect.setDisabled(disable);
            treeSelect.setLeaf(true);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(userTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList;
    }

    /**
     * 构建用户数
     *
     * @param sysUserList 用户列表
     * @param disable     多选启禁用
     * @return
     * @author zxx
     * @date 2020/9/24 6:57 下午
     */
    public static List<TreeSelect> buildUserTreeSelect(List<SysUser> sysUserList, boolean disable) {
        //用户列表
        List<TreeSelect> userTreeSelectList = sysUserList.stream().map(sysUser -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(TYPE_USER + sysUser.getUserOid());
            treeSelect.setLabel(sysUser.getName());
            treeSelect.setParentId(TYPE_ORGAN + sysUser.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("USER");
            treeSelect.setDisabled(disable);
            return treeSelect;
        }).collect(Collectors.toList());
        return userTreeSelectList;
    }

    //------------------------------------去除最底层数据分割线-----------------------------------

    /**
     * @param treeSelects tree数据
     * @param type        类型
     * @description: 去除设置类型的tree数据
     * @author: wuxx
     * @Date: 2021/2/26 11:35
     **/
    public static List<TreeSelect> buildRemoveLastTree(List<TreeSelect> treeSelects, String type) {
        if (treeSelects.size() > 0) {
            Iterator it = treeSelects.iterator();
            while (it.hasNext()) {
                TreeSelect next = (TreeSelect) it.next();
                if (null != next.getChildren() && next.getChildren().size() > 0) {
                    buildRemoveLastTree(next.getChildren(), type);
                } else {
                    if (!next.getId().contains(type)) {
                        it.remove();
                    }
                }
            }
        }
        return treeSelects;
    }

    /**
     * @param treeSelects tree数据
     * @param type        类型
     * @description: 设置类型的tree数据无最下级则设置为不可选中
     * @author: wuxx
     * @Date: 2021/2/26 11:35
     **/
    public static List<TreeSelect> buildDisabledLastTree(List<TreeSelect> treeSelects, String type) {
        if (treeSelects.size() > 0) {
            Iterator it = treeSelects.iterator();
            while (it.hasNext()) {
                TreeSelect next = (TreeSelect) it.next();
                if (null != next.getChildren() && next.getChildren().size() > 0) {
                    buildDisabledLastTree(next.getChildren(), type);
                } else {
                    if (!next.getId().contains(type)) {
                        next.setIsDisabled(true);
                        next.setDisabled(true);
                    }
                }
            }
        }
        return treeSelects;
    }

    //------------------------------------去除最底层数据分割线-----------------------------------

    //------------------------------------分割线-----------------------------------

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
