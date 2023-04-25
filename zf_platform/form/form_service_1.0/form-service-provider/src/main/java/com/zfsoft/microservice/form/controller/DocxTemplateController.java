package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.DocxTemplate;
import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.manager.DocxTemplateManager;
import com.zfsoft.microservice.form.manager.FormDesignManager;
import com.zfsoft.microservice.form.manager.FormMainManager;
import com.zfsoft.microservice.form.service.DocxTemplateService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName DocxTemplateController
 * @Description docxdocx模板的实现类
 * @Author wuxx
 * @Date 2021-12-2 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class DocxTemplateController implements DocxTemplateService {

    @Resource
    private DocxTemplateManager docxTemplateManager;

    @Resource
    private FormMainManager formMainManager;

    @Resource
    private FormDesignManager formDesignManager;

    /**
     * @description:  查询docx模板的信息列表
     * @param objectOid 存储对象oid
     * @param docxTemplateName 对象名称
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet queryFormTemplateWithPage(String docxTemplateName, String objectOid,Integer pageNum,
                                                                                      Integer pageSize){
        DocxTemplate docxTemplate = new DocxTemplate();
        docxTemplate.setDocxTemplateName(docxTemplateName);
        docxTemplate.setObjectOid(objectOid);
        PageResult<DocxTemplate> pageResult = docxTemplateManager.queryDocxTemplateWithPage(docxTemplate,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  docx模板的新增或者修改
     * @param docxTemplate docx模板实体类
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<DocxTemplate> saveDocxTemplate(@RequestBody DocxTemplate docxTemplate){
        docxTemplateManager.saveDocxTemplate(docxTemplate);
        return  new ApiResultSet<>(docxTemplate);
    }

    /**
     * @description:  docx模板的信息的删除
     * @param id docx模板实体类主键
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteDocxTemplateById(Long id){
        docxTemplateManager.deleteDocxTemplateById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取docx模板的信息
     * @param id docx模板实体类主键
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<DocxTemplate>  getDocxTemplateById(Long id){
        DocxTemplate docxTemplate = docxTemplateManager.getDocxTemplateById(id);
        return new ApiResultSet<>(docxTemplate);
    }

    /**
     * @description:  获取docx模板的信息
     * @param templateOid 业务主键
     * @author: templateOid
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<DocxTemplate>  getDocxTemplateByTemplateOid(String templateOid){
        DocxTemplate docxTemplate = docxTemplateManager.getDocxTemplateByTemplateOid(templateOid);
        return new ApiResultSet<>(docxTemplate);
    }

    /**
     * @description:  docx模板的信息的启禁用
     * @param id docx模板实体类主键
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<Integer> ableDocxTemplateById(@PathVariable("id")Long id){
        int rows = docxTemplateManager.ableDocxTemplateById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  docx模板的查询
     * @param designOid 设计oid
     * @param objectOid 存储对象oid
     * @author: wuxx
     * @Date: 2021-12-2 13:30
     **/
    @Override
    public ApiResultSet<List<DocxTemplate>> queryDocxTemplateList(String designOid,String objectOid,String formCode) {
        if(StrUtil.isBlank(designOid) && StrUtil.isBlank(formCode) && StrUtil.isBlank(objectOid)){
            throw new ResultInfoException("参数objectOid、formCode、objectOid不能同时为空！");
        }
        if(StrUtil.isNotBlank(designOid) && StrUtil.isBlank(objectOid)){
            FormDesign formDesign = formDesignManager.getFormDesignByDesignOid(designOid);
            if(null == formDesign){
                throw new ResultInfoException("参数designOid未查询到表单信息！");
            }
            objectOid = formDesign.getObjectOid();
        }else {
            if(StrUtil.isNotBlank(formCode) && StrUtil.isBlank(objectOid)){
                FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
                if(null == mainCode){
                    throw new ResultInfoException("参数formCode未查询到表单信息！");
                }
                objectOid = mainCode.getObjectOid();
            }
        }
        DocxTemplate docxTemplate = new DocxTemplate();
        docxTemplate.setObjectOid(objectOid);
        docxTemplate.setIsAble(1);
        List<DocxTemplate> docxTemplateList = docxTemplateManager.queryDocxTemplateList(docxTemplate);
        return new ApiResultSet<>(docxTemplateList);
    }


}
