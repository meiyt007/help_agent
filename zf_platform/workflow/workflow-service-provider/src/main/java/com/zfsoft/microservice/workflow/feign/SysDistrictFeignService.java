package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.microservice.platform.service.sys.SysDistrictService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysAppFeignService
 * @Description: 区划feign
 * @Author wuxx
 * @Date 2021/1/22
 **/
@FeignClient(value = "platform-service-provider",contextId = "district")
public interface SysDistrictFeignService extends SysDistrictService {
}
