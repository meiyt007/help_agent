package com.zfsoft.service.api;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/api")
public interface ApiService {
    //解析事项表中的JSon
    @ProcessFeignCalledResult
    @RequestMapping(value = "/analysisDataJson",method = {RequestMethod.GET})
    ApiResultSet analysisDataJson();
}
