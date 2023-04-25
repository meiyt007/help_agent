package com.zfsoft.superwindow.service.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.front.ManualEvaluation;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/manualEvaluation")
public interface ManualEvaluationService {

    /**
     * 获取手动评价数据
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getManualEvaluation"}, method = {RequestMethod.GET})
    ApiResultSet selectByVirtualBusinessNum(@RequestParam(value = "virtualBusinessNum", required = false) String virtualBusinessNum);

    /**
     * 新增服务评价
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/saveManualEvaluation"}, method = {RequestMethod.POST})
    ApiResultSet saveManualEvaluation(@RequestBody ManualEvaluation manualEvaluation);

    /**
     * 修改服务评价
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/updateManualEvaluation"}, method = {RequestMethod.POST})
    ApiResultSet updateManualEvaluation(ManualEvaluation manualEvaluation);

    /**
     *推送服务评价
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/pushManualEvaluation/{virtualBusinessNum}"}, method = {RequestMethod.POST})
    ApiResultSet pushManualEvaluation(@PathVariable(value = "virtualBusinessNum", required = false) String virtualBusinessNum);

    /**
     *满意度百分比
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getSatisfaction"}, method = {RequestMethod.GET})
    ApiResultSet getSatisfaction();

    /**
     *叫号点击完成判断是否评价再去判断是否推送
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getEvaluateAndPush"}, method = {RequestMethod.GET})
    ApiResultSet getEvaluateAndPush(@RequestParam(value = "virtualBusinessNum", required = false) String virtualBusinessNum);

    /**
     * 分页查询列表
     * @param manualEvaluation
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/manualEvaluationList"}, method = {RequestMethod.POST})
    ApiResultSet queryPageList(ManualEvaluation manualEvaluation,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

}
