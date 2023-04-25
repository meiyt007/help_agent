package com.zfsoft.microservice.form.feign;

import com.zfsoft.microservice.settings.service.SysDictService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysConfigFeignService
 * @Description 数据字典feign
 * @Author wuxx
 * @Date 2021-4-12 10:23
 * @Version V1.0
 **/
@FeignClient(value = "settings-service-provider",contextId = "dict")
public interface SysDictFeignService extends SysDictService {

}
