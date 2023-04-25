package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaQuestion;
import com.zfsoft.ha.data.vo.HaQuestionVo;
import com.zfsoft.ha.front.HaWorkUserQuestionService;
import com.zfsoft.ha.managers.HaUserQuestionManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 前台用户常见问题controller
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/19 10:30
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaWorkUserQuestionController implements HaWorkUserQuestionService {

    /**
     * 用户常见问题manager
     */
    private final HaUserQuestionManager haUserQuestionManager;

    /**
     * 根据用户常见问题信息模糊查询用户常见问题列表
     * @param question 用户常见问题
     * @return: ApiResultSet<List<HaQuestionVo>> 返回用户常见问题列表信息
     * @author: kangax
     * @date: 2022-07-28 00:31
     */
    @Override
    public ApiResultSet<List<HaQuestionVo>> queryHaQuestionListByLikeQuestion(String question) {
        log.info("进入根据用户常见问题信息模糊查询用户常见问题列表Controller，参数 question: {}", question);
        ApiResultSet<List<HaQuestionVo>> apiResultSet = new ApiResultSet<>();
        List<HaQuestionVo> list = haUserQuestionManager.queryHaQuestionListByLikeQuestion(question);
        apiResultSet.setCode(ApiResultSet.SUCCESS);
        apiResultSet.setMessage("请求成功");
        apiResultSet.setTime(String.valueOf(new Date()));
        apiResultSet.setData(list);
        return apiResultSet;
    }

    /**
     * @description: 根据id获取常见问题
     * @params： [id 主键]
     * @return: ApiResultSet<HaQuestionVo> 返回用户常见问题单个详细信息
     * @author: kangax
     * @date: 2022-07-28 00:50
     */
    @Override
    public ApiResultSet<HaQuestionVo> getQuestionInfoById(Long id) {
        log.info("进入根据id获取常见问题Controller，参数 id: {}", id);
        ApiResultSet<HaQuestionVo> apiResultSet = new ApiResultSet<>();
        if (id == null) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("请求参数不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        HaQuestionVo haQuestionVo = haUserQuestionManager.getQuestionInfoById(id);
        if (haQuestionVo != null) {
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("请求成功");
            apiResultSet.setTime(String.valueOf(new Date()));
            apiResultSet.setData(haQuestionVo);
            log.info("根据id获取常见问题成功返回信息：{}", haQuestionVo);
            return apiResultSet;
        } else {
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            apiResultSet.setMessage("根据id查询用户常见问题为空");
            apiResultSet.setTime(String.valueOf(new Date()));
            log.error("根据id查询用户常见问题为空");
            return apiResultSet;
        }

    }

    /**
     * @description: 保存用户常见问题
     * @params： [haQuestion 用户常见问题实体]
     * @author: kangax
     * @date: 2022-07-28 01:00
     */
    @Override
    public ApiResultSet saveQuestionInfo(HaQuestion haQuestion) {
        log.info("进入新增或修改用户常见问题信息Controller haQuestion:{}", haQuestion);
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (haQuestion.getQuestion() == null || "".equals(haQuestion.getQuestion())) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("保存时常见问题不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        if (haQuestion.getAnswer() == null || "".equals(haQuestion.getAnswer())) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("保存时：问题答案不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        //根据ID查询用户常见问题信息
        HaQuestion haQuestionById = haUserQuestionManager.getHaQuestionById(haQuestion.getId());
        if (haQuestionById != null) {
            //说明是修改设置修改人
            haQuestion.setUpdateBy(currentHaLoginUserInfo.getName());
        } else {
            //说明是新增 设置创建人
            haQuestion.setCreateBy(currentHaLoginUserInfo.getName());
        }
        //设置常见问题所属工作人员
        haQuestion.setWorkUserId(currentHaLoginUserInfo.getId());
        haQuestion.setWorkUserName(currentHaLoginUserInfo.getName());
        int index = haUserQuestionManager.saveUserQuestion(haQuestion);
        if (index > 0) {
            //说明有新增或修改
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("新增或修改成功");
            apiResultSet.setTime(String.valueOf(new Date()));
            log.info("新增或修改常见问题信息成功");
        } else {
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            apiResultSet.setMessage("新增或修改失败！");
            log.error("新增或修改常见问题信息失败");
        }
        return apiResultSet;
    }

    /**
     * @description: 单个或批量删除用户常见问题
     * @params： [ids 主键]
     * @author: kangax
     * @date: 2022-07-28 01:17
     */
    @Override
    public ApiResultSet deleteQuestion(List<Long> ids) {
        log.info("进入删除用户常见问题controller ids:{}", ids);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (ids.size() == 0) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("请求参数不完整id不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        try {
            haUserQuestionManager.deleteHaQuestionByIds(ids);
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("删除成功！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        } catch (Exception e) {
            apiResultSet.setMessage("删除失败");
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            apiResultSet.setStackTrace(e.getMessage());
            return apiResultSet;
        }
    }
}
