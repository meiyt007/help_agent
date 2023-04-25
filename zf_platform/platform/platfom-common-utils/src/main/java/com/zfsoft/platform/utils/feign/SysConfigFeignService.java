package com.zfsoft.platform.utils.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * SysConfigFeignService
 * @author wuxx
 * @date 2020/12/1 2:15 下午
 */
@FeignClient(value = "settings-service-provider",contextId ="settings-config-utils" )
@Order(Integer.MIN_VALUE)
public interface SysConfigFeignService {

    @ConditionalOnBean(SysConfigFeignService.class)
    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/config/getSysConfigByCode",method = {RequestMethod.GET})
    ApiResultSet<SysConfig> getSysConfigByCode(@RequestParam(value = "code") String code);

}
