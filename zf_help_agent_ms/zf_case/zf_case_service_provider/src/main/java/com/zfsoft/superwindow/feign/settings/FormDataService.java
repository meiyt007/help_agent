package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.feign.settings.data.FormDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ChangSheng
 * @Date 10:55 2022/6/28
 * @Description 电子表单
 **/
@FeignClient(value = "form-service-provider",contextId = "manager")
public interface FormDataService {
    @RequestMapping( value = "/getFormData",method = {RequestMethod.GET})
    ApiResultSet<FormDataVo> getFormData(@RequestParam("authorizeKey") String authorizeKey,
                                         @RequestParam("designOid") String designOid,
                                         @RequestParam(value = "reportOid", required = false) String reportOid);
}
