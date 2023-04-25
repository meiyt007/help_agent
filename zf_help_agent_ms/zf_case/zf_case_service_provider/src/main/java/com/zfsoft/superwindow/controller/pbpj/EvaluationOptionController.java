package com.zfsoft.superwindow.controller.pbpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.yxpz.EvaluationOption;
import com.zfsoft.superwindow.manager.pbpj.EvaluationOptionManager;
import com.zfsoft.superwindow.service.pbpj.EvaluationOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author wangwg
 * @description 一件事平板评价评价选项
 * @date 2021-02-25
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class EvaluationOptionController implements EvaluationOptionService {

    @Resource
    private EvaluationOptionManager evaluationOptionManager;

    @Override
    public ApiResultSet<HashMap<String, String>> saveEvaluationOption(List<EvaluationOption> evaluationOptions) {
        //获取当前登录用户信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        HashMap<String, String> map = evaluationOptionManager.saveEvaluationOption(evaluationOptions,loginUser);
        ApiResultSet<HashMap<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<EvaluationOption>> queryEvaluationOptionList(String standardOid) {
        List<EvaluationOption> evaluationOptionList =evaluationOptionManager.queryEvaluationOptionList(standardOid);
        ApiResultSet<List<EvaluationOption>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(evaluationOptionList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet delEvaluationOption(Long id) {
        evaluationOptionManager.delEvaluationOption(id);
        return null;
    }
}
