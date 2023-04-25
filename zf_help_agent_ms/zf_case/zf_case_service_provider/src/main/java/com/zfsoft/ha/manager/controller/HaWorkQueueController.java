package com.zfsoft.ha.manager.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.controller.QlCaseController;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.HaWorkQueue;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.manager.HaWorkQueueService;
import com.zfsoft.ha.managers.HaVideoConsultationManager;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/26 14:58
 */
@Slf4j
@RestController
public class HaWorkQueueController implements HaWorkQueueService {
    /**
     * 帮代办人员及办事队列service层
     */
    @Resource
    private HaWorkQueueManager haWorkQueueManager;
    @Resource
    private QlCaseController qlCaseController;
    @Resource
    private HaVideoConsultationManager haVideoConsultationManager;

    @Override
    public ApiResultSet<PageResult> queryQueueVoWithPage(HaWorkQueueVo haWorkQueueVo, Integer pageNumber, Integer pageSize) {
        log.info("办事队列：进入根据办事队列信息获取办事队列：入参：haWorkQueueVo:{}" , haWorkQueueVo);
        PageHelper.startPage(pageNumber, pageSize);
        PageResult<HaWorkQueueVo> resultSet = null;
        try {
            resultSet = haWorkQueueManager.queryWorkQueueListWithPage(haWorkQueueVo);
            log.info("办事队列：根据办事队列信息获取办事队列成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("办事队列：根据办事队列信息获取办事队列失败:" + e.getMessage());
            return new ApiResultSet<>(500, "办事队列：根据办事队列信息获取办事队列失败", e.getMessage());
        }
        return ApiResultSet.ok("办事队列：根据办事队列信息获取办事队列成功", resultSet);
    }


    @Override
    public ApiResultSet<HaWorkQueue> saveWorkQueue(HaWorkQueue haWorkQueue) {
        log.info("办事队列：进入修改或保存办事队列信息：入参：" + haWorkQueue);
        ApiResultSet<Map<String, String>> loginUser = qlCaseController.getLoginUser();
        String message;
        try {
            if (haWorkQueue.getId() != null) {
                haWorkQueue.setUpdateBy(loginUser.getData().get("userName"));
                haWorkQueue.setUpdateDate(new Date());
            } else {
                haWorkQueue.setCreateDate(new Date());
                haWorkQueue.setUpdateDate(new Date());
                haWorkQueue.setUpdateBy(loginUser.getData().get("userName"));
                haWorkQueue.setCreateBy(loginUser.getData().get("userName"));
            }
            int index = haWorkQueueManager.saveHaWorkQueue(haWorkQueue);
            if (index != 0) {
                if (haWorkQueue.getId() != null) {
                    //说明有新增或修改
                    message = "修改成功";
                } else {
                    message = "新增成功";
                }
            } else {
                message = "已存在，没有更改";
            }
            log.info("办事队列：修改或保存办事队列信息成功" + message);
        } catch (Exception e) {
            log.info("办事队列：修改或保存办事队列信息失败:" + e.getMessage());
            return new ApiResultSet<>(500, "办事队列：修改或保存办事队列信息失败", e.getMessage());
        }
        return ApiResultSet.ok("办事队列：进入根据资源名称和工作人员名称获取办事队列信息成功", haWorkQueue);

    }

    @Override
    public ApiResultSet<HaWorkQueue> deleteQueueId(String id) {
        log.info("办事队列：进入根据id删除办事队列：入参：" + id);
        String message;
        try {
            int del = haWorkQueueManager.deleteWorkQueueId(id);
            if (del != 0) {
                log.info("办事队列：根据id删除办事队列成功");
                message = "删除成功";
            } else {
                log.info("办事队列：根据id删除办事队列成功");
                message = "未找到删除对象";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("办事队列：根据id删除办事队列失败:" + e.getMessage());
            return new ApiResultSet<>(500, "办事队列：根据id删除办事队列失败", e.getMessage());
        }
        return ApiResultSet.ok("办事队列：根据id删除办事队列成功" + message, null);
    }

    @Override
    public ApiResultSet<HaWorkQueue> getWorkQueueById(String id) {
        log.info("办事队列：进入根据id获取办事队列：入参：" + id);
        HaWorkQueue workQueueById = null;

        try {
            workQueueById = haWorkQueueManager.getWorkQueueById(id);
            String record = haVideoConsultationManager.queryVideoRecord(Long.valueOf(id));
            if (StrUtil.isNotBlank(record)) {
                JSONArray jsonArray = JSON.parseArray(record);
                workQueueById.setVideoRecordList(jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("办事队列：进入根据id获取办事队列失败:" + e.getMessage());
            return new ApiResultSet<>(500, "办事队列：进入根据id获取办事队列失败", e.getMessage());
        }
        return ApiResultSet.ok("办事队列：进入根据id获取办事队列成功", workQueueById);
    }
}
