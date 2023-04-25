package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.microservice.platform.service.sys.SysRoleService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysRoleFeignService
 * @Description: 角色feign
 * @Author wuxx
 * @Date 2022/3/11
 **/
@FeignClient(value = "platform-service-provider",contextId = "sysRole")
public interface SysRoleFeignService extends SysRoleService {
}
