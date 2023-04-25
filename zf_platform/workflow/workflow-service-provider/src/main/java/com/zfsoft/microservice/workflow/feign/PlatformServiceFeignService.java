package com.zfsoft.microservice.workflow.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @description: 基础服务的feign
 * @author: wuxx
 * @Date: 2021/2/1 15:25
 **/
@FeignClient(value = "platform-service-provider",contextId ="platform-service" )
@Order(Integer.MIN_VALUE)
public interface PlatformServiceFeignService {

    /**
     * @description: 根据节假日类型获取节假日集合
     * @author: wuxx
     * @Date: 2021/2/1 15:16
     **/
    @ConditionalOnBean(PlatformServiceFeignService.class)
    @ProcessFeignCalledResult
    @RequestMapping(value = "/security/holiday/querySysHolidayMapByHolidayType", method = {RequestMethod.GET})
    ApiResultSet<Set<String>> querySysHolidaySetByHolidayType(@RequestParam("holidayType") String holidayType);

}
