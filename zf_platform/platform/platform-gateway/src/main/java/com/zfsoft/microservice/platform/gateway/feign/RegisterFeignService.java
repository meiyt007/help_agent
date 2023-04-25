package com.zfsoft.microservice.platform.gateway.feign;

import com.zfsoft.microservice.platform.service.RegisterService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description:  注册授权feign
 * @author: wuxx
 * @Date: 2020/11/3 15:26
 **/
@FeignClient(value = "platform-service-provider",contextId = "register" )
public interface RegisterFeignService extends RegisterService {
}
