package com.zfsoft.microservice.platform.controller.sys;

import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.data.sys.SysRole;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.manager.sys.SysAppManager;
import com.zfsoft.microservice.platform.manager.sys.SysRoleManager;
import com.zfsoft.microservice.platform.manager.sys.SysRolePermissionManager;
import com.zfsoft.microservice.platform.manager.sys.SysUserManager;
import com.zfsoft.microservice.platform.service.sys.SysRoleService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色controller
 *
 * @author zxx
 * @date 2020/9/12 2:15 下午
 */
@RestController
@Slf4j
public class SysRoleController implements SysRoleService{

    @Autowired
    private SysRoleManager sysRoleManager;

    @Autowired
    private SysRolePermissionManager sysRolePermissionManager;

    @Autowired
    private SysAppManager sysAppManager;

    @Autowired
    private SysUserManager sysUserManager;

    /**
     * 获取角色信息
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/12 2:16 下午
     */
    public ApiResultSet<SysRole> getSysRoleById(Long id) {
        Map<String,Object> map=new HashMap<>();
        SysRole sysRole = sysRoleManager.getSysRoleById(id);
        if(sysRole!=null){
            SysApp app = sysAppManager.getSysAppByAppOid(sysRole.getAppOid());
            sysRole.setAppName(app.getName());
        }
        List<String> permissions = sysRolePermissionManager.queryPermissionByRoleOid(sysRole.getRoleOid());
        map.put("sysRole",sysRole);
        map.put("permissionOids",permissions);
        return new ApiResultSet(map);
    }

    @Override
    public ApiResultSet<SysRole> getSysRoleByRoleOid(String roleOid) {
        SysRole sysRole = sysRoleManager.getSysRoleByRoleOid(roleOid);
        return new ApiResultSet(sysRole);
    }

    /**
     * 分页查询角色
     *
     * @param name  角色名称
     * @param appOid 应用主键
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/12 2:18 下午
     */
    public ApiResultSet queryWithPage(
            String name,
            String appOid,
            int pageNum,
            int pageSize) {
        SysRole sysRole=new SysRole();
        sysRole.setName(name);
        sysRole.setAppOid(appOid);
        PageResult<SysRole> sysRolePageResult = sysRoleManager.querySysRoleWithPage(sysRole, pageNum, pageSize);
        return new ApiResultSet(sysRolePageResult);
    }

    public ApiResultSet<SysRole> getRolesByLoginId(String loginId){
        List<SysRole> roles = sysRoleManager.getSysRolesByLoginId(loginId);
        return new ApiResultSet(roles);
    }
    /**
     * 保存角色
     *
     * @param sysRole 角色
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    public ApiResultSet save(SysRole sysRole) {
        sysRoleManager.saveSysRole(sysRole);
        return new ApiResultSet();
    }

    /**
     * 启禁用角色
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    @Override
    public ApiResultSet able(Long oid) {
        return sysRoleManager.ableSysRole(oid);
    }

    /**
     * 删除角色
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    public ApiResultSet delete(Long oid) {
        sysRoleManager.deleteSysRole(oid);
        return new ApiResultSet();
    }

    /**
     * 应用角色树
     * @author zxx
     * @date 2020/9/24 2:31 下午
     * @param appOid 应用主键
     * @param disable 多选开启状态
     * @return
     */
    public ApiResultSet roleTree(String appOid,boolean disable) {
        SysRole sysRole=new SysRole();
        sysRole.setAppOid(appOid);
        List<SysRole> sysRoleList = sysRoleManager.querySysRole(sysRole);
        SysApp sysApp=new SysApp();
        sysApp.setAppOid(appOid);
        List<SysApp> sysAppList = sysAppManager.querySysApp(sysApp);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildAppAndRoleTreeSelect(sysAppList,sysRoleList,disable);
        return new ApiResultSet(treeSelects);
    }

    /**
     * 获取角色下的用户信息
     * @author zxx
     * @date 2020/9/25 4:56 下午
     * @param roleOid
     * @return
     */
    public ApiResultSet queryRoleUser(String roleOid) {
        List<SysUser> sysUserList = sysUserManager.querySysUserByRoleOid(roleOid);
        return new ApiResultSet(sysUserList);
    }

    @Override
    public ApiResultSet queryRoleUserTree(String roleOid) {
        List<String> list = sysUserManager.queryRoleUserTree(roleOid);
        return new ApiResultSet(list);
    }

    /**
     * 保存授权用户
     * @author zxx
     * @date 2020/9/25 5:28 下午
     * @param sysRole 角色
     * @return
     */
    public ApiResultSet saveRoleUser(SysRole sysRole) {
        sysRoleManager.saveRoleUser(sysRole);
        return new ApiResultSet();
    }


}
