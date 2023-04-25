package com.zfsoft.microservice.platform.gateway.feign;

import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * SysConfigFeignService
 * @author zxx
 * @date 2020/9/28 2:15 下午
 */
@FeignClient(value = "settings-service-provider",contextId ="settings-config" )
public interface SysConfigFeignService {

    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/config/getSysConfigByCode",method = {RequestMethod.GET})
    ApiResultSet<SysConfig> getSysConfigByCode(@RequestParam String code);

    /**
     * @description: 获取登录验码的标识 true 开启  false关闭
     * @author: wuxx
     * @Date: 2020/10/26 9:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/config/getLoginCodeFlag",method = {RequestMethod.GET})
    ApiResultSet<Boolean> getLoginCodeFlag();

}
