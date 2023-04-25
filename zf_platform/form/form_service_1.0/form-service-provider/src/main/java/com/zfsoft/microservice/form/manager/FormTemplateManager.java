package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormTemplateMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTemplate;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTemplateExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormTemplateManager
 * @Description: 表单模板接口实现类
 * @Author wuxx
 * @Date 2021/4/19 10:00
 **/
@Service
@Slf4j
@RefreshScope
@CacheConfig(cacheNames = "form:template")
public class FormTemplateManager {

    @Resource
    private DbFormTemplateMapper dbFormTemplateMapper;

    @Resource
    private FormDesignManager formDesignManager;

    @Resource
    @Lazy
    private FormMainManager formMainManager;

    @Resource
    private FormAttaManager formAttaManager;

    @Value("${fdfs.fastDFSNginxUrl:''}")
    private String fastDFSNginxUrl;

    /**
     * @description: 分页查询模板的列表
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public PageResult<FormTemplate> queryFormTemplateWithPage(FormTemplate formTemplate, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormTemplateExample dbFormTemplateExample = new DbFormTemplateExample();
        DbFormTemplateExample.Criteria criteria = dbFormTemplateExample.createCriteria();
        dbFormTemplateExample.setOrderByClause("IS_FIXED DESC,ID DESC ");
        if(null!=formTemplate){
            if(StrUtil.isNotEmpty(formTemplate.getTemplateName())){
                String value = formTemplate.getTemplateName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andTemplateNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formTemplate.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formTemplate.getAuthorizeKey());
            }
            if(null!=formTemplate.getIsPublic()){
                criteria.andIsPublicEqualTo(formTemplate.getIsPublic());
            }
            if(null!=formTemplate.getIsAble()){
                criteria.andIsAbleEqualTo(formTemplate.getIsAble());
            }
            /*if(null!=formTemplate.getIsFixed() && BaseStaticParameter.N == formTemplate.getIsFixed()){
                criteria.andIsFixedIsNull();
            }*/
            if(StrUtil.isNotEmpty(formTemplate.getTemplateConfig())){
                criteria.andTemplateConfigIsNotNull();
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormTemplate> dbFormTemplates = (Page<DbFormTemplate>)dbFormTemplateMapper.selectByExampleWithBLOBs(dbFormTemplateExample);
        PageResult<FormTemplate> pageResult = new PageResult<>(dbFormTemplates.getPageNum(),dbFormTemplates.getPageSize(),dbFormTemplates.getTotal());
        List<FormTemplate> formTemplateList = dbFormTemplates.stream().map(dbFormTemplate -> {
            FormTemplate object = new FormTemplate();
            BeanUtils.copyProperties(dbFormTemplate,object);
            if(StrUtil.isNotBlank(object.getTemplateAtta())){
                FormAtta data = formAttaManager.getFormAttaByAttaOid(object.getTemplateAtta());
                object.setFastdfsUrl(null!=data?fastDFSNginxUrl +"/"+ data.getFastdfsUploadUrl():null);
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formTemplateList);
        return pageResult;
    }

    /**
     * @description:  保存模板信息
     * @param formTemplate 模板对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormTemplate(@ValidGroups(groups = {FormTemplate.INSERT_GROUP.class}) FormTemplate formTemplate) {
        if(null == formTemplate){
            throw new ResultInfoException("模板对象不能为空！");
        }
        if (null == formTemplate.getId()) {
            formTemplate.setId(null);
            formTemplate.setTemplateOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormTemplate curDict = this.getFormTemplateById(formTemplate.getId());
            if (curDict == null) {
                throw new ResultInfoException("模板对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formTemplate.getIsDelete()) {
            formTemplate.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formTemplate.getIsAble()) {
            formTemplate.setIsAble(BaseStaticParameter.Y);
        }
        if (null == formTemplate.getCreateDate()) {
            formTemplate.setCreateDate(new Date());
        }
        //默认公开
        formTemplate.setIsPublic(BaseStaticParameter.Y);
        DbFormTemplate dbFormTemplate = new DbFormTemplate();
        BeanUtils.copyProperties(formTemplate,dbFormTemplate);
        int index=0;
        if (null == formTemplate.getId()) {
            index = dbFormTemplateMapper.insert(dbFormTemplate);
        }else {
            index = dbFormTemplateMapper.updateByPrimaryKeySelective(dbFormTemplate);
        }
        formTemplate.setId(dbFormTemplate.getId());
        return index;
    }

    /**
     * @description: 表单的更新
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormTemplate(@ValidGroups(groups = {FormTemplate.UPDATE_GROUP.class}) FormTemplate formTemplate) {
        DbFormTemplate dbFormTemplate = new DbFormTemplate();
        BeanUtils.copyProperties(formTemplate,dbFormTemplate);
        int index = dbFormTemplateMapper.updateByPrimaryKeySelective(dbFormTemplate);
        return index ;
    }

    /**
     * @description:  删除模板信息
     * @param id 模板对象主键
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormTemplateById(Long id) {
        DbFormTemplate dbFormTemplate = dbFormTemplateMapper.selectByPrimaryKey(id);
        if(dbFormTemplate == null)
            throw new ResultInfoException("模板对象信息为空！");
        int index = dbFormTemplateMapper.deleteByForeignKey(dbFormTemplate.getTemplateOid());
        if(index==0){
            throw new ResultInfoException("模板对象删除失败，请稍后再试！");
        }
        return index;
    }


    /**
     * @description: 对象的启禁用操作
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormTemplateById(Long id) {
        DbFormTemplate dbFormTemplate = dbFormTemplateMapper.selectByPrimaryKey(id);
        if(dbFormTemplate == null)
            throw new ResultInfoException("模板对象信息为空！");
        Integer isAble = dbFormTemplate.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbFormTemplate.setIsAble(BaseStaticParameter.Y);
        }else {
            dbFormTemplate.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormTemplateMapper.updateByPrimaryKeySelective(dbFormTemplate);
        if(index==0){
            throw new ResultInfoException("模板对象启禁用失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 公开表单
     * @param formTemplateOid 主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int publicFormTemplate(String formTemplateOid) {
        DbFormTemplate dbFormTemplate = dbFormTemplateMapper.selectByForeignKey(formTemplateOid);
        if(dbFormTemplate == null)
            throw new ResultInfoException("模板对象信息为空！");
        dbFormTemplate.setIsPublic(FormStaticParameter.DATA_STATUS_1);
        int index = dbFormTemplateMapper.updateByPrimaryKeySelective(dbFormTemplate);
        if(index==0){
            throw new ResultInfoException("表单公开失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 设计表单设置为模板
     * @param designOid 设计表单主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int setFormTemplateByDesignOid(String designOid) {
        FormDesign design = formDesignManager.getFormDesignByDesignOid(designOid);
        if(design == null)
            throw new ResultInfoException("设计对象信息为空！");
        FormTemplate formTemplate = new FormTemplate();
        FormMain main = formMainManager.getFormMainByFormMainOid(design.getFormMainOid());
        if(design == null)
            throw new ResultInfoException("设计对象信息为空！");
        formTemplate.setAuthorizeKey(main.getAuthorizeKey());
        formTemplate.setDesignOid(designOid);
        // 设置信息的状态
        formTemplate.setId(null);
        formTemplate.setTemplateOid(IdUtil.simpleUUID());
        formTemplate.setCreateDate(new Date());
        formTemplate.setIsDelete(BaseStaticParameter.N);
        formTemplate.setIsAble(BaseStaticParameter.Y);
        formTemplate.setIsPublic(BaseStaticParameter.N);
        formTemplate.setTemplateConfig(design.getFormConfig());
        formTemplate.setTemplateName(design.getDesignName());
        DbFormTemplate dbFormTemplate = new DbFormTemplate();
        BeanUtils.copyProperties(formTemplate,dbFormTemplate);
        int index = dbFormTemplateMapper.insert(dbFormTemplate);
        if(index==0){
            throw new ResultInfoException("表单设置模板失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormTemplateById:'+#id", unless = "#result == null")
    public FormTemplate getFormTemplateById(Long id) {
        DbFormTemplate dbFormTemplate = dbFormTemplateMapper.selectByPrimaryKey(id);
        if(dbFormTemplate == null)
            return null;
        FormTemplate formTemplate = new FormTemplate();
        BeanUtils.copyProperties(dbFormTemplate,formTemplate);
        if(StrUtil.isNotBlank(formTemplate.getTemplateAtta())){
            FormAtta data = formAttaManager.getFormAttaByAttaOid(formTemplate.getTemplateAtta());
            formTemplate.setAttaName(null!=data?data.getOriginName():null);
        }
        return formTemplate;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param templateOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    @Cacheable(key = "'getFormTemplateByTemplateOid:'+#templateOid", unless = "#result == null")
    public FormTemplate getFormTemplateByTemplateOid(String templateOid) {
        DbFormTemplate dbFormTemplate = dbFormTemplateMapper.selectByForeignKey(templateOid);
        if(dbFormTemplate == null)
            return null;
        FormTemplate formTemplate = new FormTemplate();
        BeanUtils.copyProperties(dbFormTemplate,formTemplate);
        if(StrUtil.isNotBlank(formTemplate.getTemplateAtta())){
            FormAtta data = formAttaManager.getFormAttaByAttaOid(formTemplate.getTemplateAtta());
            formTemplate.setAttaName(null!=data?data.getOriginName():null);
        }
        return formTemplate;
    }

    /**
     * @description: 根据模板对象查询集合
     * @param formTemplate 模板对象
     * @author: wuxx
     * @Date: 2021/4/19 10:00
     **/
    public List<FormTemplate> queryFormTemplateList(FormTemplate formTemplate) {
        DbFormTemplateExample dbFormTemplateExample = new DbFormTemplateExample();
        dbFormTemplateExample.setOrderByClause(" ID DESC ");
        DbFormTemplateExample.Criteria criteria = dbFormTemplateExample.createCriteria();
        if(null!=formTemplate){
            if(StrUtil.isNotEmpty(formTemplate.getTemplateName())){
                String value = formTemplate.getTemplateName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andTemplateNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formTemplate.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formTemplate.getAuthorizeKey());
            }
            if(null!=formTemplate.getIsPublic()){
                criteria.andIsPublicEqualTo(formTemplate.getIsPublic());
            }
            if(null!=formTemplate.getIsFixed()){
                criteria.andIsFixedEqualTo(formTemplate.getIsFixed());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbFormTemplate> dbFormTemplateList = dbFormTemplateMapper.selectByExample(dbFormTemplateExample);
        List<FormTemplate> formTemplateList = dbFormTemplateList.stream().map(dbFormTemplate -> {
            FormTemplate object = new FormTemplate();
            BeanUtils.copyProperties(dbFormTemplate,object);
            if(StrUtil.isNotBlank(object.getTemplateAtta())){
                FormAtta data = formAttaManager.getFormAttaByAttaOid(object.getTemplateAtta());
                object.setFastdfsUrl(null!=data?fastDFSNginxUrl +"/"+ data.getFastdfsUploadUrl():null);
            }
            return object;
        }).collect(Collectors.toList());
        return formTemplateList;
    }

}
