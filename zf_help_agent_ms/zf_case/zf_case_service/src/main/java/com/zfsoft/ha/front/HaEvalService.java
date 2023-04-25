package com.zfsoft.ha.front;

import com.zfsoft.ha.data.requestData.HaEvalResultRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 帮代办评价相关接口
 * @author: kangax
 * @date: 2022-08-12 14:28
 **/
@RequestMapping("/ha/eval")
public interface HaEvalService {

    /**
     * @description: 获取评价项
     * @author: kangax
     * @date: 2022-08-12 14:31
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getItem")
    ApiResultSet getEvalItem();

    /**
     * @param haEvalResultRequestData 评价结果请求参数
     * @description: 保存评价结果
     * @author: kangax
     * @date: 2022-08-12 17:46
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveEvalResult")
    ApiResultSet saveEvalResult(@RequestBody HaEvalResultRequestData haEvalResultRequestData);

    /**
    * Description: 评价结果请求参数
    * @param haEvalResultRequestData
    * @author zhaobf
    * date: 2023/4/6 15:59
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @ProcessFeignCalledResult
    @PostMapping(value = "/updateEvalResult")
    ApiResultSet updateEvalResult(@RequestBody HaEvalResultRequestData haEvalResultRequestData);

    /**
     * 获取工作人员的平均评价得分
     *
     * @return 工作人员萍爵分
     * @author yupeng
     * @date 2022年08月15 14:03:09
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getWorkUserEvalScore")
    ApiResultSet getWorkUserEvalScore();

}
