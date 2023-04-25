package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysPermission;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

/**
 * 权限service接口
 *
 * @author zxx
 * @date 2020/9/10 10:59 上午
 */

@RequestMapping("/security")
public interface SysPermissionService {
    /**
     * 获取权限信息
     *
     * @param id 主键
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/10 5:16 下午
     */
    @GetMapping(value = "/syspermission/{id}")
     ApiResultSet<SysPermission> getSysPermissionById(@PathVariable("id") Long id);

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
    @GetMapping(value = "/syspermission/init")
     ApiResultSet init(@RequestParam(value = "appOid",required = false)String appOid,
                       @RequestParam(value = "parentOid",required = false)String parentOid,
                       @RequestParam(value = "id",required = false)Long id);

    /**
     * 获取应用权限下拉树
     *
     * @param name 权限
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/11 4:06 下午
     */
    @GetMapping(value = "/syspermission/appPermissionTree")
     ApiResultSet appPermissionTree(@RequestParam(value = "name",required = false)String name);

    /**
     * 获取权限下拉树
     * @author zxx
     * @date 2020/9/22 5:12 下午
     * @param appOid 应用主键
     * @return ApiResultSet
     */
    @GetMapping(value = "/syspermission/permissionTree")
     ApiResultSet permissionTree(
            @RequestParam(value = "appOid",required = false)String appOid,
            @RequestParam(value = "disable",required = false)boolean disable);

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
    @GetMapping(value = "/syspermission/page")
     ApiResultSet queryWithPage(
             @RequestParam(value = "name",required = false) String name,
             @RequestParam(value = "appOid",required = false) String appOid,
             @RequestParam(value = "parentOid",required = false) String parentOid,
             @RequestParam("pageNum") int pageNum,
             @RequestParam("pageSize") int pageSize);

    /**
     * 保存权限
     *
     * @param sysPermission 权限
     * @return
     * @author zxx
     * @date 2020/9/12 9:35 上午
     */
    @PostMapping(value = "/syspermission")
     ApiResultSet save(@RequestBody SysPermission sysPermission);

    /**
     * 启禁用权限
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/11 4:56 下午
     */
    @PatchMapping(value = "/syspermission/{id}")
     ApiResultSet able(@PathVariable("id") Long id);

    /**
     * 删除权限
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/11 5:26 下午
     */
    @PostMapping(value = "/syspermission/{id}")
     ApiResultSet delete(@PathVariable("id") Long id);
}
