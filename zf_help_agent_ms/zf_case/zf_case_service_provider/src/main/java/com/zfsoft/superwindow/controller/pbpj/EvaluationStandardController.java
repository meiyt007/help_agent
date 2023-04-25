package com.zfsoft.superwindow.controller.pbpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.EvaluationStandard;
import com.zfsoft.superwindow.manager.pbpj.EvaluationOptionManager;
import com.zfsoft.superwindow.manager.pbpj.EvaluationStandardManager;
import com.zfsoft.superwindow.service.pbpj.EvaluationStandardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author wangwg
 * @description 一件事平板评价评价选项
 * @date 2021-02-25
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class EvaluationStandardController implements EvaluationStandardService {

    @Resource
    private EvaluationStandardManager evaluationStandardManager;

    @Resource
    private EvaluationOptionManager evaluationOptionManager;

    @Override
    public ApiResultSet<PageResult<EvaluationStandard>> listEvaluationStandardPage(String standardName, Integer pageNum, Integer pageSize) {
        PageResult<EvaluationStandard> pageResult = evaluationStandardManager.listEvaluationStandardPage(standardName,pageNum,pageSize);
        ApiResultSet<PageResult<EvaluationStandard>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return new ApiResultSet<>(pageResult);
    }

    @Override
    public ApiResultSet<HashMap<String, String>> saveEvaluationStandard(EvaluationStandard evaluationStandard) {
        //获取当前登录用户信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        HashMap<String, String> map = evaluationStandardManager.saveEvaluationStandard(evaluationStandard,loginUser);
        ApiResultSet<HashMap<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet delEvaluation(String standardOid) {
        evaluationStandardManager.delEvaluationStandard(standardOid);
        evaluationOptionManager.delEvaluationOption(standardOid);
       return null;
    }
}
