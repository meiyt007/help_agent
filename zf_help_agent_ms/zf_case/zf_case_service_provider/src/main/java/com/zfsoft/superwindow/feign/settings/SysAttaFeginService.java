package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.microservice.platform.service.sys.SysAttaService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: wangwg
 * @create: 2020-10-31
 * @description: 实施清单外部模块调用接口
 */
@FeignClient(value = "${zfsoft.feign.platform}",contextId = "atta")
public interface SysAttaFeginService extends SysAttaService {
}
