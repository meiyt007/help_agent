package com.zfsoft.microservice.platform.gateway.feign;

import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.data.sys.SysRole;
import com.zfsoft.microservice.platform.gateway.security.data.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysUserFeignService
 * @Description
 * @Author
 * @Date2020-09-01 14:23
 * @Version V1.0
 **/
@FeignClient(value = "platform-service-provider",contextId = "sysUser")
public interface SysUserFeignService {
     @ProcessFeignCalledResult
     @RequestMapping( value = "/security/sysUser/username/{username}",method = {RequestMethod.GET})
     ApiResultSet<SysUser>   getSysUserByName(@PathVariable("username")String username);

     @ProcessFeignCalledResult
     @GetMapping("/security/syslogin/account/{account}")
     public ApiResultSet<SysLogin> getSysLoginByAccount(@PathVariable("account") String account);

     @ProcessFeignCalledResult
     @GetMapping("/security/syslogin/getSysLoginByUserOidForEeds/{userOid}")
     public ApiResultSet<SysLogin> getSysLoginByUserOidForEeds(@PathVariable("userOid") String userOid);

     /**
      * @description: 根据业务用户主键获取用户信息
      * @param userOid 业务主键
      * @author: wuxx
      * @Date: 2020/10/29 10:34
      **/
     @ProcessFeignCalledResult
     @GetMapping(value = "/security/sysuser/getSysUserByUserOid/{userOid}")
     ApiResultSet<SysUser> getSysUserByUserOid(@PathVariable("userOid") String userOid);

     @ProcessFeignCalledResult
     @GetMapping(value = "/security/syslogin/lock")
     ApiResultSet lockSysLogin(@RequestParam String account);

     @ProcessFeignCalledResult
     @GetMapping(value = "/security/sysrole/getByLoginId/{loginId}")
     ApiResultSet<List<SysRole>> getRolesByLoginId(@PathVariable("loginId") String loginId);

     /**
      * 清空失败次数
      *
      * @param account 登录名
      * @return ApiResultSet
      * @author ningzz
      * @Date 2020/11/9
      **/
     @ProcessFeignCalledResult
     @GetMapping(value = "/security/syslogin/emptyFailNum")
     ApiResultSet emptyFailNum(@RequestParam String account);

}
