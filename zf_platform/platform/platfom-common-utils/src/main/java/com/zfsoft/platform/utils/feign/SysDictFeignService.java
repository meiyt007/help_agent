package com.zfsoft.platform.utils.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysDict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "settings-service-provider",contextId ="settings-dict-common" )
public interface SysDictFeignService {

    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/dict/getSysDictByDictOid/{dictOid}",method = {RequestMethod.GET})
    ApiResultSet<SysDict>  getSysDictByDictOid(@PathVariable("dictOid")String dictOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/security/dict/getSysDictByCode",method = {RequestMethod.GET})
    ApiResultSet<SysDict> getSysDictByCode(@RequestParam("code") String code);

}