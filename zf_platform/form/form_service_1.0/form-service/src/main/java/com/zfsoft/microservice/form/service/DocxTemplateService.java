package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.DocxTemplate;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName DocxTemplateService
 * @Description docx模板组件服务定义接口
 * @Author wuxx
 * @Date 2021-12-2 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/docx/template")
public interface DocxTemplateService {

    /**
     * @description:  查询docx模板列表
     * @param objectOid 存储对象oid
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormTemplateWithPage(@RequestParam(value = "docxTemplateName", required = false) String docxTemplateName,
                                           @RequestParam(value = "objectOid") String objectOid,
                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  表单模板对象的新增或者修改
     * @param docxTemplate 表单模板对象实体类
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveDocxTemplate",method = {RequestMethod.POST})
    ApiResultSet<DocxTemplate> saveDocxTemplate(@RequestBody DocxTemplate docxTemplate);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<DocxTemplate> getDocxTemplateById(@PathVariable("id") Long id);

    /**
     * @description:  获取单个的信息
     * @param docxTemplateOid 对象业务oid
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDocxTemplateByTemplateOid/{docxTemplateOid}",method = {RequestMethod.GET})
    ApiResultSet<DocxTemplate> getDocxTemplateByTemplateOid(@PathVariable("docxTemplateOid") String docxTemplateOid);

    /**
     * 删除指定Id的信息
     * @param id 对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteDocxTemplateById(@PathVariable("id") Long id);


    /**
     * @description:  表单设计对象的信息的启禁用
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableDocxTemplateById(@PathVariable("id") Long id);

    /**
     * @description:  根据参数查询模板列表（以下参数三选一）
     * @param designOid 设计oid
     * @param objectOid 存储对象oid
     * @param formCode 表单编码
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryDocxTemplateList",method = {RequestMethod.GET})
    ApiResultSet<List<DocxTemplate>> queryDocxTemplateList(@RequestParam(value = "designOid", required = false) String designOid,
                                                           @RequestParam(value = "objectOid", required = false) String objectOid,
                                                           @RequestParam(value = "formCode", required = false) String formCode);

}
