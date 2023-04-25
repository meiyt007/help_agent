package com.zfsoft.microservice.form.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormColumnMapper;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormObjectMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormObject;
import com.zfsoft.microservice.form.dbaccess.data.DbFormObjectExample;
import com.zfsoft.microservice.form.util.Assert;
import com.zfsoft.microservice.form.util.DBHelper;
import com.zfsoft.microservice.form.util.FormUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormObjectManager
 * @Description: 存储对象接口实现类
 * @Author wuxx
 * @Date 2021/4/13 15:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:object")
public class FormObjectManager {

    @Resource
    private DbFormObjectMapper dbFormObjectMapper;

    @Resource
    private FormColumnManager formColumnManager;

    @Resource
    private FormDataSourceManager formDataSourceManager;

    @Resource
    @Lazy
    private FormMainManager formMainManager;

    @Resource
    private FormObjectExtandManager formObjectExtandManager;

    @Resource
    @Lazy
    private FormObjectManager formObjectManager;

    @Resource
    @Lazy
    private FormColumnManager formColumnMapper;

    @Resource
    @Lazy
    private FormReportManager formReportManager;
    @Resource
    @Lazy
    private FormDesignManager formDesignManager;
    /**
     * @description:  保存存储对象信息
     * @param formObject 存储对象
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public FormObject saveFormObject(@ValidGroups(groups = {FormObject.INSERT_GROUP.class}) FormObject formObject) {
        if(null == formObject){
            throw new ResultInfoException("存储对象不能为空！");
        }
        if(StrUtil.isNotEmpty(formObject.getObjectCode())){
            String message = this.checkIsExistesCode(formObject.getId(), formObject.getObjectCode(),formObject.getAuthorizeKey());
            if(StrUtil.isNotEmpty(message)){
                throw new ResultInfoException(message);
            }
        }
        if (null == formObject.getId()) {
            formObject.setId(null);
            formObject.setObjectOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormObject curDict = this.getFormObjectById(formObject.getId());
            if (curDict == null) {
                throw new ResultInfoException("存储对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formObject.getIsDelete()) {
            formObject.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formObject.getIsAble()) {
            formObject.setIsAble(BaseStaticParameter.Y);
        }
        if (null == formObject.getCreateDate()) {
            formObject.setCreateDate(new Date());
        }
        if(StrUtil.isEmpty(formObject.getObjectCode())){
            String code = "OBJCODE" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
            formObject.setObjectCode(code);
        }
        DbFormObject dbFormObject = new DbFormObject();
        BeanUtils.copyProperties(formObject,dbFormObject);
        int index=0;
        if (null == formObject.getId()) {
            index = dbFormObjectMapper.insert(dbFormObject);
        }else {
            index = dbFormObjectMapper.updateByPrimaryKeySelective(dbFormObject);
        }
        formObject.setId(dbFormObject.getId());
        formObject.setObjectOid(dbFormObject.getObjectOid());
        //插入表结构
        //先删除所有的表结构
        formColumnManager.deleteFormColumnByObjectOid(dbFormObject.getObjectOid());
        if(null!=formObject.getColumnList()){
            for(FormColumn formColumn : formObject.getColumnList()){
                formColumn.setId(null);
                formColumn.setObjectOid(dbFormObject.getObjectOid());
                formColumn.setDatasourceOid(dbFormObject.getDatasourceOid());
                formColumnManager.saveFormColumn(formColumn);
            }
        }
        //保存扩展对象信息
        formObjectExtandManager.deleteFormExtandByObjectOid(dbFormObject.getObjectOid());
        if(null!=formObject.getExtandList()){
            for (FormObjectExtand formObjectExtand : formObject.getExtandList()) {
                formObjectExtand.setId(null);
                formObjectExtand.setMainObjectOid(dbFormObject.getObjectOid());
                formObjectExtandManager.saveFormObjectExtand(formObjectExtand);
            }
        }
        return formObject;
    }


    /**
     * @description:  保存存储对象信息--针对组件集成方法
     * @param formObject 存储对象
     * @author: wuxx
     * @Date: 2021/8/3 15:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public FormMain saveObjectFormObject(@ValidGroups(groups = {FormObject.INSERT_GROUP.class}) FormObject formObject) {
        if(null == formObject){
            throw new ResultInfoException("存储对象不能为空！");
        }
        if(StrUtil.isNotEmpty(formObject.getObjectCode())){
            String message = this.checkIsExistesCode(formObject.getId(), formObject.getObjectCode(),formObject.getAuthorizeKey());
            if(StrUtil.isNotEmpty(message)){
                throw new ResultInfoException(message);
            }
        }
        if (null == formObject.getId()) {
            formObject.setId(null);
            formObject.setObjectOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormObject curDict = this.getFormObjectById(formObject.getId());
            if (curDict == null) {
                throw new ResultInfoException("存储对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formObject.getIsDelete()) {
            formObject.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formObject.getIsAble()) {
            formObject.setIsAble(BaseStaticParameter.Y);
        }
        if (null == formObject.getCreateDate()) {
            formObject.setCreateDate(new Date());
        }
        if(StrUtil.isEmpty(formObject.getObjectCode())){
            String code = "OBJCODE" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
            formObject.setObjectCode(code);
        }
        DbFormObject dbFormObject = new DbFormObject();
        BeanUtils.copyProperties(formObject,dbFormObject);
        if (null == formObject.getId()) {
            dbFormObjectMapper.insert(dbFormObject);
        }else {
            dbFormObjectMapper.updateByPrimaryKeySelective(dbFormObject);
        }
        formObject.setId(dbFormObject.getId());
        //插入表结构
        //先删除所有的表结构
        formColumnManager.deleteFormColumnByObjectOid(dbFormObject.getObjectOid());
        if(null!=formObject.getColumnList()){
            for(FormColumn formColumn : formObject.getColumnList()){
                formColumn.setId(null);
                formColumn.setObjectOid(dbFormObject.getObjectOid());
                formColumn.setDatasourceOid(dbFormObject.getDatasourceOid());
                formColumnManager.saveFormColumn(formColumn);
            }
        }
        //保存扩展对象信息
        formObjectExtandManager.deleteFormExtandByObjectOid(dbFormObject.getObjectOid());
        if(null!=formObject.getExtandList()){
            for (FormObjectExtand formObjectExtand : formObject.getExtandList()) {
                formObjectExtand.setId(null);
                formObjectExtand.setMainObjectOid(dbFormObject.getObjectOid());
                formObjectExtandManager.saveFormObjectExtand(formObjectExtand);
            }
        }
        FormMain formMain = new FormMain();
        if(StrUtil.isNotEmpty(formObject.getFormCode()) && StrUtil.isNotBlank(formObject.getFormCode())){
            formMain =  formMainManager.getFormMainByFormMainCode(formObject.getFormCode());
            formMain.setObjectOid(formObject.getObjectOid());
            formMainManager.updateFormMain(formMain);
        }else{
            //生成表单
            formMain.setModuleOid(formObject.getModuleOid());
            String name = "FORMNAME" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
            formMain.setFormName(name);
            formMain.setAuthorizeKey(formObject.getAuthorizeKey());
            formMain.setObjectOid(formObject.getObjectOid());
            if(StrUtil.isNotEmpty(formObject.getDatasourceOid())){
                formMain.setSaveDataType(2);
            }else{
                formMain.setSaveDataType(0);
            }
            formMainManager.saveFormMain(formMain);
           // throw new ResultInfoException("存储对象缺少formCode字段数据!");
        }

        return formMain;
    }

    /**
     * 检查code是否存在
     * @return
     * @throws Exception
     */
    public String checkIsExistesCode(Long id, String code,String authorizeKey) {
        if(StrUtil.isEmpty(code)){
            throw new ResultInfoException("当前存储对象编码不能为空！");
        }
        DbFormObjectExample dbFormObjectExample = new DbFormObjectExample();
        DbFormObjectExample.Criteria criteria = dbFormObjectExample.createCriteria();
        criteria.andObjectCodeEqualTo(code);
        criteria.andAuthorizeKeyEqualTo(authorizeKey);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormObject> dbFormObjects = dbFormObjectMapper.selectByExample(dbFormObjectExample);
        DbFormObject dbFormObject = null!=dbFormObjects && dbFormObjects.size()>0?dbFormObjects.get(0):null;
        if (dbFormObject != null && authorizeKey.equals(dbFormObject.getAuthorizeKey()) && (null==id ||dbFormObject.getId().longValue()!=id.longValue())) {
            throw new ResultInfoException("当前存储对象编码已经存在！");
        }
        return null;
    }

    /**
     * @description:  删除存储对象信息
     * @param id 存储对象主键
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormObjectById(Long id) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByPrimaryKey(id);
        if(dbFormObject == null)
            throw new ResultInfoException("存储对象信息为空！");
        //先判断是否被引用
        if(!this.checkIsExitsByOid(dbFormObject.getObjectOid(),0)){
            throw new ResultInfoException("删除失败，当前存储对象存在未删除的表单设计信息！");
        }
        //判断是否被扩展对象引用
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(null, dbFormObject.getObjectOid(), null, null, null);
        if(formObjectExtands!=null&&formObjectExtands.size()>0){
            throw new ResultInfoException("删除失败，当前存储对象是其他存储对象的扩展对象！");
        }
        int index = dbFormObjectMapper.deleteByForeignKey(dbFormObject.getObjectOid());
        if(index==0){
            throw new ResultInfoException("存储对象删除失败，请稍后再试！");
        }
        //删除所有的表结构
        formColumnManager.deleteFormColumnByObjectOid(dbFormObject.getObjectOid());
        formObjectExtandManager.deleteFormExtandByObjectOid(dbFormObject.getObjectOid());
        return index;
    }


    /**
     * @description: 对象的启禁用操作
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormObjectById(Long id) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByPrimaryKey(id);
        if(dbFormObject == null)
            throw new ResultInfoException("存储对象信息为空！");
        Integer isAble = dbFormObject.getIsAble();
        if(BaseStaticParameter.N == isAble){
            FormDataSource formDataSource = this.formDataSourceManager.getFormDataSourceByDataSourceOid(dbFormObject.getDatasourceOid());
            if(null!=formDataSource)
            Assert.isTrue(formDataSource.getIsAble() == BaseStaticParameter.Y, MessageFormat.format("数据源{0}处于禁用状态，请先启用数据源!", formDataSource.getDatasourceName()));
            // 20210825 modify by kkfan 启用存储对象前校验关联对象是否启用
            List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(dbFormObject.getObjectOid(), null);
            List<FormObjectExtand> extandList = formObjectExtands.stream().filter(formObjectExtand -> {
                if (StringUtils.isBlank(formObjectExtand.getSecondaryObjectOid())) {
                    return false;
                } else {
                    FormObject secondaryFormObject = this.formObjectManager.getFormObjectByObjectOid(formObjectExtand.getSecondaryObjectOid());
                    Assert.notNull(secondaryFormObject, MessageFormat.format("数据异常, 未找到关联对象objectOid为{0}的对象{1}", formObjectExtand.getSecondaryObjectOid(), formObjectExtand.getVariableName()));
                    return secondaryFormObject.getIsAble() == BaseStaticParameter.N;
                }
            }).collect(Collectors.toList());
            Assert.isTrue(CollectionUtils.isEmpty(extandList), MessageFormat.format("当前存储对象关联对象{0}未启用，请先启用关联存储对象", extandList.stream().map(FormObjectExtand::getVariableName).collect(Collectors.joining(","))));
            dbFormObject.setIsAble(BaseStaticParameter.Y);
        }else {
            //先判断是否被引用
            if(!this.checkIsExitsByOid(dbFormObject.getObjectOid(),1)){
                throw new ResultInfoException("禁用失败，当前存储对象存在启用的表单设计信息！");
            }
            // 20210825 modify by kkfan 禁用存储对象前校验是否被其它已启用的存储对象关联
            List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(null,dbFormObject.getObjectOid(), null,null,null);
            List<FormObject> objectList = formObjectExtands.stream().map(formObjectExtand -> {
                FormObject secondaryFormObject = this.formObjectManager.getFormObjectByObjectOid(formObjectExtand.getMainObjectOid());
                Assert.notNull(secondaryFormObject, MessageFormat.format("数据异常, 未找到关联对象objectOid为{0}的对象{1}", formObjectExtand.getSecondaryObjectOid(), formObjectExtand.getVariableName()));
                return secondaryFormObject;
            }).filter(formObject -> formObject.getIsAble() == BaseStaticParameter.Y).collect(Collectors.toList());
            Assert.isTrue(CollectionUtils.isEmpty(objectList), MessageFormat.format("当前存储对象存在已启用的关联对象，请先禁用存储对象：{0}", objectList.stream().map(FormObject::getObjectName).collect(Collectors.joining(","))));
            dbFormObject.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormObjectMapper.updateByPrimaryKeySelective(dbFormObject);
        if(index==0){
            throw new ResultInfoException("存储对象启禁用失败，请稍后再试！");
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
        FormMain formMain = new FormMain();
        formMain.setObjectOid(oid);
        formMain.setIsDelete(BaseStaticParameter.N);
        if(1 == flag){
            formMain.setIsAble(BaseStaticParameter.Y);
        }
        int count = formMainManager.queryFormMainCount(formMain);
        if(count == 0){
            return true;
        }
        return false;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Cacheable(key = "'getFormObjectById:'+#id", unless = "#result == null")
    public FormObject getFormObjectById(Long id) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByPrimaryKey(id);
        if(dbFormObject == null)
            return null;
        FormObject formObject = new FormObject();
        BeanUtils.copyProperties(dbFormObject,formObject);
        return formObject;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param objectOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Cacheable(key = "'getFormObjectByObjectOid:'+#objectOid", unless = "#result == null")
    public FormObject getFormObjectByObjectOid(String objectOid) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByForeignKey(objectOid);
        if(dbFormObject == null)
            return null;
        FormObject formObject = new FormObject();
        BeanUtils.copyProperties(dbFormObject,formObject);
        return formObject;
    }

    /**
     * @description: 根据业务主键获取对象信息--包含字段
     * @param objectOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public FormObject getFormObjectComponentByObjectOid(String objectOid) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByForeignKey(objectOid);
        if(dbFormObject == null)
            return null;
        FormObject formObject = new FormObject();
        BeanUtils.copyProperties(dbFormObject,formObject);
        FormColumn column = new FormColumn();
        column.setObjectOid(formObject.getObjectOid());
        column.setDatasourceOid(formObject.getDatasourceOid());
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(column);
        formObject.setColumnList(formColumnList);
        return formObject;
    }

    /**
     * @description: 根据编码获取对象信息
     * @param objectCode 编码
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Cacheable(key = "'getFormObjectByObjectCode:'+#objectCode", unless = "#result == null")
    public FormObject getFormObjectByObjectCode(String objectCode) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByObjectCode(objectCode);
        if(dbFormObject == null)
            return null;
        FormObject formObject = new FormObject();
        BeanUtils.copyProperties(dbFormObject,formObject);
        return formObject;
    }

    /**
     * @description: 分页查询存储对象的列表
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public PageResult<FormObject> queryFormObjectWithPage(FormObject formObject, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormObjectExample dbFormObjectExample = new DbFormObjectExample();
        DbFormObjectExample.Criteria criteria = dbFormObjectExample.createCriteria();
        dbFormObjectExample.setOrderByClause(" ID DESC ");
        if(null!=formObject){
            if(StrUtil.isNotEmpty(formObject.getObjectName())){
                String value = formObject.getObjectName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getObjectCode())){
                String value = formObject.getObjectCode().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectCodeLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formObject.getAuthorizeKey());
            }
            //存储类型 {'0':'存储对象','1':'逻辑对象'},
            if(StrUtil.isNotEmpty(formObject.getSaveType())){
                if(BaseStaticParameter.STR_ONE.equals(formObject.getSaveType())){
                    criteria.andDatasourceOidIsNull();
                }else{
                    criteria.andDatasourceOidIsNotNull();
                }
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormObject> dbFormObjects = (Page<DbFormObject>)dbFormObjectMapper.selectByExample(dbFormObjectExample);
        PageResult<FormObject> pageResult = new PageResult<>(dbFormObjects.getPageNum(),dbFormObjects.getPageSize(),dbFormObjects.getTotal());
        List<FormObject> formObjectList = dbFormObjects.stream().map(dbFormObject -> {
            FormObject object = new FormObject();
            BeanUtils.copyProperties(dbFormObject,object);
            if(StrUtil.isNotBlank(object.getDatasourceOid())){
                FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(object.getDatasourceOid());
                object.setDatasourceName(null!=dataSource?dataSource.getConnectionName():"");
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formObjectList);
        return pageResult;
    }

    /**
     * @description: 根据存储对象查询集合
     * @param formObject 存储对象
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public List<FormObject> queryFormObjectList(FormObject formObject) {
        DbFormObjectExample dbFormObjectExample = new DbFormObjectExample();
        DbFormObjectExample.Criteria criteria = dbFormObjectExample.createCriteria();
        dbFormObjectExample.setOrderByClause(" ID DESC ");
        if(null!=formObject){
            if(StrUtil.isNotEmpty(formObject.getObjectName())){
                String value = formObject.getObjectName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getObjectCode())){
                String value = formObject.getObjectCode().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectCodeLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formObject.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formObject.getObjectOid())){
                criteria.andObjectOidEqualTo(formObject.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formObject.getModuleOid())){
                criteria.andModuleOidEqualTo(formObject.getModuleOid());
            }
            if(StrUtil.isNotEmpty(formObject.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formObject.getDatasourceOid());
            }
            if(null != formObject.getIsDelete()){
                criteria.andIsDeleteEqualTo(formObject.getIsDelete());
            }
            if(null != formObject.getIsAble()){
                criteria.andIsAbleEqualTo(formObject.getIsAble());
            }
            if(BaseStaticParameter.STR_ZERO.equals(formObject.getSaveType())){
                criteria.andDatasourceOidIsNotNull();
            }else if(BaseStaticParameter.STR_ONE.equals(formObject.getSaveType())){
                criteria.andDatasourceOidIsNull();
            }
        }
        List<DbFormObject> dbFormObjectList = dbFormObjectMapper.selectByExample(dbFormObjectExample);
        List<FormObject> formObjectList = dbFormObjectList.stream().map(dbFormObject -> {
            FormObject object = new FormObject();
            BeanUtils.copyProperties(dbFormObject,object);
            return object;
        }).collect(Collectors.toList());
        return formObjectList;
    }

    /**
     * @description: 检查是否存在数据
     * @param formObject 存储对象
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public int queryFormObjectCount(FormObject formObject) {
        DbFormObjectExample dbFormObjectExample = new DbFormObjectExample();
        DbFormObjectExample.Criteria criteria = dbFormObjectExample.createCriteria();
        if(null!=formObject){
            if(StrUtil.isNotEmpty(formObject.getObjectName())){
                String value = formObject.getObjectName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getObjectCode())){
                String value = formObject.getObjectCode().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andObjectCodeLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formObject.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formObject.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formObject.getObjectOid())){
                criteria.andObjectOidEqualTo(formObject.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formObject.getModuleOid())){
                criteria.andModuleOidEqualTo(formObject.getModuleOid());
            }
            if(StrUtil.isNotEmpty(formObject.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formObject.getDatasourceOid());
            }
            if(null != formObject.getIsDelete()){
                criteria.andIsDeleteEqualTo(formObject.getIsDelete());
            }
            if(null != formObject.getIsAble()){
                criteria.andIsAbleEqualTo(formObject.getIsAble());
            }
        }
        int count = dbFormObjectMapper.countByExample(dbFormObjectExample);
        return count;
    }

    /**
     * @description: 传formCode集合，返回所有的存储对象
     * @param formCodes 表单编码,多个用逗号隔开
     * @author: wuxx
     * @Date: 2022/1/12 11:27
     **/
    public List<FormObject> getFormObjectByFormCodes(String formCodes){
        String[] splitFormCodes = formCodes.split(",");
        List<FormObject> formObjectList = new ArrayList<>();
        for(String formCode : splitFormCodes){
            FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
            if(null == mainCode || StrUtil.isEmpty(mainCode.getObjectOid())){
                continue;
            }
            DbFormObject dbFormObject = dbFormObjectMapper.selectByForeignKey(mainCode.getObjectOid());
            FormObject object = new FormObject();
            BeanUtils.copyProperties(dbFormObject,object);
            List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(dbFormObject.getObjectOid(), null);
            object.setExtandList(formObjectExtands);
            formObjectList.add(object);
        }
        return formObjectList;
    }

    /**
     * @description:  获取设置的下拉字段信息
     * @param objectCode 存储编码
     * @author: wuxx
     * @Date: 2021/11/11 10:45
     **/
    public Object queryDictListByObjectCode(String objectCode,Map<String, Object> map) {
        DbFormObject dbFormObject = dbFormObjectMapper.selectByObjectCode(objectCode);
        if(null == dbFormObject){
            throw new ResultInfoException("未找到对应编码的对象信息！");
        }
        String tableName = dbFormObject.getObjectForm();
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(dbFormObject.getObjectOid());
        formColumn.setDatasourceOid(dbFormObject.getDatasourceOid());
        List<FormColumn> formColumns = formColumnMapper.queryFormColumnList(formColumn);
        Map<String, Object> dataMap = new HashMap<>();
        for(FormColumn column : formColumns){
            if("modifyDate".equals(column.getObjectProperty())){
                continue;
            }
            dataMap.put(column.getObjectProperty(),null);
        }
        Set<String> keySet = dataMap.keySet();
        String sql = "SELECT * FROM "+tableName + " WHERE 1=1 ";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if(!keySet.contains(key)){
                throw new ResultInfoException("请求参数"+key+"是否配置正确！");
            }
            Object value = entry.getValue();
            sql = sql + " AND "+key + "='" + value +"'";
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(dbFormObject.getDatasourceOid());
        DBHelper dbHelper = new DBHelper(source);

        List<Map<String, Object>> mapList = dbHelper.selectDbSql(sql, dataMap);
        //System.out.println(JSONUtil.toJsonStr(mapList));
        return mapList;
    }

    /**
     * @param objectOid 存储对象业务oid
     * @description: 根据objectOid查询是否存在填报的数据
     * @author: wuxx
     * @Date: 2021/11/23 10:14
     **/
    public Boolean checkReportDataExist(String objectOid) {
        FormObject formObject = this.getFormObjectByObjectOid(objectOid);
        if(null == formObject){
            throw new ResultInfoException("存储对象信息不存在！");
        }
        String datasourceOid = formObject.getDatasourceOid();
        if(StrUtil.isNotEmpty(datasourceOid)){
            FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
            DBHelper dbHelper = new DBHelper(source);
            String sql = "SELECT COUNT(1) FROM "+formObject.getObjectForm();
            int sqlCount = dbHelper.selectDbSqlCount(sql);
            if(sqlCount>0){
                return true;
            }
        }else {
            FormDesign formDesign = new FormDesign();
            formDesign.setObjectOid(formObject.getObjectOid());
            formDesign.setReleaseStatus(2);
            List<FormDesign> formDesigns = formDesignManager.queryFormDesignList(formDesign);
            for (FormDesign design : formDesigns){
                FormReport formReport = new FormReport();
                formReport.setDesignOid(design.getDesignOid());
                List<FormReport> reportList = formReportManager.queryFormReportList(formReport);
                if(null!=reportList && reportList.size()>0){
                    return true;
                }
            }
        }

        return  false;
    }
}
