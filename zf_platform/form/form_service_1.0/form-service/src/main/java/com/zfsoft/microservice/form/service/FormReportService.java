package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormReport;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormReportService
 * @Description 表单填报数据组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/report")
public interface FormReportService {


    /**
     * @description:  查询表单设计对象的信息列表
     * @param authorizeKey 授权ey
     * @param businessKey 业务唯一标识key
     * @param formMainOid 设计主表oid
     * @param designOid 历史记录设计oid
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormReportWithPage(@RequestParam(value = "authorizeKey") String authorizeKey,
                                           @RequestParam(value = "businessKey", required = false) String businessKey,
                                           @RequestParam(value = "formMainOid", required = false) String formMainOid,
                                           @RequestParam(value = "designOid", required = false) String designOid,
                                           @RequestParam(value = "startTime", required = false) String startTime,
                                           @RequestParam(value = "endTime", required = false) String endTime,
                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  表单填报数据对象的新增或者修改
     * @param formReport 表单填报数据对象实体类
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormReport> saveFormReport(@RequestBody FormReport formReport);

    /**
     * @description:  初始化表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormReport(@RequestParam(value = "id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormReport> getFormReportById(@PathVariable("id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param reportOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormReportByReportOid/{reportOid}",method = {RequestMethod.GET})
    ApiResultSet<FormReport> getFormReportByReportOid(@PathVariable("reportOid") String reportOid);

    /**
     * 删除指定Id的表单设计对象信息
     * @param id 表单设计对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormReportById(@PathVariable("id") Long id);

    /**
     * @description:  根据授权key查询填报数据列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormReportList",method = {RequestMethod.GET})
    ApiResultSet<List<FormReport>> queryFormReportList(@RequestParam("authorizeKey") String authorizeKey,
                                                           @RequestParam(value = "businessKey", required = false) String businessKey);

}
