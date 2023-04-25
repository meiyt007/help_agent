package com.zfsoft.cases.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseSituationTitleValRelationService
 * @description: 办件情形标题选项关系服务定义接口
 * @author: wangwg
 * @date: 2020/11/30
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/relationService")
public interface QlCaseSituationTitleValRelationService {

    /**
     * 根据办件获取情形标题选项信息
     *
     * @param caseOid
     * @author wangwg
     * @date 2020/11/30
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCaseTitleValueList", method = {RequestMethod.GET})
    ApiResultSet<List<Map<String, String>>> getCaseTitleValueList(@RequestParam(value = "caseOid") String caseOid);

}
