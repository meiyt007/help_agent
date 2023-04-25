package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormTemplate;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormTemplateService
 * @Description 表单模板组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/template")
public interface FormTemplateService {


    /**
     * @description:  查询表单设计对象的信息列表
     * @param templateName 模板名称
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormTemplateWithPage(@RequestParam(value = "authorizeKey", required = false) String authorizeKey,
                                       @RequestParam(value = "templateName", required = false) String templateName,
                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  查询表单设计对象的信息列表(设计表单选择页面)
     * @param templateName 模板名称
     * @author: wuxx
     * @Date: 2021/05/26 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectPage",method = {RequestMethod.GET})
    ApiResultSet queryFormTemplateWithselectPage(@RequestParam(value = "authorizeKey", required = false) String authorizeKey,
                                           @RequestParam(value = "templateName", required = false) String templateName,
                                           @RequestParam(value = "isPublic", required = false) Integer isPublic,
                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * @description:  表单模板对象的新增或者修改
     * @param formTemplate 表单模板对象实体类
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveFormTemplate",method = {RequestMethod.POST})
    ApiResultSet<FormTemplate> saveFormTemplate(@RequestBody FormTemplate formTemplate);

    /**
     * @description:  初始化表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormTemplate(@RequestParam(value = "id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormTemplate> getFormTemplateById(@PathVariable("id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param templateOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormTemplateByTemplateOid/{templateOid}",method = {RequestMethod.GET})
    ApiResultSet<FormTemplate> getFormTemplateByTemplateOid(@PathVariable("templateOid") String templateOid);

    /**
     * 删除指定Id的表单设计对象信息
     * @param id 表单设计对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormTemplateById(@PathVariable("id") Long id);

    /**
     * @description:  表单设计对象的信息的启禁用
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormTemplateById(@PathVariable("id") Long id);

    /**
     * @description:  表单模板的设置
     * @param designOid 设计的主键
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/setFormTemplateByDesignOid/{designOid}",method = {RequestMethod.POST})
    ApiResultSet<Integer> setFormTemplateByDesignOid(@PathVariable("designOid") String designOid);


    /**
     * @description:  根据授权key查询模板列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/19 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormTemplateList",method = {RequestMethod.GET})
    ApiResultSet<List<FormTemplate>> queryFormTemplateList(@RequestParam(value = "authorizeKey",required = false) String authorizeKey,
                                                   @RequestParam(value = "templateName", required = false) String templateName,
                                                   @RequestParam(value = "isFixed", required = false) Integer isFixed);

}
