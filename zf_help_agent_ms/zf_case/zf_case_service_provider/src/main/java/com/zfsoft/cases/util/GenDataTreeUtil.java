package com.zfsoft.cases.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.data.vo.SysUserVo;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.superwindow.data.yxpz.SysAreaSite;

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

    public static List<TreeSelect> buildServiceTypeTreeSelect(List<SxServiceType> sxServiceTypeList) {
        List<TreeSelect> treeSelectList = sxServiceTypeList.stream().map(sxServiceType -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sxServiceType.getServiceTypeOid());
            treeSelect.setLabel(sxServiceType.getServiceTypeName());
            //treeSelect.setParentId();
            treeSelect.setChildren(new ArrayList<>());
            //treeSelect.setIdentity("SERVICETYPE");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList = buildTree(treeSelectList);
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
            treeSelect.setId(String.valueOf(config.getId()));
            treeSelect.setLabel(config.getName());
            treeSelect.setParentId(String.valueOf(null == config.getParentOid() ? 0 : Long.parseLong(config.getParentOid())));
            treeSelect.setChildren(new ArrayList<>());
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    //--------------------------------------------------分割线----------------------------------------

    /**
     * 下拉树构建
     *
     * @param treeSelects 下拉树
     * @return
     * @author wuxx
     * @date 2020/9/11 4:05 下午
     */
    private static List<TreeSelect> buildTree(List<TreeSelect> treeSelects) {
        List<TreeSelect> returnList = new ArrayList<TreeSelect>();
        List<String> tempList = new ArrayList<>();
        for (TreeSelect treeSelect : treeSelects) {
            tempList.add(treeSelect.getId());
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
            if (n.getParentId() != null && n.getParentId().equals(t.getId())) {
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

    public static List<TreeSelect> buildSiteAuthorizeTreeSelect(List<SysAreaSite> sysAreaSiteList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //事项列表
        List<TreeSelect> sxTreeSelectList = Optional.ofNullable(sysAreaSiteList).orElseGet(Lists::newArrayList).stream().map(sx -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sx.getSysAreaSiteOid());
            treeSelect.setLabel(sx.getSiteName());
            //treeSelect.setDisabled(true);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(sxTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public static List<TreeSelect> buildUserTreeSelect(List<SysUser> sysUserList) {
        List<TreeSelect> treeSelectList = sysUserList.stream().map(sysuser -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sysuser.getUserOid());
            treeSelect.setLabel(sysuser.getName());
            treeSelect.setParentId("0");
            treeSelect.setIdentity("USER");
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public static List<TreeSelect> buildUserTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList, List<SysUserVo> userList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //用户列表
        Set<String> organSet = new HashSet<>();
        Set<String> districtSet = new HashSet<>();
        List<TreeSelect> userTreeSelectList = Optional.ofNullable(userList).orElseGet(Lists::newArrayList).stream().map(user -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(user.getUserOid());
            treeSelect.setLabel(user.getName());
            treeSelect.setParentId(user.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("USER");
            treeSelect.setDisabled(true);
            districtSet.add(user.getDistrictOid());
            organSet.add(user.getOrganOid());
            return treeSelect;
        }).collect(Collectors.toList());
        //区划列表
        List<TreeSelect> districtTreeSelectList = new ArrayList<>();
        for (SysDistrict sysDistrict : sysDistrictList) {
            if (districtSet.contains(sysDistrict.getDistrictOid())) {
                TreeSelect treeSelect = new TreeSelect();
                treeSelect.setId(sysDistrict.getDistrictOid());
                treeSelect.setLabel(sysDistrict.getName());
                treeSelect.setParentId(sysDistrict.getParentOid());
                treeSelect.setChildren(new ArrayList<>());
                treeSelect.setIdentity("DISTRICT");
                treeSelect.setDisabled(false);
                districtTreeSelectList.add(treeSelect);
            }
        }
        //机构列表
        List<TreeSelect> organTreeSelectList = new ArrayList<>();
        for (SysOrgan sysOrgan : sysOrganList) {
            if (organSet.contains(sysOrgan.getOrganOid())) {
                TreeSelect treeSelect = new TreeSelect();
                treeSelect.setId(sysOrgan.getOrganOid());
                treeSelect.setLabel(sysOrgan.getName());
                treeSelect.setParentId(String.valueOf(sysOrgan.getDistrictOid()));
                treeSelect.setChildren(new ArrayList<>());
                treeSelect.setIdentity("ORGAN");
                treeSelect.setDisabled(false);
                organTreeSelectList.add(treeSelect);
            }
        }
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(userTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public static List<TreeSelect> buildDistrictAndOrganAndServiceTreeSelect(List<SysDistrict> sysDistrictList, List<SysOrgan> sysOrganList, List<SxService> sxServiceList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //区划列表
        List<TreeSelect> districtTreeSelectList = sysDistrictList.stream().map(sysDistrict -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            treeSelect.setParentId(sysDistrict.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            treeSelect.setDisabled(false);
            return treeSelect;
        }).collect(Collectors.toList());
        //机构列表
        List<TreeSelect> organTreeSelectList = Optional.ofNullable(sysOrganList).orElseGet(Lists::newArrayList).stream().map(sysOrgan -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sysOrgan.getOrganOid());
            treeSelect.setLabel(sysOrgan.getName());
            treeSelect.setParentId(String.valueOf(sysOrgan.getDistrictOid()));
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("ORGAN");
            treeSelect.setDisabled(false);
            return treeSelect;
        }).collect(Collectors.toList());
        //事项列表
        List<TreeSelect> sxTreeSelectList = Optional.ofNullable(sxServiceList).orElseGet(Lists::newArrayList).stream().map(sxService -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sxService.getServiceOid());
            treeSelect.setLabel(sxService.getServiceName());
            treeSelect.setParentId(sxService.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("SXSERVICE");
            treeSelect.setDisabled(true);
            return treeSelect;
        }).collect(Collectors.toList());
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(sxTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public static List<TreeSelect> buildDistrictAndOrganAndUserTreeSelect(List<SysDistrict> sysDistrictList
            , List<SysOrgan> sysOrganList, List<SysUser> sysUserList) {
        List<TreeSelect> treeSelectList = new ArrayList<>();
        //事项列表
        Set<String> organSet = new HashSet<>();
        Set<String> districtSet = new HashSet<>();
        List<TreeSelect> userTreeSelectList = Optional.ofNullable(sysUserList).orElseGet(Lists::newArrayList).stream().map(sysUser -> {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sysUser.getUserOid());
            treeSelect.setLabel(sysUser.getName());
            treeSelect.setParentId(sysUser.getOrganOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("USER");
            districtSet.add(sysUser.getDistrictOid());
            organSet.add(sysUser.getOrganOid());
            return treeSelect;
        }).collect(Collectors.toList());
        //区划列表
        List<TreeSelect> districtTreeSelectList = new ArrayList<>();
        for (SysDistrict sysDistrict : sysDistrictList) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(sysDistrict.getDistrictOid());
            treeSelect.setLabel(sysDistrict.getName());
            treeSelect.setParentId(sysDistrict.getParentOid());
            treeSelect.setChildren(new ArrayList<>());
            treeSelect.setIdentity("DISTRICT");
            districtTreeSelectList.add(treeSelect);
        }
        //机构列表
        List<TreeSelect> organTreeSelectList = new ArrayList<>();
        for (SysOrgan sysOrgan : sysOrganList) {
            if (organSet.contains(sysOrgan.getOrganOid())) {
                TreeSelect treeSelect = new TreeSelect();
                treeSelect.setId(sysOrgan.getOrganOid());
                treeSelect.setLabel(sysOrgan.getName());
                treeSelect.setParentId(String.valueOf(sysOrgan.getDistrictOid()));
                treeSelect.setChildren(new ArrayList<>());
                treeSelect.setIdentity("ORGAN");
                organTreeSelectList.add(treeSelect);
            }
        }
        treeSelectList.addAll(districtTreeSelectList);
        treeSelectList.addAll(organTreeSelectList);
        treeSelectList.addAll(userTreeSelectList);
        treeSelectList = buildTree(treeSelectList);
        return treeSelectList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}