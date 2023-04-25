package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaPerformancePhoneRecord;
import com.zfsoft.ha.data.requestData.HaPerPhoneRequestData;
import com.zfsoft.ha.front.HaPerformancePhoneService;
import com.zfsoft.ha.managers.HaPerformancePhoneRecordManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/27 9:22
 */

@RestController
@Slf4j
public class HaPerformancePhoneController implements HaPerformancePhoneService {
    /**
     * 帮代办人员电话绩效表实现层
     */
    @Resource
    private HaPerformancePhoneRecordManager haPerformancePhoneRecordManager;

    /**
     * @description:  查询帮代办人员电话绩效分页信息列表
     * @author: zhaobf
     * @Date: 2022年7月26日14:04:40
     * @return  ApiResultSet<PageResult<HaWorkGroup>> 获取帮代办人员电话绩效分页信息列表详情
     **/
    @Override
    public ApiResultSet<PageResult<HaPerformancePhoneRecord>> queryPerPhoneWithPage(HaPerPhoneRequestData haPerPhoneRequestData) throws Exception {
        log.info("查询帮代办人员电话绩效分页信息列表， haPerPhoneRequestData:{}",haPerPhoneRequestData);
        PageResult<HaPerformancePhoneRecord> pageResult = haPerformancePhoneRecordManager.queryPerPhoneWithPage(haPerPhoneRequestData);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaPerformancePhoneRecord>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files)  {
        log.info("进入导入电话绩效");
        Map<String, String> resultSet = haPerformancePhoneRecordManager.ImportPhoneExcel(request,files);
        log.info("进入导入电话绩效结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);
    }

}
