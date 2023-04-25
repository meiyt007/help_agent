package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 调用用户服务的feign
 * @author: wuxx
 * @Date: 2021/1/15 14:21
 **/
@FeignClient(value = "platform-service-provider",contextId = "sysUser")
public interface SysUserFeignService {

     /**
      * @description: 根据业务用户主键获取用户信息
      * @param userOid 业务主键
      * @author: wuxx
      * @Date: 2020/10/29 10:34
      **/
     @ProcessFeignCalledResult
     @GetMapping(value = "/security/sysuser/getSysUserByUserOid/{userOid}")
     ApiResultSet<SysUser> getSysUserByUserOid(@PathVariable("userOid") String userOid);

     /**
      * @description: 根据岗位oid获取用户信息集合
      * @param postOids 岗位oids,用逗号分割
      * @author: wuxx
      * @Date: 2021/1/22 10:34
      **/
     @GetMapping(value = "/security/sysuser/getSysUserListByPostOids")
     ApiResultSet<List<SysUser>> getSysUserListByPostOids(@RequestParam("postOids") String postOids);

     /**
      * @description: 根据角色oid获取用户信息集合
      * @param roleOids 角色oids,用逗号分割
      * @author: wuxx
      * @Date: 2022/3/14 10:34
      **/
     @GetMapping(value = "/security/sysuser/getSysUserListByRoleOids")
     ApiResultSet<List<SysUser>> getSysUserListByRoleOids(@RequestParam("roleOids") String roleOids);

     /**
      * @description: 根据机构oid获取用户信息集合
      * @param organOids 机构oids,用逗号分割
      * @author: wuxx
      * @Date: 2022/3/14 10:34
      **/
     @GetMapping(value = "/security/sysuser/getSysUserListByOrganOids")
     ApiResultSet<List<SysUser>> getSysUserListByOrganOids(@RequestParam("organOids") String organOids);
}
