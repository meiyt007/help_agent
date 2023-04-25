package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.DocxTemplate;
import com.zfsoft.microservice.form.data.FormAtta;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.microservice.form.data.vo.AppendObjectVo;
import com.zfsoft.microservice.form.dbaccess.dao.DbDocxTemplateMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbDocxTemplate;
import com.zfsoft.microservice.form.dbaccess.data.DbDocxTemplateExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName DocxTemplateManager
 * @Description: docx模板接口实现类
 * @Author wuxx
 * @Date 2021/12/2 15:14
 **/
@Service
@Slf4j
@RefreshScope
@CacheConfig(cacheNames = "form:docx:template")
public class DocxTemplateManager {

    @Resource
    private DbDocxTemplateMapper dbDocxTemplateMapper;

    @Resource
    @Lazy
    private FormObjectManager formObjectManager;

    @Resource
    private FormAttaManager formAttaManager;

    /**
     * @description: 分页查询模板的列表
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    public PageResult<DocxTemplate> queryDocxTemplateWithPage(DocxTemplate docxTemplate, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbDocxTemplateExample dbDocxTemplateExample = new DbDocxTemplateExample();
        DbDocxTemplateExample.Criteria criteria = dbDocxTemplateExample.createCriteria();
        dbDocxTemplateExample.setOrderByClause(" CREATE_DATE DESC ");
        if(null!=docxTemplate){
            if(StrUtil.isNotEmpty(docxTemplate.getDocxTemplateName())){
                String value = docxTemplate.getDocxTemplateName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andDocxTemplateNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(docxTemplate.getObjectOid())){
                criteria.andObjectOidEqualTo(docxTemplate.getObjectOid());
            }
            if(StrUtil.isNotEmpty(docxTemplate.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(docxTemplate.getAuthorizeKey());
            }
            if(null!=docxTemplate.getIsAble()){
                criteria.andIsAbleEqualTo(docxTemplate.getIsAble());
            }

        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbDocxTemplate> dbDocxTemplates = (Page<DbDocxTemplate>)dbDocxTemplateMapper.selectByExample(dbDocxTemplateExample);
        PageResult<DocxTemplate> pageResult = new PageResult<>(dbDocxTemplates.getPageNum(),dbDocxTemplates.getPageSize(),dbDocxTemplates.getTotal());
        List<DocxTemplate> docxTemplateList = dbDocxTemplates.stream().map(dbDocxTemplate -> {
            DocxTemplate object = new DocxTemplate();
            BeanUtils.copyProperties(dbDocxTemplate,object);
            if(StrUtil.isNotBlank(object.getObjectOid())){
                FormObject formObject = formObjectManager.getFormObjectByObjectOid(object.getObjectOid());
                object.setObjectName(null!=formObject?formObject.getObjectName():null);
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(docxTemplateList);
        return pageResult;
    }

    /**
     * @description:  保存模板信息
     * @param docxTemplate 模板对象
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveDocxTemplate(@ValidGroups(groups = {DocxTemplate.INSERT_GROUP.class}) DocxTemplate docxTemplate) {
        if(null == docxTemplate){
            throw new ResultInfoException("模板对象不能为空！");
        }
        if (null == docxTemplate.getId()) {
            docxTemplate.setId(null);
            docxTemplate.setDocxTemplateOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            DocxTemplate curDict = this.getDocxTemplateById(docxTemplate.getId());
            if (curDict == null) {
                throw new ResultInfoException("模板对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == docxTemplate.getIsDelete()) {
            docxTemplate.setIsDelete(BaseStaticParameter.N);
        }
        if (null == docxTemplate.getIsAble()) {
            docxTemplate.setIsAble(BaseStaticParameter.Y);
        }
        if (null == docxTemplate.getCreateDate()) {
            docxTemplate.setCreateDate(new Date());
        }
        List<AppendObjectVo> appendObjectList = docxTemplate.getAppendObjectList();
        if(null!=appendObjectList && appendObjectList.size()>0){
            List<String> appendObjectOidList = new ArrayList<>();
            for (AppendObjectVo obj : appendObjectList){
                appendObjectOidList.add(obj.getKey()+"~"+obj.getObjectOid());
            }
            String appendObjectOids = StringUtils.join(appendObjectOidList.toArray(), ";");
            docxTemplate.setAppendObjectOids(appendObjectOids);
        }else{
            docxTemplate.setAppendObjectOids(null);
        }

        DbDocxTemplate dbDocxTemplate = new DbDocxTemplate();
        BeanUtils.copyProperties(docxTemplate,dbDocxTemplate);
        int index=0;
        if (null == docxTemplate.getId()) {
            index = dbDocxTemplateMapper.insert(dbDocxTemplate);
        }else {
            index = dbDocxTemplateMapper.updateByPrimaryKey(dbDocxTemplate);
        }
        docxTemplate.setId(dbDocxTemplate.getId());
        return index;
    }

    /**
     * @description: 表单的更新
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateDocxTemplate(@ValidGroups(groups = {DocxTemplate.UPDATE_GROUP.class}) DocxTemplate docxTemplate) {
        DbDocxTemplate dbDocxTemplate = new DbDocxTemplate();
        BeanUtils.copyProperties(docxTemplate,dbDocxTemplate);
        int index = dbDocxTemplateMapper.updateByPrimaryKeySelective(dbDocxTemplate);
        return index ;
    }

    /**
     * @description:  删除模板信息
     * @param id 模板对象主键
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteDocxTemplateById(Long id) {
        DbDocxTemplate dbDocxTemplate = dbDocxTemplateMapper.selectByPrimaryKey(id);
        if(dbDocxTemplate == null)
            throw new ResultInfoException("模板对象信息为空！");
        int index = dbDocxTemplateMapper.deleteByForeignKey(dbDocxTemplate.getDocxTemplateOid());
        if(index==0){
            throw new ResultInfoException("模板对象删除失败，请稍后再试！");
        }
        return index;
    }


    /**
     * @description: 对象的启禁用操作
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableDocxTemplateById(Long id) {
        DbDocxTemplate dbDocxTemplate = dbDocxTemplateMapper.selectByPrimaryKey(id);
        if(dbDocxTemplate == null)
            throw new ResultInfoException("模板对象信息为空！");
        Integer isAble = dbDocxTemplate.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbDocxTemplate.setIsAble(BaseStaticParameter.Y);
        }else {
            dbDocxTemplate.setIsAble(BaseStaticParameter.N);
        }
        int index = dbDocxTemplateMapper.updateByPrimaryKeySelective(dbDocxTemplate);
        if(index==0){
            throw new ResultInfoException("模板对象启禁用失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Cacheable(key = "'getDocxTemplateById:'+#id", unless = "#result == null")
    public DocxTemplate getDocxTemplateById(Long id) {
        DbDocxTemplate dbDocxTemplate = dbDocxTemplateMapper.selectByPrimaryKey(id);
        if(dbDocxTemplate == null)
            return null;
        DocxTemplate docxTemplate = new DocxTemplate();
        BeanUtils.copyProperties(dbDocxTemplate,docxTemplate);
        if(StrUtil.isNotBlank(dbDocxTemplate.getObjectOid())){
            FormObject formObject = formObjectManager.getFormObjectByObjectOid(dbDocxTemplate.getObjectOid());
            docxTemplate.setObjectName(null!=formObject?formObject.getObjectName():null);
        }
        if(StrUtil.isNotBlank(dbDocxTemplate.getDocxAttaOid())){
            FormAtta data = formAttaManager.getFormAttaByAttaOid(dbDocxTemplate.getDocxAttaOid());
            docxTemplate.setAttaName(null!=data?data.getOriginName():null);
        }
        return docxTemplate;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param templateOid 业务主键id
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    @Cacheable(key = "'getDocxTemplateByTemplateOid:'+#templateOid", unless = "#result == null")
    public DocxTemplate getDocxTemplateByTemplateOid(String templateOid) {
        DbDocxTemplate dbDocxTemplate = dbDocxTemplateMapper.selectByForeignKey(templateOid);
        if(dbDocxTemplate == null)
            return null;
        DocxTemplate docxTemplate = new DocxTemplate();
        BeanUtils.copyProperties(dbDocxTemplate,docxTemplate);
        if(StrUtil.isNotBlank(dbDocxTemplate.getObjectOid())){
            FormObject formObject = formObjectManager.getFormObjectByObjectOid(dbDocxTemplate.getObjectOid());
            docxTemplate.setObjectName(null!=formObject?formObject.getObjectName():null);
        }
        if(StrUtil.isNotBlank(dbDocxTemplate.getDocxAttaOid())){
            FormAtta data = formAttaManager.getFormAttaByAttaOid(dbDocxTemplate.getDocxAttaOid());
            docxTemplate.setAttaName(null!=data?data.getOriginName():null);
        }
        return docxTemplate;
    }

    /**
     * @description: 根据模板对象查询集合
     * @param docxTemplate 模板对象
     * @author: wuxx
     * @Date: 2021/12/2 15:14
     **/
    public List<DocxTemplate> queryDocxTemplateList(DocxTemplate docxTemplate) {
        DbDocxTemplateExample dbDocxTemplateExample = new DbDocxTemplateExample();
        dbDocxTemplateExample.setOrderByClause(" CREATE_DATE DESC ");
        DbDocxTemplateExample.Criteria criteria = dbDocxTemplateExample.createCriteria();
        if(null!=docxTemplate){
            if(StrUtil.isNotEmpty(docxTemplate.getObjectOid())){
                criteria.andObjectOidEqualTo(docxTemplate.getObjectOid());
            }
            if(StrUtil.isNotEmpty(docxTemplate.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(docxTemplate.getAuthorizeKey());
            }
            if(null!=docxTemplate.getIsAble()){
                criteria.andIsAbleEqualTo(docxTemplate.getIsAble());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbDocxTemplate> dbDocxTemplateList = dbDocxTemplateMapper.selectByExample(dbDocxTemplateExample);
        List<DocxTemplate> docxTemplateList = dbDocxTemplateList.stream().map(dbDocxTemplate -> {
            DocxTemplate object = new DocxTemplate();
            BeanUtils.copyProperties(dbDocxTemplate,object);
            if(StrUtil.isNotBlank(dbDocxTemplate.getObjectOid())){
                FormObject formObject = formObjectManager.getFormObjectByObjectOid(dbDocxTemplate.getObjectOid());
                docxTemplate.setObjectName(null!=formObject?formObject.getObjectName():null);
            }
            return object;
        }).collect(Collectors.toList());
        return docxTemplateList;
    }

}
