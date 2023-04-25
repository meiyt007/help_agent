package com.zfsoft.microservice.platform.gateway.feign;

import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SysConfigFeignService
 * @author zxx
 * @date 2020/9/28 2:15 下午
 */
@FeignClient(value = "${zfsoft.feign.settings}",contextId ="dict" )
@RequestMapping("/security/dict")
public interface SysDictFeignService {

    /**
     * @description:  获取数据字典的信息
     * @param dictOid 数据字典业务主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysDictByDictOid/{dictOid}",method = {RequestMethod.GET})
    ApiResultSet<SysDict>  getSysDictByDictOid(@PathVariable("dictOid")String dictOid);
}
