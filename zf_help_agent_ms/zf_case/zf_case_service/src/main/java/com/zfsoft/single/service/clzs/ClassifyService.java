package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hut
 * @date 2022/7/12
 */
@RequestMapping(value = "/classify")
public interface ClassifyService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getUploadMaterialClassification", method = {RequestMethod.POST})
    ApiResultSet getUploadMaterialClassification(@RequestParam(value = "caseOid") String caseOid,
                                                 @RequestParam(value = "caseMaterialOid") String caseMaterialOid,
                                                 @RequestParam(value = "attaOid") String attaOid);


}
