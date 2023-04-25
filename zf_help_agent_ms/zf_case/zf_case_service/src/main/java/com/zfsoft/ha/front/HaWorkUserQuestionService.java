package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaQuestion;
import com.zfsoft.ha.data.vo.HaQuestionVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 前台用户常见问题信息相关接口
 * @author kangax
 * @version 1.0
 * @date 2022/7/19 10:11
 */
@RequestMapping("/ha/question")
public interface HaWorkUserQuestionService {
    /**
     * @description:  根据用户常见问题信息模糊查询用户常见问题列表
     * @params：[ question 用户常见问题]
     * @return:  ApiResultSet<List<HaQuestionVo>> 返回用户常见信息列表
     * @author: kangax
     * @date: 2022-07-28 00:29
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/listQuestion", method = {RequestMethod.GET})
    ApiResultSet<List<HaQuestionVo>> queryHaQuestionListByLikeQuestion(@RequestParam String question);

    /**
     * @description: 获取常见问题信息
     * @params： [id 主键]
     * @return:  ApiResultSet<HaQuestionVo> 返回单个用户常见问题信息
     * @author: kangax
     * @date: 2022-07-28 00:49
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQuestionInfo", method = {RequestMethod.GET})
    ApiResultSet<HaQuestionVo> getQuestionInfoById(@RequestParam Long id);

    /**
     * @description: 保存用户常见问题
     * @params： [haQuestion 用户常见问题实体]
     * @author: kangax
     * @date: 2022-07-28 00:58
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveQuestionInfo", method = {RequestMethod.POST})
    ApiResultSet saveQuestionInfo(HaQuestion haQuestion);

    /**
     * @description: 单个或批量删除用户常见问题
     * @params： [ids 主键,]
     * @author: kangax
     * @date: 2022-07-28 01:15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteQuestion", method = {RequestMethod.POST})
    ApiResultSet deleteQuestion(@RequestParam List<Long> ids);

}
