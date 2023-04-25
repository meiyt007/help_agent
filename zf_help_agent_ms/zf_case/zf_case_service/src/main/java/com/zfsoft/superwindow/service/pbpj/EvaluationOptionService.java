package com.zfsoft.superwindow.service.pbpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.EvaluationOption;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName EvaluationOptionService
 * @Description 平板评价评价选项
 * @Author wangwg
 * @Since 2021-02-25
 **/
@RequestMapping("/evaluation/evaluationOption")
public interface EvaluationOptionService {


    /**
     * 保存平板评价选项
     *
     * @author wangwg
     * @date 2021-02-23
     * @param evaluationOptions 评价选项
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveEvaluationOption",method = {RequestMethod.POST})
    ApiResultSet<HashMap<String,String>> saveEvaluationOption(@RequestBody List<EvaluationOption> evaluationOptions);


    /**
     * 平板评价选项list
     *
     * @author wangwg
     * @date 2021-02-23
     * @param standardOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryEvaluationOptionList",method = {RequestMethod.GET})
    ApiResultSet<List<EvaluationOption>> queryEvaluationOptionList(@RequestParam(value = "standardOid", required = false) String standardOid);

    /**
     * 删除选项
     *
     * @author wangwg
     * @date 2021-02-23
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delEvaluationOption",method = {RequestMethod.GET})
    ApiResultSet delEvaluationOption(@RequestParam(value = "id", required = false) Long id);
}
