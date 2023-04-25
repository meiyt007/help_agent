package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.microservice.settings.service.SysConfigService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysConfigFeignService
 * @Description feign
 * @Author wangxl
 * @Date 2020-11-26
 * @Version V1.0
 **/
@FeignClient(value = "${zfsoft.feign.settings}",contextId = "sysconfig")
public interface SysConfigFeignService extends SysConfigService {

}
