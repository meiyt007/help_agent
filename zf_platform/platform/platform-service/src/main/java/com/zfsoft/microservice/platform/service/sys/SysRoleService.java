package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysRole;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

/**
 * 角色service接口
 *
 * @author zxx
 * @date 2020/9/12 10:26 上午
 */
@RequestMapping("/security")
public interface SysRoleService {
    /**
     * 获取角色信息
     *
     * @param id 主键
     * @return
     * @author zxx
     * @date 2020/9/12 2:16 下午
     */
    @GetMapping(value = "/sysrole/{id}")
     ApiResultSet<SysRole> getSysRoleById(@PathVariable("id") Long id);

    /**
     * @description:  根据业务主键获取角色信息
     * @param roleOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/29 16:38
     **/
    @GetMapping(value = "/sysrole/getSysRoleByRoleOid/{roleOid}")
    ApiResultSet<SysRole> getSysRoleByRoleOid(@PathVariable("roleOid") String roleOid);

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
    @GetMapping(value = "/sysrole/page")
     ApiResultSet queryWithPage(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "appOid",required = false)String appOid,
            @RequestParam(name = "pageNum",required = false)int pageNum,
            @RequestParam(name = "pageSize",required = false)int pageSize);

    @GetMapping(value = "/sysrole/getByLoginId/{loginId}")
     ApiResultSet<SysRole> getRolesByLoginId(@PathVariable("loginId") String loginId);

    /**
     * 保存角色
     *
     * @param sysRole 角色
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    @PostMapping(value = "/sysrole")
     ApiResultSet save(@RequestBody SysRole sysRole);

    /**
     * 启禁用角色
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    @PatchMapping(value = "/sysrole/{oid}")
     ApiResultSet able(@PathVariable("oid") Long oid);

    /**
     * 删除角色
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 2:47 下午
     */
    @PostMapping(value = "/sysrole/{oid}")
     ApiResultSet delete(@PathVariable("oid") Long oid);

    /**
     * 应用角色树
     * @author zxx
     * @date 2020/9/24 2:31 下午
     * @param appOid 应用主键
     * @param disable 多选开启状态
     * @return
     */
    @GetMapping(value = "/sysrole/roleTree")
     ApiResultSet roleTree(
            @RequestParam(value = "appOid",required = false) String appOid,
            @RequestParam(value = "disable",required = false) boolean disable);

    /**
     * 获取角色下的用户信息
     * @author zxx
     * @date 2020/9/25 4:56 下午
     * @param roleOid
     * @return
     */
    @GetMapping(value = "/sysrole/user/{roleOid}")
     ApiResultSet queryRoleUser(@PathVariable("roleOid") String roleOid);

    /**
     * 获取角色下的用户信息Tree
     * @author wuxx
     * @date 2021/03/22 4:56 下午
     * @param roleOid
     * @return
     */
    @GetMapping(value = "/sysrole/userTree/{roleOid}")
    ApiResultSet queryRoleUserTree(@PathVariable("roleOid") String roleOid);


    /**
     * 保存授权用户
     * @author zxx
     * @date 2020/9/25 5:28 下午
     * @param sysRole 角色
     * @return
     */
    @PostMapping(value = "/sysrole/saveRoleUser")
     ApiResultSet saveRoleUser(@RequestBody SysRole sysRole);
}
