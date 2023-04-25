package com.zfsoft.microservice.platform.feign.settings;

import com.zfsoft.microservice.settings.service.SysDictService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysConfigFeignService
 * @Description 数据字典feign
 * @Author wuxx
 * @Date 2020-09-14 10:23
 * @Version V1.0
 **/
@FeignClient(value = "settings-service-provider",contextId = "dict")
public interface SysDictFeignService extends SysDictService {

}
