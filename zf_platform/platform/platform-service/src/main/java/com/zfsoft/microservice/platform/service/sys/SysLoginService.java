package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.data.vo.SysLoginVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SysLoginService
 * @Description
 * @Author
 * @Date2020-09-11 17:17
 * @Version V1.0
 **/
@RequestMapping("/security")
public interface SysLoginService {
    /**
     * @description:  初始化登录
     * @param oid 登录账号主键
     * @author: wuxx
     * @Date: 2020/9/23 10:07
     **/
    @GetMapping(value = "/syslogin/init/{oid}")
    ApiResultSet<SysLogin> initSysLoginById(@PathVariable("oid") Long oid);


    /**
     * 根据账户获取登录信息
     *
     * @param account 账户
     * @return
     * @author zxx
     * @date 2020/9/15 2:49 下午
     */
    @GetMapping("/syslogin/account/{account}")
     ApiResultSet<SysLogin> getSysLoginByAccount(@PathVariable("account") String account);

    /**
     * 根据账户获取登录信息
     *
     * @param userOid 账户oid
     * @return
     * @author zxx
     * @date 2020/9/15 2:49 下午
     */
    @GetMapping("/syslogin/getSysLoginByUserOidForEeds/{userOid}")
    ApiResultSet<SysLogin> getSysLoginByUserOidForEeds(@PathVariable("userOid") String userOid);

    /**
     * 获取登录信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    @GetMapping(value = "/syslogin/getOne/{oid}")
    ApiResultSet<SysLogin> getSysLoginById(@PathVariable("oid") Long oid);

    /**
     * 获取登录信息
     *
     * @param userOid
     * @return
     * @author ningzz
     * @Date 2020/12/3
     **/
    @GetMapping(value = "/syslogin/getSysLoginByUserOid")
    ApiResultSet<SysLogin> getSysLoginByUserOid(@RequestParam(value = "userOid", required = false) String userOid);

    /**
     * @description: 根据主键获取登录vo
     * @param oid 主键
     * @author: wuxx
     * @Date: 2020/9/23 14:39
     **/
    @GetMapping(value = "/syslogin/getOneVo/{oid}")
     ApiResultSet<SysLoginVo> getOneVo(@PathVariable("oid") Long oid);

    /**
     * @description:  查询登录管理vo列表
     * @param districtOid 区划oid
     * @param organOid 机构oid
     * @param account 登录名
     * @param lockStatus 锁定状态
     * @param pageNum  页数
     * @param pageSize 页数大小
     * @author: wuxx
     * @Date: 2020/9/23 13:19
     **/
    @GetMapping(value = "/syslogin/page")
     ApiResultSet page(
            @RequestParam(value = "districtOid",required = false)String districtOid,
            @RequestParam(value = "organOid",required = false)String organOid,
            @RequestParam(value = "account",required = false)String account,
            @RequestParam(value = "userName",required = false)String userName,
            @RequestParam(value = "lockStatus",required = false)Integer lockStatus,
            @RequestParam(value = "pageNum",required = false)Integer pageNum,
            @RequestParam(value = "pageSize",required = false)Integer pageSize);

    /**
     * 保存登录信息
     *
     * @param sysLogin 登录信息
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    @PostMapping(value = "/syslogin/save")
     ApiResultSet save(@RequestBody SysLogin sysLogin);

    /**
     * @description:  修改密码
     * @param sysLoginVo 登录vo
     * @author: wuxx
     * @Date: 2020/9/23 15:45
     **/
    @PostMapping( value = "/syslogin/updatePassword")
     ApiResultSet updatePassword(@RequestBody SysLoginVo sysLoginVo);

    /**
     * 启禁用登录信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    @PostMapping(value = "/syslogin/able/{oid}")
     ApiResultSet able(@PathVariable("oid") Long oid);

    /**
     * 删除登录信息
     *
     * @param oid 主键
     * @return
     * @author zxx
     * @date 2020/9/15 3:33 下午
     */
    @DeleteMapping(value = "/syslogin/delete/{oid}")
     ApiResultSet delete(@PathVariable("oid") Long oid);


    /**
     * 获取路由
     * @author zxx
     * @date 2020/9/18 8:57 上午
     * @return
     */
    @GetMapping("/syslogin/getRouters")
     ApiResultSet getRouters(@RequestParam(value = "appOid",required = false)String appOid);

    /**
     * 获取公钥
     * @author zxx
     * @date 2020/9/30 11:30 上午
     * @return
     */
    @GetMapping("/syslogin/getPublicKey")
    ApiResultSet getPublicKey();


    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/syslogin/getUserInfo")
     ApiResultSet getUserInfo();

    /**
     * 锁定账户
     * @author zxx
     * @date 2020/10/9 4:41 下午
     * @param account
     * @return
     */
    @GetMapping(value = "/syslogin/lock")
    ApiResultSet lockSysLogin(@RequestParam(value = "account",required = false) String account);

    /**
     * @description:  解锁账户
     * @param oid 登录主键
     * @author: wuxx
     * @Date: 2020/10/22 11:58
     **/
    @PostMapping(value = "/syslogin/delock/{oid}")
    ApiResultSet delockSysLogin(@PathVariable("oid") Long oid);

    /**
     * 清空失败次数
     *
     * @param account 登录名
     * @return ApiResultSet
     * @author ningzz
     * @Date 2020/11/9
     **/
    @GetMapping(value = "/syslogin/emptyFailNum")
    ApiResultSet emptyFailNum(@RequestParam(value = "account", required = false) String account);

}
