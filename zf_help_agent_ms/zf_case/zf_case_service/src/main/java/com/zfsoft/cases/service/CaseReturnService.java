package com.zfsoft.cases.service;

import com.zfsoft.cases.data.CaseReturn;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @（#）: caseReturnService
 * @description: 办件退件接口
 * @author: liangss
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/caseReturnService")
public interface CaseReturnService {


    /**
     * 材料退件列表
     * @param caseNumber
     * @param organOid
     * @param applyUserName
     * @param caseStatus
     * @param startDate
     * @param endDate
     * @param sourceApp
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryCaseReturnList",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryCaseReturnList(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                         @RequestParam(value = "organOid", required = false) String organOid,
                                                         @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                         @RequestParam(value = "returnStatus", required = false) String returnStatus,
                                                         @RequestParam(value = "informStatus", required = false) String informStatus,
                                                         @RequestParam(value = "caseStatus", required = false) Integer caseStatus,
                                                         @RequestParam(value = "startDate", required = false) String startDate,
                                                         @RequestParam(value = "endDate", required = false) String endDate,
                                                         @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                         @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 保存退件图片
     * @param caseReturn
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCaseReturn", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveCaseReturn(@RequestBody CaseReturn caseReturn);


    /**
     * 保存退件
     * @param caseReturn
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/outReturnFile", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> outReturnFile(@RequestBody CaseReturn caseReturn);


    /**
     * 保存退件告知
     * @param caseReturn
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCaseReturnInform", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveCaseReturnInform(@RequestBody CaseReturn caseReturn);

    /**
     * 根据办件caseId查询退件信息
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryCaseReturnByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<CaseReturn> queryCaseReturnByCaseOid(@RequestParam("caseOid") String caseOid);


    /**
     * 根据办件编号查询退件办件信息
     * @param caseNumber
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOneRetuenCaseByCaseNumber", method = {RequestMethod.GET})
    ApiResultSet<QlCase> getOneRetuenCaseByCaseNumber(@RequestParam("caseNumber") String caseNumber);


}
