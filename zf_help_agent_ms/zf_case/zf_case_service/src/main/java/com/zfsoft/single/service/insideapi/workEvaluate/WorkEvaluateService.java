package com.zfsoft.single.service.insideapi.workEvaluate;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: workEvaluate
 * @description: 智能登记好差评对接service
 * @author: wangwg
 * @date: 2021/7/15
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/evaluateService")
public interface WorkEvaluateService {


    /**
     * 获取好差评评价内容
     *
     * @return
     * @author wangwg
     * @date 2021/28
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryEvaluateContent", method = {RequestMethod.GET})
    ApiResultSet queryEvaluateContent(@RequestParam(value = "star", required = false) String star);



    /**
     * 好差评系统评价保存办件信息
     *
     * @param caseOid
     * @return
     * @author wangwg
     * @date 2021/7/15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/evaluateaveQlCase", method = {RequestMethod.GET})
    ApiResultSet<String> evaluateSaveQlCase(@RequestParam(value = "caseOid", required = false) String caseOid);


    /**
     * 获取办件评价信息
     *
     * @param caseNum
     * @return
     * @author wangwg
     * @date 2021/7/15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEvaluateInfo", method = {RequestMethod.GET})
    ApiResultSet<String> getEvaluateInfo(@RequestParam(value = "caseNum", required = false) String caseNum);

    /**
     * 是否已评价
     *
     * @param caseNum
     * @return
     * @author wangwg
     * @date 2021/7/15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/isEvaluate", method = {RequestMethod.GET})
    ApiResultSet<String> isEvaluate(@RequestParam(value = "caseNum", required = false) String caseNum);

    /**
     * 好差评系统保存办件评价信息
     *
     * @param caseNUm,starNUm,contentOids
     * @return
     * @author wangwg
     * @date 2021/7/15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveQLCaseEvaluation", method = {RequestMethod.GET})
    ApiResultSet<String> saveQLCaseEvaluation(@RequestParam(value = "caseNUm", required = false) String caseNUm,
                                              @RequestParam(value = "starNUm", required = false) String starNUm,
                                              @RequestParam(value = "contentOids", required = false) String contentOids,
                                              @RequestParam(value = "content", required = false) String content);


}
