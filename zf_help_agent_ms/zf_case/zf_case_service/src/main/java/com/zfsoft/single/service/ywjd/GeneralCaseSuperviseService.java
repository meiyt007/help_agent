package com.zfsoft.single.service.ywjd;

import com.zfsoft.cases.data.QlCase;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: wangwg
 * @create: 2021-05-17
 * @description: 办件电子表单
 */
@RequestMapping(value = "/caseSupervise")
public interface GeneralCaseSuperviseService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryPageList",method = RequestMethod.GET)
    ApiResultSet<PageResult<QlCase>> queryGeneralCaseSupervisePage(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                                   @RequestParam(value = "registerUser", required = false) String registerUser,
                                                                   @RequestParam(value = "applyUserName", required = false) String applyUserName,
                                                                   @RequestParam(value = "startDate", required = false) String startDate,
                                                                   @RequestParam(value = "endDate", required = false) String endDate,
                                                                   @RequestParam(value = "overTime", required = false) String overTime,
                                                                   @RequestParam(value = "sourceApp", required = false) Integer sourceApp,
                                                                   @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOrganUserList",method = RequestMethod.GET)
    ApiResultSet<PageResult<QlCase>> getOrganUserList();

    @ProcessFeignCalledResult
    @PostMapping(value = "/queryUserSimpleTree")
    ApiResultSet queryUserSimpleTree();
}
