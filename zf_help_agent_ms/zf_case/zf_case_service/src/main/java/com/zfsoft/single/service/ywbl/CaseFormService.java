
package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.ywbl.CaseForm;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: dongxl
 * @create: 2020-12-01
 * @description: 办件电子表单
 */
@RequestMapping(value = "/caseForm")
public interface CaseFormService {

    /**
     * 保存或者更新
     * dongxl
     * @param caseForm
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated CaseForm caseForm);

    @ProcessFeignCalledResult
    @PostMapping(value = "/caseFormByCaseOid")
    ApiResultSet caseFormByCaseOid(String caseOid, String sxSerFormOid);

    @ProcessFeignCalledResult
    @PostMapping(value = "/caseFormByCase")
    ApiResultSet caseFormByCase(CaseForm caseForm);

    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdateComboCaseForm")
    ApiResultSet saveOrUpdateComboCaseForm(@RequestBody CaseForm caseForm);

}
