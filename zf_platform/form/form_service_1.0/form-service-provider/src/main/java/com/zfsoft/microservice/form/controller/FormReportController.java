package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormReport;
import com.zfsoft.microservice.form.manager.FormReportManager;
import com.zfsoft.microservice.form.service.FormReportService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormReportController
 * @Description 表单填报数据的实现类
 * @Author wuxx
 * @Date 2021-04-19 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormReportController implements FormReportService {

    @Resource
    private FormReportManager formReportManager;

    /**
     * @description:  查询表单填报数据的信息列表
     * @param authorizeKey 授权key
     * @param businessKey 与业务系统关联的第三方key
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<PageResult<FormReport>> queryFormReportWithPage(String authorizeKey, String businessKey,
                                                                        String formMainOid ,String designOid,
                                                                        String startTime,String endTime,
                                                                        Integer pageNum, Integer pageSize){
        FormReport formReport = new FormReport();
        formReport.setBusinessKey(businessKey);
        formReport.setAuthorizeKey(authorizeKey);
        formReport.setDesignOid(designOid);
        formReport.setFormMainOid(formMainOid);
        formReport.setStartTime(startTime);
        formReport.setEndTime(endTime);
        PageResult<FormReport> pageResult = formReportManager.queryFormReportWithPage(formReport,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化表单填报数据的信息
     * @param id 表单填报数据实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet initFormReport(Long id){
        FormReport formReport = formReportManager.getFormReportById(id);
        return new ApiResultSet<>(formReport);
    }

    /**
     * @description:  表单填报数据的新增或者修改
     * @param formReport 表单填报数据实体类
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormReport> saveFormReport(@RequestBody FormReport formReport){
        formReportManager.saveFormReport(formReport);
        return  new ApiResultSet<>(formReport);
    }

    /**
     * @description:  表单填报数据的信息的删除
     * @param id 表单填报数据实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteFormReportById(Long id){
        formReportManager.deleteFormReportById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取表单填报数据的信息
     * @param id 表单填报数据实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormReport>  getFormReportById(Long id){
        FormReport formReport = formReportManager.getFormReportById(id);
        return new ApiResultSet<>(formReport);
    }

    /**
     * @description:  获取表单填报数据的信息
     * @param templateOid 业务主键
     * @author: templateOid
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormReport> getFormReportByReportOid(@PathVariable("templateOid")String templateOid){
        FormReport formReport = formReportManager.getFormReportByFormReportOid(templateOid);
        return new ApiResultSet<>(formReport);
    }

    /**
     * @description:  表单填报数据的查询
     * @param authorizeKey 授权key
     * @param businessKey 业务系统标识
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<List<FormReport>> queryFormReportList(String authorizeKey,String businessKey) {
        FormReport formReport = new FormReport();
        formReport.setAuthorizeKey(authorizeKey);
        formReport.setBusinessKey(businessKey);
        List<FormReport> formReportList = formReportManager.queryFormReportList(formReport);
        return new ApiResultSet<>(formReportList);
    }

}
