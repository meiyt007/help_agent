package com.zfsoft.single.feign.settings;

import com.zfsoft.microservice.platform.service.sys.SysHolidayService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: wangwg
 * @create: 2021-05-15
 * @description: 基础服务节假日接口调用模块
 */
@FeignClient(value ="${zfsoft.feign.platform}",contextId = "holiday")
public interface SysHolidayServiceFeginService extends SysHolidayService {
}
