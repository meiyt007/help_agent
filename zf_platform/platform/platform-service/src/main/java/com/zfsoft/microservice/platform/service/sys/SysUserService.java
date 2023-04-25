package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.data.vo.SysUserVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息service接口
 *
 * @author zxx
 * @date 2020/9/14 2:54 下午
 */
@RequestMapping("/security")
public interface SysUserService {

    /**
     * @description: 根据业务用户主键获取用户信息
     * @param userOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/29 10:34
     **/
    @GetMapping(value = "/sysuser/getSysUserByUserOid/{userOid}")
    ApiResultSet<SysUser> getSysUserByUserOid(@PathVariable("userOid") String userOid);

    /**
     * @description: 根据业务组织机构主键获取用户信息
     * @param organOid 组织机构主键
     * @author: wuxx
     * @Date: 2020/12/9 17:34
     **/
    @GetMapping(value = "/sysuser/getSysUserListByOrganOid/{organOid}")
    ApiResultSet<List<SysUser>> getSysUserListByOrganOid(@PathVariable("organOid") String organOid);

    /**
     * 分页查询用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author zxx
     * @date 2020/9/15 8:15 下午
     */
    @GetMapping(value = "/sysuser/page")
    ApiResultSet queryWithPage(@RequestParam(value = "districtOid",required = false)String districtOid,
                               @RequestParam(value = "organOid",required = false)String organOid,
                               @RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "isAble",required = false)Integer isAble,
                               @RequestParam(value = "pageNum",required = false)Integer pageNum,
                               @RequestParam(value = "pageSize",required = false)Integer pageSize);

    /**
     * 保存用户信息
     *
     * @param sysUser 用户信息
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    @PostMapping(value = "/sysuser")
     ApiResultSet save(@RequestBody SysUser sysUser);

    /**
     * @description: 修改用户信息，只修改有值的
     * @param sysUser 用户信息
     * @author: wuxx
     * @Date: 2020/10/31 15:13
     **/
    @PostMapping(value = "/updateSysUser")
    ApiResultSet updateSysUser(@RequestBody SysUser sysUser);

    /**
     * 启禁用用户信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    @PatchMapping(value = "/sysuser/{id}")
     ApiResultSet able(@PathVariable("id") Long id);

    /**
     * 删除用户信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 8:16 下午
     */
    @PostMapping(value = "/sysuser/{oid}")
     ApiResultSet delete(@PathVariable("oid") Long oid);


    /**
     * @description: 根据用户查询用户信息
     * @param sysUser
     * @author: wuxx
     * @Date: 2020/10/30 15:24
     **/
    @GetMapping(value = "/sysuser/queryWithPageBySysUser")
    ApiResultSet<SysUser> queryWithPageBySysUser(SysUser sysUser,
                                                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize);

    /**
     * 获取用户的角色
     *
     * @param userOid 主键
     * @return
     * @author zxx
     * @date 2020/9/24 2:56 下午
     */
    @GetMapping(value = "/sysuser/role/{userOid}")
     ApiResultSet getUserRole(@PathVariable("userOid") String userOid);

    /**
     * 保存用户授权
     *
     * @param sysUser 用户
     * @return
     * @author zxx
     * @date 2020/9/24 6:52 下午
     */
    @PostMapping(value = "/sysuser/saveUserRole")
     ApiResultSet saveUserRole(@RequestBody SysUser sysUser);

    /**
     * 根据机构获取用户树
     *
     * @param organOid 机构主键
     * @return
     * @author zxx
     * @date 2020/9/24 6:53 下午
     */
    @GetMapping(value = "/sysuser/userTree")
     ApiResultSet queryUserTreeByOrganOid(
            @RequestParam(value = "organOid",required = false) String organOid,
            @RequestParam(value = "disable",required = false) boolean disable);

    /**
     * 更新用户头像
     * @author zxx
     * @date 2020/9/27 4:22 下午
     * @param imageOid
     * @return
     */
    @PatchMapping(value = "/sysuser/avatar")
     ApiResultSet updateUserAvatar(@RequestParam(value = "oid",required = false) String imageOid);

    /**
     * @description: 根据岗位oid获取用户信息集合
     * @param postOid 岗位oids
     * @author: wuxx
     * @Date: 2021/1/22 10:34
     **/
    @GetMapping(value = "/sysuser/getSysUserListByPostOid")
    ApiResultSet<List<SysUser>> getSysUserListByPostOid(@RequestParam("postOid") String postOid);

    /**
     * @description: 根据岗位oid获取用户信息集合
     * @param postOids 岗位oids,用逗号分割
     * @author: wuxx
     * @Date: 2021/1/22 10:34
     **/
    @GetMapping(value = "/sysuser/getSysUserListByPostOids")
    ApiResultSet<List<SysUser>> getSysUserListByPostOids(@RequestParam("postOids") String postOids);


   /**
    * @description: 获取所有的用户vo
    * @author: wuxx
    * @Date: 2021/6/3 17:35
    **/
    @GetMapping(value = "/sysuser/getAllUserVoList")
    ApiResultSet<List<SysUserVo>> getAllUserVoList();

    /**
     * @description: 根据角色oid获取用户信息集合
     * @param roleOids 角色oids,用逗号分割
     * @author: wuxx
     * @Date: 2022/3/14 10:34
     **/
    @GetMapping(value = "/sysuser/getSysUserListByRoleOids")
    ApiResultSet<List<SysUser>> getSysUserListByRoleOids(@RequestParam("roleOids") String roleOids);

    /**
     * @description: 根据机构oid获取用户信息集合
     * @param organOids 机构oids,用逗号分割
     * @author: wuxx
     * @Date: 2022/3/14 10:34
     **/
    @GetMapping(value = "/sysuser/getSysUserListByOrganOids")
    ApiResultSet<List<SysUser>> getSysUserListByOrganOids(@RequestParam("organOids") String organOids);

    @PostMapping(value = "/sysuser/changeStatisticTypes")
    ApiResultSet changeStatisticTypes(String statisticTypes);
}
