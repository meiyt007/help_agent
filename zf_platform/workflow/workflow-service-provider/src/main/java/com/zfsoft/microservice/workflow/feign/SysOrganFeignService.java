package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.microservice.platform.service.sys.SysOrganService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysAppFeignService
 * @Description: 调用组织机构服务的feign
 * @Author wuxx
 * @Date 2021/1/22
 **/
@FeignClient(value = "platform-service-provider",contextId = "organ")
public interface SysOrganFeignService extends SysOrganService {
}
