package com.zfsoft.cases.feign;

import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.microservice.settings.service.SysDictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * @author: wangwg
 * @create: 2020-11-17
 * @description: 字典信息外部模块调用接口
 */
@FeignClient(value = "${zfsoft.feign.settings}",contextId = "dictSettings")
public interface SysDictFeignService extends SysDictService {

}
