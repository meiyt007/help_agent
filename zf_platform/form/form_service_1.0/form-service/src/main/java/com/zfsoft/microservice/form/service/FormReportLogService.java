package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormReportLog;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName FormReportLogService
 * @Description 表单填报数据日志日志组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/reportLog")
public interface FormReportLogService {

    /**
     * @description:  查询数据填报日志的信息列表
     * @param formMainOid 主表oid
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormReportLogWithPage(@RequestParam(value = "formMainOid", required = false) String formMainOid,
                                         @RequestParam(value = "startTime", required = false) String startTime,
                                         @RequestParam(value = "endTime", required = false) String endTime,
                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormReportLog> getFormReportLogById(@PathVariable("id") Long id);

    /**
     * @description:  获取表单设计对象的配置信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormConfigAndData/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormReportLog> getFormConfigAndData(@PathVariable("id") Long id);

}
