package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormTemplate;
import com.zfsoft.microservice.form.manager.FormTemplateManager;
import com.zfsoft.microservice.form.service.FormTemplateService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FormTemplateController
 * @Description 表单模板的实现类
 * @Author wuxx
 * @Date 2021-04-19 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormTemplateController implements FormTemplateService {

    @Resource
    private FormTemplateManager formTemplateManager;

    /**
     * @description:  查询表单模板的信息列表
     * @param authorizeKey 授权key
     * @param templateName 对象名称
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet queryFormTemplateWithPage(String authorizeKey, String templateName,Integer pageNum,
                                                                                      Integer pageSize){
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setTemplateName(templateName);
        formTemplate.setAuthorizeKey(authorizeKey);
        PageResult<FormTemplate> pageResult = formTemplateManager.queryFormTemplateWithPage(formTemplate,pageNum,pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("pageResult",pageResult);
        //当前登录人
        boolean isAdminUser = CurrentLoginUserHolder.getIsAdminUser();
        //管理员查看所有
        if(isAdminUser){
            map.put("isAdminUser",true);
        }
        return new ApiResultSet<>(map);
    }

    /**
     * @description:  查询表单设计对象的信息列表(设计表单选择页面)
     * @param templateName 模板名称
     * @author: wuxx
     * @Date: 2021/05/26 15:14
     **/
    @Override
    public ApiResultSet<PageResult<FormTemplate>> queryFormTemplateWithselectPage(String authorizeKey, String templateName,Integer isPublic,Integer pageNum,
                                                                            Integer pageSize){
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setTemplateName(templateName);
        formTemplate.setAuthorizeKey(authorizeKey);
        formTemplate.setIsPublic(isPublic);
        formTemplate.setIsAble(BaseStaticParameter.Y);
        formTemplate.setTemplateConfig(BaseStaticParameter.YES);
        formTemplate.setIsFixed(BaseStaticParameter.N);
        PageResult<FormTemplate> pageResult = formTemplateManager.queryFormTemplateWithPage(formTemplate,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化表单模板的信息
     * @param id 表单模板实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet initFormTemplate(Long id){
        FormTemplate formTemplate = formTemplateManager.getFormTemplateById(id);
        return new ApiResultSet<>(formTemplate);
    }

    /**
     * @description:  表单模板的新增或者修改
     * @param formTemplate 表单模板实体类
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormTemplate> saveFormTemplate(@RequestBody FormTemplate formTemplate){
        formTemplateManager.saveFormTemplate(formTemplate);
        return  new ApiResultSet<>(formTemplate);
    }

    /**
     * @description:  表单模板的信息的删除
     * @param id 表单模板实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteFormTemplateById(Long id){
        formTemplateManager.deleteFormTemplateById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取表单模板的信息
     * @param id 表单模板实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormTemplate>  getFormTemplateById(Long id){
        FormTemplate formTemplate = formTemplateManager.getFormTemplateById(id);
        return new ApiResultSet<>(formTemplate);
    }

    /**
     * @description:  获取表单模板的信息
     * @param templateOid 业务主键
     * @author: templateOid
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<FormTemplate>  getFormTemplateByTemplateOid(@PathVariable("templateOid")String templateOid){
        FormTemplate formTemplate = formTemplateManager.getFormTemplateByTemplateOid(templateOid);
        return new ApiResultSet<>(formTemplate);
    }

    /**
     * @description:  表单模板的信息的启禁用
     * @param id 表单模板实体类主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<Integer> ableFormTemplateById(@PathVariable("id")Long id){
        int rows = formTemplateManager.ableFormTemplateById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  表单模板的设置
     * @param designOid 设计的主键
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<Integer> setFormTemplateByDesignOid(String designOid) {
        int rows = formTemplateManager.setFormTemplateByDesignOid(designOid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  表单模板的查询
     * @param authorizeKey 授权key
     * @param templateName 模板名称
     * @author: wuxx
     * @Date: 2021-04-19 13:30
     **/
    @Override
    public ApiResultSet<List<FormTemplate>> queryFormTemplateList(String authorizeKey,String templateName,Integer isFixed) {
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setAuthorizeKey(authorizeKey);
        formTemplate.setTemplateName(templateName);
        formTemplate.setIsFixed(isFixed);
        List<FormTemplate> formTemplateList = formTemplateManager.queryFormTemplateList(formTemplate);
        return new ApiResultSet<>(formTemplateList);
    }


}
