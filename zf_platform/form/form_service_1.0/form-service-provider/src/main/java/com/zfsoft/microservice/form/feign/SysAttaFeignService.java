package com.zfsoft.microservice.form.feign;

import com.zfsoft.microservice.platform.service.sys.SysAttaService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysAttaFeignService
 * @Description 附件feign
 * @Author wuxx
 * @Date 2021-4-19 10:23
 * @Version V1.0
 **/
@FeignClient(value = "platform-service-provider",contextId = "atta")
public interface SysAttaFeignService extends SysAttaService {

}
