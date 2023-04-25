package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCaseExt;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: QlCaseExtService
 * @description: 办件扩展信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseExtService")
public interface QlCaseExtService {

    /**
     * 保存办件扩展信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param qlCaseExt
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseExt>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseExt",method = {RequestMethod.POST})
    ApiResultSet<QlCaseExt> saveQlCaseExt(@RequestBody QlCaseExt qlCaseExt);


    /**
     *办件业务主键查询办件扩展信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseExtByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseExt> queryQlCaseExtByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * @description 网站端保存办件扩展信息
     * @param qlCaseExt
     * @author meiyt
     * @date 2022/6/11
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseExtForWeb",method = {RequestMethod.POST})
    ApiResultSet<QlCaseExt> saveQlCaseExtForBrowers(@RequestBody QlCaseExt qlCaseExt);
}
