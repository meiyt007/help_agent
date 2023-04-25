package com.zfsoft.ha.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.requestData.HaEvalResultRequestData;
import com.zfsoft.ha.data.responseData.HaEvalItemResponseData;
import com.zfsoft.ha.data.responseData.HaEvalResultResponseData;
import com.zfsoft.ha.front.HaEvalService;
import com.zfsoft.ha.managers.HaEvalManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 帮代办评价控制层
 * @author: kangax
 * @date: 2022-08-12 14:32
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaEvalController implements HaEvalService {

    /**
     * 帮代办评价manager
     */
    private final HaEvalManager haEvalManager;

    /**
     * @description: 获取评价项
     * @author: kangax
     * @date: 2022-08-12 17:48
     */
    @Override
    public ApiResultSet getEvalItem() {
        log.info("进入获取评价项Controller");
        List<HaEvalItemResponseData> haEvalItemResponseDataList = haEvalManager.getEvalItem();
        log.info("评价项获取成功，responseData:  {}", haEvalItemResponseDataList);
        return ApiResultSet.ok("请求成功", haEvalItemResponseDataList);
    }

    /**
     * @param haEvalResultRequestData
     * @description:保存评价信息内容
     * @author: kangax
     * @date: 2022-08-12 17:48
     */
    @Override
    public ApiResultSet saveEvalResult(HaEvalResultRequestData haEvalResultRequestData) {
        log.info("进入保存评价信息Controller,参数：haEvalResultRequestData: {} ", haEvalResultRequestData);
        try {
            HaEvalResultResponseData haEvalResultRequestData1 = haEvalManager.saveEvalResult(haEvalResultRequestData);
            return ApiResultSet.ok("请求成功",haEvalResultRequestData1);
        } catch (ServiceException e) {
            log.error("保存帮代办评价信息失败，errorMessage: {}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "保存帮代办评价信息失败: " + e.getMessage());
        }
    }

    /**
     * @param haEvalResultRequestData
     * @description:保存评价信息内容
     * @author: kangax
     * @date: 2022-08-12 17:48
     */
    @Override
    public ApiResultSet updateEvalResult(HaEvalResultRequestData haEvalResultRequestData) {
        log.info("进入保存评价信息Controller,参数：haEvalResultRequestData: {} ", haEvalResultRequestData);
        try {
            haEvalManager.updateEvalResult(haEvalResultRequestData);
            return ApiResultSet.ok("请求成功");
        } catch (ServiceException e) {
            log.error("保存帮代办评价信息失败，errorMessage: {}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "保存帮代办评价信息失败: " + e.getMessage());
        }
    }


    /**
     * 获取工作人员的平均评价得分
     *
     * @return 工作人员萍爵分
     * @author yupeng
     * @date 2022年08月15 14:03:09
     */
    @Override
    public ApiResultSet getWorkUserEvalScore() {
        log.info("获取工作人员的平均评价得分");
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        BigDecimal evalScore = haEvalManager.getWorkUserEvalScore(currentHaLoginUserInfo.getId());
        JSONObject result = new JSONObject();
        result.put("evalScore", evalScore);
        return ApiResultSet.ok(result);
    }


}
