package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.microservice.platform.service.sys.SysLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: wangwg
 * @create: 2020-10-31
 * @description: 登录信息外部模块调用接口
 */
@FeignClient(value ="${zfsoft.feign.platform}",contextId = "login")
public interface SysLoginFeginService  extends SysLoginService {

}
