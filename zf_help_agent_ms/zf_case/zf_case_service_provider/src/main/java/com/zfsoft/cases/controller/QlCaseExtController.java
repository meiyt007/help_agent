package com.zfsoft.cases.controller;

import com.zfsoft.cases.data.QlCaseExt;
import com.zfsoft.cases.manager.QlCaseExtManager;
import com.zfsoft.cases.service.QlCaseExtService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @（#）: QlCaseExtController
 * @description: 办件扩展信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseExtController implements QlCaseExtService {

    @Resource
    private QlCaseExtManager qlCaseExtManager;

    @Override
    public ApiResultSet<QlCaseExt> saveQlCaseExt(QlCaseExt qlCaseExt) {
        qlCaseExtManager.saveQlCaseExt(qlCaseExt);
        ApiResultSet<QlCaseExt> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseExt);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseExt> queryQlCaseExtByCaseOid(String caseOid) {
        QlCaseExt qlCaseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
        ApiResultSet<QlCaseExt> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseExt);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseExt> saveQlCaseExtForBrowers(QlCaseExt qlCaseExt) {
        qlCaseExtManager.saveQlCaseExt(qlCaseExt);
        ApiResultSet<QlCaseExt> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseExt);
        return apiResultSet;
    }
}
