package com.zfsoft.single.service.formTemplate;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/template")
public interface MaterialTemplateService {

    /**
     * @description 获取表单填充模板集合
     * @param reportOid 办件OID
     * @param serviceOid 事项OID
     * @author meiyt
     * @date 2022/6/1
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getTemplateList"}, method = {RequestMethod.GET})
    ApiResultSet<List<TermlateDataDto>> getTemplateList(@RequestParam(value = "reportOid") String reportOid,
                                                        @RequestParam(value = "serviceOid") String serviceOid);
}
