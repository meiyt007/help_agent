package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.clzs.dto.BusinessLiceInfo;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @description: 营业执照信息接口
 * @author: wangwg
 * @create: 2021-01-11
 */
@RequestMapping(value = "/businessLice")
public interface BusinessLiceService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getBusinessLiceInfo", method = {RequestMethod.GET})
    ApiResultSet<BusinessLiceInfo> getBusinessLiceInfo(@RequestParam(value = "picBase64", required = false) String picBase64);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/discernBusinessBaiDu", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> discernBusinessBaiDu(@RequestParam(value = "businessLiceInfo", required = false) BusinessLiceInfo businessLiceInfo,
                                                           @RequestParam(value = "materialOid", required = false) String materialOid,
                                                           @RequestParam(value = "cata", required = false) MaterialCatalog cata);

}
