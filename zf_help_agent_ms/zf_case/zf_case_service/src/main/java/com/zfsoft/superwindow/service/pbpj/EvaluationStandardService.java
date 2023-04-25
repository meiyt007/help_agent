package com.zfsoft.superwindow.service.pbpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.EvaluationStandard;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * @ClassName EvaluationStandardService
 * @Description 平板评价评价标准
 * @Author wangwg
 * @Since 2021-02-25
 **/
@RequestMapping("/evaluation/evaluationStandard")
public interface EvaluationStandardService {

    /**
     * 平板评价配置指标内容
     *
     * @author wangwg
     * @date 2021-02-23
     * @param standardName 评价维度名称
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/listEvaluationStandardPage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<EvaluationStandard>> listEvaluationStandardPage(@RequestParam(value = "standardName", required = false) String standardName,
                                                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 保存平板评价配置指标内容
     *
     * @author wangwg
     * @date 2021-02-23
     * @param evaluationStandard 评价维度名称
     * @return
     *
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveEvaluationStandard",method = {RequestMethod.POST})
    ApiResultSet<HashMap<String,String>> saveEvaluationStandard(@RequestBody EvaluationStandard evaluationStandard);

    /**
     * 删除
     *
     * @author wangwg
     * @date 2021-02-23
     * @param standardOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delEvaluation",method = {RequestMethod.GET})
    ApiResultSet delEvaluation(@RequestParam(value = "standardOid", required = false) String standardOid);

}
