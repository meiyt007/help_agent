package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.ReguserInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 登记人信息
 */
@RequestMapping("/reguserInfo")
public interface ReguserInfoService {
    /**
     保存信息
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet<ReguserInfo> saveOrUpdate(@RequestBody ReguserInfo reguserInfo);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getRegUserInfoByUserOid", method = {RequestMethod.GET})
    ApiResultSet<ReguserInfo> getRegUserInfoByUserOid();

}
