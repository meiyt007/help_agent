package com.zfsoft.cases.controller;

import com.zfsoft.cases.data.QlCaseLinkResult;
import com.zfsoft.cases.manager.QlCaseLinkResultManager;
import com.zfsoft.cases.service.QlCaseLinkResultService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseLinkResultController
 * @description: 办件审批环节信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseLinkResultController implements QlCaseLinkResultService {

    @Resource
    private QlCaseLinkResultManager qlCaseLinkResultManager;

    @Override
    public ApiResultSet<Map<String, String>> saveQlCaseLinkResult(QlCaseLinkResult qlCaseLinkResult) {
        Map<String, String> map=qlCaseLinkResultManager.saveQlCaseLinkResult(qlCaseLinkResult);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseLinkResult>> queryQlCaseLinkResultListByCaseOid(String caseOid) {
        List<QlCaseLinkResult> list = qlCaseLinkResultManager.queryQlCaseLinkResultListByCaseOid(caseOid);
        ApiResultSet<List<QlCaseLinkResult>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseLinkResult> queryBjQlCaseLinkResultByCaseOid(String caseOid) {
        QlCaseLinkResult qlCaseLinkResult = qlCaseLinkResultManager.queryBjQlCaseLinkResultByCaseOid(caseOid);
        ApiResultSet<QlCaseLinkResult> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseLinkResult);
        return apiResultSet;
    }
}
