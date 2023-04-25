package com.zfsoft.microservice.platform.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.data.sys.SysPermission;
import com.zfsoft.microservice.platform.manager.sys.SysAppManager;
import com.zfsoft.microservice.platform.manager.sys.SysPermissionManager;
import com.zfsoft.microservice.platform.service.sys.SysPermissionService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限controller
 *
 * @author zxx
 * @date 2020/9/10 5:11 下午
 */
@RestController
@Slf4j
public class SysPermissionController implements SysPermissionService {

    @Autowired
    private SysPermissionManager sysPermissionManager;

    @Autowired
    private SysAppManager sysAppManager;


    /**
     * 获取权限信息
     *
     * @param oid 主键
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/10 5:16 下午
     */
    public ApiResultSet<SysPermission> getSysPermissionById(Long oid) {
        SysPermission sysPermission = sysPermissionManager.getSysPermissionById(oid);
        return new ApiResultSet(sysPermission);
    }

    /**
     * 添加修改初始化数据获取
     *
     * @param appOid    应用Oid
     * @param parentOid 父集Oid
     * @param id       主键
     * @return
     * @author zxx
     * @date 2020/9/21 10:21 上午
     */
    public ApiResultSet init(String appOid,String parentOid,Long id) {
        SysPermission sysPermission = null;
        if (id != null) {
            sysPermission = sysPermissionManager.getSysPermissionById(id);
            appOid = sysPermission.getAppOid();
            parentOid = sysPermission.getParentOid();
        } else {
            sysPermission = new SysPermission();
        }
        if (parentOid != null) {
            SysPermission parent = sysPermissionManager.getSysPermissionByPermissionOid(parentOid);
            sysPermission.setParentOid(parentOid);
            sysPermission.setParentName(parent.getName());
            appOid=parent.getAppOid();
        }
        if (appOid != null) {
            SysApp sysApp = sysAppManager.getSysAppByAppOid(appOid);
            sysPermission.setAppOid(appOid);
            sysPermission.setAppName(sysApp.getName());
        }
        return new ApiResultSet(sysPermission);
    }

    /**
     * 获取应用权限下拉树
     *
     * @param name 权限
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/11 4:06 下午
     */
    public ApiResultSet appPermissionTree(String name) {
        List<SysApp> sysApps = sysAppManager.querySysApp(null);
        List<SysPermission> sysPermissionList = sysPermissionManager.querySysPermission(null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildAppAndPermissionTreeSelect(sysApps, sysPermissionList);
        return new ApiResultSet(treeSelects);
    }

    /**
     * 获取权限下拉树
     * @author zxx
     * @date 2020/9/22 5:12 下午
     * @param appOid 应用主键
     * @return ApiResultSet
     */
    @Override
    public ApiResultSet permissionTree(String appOid, boolean disable) {
        SysPermission sysPermission=new SysPermission();
        sysPermission.setAppOid(appOid);
        sysPermission.setIsAble(BaseStaticParameter.Y);
        List<SysPermission> sysPermissionList = sysPermissionManager.querySysPermission(sysPermission);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildPermissionTreeSelect(sysPermissionList,disable);
        return new ApiResultSet(treeSelects);
    }


    /**
     * 分页查询权限列表
     *
     * @param name 权限名称
     * @param appOid 应用
     * @param parentOid 父级主键
     * @param pageNum       页码
     * @param pageSize      每页数量
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/11 4:10 下午
     */
    public ApiResultSet queryWithPage(String name,String appOid,String parentOid, int pageNum, int pageSize) {
        SysPermission sysPermission=new SysPermission();
        sysPermission.setName(name);
        sysPermission.setAppOid(appOid);
        sysPermission.setParentOid(parentOid);
        PageResult<SysPermission> sysPermissionPageResult = sysPermissionManager.querySysPermissionWithPage(sysPermission, pageNum, pageSize);
        return new ApiResultSet(sysPermissionPageResult);
    }


    /**
     * 保存权限
     *
     * @param sysPermission 权限
     * @return
     * @author zxx
     * @date 2020/9/12 9:35 上午
     */
    public ApiResultSet save(SysPermission sysPermission) {
        SysPermission permission = sysPermissionManager.saveSysPermission(sysPermission);
        return new ApiResultSet(permission);
    }

    /**
     * 启禁用权限
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/11 4:56 下午
     */
    @Override
    public ApiResultSet able(Long id) {
        return sysPermissionManager.ableSysPermission(id);
    }

    /**
     * 删除权限
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/11 5:26 下午
     */
    @Override
    public ApiResultSet delete(Long oid) {
        return sysPermissionManager.deleteSysPermission(oid);
    }

    /**
     * @description: 根据当前登录主键获取所有的权限集合
     * @author: wuxx
     * @Date: 2021/3/23 18:35
     **/
    @GetMapping(value = "/getPermissionLinkByLoginOid")
    public ApiResultSet getPermissionLinkByLoginOid(String path){
        if(StrUtil.isEmpty(path)){
            return new ApiResultSet(true);
        }
        String loginOid = CurrentLoginUserHolder.getCurrentLoginUser().getLoginOid();
        path = path.substring(1);
        List<String> strList = sysPermissionManager.getPermissionStrList(loginOid, null);
        String finalPath = path;
        String orElse = strList.stream().filter(str -> str.contains(finalPath)).findFirst()
                .orElse(null);
        if(StrUtil.isNotEmpty(orElse)){
            return new ApiResultSet(false);
        }
        return new ApiResultSet(true);
    }
}
