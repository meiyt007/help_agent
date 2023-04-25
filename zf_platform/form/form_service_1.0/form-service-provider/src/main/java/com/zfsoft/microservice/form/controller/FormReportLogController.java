package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormReportLog;
import com.zfsoft.microservice.form.manager.FormReportLogManager;
import com.zfsoft.microservice.form.service.FormReportLogService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName FormReportController
 * @Description 表单填报数据日志的实现类
 * @Author wuxx
 * @Date 2021-04-19 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormReportLogController implements FormReportLogService {

    @Resource
    private FormReportLogManager formReportLogManager;

    /**
     * @description:  查询表单填报数据日志的信息列表
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<PageResult<FormReportLog>> queryFormReportLogWithPage(String formMainOid,
                                                                              String startTime, String endTime,
                                                                              Integer pageNum, Integer pageSize){
        FormReportLog formReport = new FormReportLog();
        formReport.setFormMainOid(formMainOid);
        formReport.setStartTime(startTime);
        formReport.setEndTime(endTime);
        PageResult<FormReportLog> pageResult = formReportLogManager.queryFormReportLogWithPage(formReport,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }


    /**
     * @description:  获取表单填报数据的信息
     * @param id 表单填报数据实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormReportLog>  getFormReportLogById(Long id){
        FormReportLog formReportLog = formReportLogManager.getFormReportLogById(id);
        return new ApiResultSet<>(formReportLog);
    }

    @Override
    public ApiResultSet<FormReportLog> getFormConfigAndData(Long id) {
        FormReportLog formReportLog = formReportLogManager.getFormConfigAndData(id);
        return new ApiResultSet<>(formReportLog);
    }

}
