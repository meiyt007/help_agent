package com.zfsoft.microservice.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.manager.sys.*;
import com.zfsoft.microservice.platform.service.CommonService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CommonController
 * @Description: 通用的Controller
 * @Author wuxx
 * @Date 2020/9/9
 **/
@RestController
@Slf4j
public class CommonController implements CommonService {

    @Autowired
    private SysDistrictManager sysDistrictManager;

    @Autowired
    private SysOrganManager sysOrganManager;

    @Autowired
    private SysUserManager sysUserManager;

    @Autowired
    private SysPostManager sysPostManager;

    @Autowired
    private SysRoleManager sysRoleManager;


    /**
     * @description:  根据区划oid查询区划对象集合
     * @author: wangyh
     * @Date: 2022/8/2 11:23
     **/
    @Override
    public ApiResultSet <List<SysDistrict>>queryDistrictTree(String districtOid) {
        log.info("进入区划查询列表方法");
        List<SysDistrict> districtList = sysDistrictManager.distList();
        return ApiResultSet.ok("获取区划表成功",districtList);
    }

    /**
     * @description:  根据当前登录人区划下级的区划SimpleJson 树
     * 当districtOid为空时，查询所有的记录，当districtOid对应的编号查询不到区划信息时，无法查询到下级区划信息
     * @param districtOid 区划实体类主键
     * @param districtOids 在当前区划外的区划 用于编辑时回显区划
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @Override
    public ApiResultSet<List<TreeSelect>> queryDistrictSimpleTree(String districtOid, String districtOids){
        if(StrUtil.isEmpty(districtOid)){
            districtOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
        }
        List<TreeSelect> treeSelects = sysDistrictManager.queryDistrictSimpleTree(districtOid, districtOids);
        return new ApiResultSet(treeSelects);
    }

    @Override
    public ApiResultSet<List<TreeSelect>> queryAllDistrictSimpleTree() {
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictSimpleTreeList(null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysDistrictTreeSelect(sysDistrictList);
        return new ApiResultSet(treeSelects);
    }

    /**
     * @description:  根据区划oid的查询区划树(可设置排除在外的区划)
     * 当districtOid为空时，查询所有的记录，当districtOid对应的编号查询不到区划信息时，无法查询到下级区划信息
     * @param districtOid 区划实体类主键
     * @param noIncludeOids 区划树不包含的主键以逗号隔开1,2,3
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    public ApiResultSet<List<TreeSelect>>  queryDistrictTreeNoInclude(String districtOid,String noIncludeOids){
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictTreeNoIncludeList(districtOid,noIncludeOids);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysDistrictTreeSelect(sysDistrictList);
        return new ApiResultSet(treeSelects);
    }

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成组织机构树
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2020/9/9 18:14
     **/
    public ApiResultSet<List<TreeSelect>> querySysOrganWithPage(String districtOid){
        List<SysOrgan> sysOrganList = sysOrganManager.queryOrganTreeList(districtOid);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildSysOrganTreeSelect(sysOrganList);
        return new ApiResultSet(treeSelects);
    }

    /**
     * 异步区划机构用户树
     * @author zxx
     * @date 2020/9/24 7:56 下午
     * @param oid
     * @return
     */
    public ApiResultSet queryDistrictAndOrganAndUserTreeSelect(String oid,String identity){
        List<TreeSelect> treeSelectList = sysUserManager.queryDistrictAndOrganAndUserTreeSelect(oid, identity);
        return new ApiResultSet(treeSelectList);
    }

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成区划组织机构树
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/01/04 10:14
     **/
    @Override
    public ApiResultSet<List<TreeSelect>> queryDistrictOrganTree(String districtOid) {
        List<TreeSelect> treeSelectList =  sysOrganManager.queryDistrictOrganTree(districtOid);
        return new ApiResultSet(treeSelectList);
    }

    /**
     * @description: 查询所有区划的组织机构、用户信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @Override
    public ApiResultSet<List<TreeSelect>> queryAllDistrictOrganUserTree() {
        List<TreeSelect> treeSelectList = sysUserManager.queryAllDistrictOrganUserTree();
        return new ApiResultSet(treeSelectList);
    }

    /**
     * @description: 查询所有区划的组织机构、岗位信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @Override
    public ApiResultSet<List<TreeSelect>> queryAllDistrictOrganPostTree() {
        List<TreeSelect> treeSelectList = sysPostManager.queryAllDistrictOrganPostTree();
        return new ApiResultSet(treeSelectList);
    }

    @Override
    public ApiResultSet<List<TreeSelect>> queryAppRoleTree() {
        List<TreeSelect> treeSelectList = sysRoleManager.queryAppRoleTree();
        return new ApiResultSet(treeSelectList);
    }

    @Override
    public ApiResultSet<List<TreeSelect>> queryWorkFlowDistrictOrganTree() {
        List<TreeSelect> treeSelectList =  sysOrganManager.queryDistrictOrganTree(null);
        treeSelectList = GenDataTreeUtil.buildDisabledLastTree(treeSelectList,GenDataTreeUtil.TYPE_ORGAN);
        return new ApiResultSet(treeSelectList);
    }
}
