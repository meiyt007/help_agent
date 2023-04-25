package com.zfsoft.cases.controller;

import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.manager.QlCaseApplayManager;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.ha.data.responseData.HaQlCaseAppayResponseData;
import com.zfsoft.platform.common.data.ApiResultSet;
import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: QlCaseApplayController
 * @description: 办件申请信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseApplayController implements QlCaseApplayService {

    @Resource
    private QlCaseApplayManager qlCaseApplayManager;

    @Override
    public ApiResultSet<QlCaseApplay> saveQlCaseApplay(QlCaseApplay qlCaseApplay) {
        qlCaseApplayManager.saveQlCaseApplay(qlCaseApplay);
        ApiResultSet<QlCaseApplay> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseApplay);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseApplay> queryQlCaseApplayByCaseOid(String caseOid) {
        QlCaseApplay qlCaseApplay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
        ApiResultSet<QlCaseApplay> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseApplay);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseApplay> savePostResultInfoForBrowers(QlCaseApplay qlCaseApplay) {
        qlCaseApplayManager.savePostResultInfoForBrowers(qlCaseApplay);
        ApiResultSet<QlCaseApplay> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseApplay);
        return apiResultSet;
    }


}
