package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hut
 * @date 2022/7/12
 */
@RequestMapping(value = "/preTrial")
public interface IntelligentPreTrialService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getIntelligentAuditMaterialList", method = {RequestMethod.GET})
    ApiResultSet getIntelligentAuditMaterialList(@RequestParam(value = "caseOid") String caseOid,
                                                 @RequestParam(value = "serviceOid") String serviceOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/viewDetailResult", method = {RequestMethod.GET})
    ApiResultSet viewDetailResult(@RequestParam(value = "caseOid") String caseOid,
                                  @RequestParam(value = "caseMaterialOid") String caseMaterialOid,
                                  @RequestParam(value = "materialOid", required = false) String materialOid,
                                  @RequestParam(value = "serviceOid", required = false) String serviceOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/intelligentAuditByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet intelligentAuditByCaseMaterialOid(@RequestParam(value = "caseMaterialOid") String caseMaterialOid);
}
