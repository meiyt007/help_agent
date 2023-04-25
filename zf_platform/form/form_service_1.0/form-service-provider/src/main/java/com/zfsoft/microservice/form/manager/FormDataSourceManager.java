package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.microservice.form.data.FormModule;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormDataSourceMapper;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormModuleMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormDataSource;
import com.zfsoft.microservice.form.dbaccess.data.DbFormDataSourceExample;
import com.zfsoft.microservice.form.dbaccess.data.DbFormModule;
import com.zfsoft.microservice.form.util.DBHelper;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormDataSourceManager
 * @Description: 数据源配置接口实现类
 * @Author wuxx
 * @Date 2021/3/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:dataSource")
public class FormDataSourceManager {

    @Resource
    private DbFormDataSourceMapper dbFormDataSourceMapper;

    @Resource
    private DbFormModuleMapper dbFormModuleMapper;

    @Resource
    @Lazy
    private FormObjectManager formObjectManager;

    @Resource
    @Lazy
    private FormModuleManager formModuleManager;


    /**
     * @description:  保存数据源配置信息
     * @param formDataSource 数据源配置对象
     * @author: wuxx
     * @Date: 2021/3/15 9:18
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormDataSource(@ValidGroups(groups = {FormDataSource.INSERT_GROUP.class}) FormDataSource formDataSource) {
        if(null == formDataSource){
            throw new ResultInfoException("数据源配置对象不能为空！");
        }
        if (null == formDataSource.getId()) {
            formDataSource.setId(null);
            formDataSource.setDatasourceOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormDataSource curDict = this.getFormDataSourceById(formDataSource.getId());
            if (curDict == null) {
                throw new ResultInfoException("数据源配置对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formDataSource.getIsDelete()) {
            formDataSource.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formDataSource.getIsAble()) {
            formDataSource.setIsAble(BaseStaticParameter.Y);
        }
        DbFormDataSource dbFormDataSource = new DbFormDataSource();
        BeanUtils.copyProperties(formDataSource,dbFormDataSource);
        int index=0;
        if (null == formDataSource.getId()) {
            index = dbFormDataSourceMapper.insert(dbFormDataSource);
        }else {
            index = dbFormDataSourceMapper.updateByPrimaryKeySelective(dbFormDataSource);
        }
        formDataSource.setId(dbFormDataSource.getId());
        return index;
    }

    /**
     * @description:  删除数据源配置信息
     * @param id 数据源配置对象主键
     * @author: wuxx
     * @Date: 2021/3/15 9:18
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormDataSourceById(Long id) {
        DbFormDataSource dbFormDataSource = dbFormDataSourceMapper.selectByPrimaryKey(id);
        if(dbFormDataSource == null)
            throw new ResultInfoException("数据源配置对象信息为空！");
        //先判断数据源是否被引用
        if(!this.checkIsExitsByOid(dbFormDataSource.getDatasourceOid(),0)){
            throw new ResultInfoException("删除失败，当前数据源存在未删除的存储对象信息！");
        }
        int index = dbFormDataSourceMapper.deleteByDataSourceOid(dbFormDataSource.getDatasourceOid());
        if(index==0){
            throw new ResultInfoException("数据源配置对象删除失败，请稍后再试！");
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
    public boolean checkIsExitsByOid(String oid,int flag) {
        FormObject formObject = new FormObject();
        formObject.setDatasourceOid(oid);
        formObject.setIsDelete(BaseStaticParameter.N);
        if(1 == flag) {
            formObject.setIsAble(BaseStaticParameter.Y);
        }
        int count = formObjectManager.queryFormObjectCount(formObject);
        if(count == 0){
            return true;
        }
        return false;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormDataSourceById(Long id) {
        DbFormDataSource dbFormDataSource = dbFormDataSourceMapper.selectByPrimaryKey(id);
        if(dbFormDataSource == null)
            throw new ResultInfoException("数据源配置对象信息为空！");
        Integer isAble = dbFormDataSource.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbFormDataSource.setIsAble(BaseStaticParameter.Y);
        }else {
            //先判断数据源是否被引用
            if(!this.checkIsExitsByOid(dbFormDataSource.getDatasourceOid(),1)){
                throw new ResultInfoException("禁用失败，当前数据源存在启用的存储对象信息！");
            }
            dbFormDataSource.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormDataSourceMapper.updateByPrimaryKeySelective(dbFormDataSource);
        if(index==0){
            throw new ResultInfoException("数据源配置对象启禁用失败，请稍后再试！");
        }
        return index;
    }

    @Cacheable(key = "'getFormDataSourceById:'+#id", unless = "#result == null")
    public FormDataSource getFormDataSourceById(Long id) {
        DbFormDataSource dbFormDataSource = dbFormDataSourceMapper.selectByPrimaryKey(id);
        if(dbFormDataSource == null)
            return null;
        FormDataSource formDataSource = new FormDataSource();
        BeanUtils.copyProperties(dbFormDataSource,formDataSource);
        if(StrUtil.isNotEmpty(formDataSource.getModuleOid())){
            DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(formDataSource.getModuleOid());
            formDataSource.setModuleName(null!=formModule?formModule.getModuleName():null);
        }
        return formDataSource;
    }

    @Cacheable(key = "'getFormDataSourceByDataSourceOid:'+#dataSourceOid", unless = "#result == null")
    public FormDataSource getFormDataSourceByDataSourceOid(String dataSourceOid) {
        DbFormDataSource dbFormDataSource = dbFormDataSourceMapper.selectByDataSourceOid(dataSourceOid);
        if(dbFormDataSource == null)
            return null;
        FormDataSource formDataSource = new FormDataSource();
        BeanUtils.copyProperties(dbFormDataSource,formDataSource);
        if(StrUtil.isNotEmpty(formDataSource.getModuleOid())){
            DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(formDataSource.getModuleOid());
            formDataSource.setModuleName(null!=formModule?formModule.getModuleName():null);
        }
        return formDataSource;
    }


    public PageResult<FormDataSource> queryFormDataSourceWithPage(FormDataSource formDataSource, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormDataSourceExample dbFormDataSourceExample = new DbFormDataSourceExample();
        DbFormDataSourceExample.Criteria criteria = dbFormDataSourceExample.createCriteria();
        if(null!=formDataSource){
            if(StrUtil.isNotEmpty(formDataSource.getConnectionName())){
                String value =formDataSource.getConnectionName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andConnectionNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formDataSource.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formDataSource.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formDataSource.getModuleOid())){
                criteria.andModuleOidEqualTo(formDataSource.getModuleOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormDataSource> dbFormDataSources = (Page<DbFormDataSource>)dbFormDataSourceMapper.selectByExample(dbFormDataSourceExample);
        PageResult<FormDataSource> pageResult = new PageResult<>(dbFormDataSources.getPageNum(),dbFormDataSources.getPageSize(),dbFormDataSources.getTotal());
        List<FormDataSource> formDataSourceList = dbFormDataSources.stream().map(dbFormDataSource -> {
            FormDataSource object = new FormDataSource();
            BeanUtils.copyProperties(dbFormDataSource,object);
            object.setPassword(null);
            if(StrUtil.isNotBlank(object.getModuleOid())){
                FormModule module = formModuleManager.getFormModuleByModuleOid(object.getModuleOid());
                object.setModuleName(null == module?"":module.getModuleName());
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formDataSourceList);
        return pageResult;
    }

    /**
     * @description: 根据接入系统数据源配置对象查询集合
     * @param formDataSource 接入系统数据源配置对象
     * @author: wuxx
     * @Date: 2021/3/15 10:57
     **/
    public List<FormDataSource> queryFormDataSourceList(FormDataSource formDataSource) {
        DbFormDataSourceExample dbFormDataSourceExample = new DbFormDataSourceExample();
        DbFormDataSourceExample.Criteria criteria = dbFormDataSourceExample.createCriteria();
        if(null!=formDataSource){
            if(StrUtil.isNotEmpty(formDataSource.getConnectionName())){
                String value =formDataSource.getConnectionName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andConnectionNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formDataSource.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formDataSource.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formDataSource.getModuleOid())){
                criteria.andModuleOidEqualTo(formDataSource.getModuleOid());
            }
            if(null != formDataSource.getIsDelete()){
                criteria.andIsDeleteEqualTo(formDataSource.getIsDelete());
            }
            if(null != formDataSource.getIsAble()){
                criteria.andIsAbleEqualTo(formDataSource.getIsAble());
            }
        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        //criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbFormDataSource> dbFormDataSourceList = dbFormDataSourceMapper.selectByExample(dbFormDataSourceExample);
        List<FormDataSource> formDataSourceList = dbFormDataSourceList.stream().map(dbFormDataSource -> {
            FormDataSource object = new FormDataSource();
            BeanUtils.copyProperties(dbFormDataSource,object);
            object.setPassword(null);
            return object;
        }).collect(Collectors.toList());
        return formDataSourceList;
    }

    /**
     * @description:  检查数据源配置是否正常连接
     * @param formDataSource 数据源配置实体类
     * @author: wuxx
     * @Date: 2021/4/1 16:40
     **/
    @Transactional
    @ParamValid
    public boolean checkFormDataSourceConnection(@ValidGroups(groups = {FormDataSource.INSERT_GROUP.class}) FormDataSource formDataSource) {
        DBHelper dBHelper = new DBHelper(formDataSource);
        Map<String, String> checkMap = dBHelper.checkConnection();
        if("true".equals(checkMap.get("success"))){
            return true;
        }else {
            throw new ResultInfoException("连接失败！原因："+checkMap.get("message"));
        }
    }

    /**
     * @description: 检查是否存在数据
     * @param formDataSource 接入系统数据源配置对象
     * @author: wuxx
     * @Date: 2021/3/15 10:57
     **/
    public int queryFormDataSourceCount(FormDataSource formDataSource) {
        DbFormDataSourceExample dbFormDataSourceExample = new DbFormDataSourceExample();
        DbFormDataSourceExample.Criteria criteria = dbFormDataSourceExample.createCriteria();
        if(null!=formDataSource){
            if(StrUtil.isNotEmpty(formDataSource.getConnectionName())){
                String value =formDataSource.getConnectionName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andConnectionNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formDataSource.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formDataSource.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formDataSource.getModuleOid())){
                criteria.andModuleOidEqualTo(formDataSource.getModuleOid());
            }
            if(null != formDataSource.getIsDelete()){
                criteria.andIsDeleteEqualTo(formDataSource.getIsDelete());
            }
            if(null != formDataSource.getIsAble()){
                criteria.andIsAbleEqualTo(formDataSource.getIsAble());
            }
        }
        int count = dbFormDataSourceMapper.countByExample(dbFormDataSourceExample);
        return count;
    }
}
