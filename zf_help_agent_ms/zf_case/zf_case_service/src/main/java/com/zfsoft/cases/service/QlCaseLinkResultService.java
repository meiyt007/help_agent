package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCaseLinkResult;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseLinkResultService
 * @description: 办件环节审核信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseLinkResultService")
public interface QlCaseLinkResultService {

    /**
     * 保存办件环节审核信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param qlCaseLinkResult
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseLinkResult>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseLinkResult",method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveQlCaseLinkResult(@RequestBody QlCaseLinkResult qlCaseLinkResult);

    
    /**
     * 通过办件业务主键获取办件环节审核列表信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.util.List<com.zfsoft.data.QlCaseLinkResult>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryQlCaseLinkResultListByCaseOid",method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseLinkResult>> queryQlCaseLinkResultListByCaseOid(@RequestParam("caseOid") String caseOid);


    /**
     * 通过办件业务主键获取办件办结环节审核信息
     *
     * @author wangwg
     * @date 2021/03/31
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseLinkResult>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryBjQlCaseLinkResultByCaseOid",method = {RequestMethod.GET})
    ApiResultSet<QlCaseLinkResult> queryBjQlCaseLinkResultByCaseOid(@RequestParam("caseOid") String caseOid);
}
