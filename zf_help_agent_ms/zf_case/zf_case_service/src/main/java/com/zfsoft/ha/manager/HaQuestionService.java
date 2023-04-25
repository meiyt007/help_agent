package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaQuestion;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理：帮代办服务，用户常见问题管理
 * @author kangax
 * @version 1.0
 * @date 2022/7/15 01:17
 */
@RequestMapping("/userManagement/question")
public interface HaQuestionService {
    /**
     * @description: 用户常见问题管理
     *
     * @param question 问题
     * @param workUserId 帮代办工作人员ID
     * @param pageNum 当前页码
     * @param pageSize 分页数量
     * @author: kangax
     * @date: 2022-07-28 00:04
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryQuestionWithPage(@RequestParam(value = "question", required = false) String question,
                                                   @RequestParam(value = "workUserId", required = false) String workUserId,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description: 保存用户常见问题
     * @param haQuestion 常见问题实体
     * @author: kangax
     * @date: 2022-07-28 00:05
     * */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<HaQuestion> saveUserQuestion(@RequestBody HaQuestion haQuestion);

    /**
     * @description: 获取单个用户常见问题信息
     * @param id 用户常见问题主键
     * @author: kangax
     * @date: 2022-07-28 00:04
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<HaQuestion> getHaQuestionById(@PathVariable("id") Long id);

    /**
     * @description: 删除指定Id的用户常见问题信息
     * @param id 用户常见问题主键
     * @author: kangax
     * @date: 2022-07-28 00:04
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    ApiResultSet<HaQuestion> deleteHaQuestionById(@PathVariable("id") Long id);


}
