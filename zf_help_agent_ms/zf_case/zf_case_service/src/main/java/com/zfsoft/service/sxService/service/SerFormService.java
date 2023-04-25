package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SerForm;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName SerFormService
 * @Description 实施清单表单配置服务定义接口
 * @Author wangxl
 * @Date 2020-10-27
 * @Version V1.0
 **/
@RequestMapping("/affair/serForm")
public interface SerFormService {
    /**
     * @description:  根据实施清单业务主健获取实施清单表单配置信息
     * @param serviceOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSerFormByServiceOid/{serviceOid}",method = {RequestMethod.GET})
    ApiResultSet<SerForm>  getSerFormByServiceOid(@PathVariable("serviceOid") String serviceOid);
}
