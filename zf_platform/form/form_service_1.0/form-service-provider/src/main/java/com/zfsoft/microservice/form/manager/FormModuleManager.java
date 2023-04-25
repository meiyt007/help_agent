package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.data.FormModule;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormModuleMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormModule;
import com.zfsoft.microservice.form.dbaccess.data.DbFormModuleExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormModuleManager
 * @Description: 模块管理接口实现类
 * @Author wuxx
 * @Date 2021/4/2
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:module")
public class FormModuleManager {

    @Resource
    private DbFormModuleMapper dbFormModuleMapper;

    @Resource
    @Lazy
    private FormMainManager formMainManager;

    @Resource
    @Lazy
    private FormDataSourceManager formDataSourceManager;

    @Resource
    @Lazy
    private FormObjectManager formObjectManager;

    /**
     * @description:  保存授权信息
     * @param formModule 模块信息
     * @author: wuxx
     * @Date: 2021/3/11 9:18
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormModule(@ValidGroups(groups = {FormModule.INSERT_GROUP.class})FormModule formModule) {
        if(null == formModule){
            throw new ResultInfoException("模块信息不能为空！");
        }
        if (null == formModule.getId()) {
            formModule.setId(null);
            formModule.setModuleOid(IdUtil.simpleUUID());
            formModule.setCreateDate(new Date());
        } else {
            // 字典oid不为空
            FormModule curDict = this.getFormModuleById(formModule.getId());
            if (curDict == null) {
                throw new ResultInfoException("未查询到相应的模块信息!");
            }
        }
        // 设置区划信息的状态
        if (null == formModule.getIsDelete()) {
            formModule.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formModule.getIsAble()) {
            formModule.setIsAble(BaseStaticParameter.Y);
        }
        DbFormModule dbFormModule = new DbFormModule();
        BeanUtils.copyProperties(formModule,dbFormModule);
        int index=0;
        if (null == formModule.getId()) {
            index = dbFormModuleMapper.insert(dbFormModule);
        }else {
            index = dbFormModuleMapper.updateByPrimaryKeySelective(dbFormModule);
        }
        formModule.setId(dbFormModule.getId());
        return index;
    }

    /**
     * @description:  删除授权信息
     * @param id 模块信息主键
     * @author: wuxx
     * @Date: 2021/3/11 9:18
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormModuleById(Long id) {
        DbFormModule dbFormModule = dbFormModuleMapper.selectByPrimaryKey(id);
        if(dbFormModule == null)
            throw new ResultInfoException("模块信息信息为空！");
        this.checkIsExitsByOid(dbFormModule.getModuleOid(),0);
        int index = dbFormModuleMapper.deleteByModuleOid(dbFormModule.getModuleOid());
        if(index==0){
            throw new ResultInfoException("模块信息删除失败，请稍后再试！");
        }
        return index;
    }


    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormModuleById(Long id) {
        DbFormModule dbFormModule = dbFormModuleMapper.selectByPrimaryKey(id);
        if(dbFormModule == null)
            throw new ResultInfoException("模块信息为空！");
        Integer isAble = dbFormModule.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbFormModule.setIsAble(BaseStaticParameter.Y);
        }else {
            this.checkIsExitsByOid(dbFormModule.getModuleOid(),1);
            dbFormModule.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormModuleMapper.updateByPrimaryKeySelective(dbFormModule);
        if(index==0){
            throw new ResultInfoException("模块启禁用失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description:  检查业务主键是否存在关联
     * @param oid 业务主键
     * @param flag 0删除  1启禁用
     * @author: wuxx
     * @Date: 2021/4/28 13:28
     **/
    public void checkIsExitsByOid(String oid,int flag) {
        //表单设计部分
        FormMain formMain = new FormMain();
        formMain.setModuleOid(oid);
        formMain.setIsDelete(BaseStaticParameter.N);
        if(1 == flag){
            formMain.setIsAble(BaseStaticParameter.Y);
        }
        int mainCount = formMainManager.queryFormMainCount(formMain);
        if(mainCount>0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前模块存在未删除的表单设计信息！");
            }else{
                throw new ResultInfoException("禁用失败，当前模块对象存在启用的表单设计信息！");
            }
        }
        //数据源部分
        FormDataSource formDataSource = new FormDataSource();
        formDataSource.setModuleOid(oid);
        formDataSource.setIsDelete(BaseStaticParameter.N);
        if(1 == flag){
            formDataSource.setIsAble(BaseStaticParameter.Y);
        }
        int sourceCount = formDataSourceManager.queryFormDataSourceCount(formDataSource);
        if(sourceCount > 0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前模块存在未删除的数据源信息！");
            }else{
                throw new ResultInfoException("禁用失败，当前模块对象存在启用的数据源信息！");
            }
        }

        //存储对象部分
        FormObject formObject = new FormObject();
        formObject.setModuleOid(oid);
        formObject.setIsDelete(BaseStaticParameter.N);
        if(1 == flag){
            formObject.setIsAble(BaseStaticParameter.Y);
        }
        int objectCount = formObjectManager.queryFormObjectCount(formObject);
        if(objectCount > 0){
            if(0 == flag){
                throw new ResultInfoException("删除失败，当前模块存在未删除的存储对象信息！");
            }else{
                throw new ResultInfoException("禁用失败，当前模块对象存在启用的存储对象信息！");
            }
        }
    }

    @Cacheable(key = "'getFormModuleById:'+#id", unless = "#result == null")
    public FormModule getFormModuleById(Long id) {
        DbFormModule dbFormModule = dbFormModuleMapper.selectByPrimaryKey(id);
        if(dbFormModule == null)
            return null;
        FormModule formModule = new FormModule();
        BeanUtils.copyProperties(dbFormModule,formModule);
        return formModule;
    }

    @Cacheable(key = "'getFormModuleByModuleOid:'+#moduleOid", unless = "#result == null")
    public FormModule getFormModuleByModuleOid(String moduleOid) {
        DbFormModule dbFormModule = dbFormModuleMapper.selectByModuleOid(moduleOid);
        if(dbFormModule == null)
            return null;
        FormModule formModule = new FormModule();
        BeanUtils.copyProperties(dbFormModule,formModule);
        return formModule;
    }

    public PageResult<FormModule> queryFormModuleWithPage(FormModule formModule, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormModuleExample dbFormModuleExample = new DbFormModuleExample();
        DbFormModuleExample.Criteria criteria = dbFormModuleExample.createCriteria();
        dbFormModuleExample.setOrderByClause(" ID DESC ");
        if(null!=formModule){
            if(StrUtil.isNotEmpty(formModule.getModuleName())){
                String value = formModule.getModuleName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andModuleNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formModule.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formModule.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formModule.getModuleOid())){
                criteria.andModuleOidEqualTo(formModule.getModuleOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormModule> dbFormModules = (Page<DbFormModule>)dbFormModuleMapper.selectByExample(dbFormModuleExample);
        PageResult<FormModule> pageResult = new PageResult<>(dbFormModules.getPageNum(),dbFormModules.getPageSize(),dbFormModules.getTotal());
        List<FormModule> formModuleList = dbFormModules.stream().map(dbFormModule -> {
            FormModule dict = new FormModule();
            BeanUtils.copyProperties(dbFormModule,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(formModuleList);
        return pageResult;
    }

    /**
     * @description: 根据接入系统模块信息查询集合
     * @param formModule 接入系统模块信息
     * @author: wuxx
     * @Date: 2021/3/11 10:57
     **/
    public List<FormModule> queryFormModuleList(FormModule formModule) {
        DbFormModuleExample dbFormModuleExample = new DbFormModuleExample();
        DbFormModuleExample.Criteria criteria = dbFormModuleExample.createCriteria();
        dbFormModuleExample.setOrderByClause(" ID DESC ");
        if(null!=formModule){
            if(StrUtil.isNotEmpty(formModule.getModuleName())){
                String value = formModule.getModuleName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andModuleNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formModule.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formModule.getAuthorizeKey());
            }
            if(null != formModule.getIsDelete()){
                criteria.andIsDeleteEqualTo(formModule.getIsDelete());
            }
            if(null != formModule.getIsAble()){
                criteria.andIsAbleEqualTo(formModule.getIsAble());
            }
        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        //criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbFormModule> dbFormModuleList = dbFormModuleMapper.selectByExample(dbFormModuleExample);
        List<FormModule> formModuleList = dbFormModuleList.stream().map(dbFormModule -> {
            FormModule object = new FormModule();
            BeanUtils.copyProperties(dbFormModule,object);
            return object;
        }).collect(Collectors.toList());
        return formModuleList;
    }

}
