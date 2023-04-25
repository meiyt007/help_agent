package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaQuestion;
import com.zfsoft.ha.dbaccess.data.DbHaUserQuestion;
import com.zfsoft.ha.manager.HaQuestionService;
import com.zfsoft.ha.managers.HaUserQuestionManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台用户常见问题管理控制层
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/15 10:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaUserQuestionController implements HaQuestionService {

    /**
     * 用户问题管理manager
     */
    private final HaUserQuestionManager haUserQuestionManager;

    /**
     * 分页查询用户问题管理信息
     *
     * @param question 问题
     * @param workUserId 帮代办人员ID
     * @param pageNum 当前页码
     * @param pageSize 分页数量
     * @author: kangax
     * @date: 2022-07-28 00:04
     * */
    @Override
    public ApiResultSet<PageResult> queryQuestionWithPage(String question, String workUserId, Integer pageNum, Integer pageSize) {
        log.info("进入用户常见问题分页查询controller查询参数 question: {},workUserId: {}", question, workUserId);
        PageResult<DbHaUserQuestion> pageResult = this.haUserQuestionManager.queryInfoWithPage(question, workUserId, pageNum, pageSize);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description: 用户常见问题保存
     *
     * @param haQuestion 用户常见问题实体
     * @author: kangax
     * @date: 2022-07-28 00:04
     */
    @Override
    public ApiResultSet<HaQuestion> saveUserQuestion(@RequestBody HaQuestion haQuestion) {
        log.info("进入修改或保存用户常见问题信息Controller,参数 haQuestion : {}",haQuestion);
        ApiResultSet<HaQuestion> apiResultSet = new ApiResultSet<>();
        int index = haUserQuestionManager.saveUserQuestion(haQuestion);
        if (index >0) {
            //说明有新增或修改
            log.info("修改或保存用户常见问题信息成功");
            apiResultSet.setCode(200);
        } else {
            log.error("修改或保存用户常见问题信息成功");
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }

    /**
     * @description: 根据ID获取单个信息
     *
     * @param id 用户常见问题主键
     */
    @Override
    public ApiResultSet<HaQuestion> getHaQuestionById(Long id) {
        log.info("进入根据ID获取单个信息Controller，id : {}",id);
        HaQuestion haQuestion = haUserQuestionManager.getHaQuestionById(id);
        return new ApiResultSet<HaQuestion>(haQuestion);
    }

    /**
     * @description: 删除单个信息
     *
     * @param id 用户常见问题主键
     */
    @Override
    public ApiResultSet<HaQuestion> deleteHaQuestionById(Long id) {
        log.info("进入逻辑删除用户常见问题信息Controller，id : {}",id);
        ApiResultSet<HaQuestion> apiResultSet = new ApiResultSet<>();
        int index = haUserQuestionManager.deleteHaQuestionById(id);
        if (index > 0) {
            //删除成功
            log.info("逻辑删除用户常见问题信息成功");
            apiResultSet.setCode(200);
        } else {
            log.error("逻辑删除用户常见问题信息失败");
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }
}
