package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.microservice.platform.service.sys.SysAppService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysAppFeignService
 * @Description: 所属应用feign
 * @Author wuxx
 * @Date 2021/1/22
 **/
@FeignClient(value = "platform-service-provider",contextId = "sysApp")
public interface SysAppFeignService extends SysAppService {
}
