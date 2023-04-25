package com.zfsoft.microservice.platform.feign.settings;

import com.zfsoft.microservice.settings.service.SysConfigService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysConfigFeignService
 * @Description 参数配置feign
 * @Author wuxx
 * @Date 2020-09-14 10:23
 * @Version V1.0
 **/
@FeignClient(value = "settings-service-provider",contextId = "config")
public interface SysConfigFeignService extends SysConfigService {

}
