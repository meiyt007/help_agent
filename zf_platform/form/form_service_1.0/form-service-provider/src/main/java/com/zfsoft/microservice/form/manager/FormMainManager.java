package com.zfsoft.microservice.form.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.data.vo.FormMainVo;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormMainMapper;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormModuleMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormMain;
import com.zfsoft.microservice.form.dbaccess.data.DbFormMainExample;
import com.zfsoft.microservice.form.dbaccess.data.DbFormModule;
import com.zfsoft.microservice.form.util.Assert;
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
 * @ClassName FormMainManager
 * @Description: 表单主表接口实现类
 * @Author wuxx
 * @Date 2021/4/16 10:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:main")
public class FormMainManager {

    @Resource
    private DbFormMainMapper dbFormMainMapper;

    @Resource
    private DbFormModuleMapper dbFormModuleMapper;

    @Resource
    @Lazy
    private FormDesignManager formDesignManager;

    @Resource
    @Lazy
    private FormObjectManager formObjectManager;

    @Resource
    @Lazy
    private FormColumnManager formColumnManager;

    @Resource
    @Lazy
    private FormObjectExtandManager formObjectExtandManager;


    /**
     * @description: 分页查询表单主表的列表
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public PageResult<FormMain> queryFormMainWithPage(FormMain formMain, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        dbFormMainExample.setOrderByClause(" ID DESC ");
        if(null!=formMain){
            if(StrUtil.isNotEmpty(formMain.getFormName())){
                String value =formMain.getFormName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andFormNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMain.getFormCode())){
                String value =formMain.getFormCode().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andFormCodeLike("%"+value+"%");
            }
            if(null != formMain.getFormStatus()){
                criteria.andFormStatusEqualTo(formMain.getFormStatus());
            }
            if(StrUtil.isNotEmpty(formMain.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMain.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMain.getModuleOid())){
                criteria.andModuleOidEqualTo(formMain.getModuleOid());
            }
            if(StrUtil.isNotEmpty(formMain.getObjectOid())){
                criteria.andObjectOidEqualTo(formMain.getObjectOid());
            }
            if (1 == formMain.getIsAble()){
                criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormMain> dbFormMains = (Page<DbFormMain>)dbFormMainMapper.selectByExample(dbFormMainExample);
        PageResult<FormMain> pageResult = new PageResult<>(dbFormMains.getPageNum(),dbFormMains.getPageSize(),dbFormMains.getTotal());
        List<FormMain> formMainList = dbFormMains.stream().map(dbFormMain -> {
            FormMain object = new FormMain();
            BeanUtils.copyProperties(dbFormMain,object);
            if(StrUtil.isNotEmpty(object.getModuleOid())){
                DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(object.getModuleOid());
                object.setModuleName(null!=formModule?formModule.getModuleName():null);
            }
            if(StrUtil.isNotEmpty(object.getObjectOid())){
                FormObject object1 = formObjectManager.getFormObjectByObjectOid(object.getObjectOid());
                object.setObjectName(null != object1?object1.getObjectName():null);
            }
            //查询最新的设计主键
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(object.getFormMainOid());
            object.setDesignOid(null!=design?design.getDesignOid():null);
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formMainList);
        return pageResult;
    }

    /**
     * @description:  保存表单主表信息
     * @param formMain 表单主表对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormMain(@ValidGroups(groups = {FormMain.INSERT_GROUP.class}) FormMain formMain) {
        if(null == formMain){
            throw new ResultInfoException("表单主表对象不能为空！");
        }
        /*String message = this.checkIsExistesCode(formMain.getId(), formMain.getFormCode(),formMain.getAuthorizeKey());
        if(StrUtil.isNotEmpty(message)){
            throw new ResultInfoException(message);
        }*/
        if (null == formMain.getId()) {
            formMain.setId(null);
            formMain.setFormMainOid(IdUtil.simpleUUID());
            //表单编码更改为自动生成无需修改
            String code = "FORM" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
            formMain.setFormCode(code);
        } else {
            // 对象不为空
            FormMain curDict = this.getFormMainById(formMain.getId());
            if (curDict == null) {
                throw new ResultInfoException("表单主表对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        if (null == formMain.getIsDelete()) {
            formMain.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formMain.getIsAble()) {
            formMain.setIsAble(BaseStaticParameter.Y);
        }
        if (null == formMain.getCreateDate()) {
            formMain.setCreateDate(new Date());
        }
        if (null == formMain.getFormStatus()) {
            formMain.setFormStatus(BaseStaticParameter.N);
        }
        /*if (null == formMain.getVersion()) {
            formMain.setVersion(1);
        }*/
        DbFormMain dbFormMain = new DbFormMain();
        BeanUtils.copyProperties(formMain,dbFormMain);
        int index=0;
        if (null == formMain.getId()) {
            index = dbFormMainMapper.insert(dbFormMain);
        }else {
            index = dbFormMainMapper.updateByPrimaryKeySelective(dbFormMain);
        }
        formMain.setId(dbFormMain.getId());
        FormDesign formDesign = this.formDesignManager.getFormDesignByFormMainOid(dbFormMain.getFormMainOid());
        if (null != formDesign) {
            formDesign.setDesignName(dbFormMain.getFormName());
            this.formDesignManager.updateFormDesign(formDesign);
        }
        return index;
    }



    /**
     * @description: 表单的更新
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormMain(@ValidGroups(groups = {FormMain.UPDATE_GROUP.class}) FormMain formMain) {
        DbFormMain dbFormMain = new DbFormMain();
        BeanUtils.copyProperties(formMain,dbFormMain);
        int index = dbFormMainMapper.updateByPrimaryKeySelective(dbFormMain);
        return index ;
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
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        criteria.andFormCodeEqualTo(code);
        criteria.andAuthorizeKeyEqualTo(authorizeKey);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormMain> dbFormMains = dbFormMainMapper.selectByExample(dbFormMainExample);
        DbFormMain dbFormMain = null!=dbFormMains && dbFormMains.size()>0?dbFormMains.get(0):null;
        if (dbFormMain != null  && (null==id ||dbFormMain.getId().longValue()!=id.longValue())) {
            throw new ResultInfoException("当前表单编码已经存在！");
        }
        return null;
    }

    /**
     * @description:  删除表单主表信息
     * @param id 表单主表对象主键
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormMainById(Long id) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByPrimaryKey(id);
        if(dbFormMain == null)
            throw new ResultInfoException("表单主表对象信息为空！");
        int index = dbFormMainMapper.deleteByForeignKey(dbFormMain.getFormMainOid());
        if(index==0){
            throw new ResultInfoException("表单主表对象删除失败，请稍后再试！");
        }
        //删除所有的历史设计 不删除保持记录
        //formDesignManager.deleteFormDesignByFormMainOid(dbFormMain.getFormMainOid());
        return index;
    }


    /**
     * @description: 对象的启禁用操作
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormMainById(Long id) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByPrimaryKey(id);
        if(dbFormMain == null)
            throw new ResultInfoException("表单主表对象信息为空！");
        // 20210825 modify by kkfan 启用表单前校验存储对象及其关联对象是否启用
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(dbFormMain.getObjectOid());
        Assert.isTrue(formObject.getIsAble() == BaseStaticParameter.Y, MessageFormat.format("关联存储对象未启用，请先启用存储对象：{0}", formObject.getObjectName()));
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(formObject.getObjectOid(), null);
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
        Integer isAble = dbFormMain.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbFormMain.setIsAble(BaseStaticParameter.Y);
        }else {
            dbFormMain.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormMainMapper.updateByPrimaryKeySelective(dbFormMain);
        if(index==0){
            throw new ResultInfoException("表单主表对象启禁用失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 发布表单
     * @param formMainOid 主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int deployFormMain(String formMainOid) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByForeignKey(formMainOid);
        if(dbFormMain == null)
            throw new ResultInfoException("表单主表对象信息为空！");
        FormDesign formDesign = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        formDesign.setReleaseStatus(FormStaticParameter.DATA_STATUS_2);
        formDesignManager.updateFormDesign(formDesign);
        dbFormMain.setFormStatus(FormStaticParameter.DATA_STATUS_2);
        int index = dbFormMainMapper.updateByPrimaryKeySelective(dbFormMain);
        if(index==0){
            throw new ResultInfoException("表单发布失败，请稍后再试！");
        }
        return index;
    }

    /**
     * @description: 复制表单
     * @param formMainOid 主键id
     * @author: wuxx
     * @Date: 2021/5/6 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int copyFormMain(String formMainOid) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByForeignKey(formMainOid);
        if(dbFormMain == null)
            throw new ResultInfoException("表单主表对象信息为空！");
        DbFormMain copyDbFormMain = new DbFormMain();
        BeanUtils.copyProperties(dbFormMain,copyDbFormMain);
        copyDbFormMain.setId(null);
        copyDbFormMain.setCreateDate(new Date());
        String mainOid = IdUtil.simpleUUID();
        copyDbFormMain.setFormMainOid(mainOid);
        //表单编码更改为自动生成无需修改
        String code = "FORM" + DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
        copyDbFormMain.setFormCode(code);
        copyDbFormMain.setVersion(1);
        int insert = dbFormMainMapper.insert(copyDbFormMain);
        FormDesign formDesign = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        if(null == formDesign){
            return insert;
        }
        FormDesign copyFormDesign = new FormDesign();
        BeanUtils.copyProperties(formDesign,copyFormDesign);
        copyFormDesign.setId(null);
        copyFormDesign.setVersion(1);
        copyFormDesign.setDesignOid(IdUtil.simpleUUID());
        copyFormDesign.setFormMainOid(mainOid);
        copyFormDesign.setCreateDate(new Date());
        formDesignManager.copySaveFormDesign(copyFormDesign);
        return insert;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Cacheable(key = "'getFormMainById:'+#id", unless = "#result == null")
    public FormMain getFormMainById(Long id) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByPrimaryKey(id);
        if(dbFormMain == null)
            return null;
        FormMain formMain = new FormMain();
        BeanUtils.copyProperties(dbFormMain,formMain);
        if(StrUtil.isNotBlank(formMain.getModuleOid())){
            DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(formMain.getModuleOid());
            formMain.setModuleName(null!=formModule?formModule.getModuleName():null);
        }
        if(StrUtil.isNotBlank(formMain.getObjectOid())){
            FormObject object = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
            formMain.setObjectName(null!=object?object.getObjectName():null);
        }
        return formMain;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param formMainCode 业务b编码
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Cacheable(key = "'getFormMainByFormMainCode:'+#formMainCode", unless = "#result == null")
    public FormMain getFormMainByFormMainCode(String formMainCode) {
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        dbFormMainExample.setOrderByClause(" ID DESC ");
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        criteria.andFormCodeEqualTo(formMainCode);
        List<DbFormMain> mainList = dbFormMainMapper.selectByExample(dbFormMainExample);
        DbFormMain dbFormMain = mainList.size()>0?mainList.get(0):null;
        if(dbFormMain == null)
            return null;
        FormMain formMain = new FormMain();
        BeanUtils.copyProperties(dbFormMain,formMain);
        FormDesign design = formDesignManager.getFormDesignByFormMainOid(formMain.getFormMainOid());
        formMain.setDesignOid(null!=design?design.getDesignOid():null);

        if(StrUtil.isNotBlank(formMain.getModuleOid())){
            DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(formMain.getModuleOid());
            formMain.setModuleName(null!=formModule?formModule.getModuleName():null);
        }
        if(StrUtil.isNotBlank(formMain.getObjectOid())){
            FormObject object = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
            formMain.setObjectName(null!=object?object.getObjectName():null);
        }

        return formMain;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param formMainOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Cacheable(key = "'getFormMainByFormMainOid:'+#formMainOid", unless = "#result == null")
    public FormMain getFormMainByFormMainOid(String formMainOid) {
        DbFormMain dbFormMain = dbFormMainMapper.selectByForeignKey(formMainOid);
        if(dbFormMain == null)
            return null;
        FormMain formMain = new FormMain();
        BeanUtils.copyProperties(dbFormMain,formMain);
        if(StrUtil.isNotBlank(formMain.getModuleOid())){
            DbFormModule formModule = dbFormModuleMapper.selectByModuleOid(formMain.getModuleOid());
            formMain.setModuleName(null!=formModule?formModule.getModuleName():null);
        }
        if(StrUtil.isNotBlank(formMain.getObjectOid())){
            FormObject object = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
            formMain.setObjectName(null!=object?object.getObjectName():null);
        }
        return formMain;
    }

    /**
     * @description: 根据表单主表对象查询集合
     * @param formMain 表单主表对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public List<FormMain> queryFormMainList(FormMain formMain) {
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        dbFormMainExample.setOrderByClause(" ID DESC ");
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        if(null!=formMain){
            if(StrUtil.isNotEmpty(formMain.getFormName())){
                String value =formMain.getFormName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andFormNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMain.getFormCode())){
                criteria.andFormCodeEqualTo(formMain.getFormCode().trim());
            }
            if(null != formMain.getFormStatus()){
                criteria.andFormStatusEqualTo(formMain.getFormStatus());
            }
            if(StrUtil.isNotEmpty(formMain.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMain.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMain.getModuleOid())){
                criteria.andModuleOidEqualTo(formMain.getModuleOid());
            }
            if(null != formMain.getIsDelete()){
                criteria.andIsDeleteEqualTo(formMain.getIsDelete());
            }
            if(null != formMain.getIsAble()){
                criteria.andIsAbleEqualTo(formMain.getIsAble());
            }
        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        //criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbFormMain> dbFormMainList = dbFormMainMapper.selectByExample(dbFormMainExample);
        List<FormMain> formMainList = dbFormMainList.stream().map(dbFormMain -> {
            FormMain object = new FormMain();
            BeanUtils.copyProperties(dbFormMain,object);
            return object;
        }).collect(Collectors.toList());
        return formMainList;
    }

    /**
     * @description: 根据授权key对象查询集合
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/5/18 10:00
     **/
    public List<FormMainVo> queryFormMainListByAuthorizeKey(String authorizeKey,Integer formStatus) {
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        dbFormMainExample.setOrderByClause(" ID DESC ");
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        criteria.andAuthorizeKeyEqualTo(authorizeKey);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        if(null!=formStatus){
            criteria.andFormStatusEqualTo(formStatus);
        }
        List<DbFormMain> dbFormMainList = dbFormMainMapper.selectByExample(dbFormMainExample);
        List<FormMainVo> formMainList = dbFormMainList.stream().map(dbFormMain -> {
            FormMainVo object = new FormMainVo();
            BeanUtils.copyProperties(dbFormMain,object);
            //查询最新的设计主键
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(object.getFormMainOid());
            object.setDesignOid(null!=design?design.getDesignOid():null);
            return object;
        }).collect(Collectors.toList());
        return formMainList;
    }

    /**
     * @description: 根据FormMain对象查询vo集合
     * @author: wuxx
     * @Date: 2021/7/21 10:00
     **/
    public List<FormMainVo> queryFormMainVoList(FormMain formMain) {
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        dbFormMainExample.setOrderByClause(" ID DESC ");
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        if(null!=formMain){
            if(StrUtil.isNotEmpty(formMain.getFormName())){
                String value =formMain.getFormName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andFormNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMain.getFormCode())){
                criteria.andFormCodeEqualTo(formMain.getFormCode().trim());
            }
            if(null != formMain.getFormStatus()){
                criteria.andFormStatusEqualTo(formMain.getFormStatus());
            }
            if(StrUtil.isNotEmpty(formMain.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMain.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMain.getModuleOid())){
                criteria.andModuleOidEqualTo(formMain.getModuleOid());
            }
            if(null != formMain.getIsDelete()){
                criteria.andIsDeleteEqualTo(formMain.getIsDelete());
            }
            if(null != formMain.getIsAble()){
                criteria.andIsAbleEqualTo(formMain.getIsAble());
            }
        }
        List<DbFormMain> dbFormMainList = dbFormMainMapper.selectByExample(dbFormMainExample);
        List<FormMainVo> formMainList = dbFormMainList.stream().map(dbFormMain -> {
            FormMainVo object = new FormMainVo();
            BeanUtils.copyProperties(dbFormMain,object);
            //查询最新的设计主键
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(object.getFormMainOid());
            object.setDesignOid(null!=design?design.getDesignOid():null);
            return object;
        }).collect(Collectors.toList());
        return formMainList;
    }

    /**
     * @description: 检查是否存在数据
     * @param formMain 表单主表对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public int queryFormMainCount(FormMain formMain) {
        DbFormMainExample dbFormMainExample = new DbFormMainExample();
        DbFormMainExample.Criteria criteria = dbFormMainExample.createCriteria();
        if(null!=formMain){
            if(StrUtil.isNotEmpty(formMain.getFormName())){
                String value =formMain.getFormName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andFormNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMain.getFormCode())){
                criteria.andFormCodeEqualTo(formMain.getFormCode().trim());
            }
            if(null != formMain.getFormStatus()){
                criteria.andFormStatusEqualTo(formMain.getFormStatus());
            }
            if(null != formMain.getObjectOid()){
                criteria.andObjectOidEqualTo(formMain.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formMain.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMain.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMain.getModuleOid())){
                criteria.andModuleOidEqualTo(formMain.getModuleOid());
            }
            if(null != formMain.getIsDelete()){
                criteria.andIsDeleteEqualTo(formMain.getIsDelete());
            }
            if(null != formMain.getIsAble()){
                criteria.andIsAbleEqualTo(formMain.getIsAble());
            }
        }
        int count = dbFormMainMapper.countByExample(dbFormMainExample);
        return count;
    }

    /**
     * @description: 根据表单编码集合合并表单成新的表单
     * @param formCodes 表单编码集合，多个用逗号分割
     * @param saveDataType 数据库存储类型  0本地  1API  2数据库
     * @param formConfigJson 合并规则json
     * @param formCode 合并后的表单code
     * @author: wuxx
     * @Date: 2021/8/2 9:30
     **/
    public FormMainVo mergeFormByFormCodes(String formCodes,String objectOid,Integer saveDataType,String formConfigJson,String formCode,Boolean includeChild) {
        String[] splitFormCodes = formCodes.split(",");
        //只有一个可直接使用表单
        if(splitFormCodes.length > 0){
            //多个需要判断是否存在
            //第一个表单设计
            FormMain firstFormMain = null;
            //合并的表单对象
            List<FormMain> formMainList  = new ArrayList<>();
            for (int i=0;i<splitFormCodes.length;i++){
                FormMain mainCode = this.getFormMainByFormMainCode(splitFormCodes[i]);
                if(null==mainCode){
                    throw new ResultInfoException("表单编码"+splitFormCodes[i]+",表单设计对象不存在！");
                }
                if(2 != mainCode.getFormStatus()){
                    throw new ResultInfoException("表单编码"+splitFormCodes[i]+",表单设计对象处于未发布状态！");
                }
                if(i == 0){
                    firstFormMain = mainCode;
                }
                formMainList.add(mainCode);

                FormObject formObject = formObjectManager.getFormObjectByObjectOid(mainCode.getObjectOid());
                if(null==formObject){
                    throw new ResultInfoException("表单编码"+splitFormCodes[i]+",存储对象不存在！");
                }
            }
            if(null!=firstFormMain){
                FormObject newFormObject = formObjectManager.getFormObjectByObjectOid(objectOid);
                if(null == newFormObject){
                    throw new ResultInfoException("参数"+objectOid+",不正确！");
                }
                if(null == saveDataType && StrUtil.isNotEmpty(newFormObject.getDatasourceOid())){
                    saveDataType = 2;
                }else if(null == saveDataType){
                    saveDataType = 0;
                }
                FormMainVo formMainVo = this.createMergeFormMain(formMainList, newFormObject, null == saveDataType ? 0 : saveDataType, formConfigJson,formCode,includeChild);
                return formMainVo;
            }else {
                throw new ResultInfoException("参数"+formCodes+"合并表单对象异常！");
            }
        }
        return null;
    }

    /**
     * @description: 根据表单编码集合合并表单成新的表单
     * @param formMainOids 表单业务主键集合，多个用逗号分割
     * @param objectOid 新存储对象的业务主键
     * @param saveDataType 数据库存储类型  0本地  1API  2数据库
     * @param formConfigJson 合并规则json
     * @param formMainOid 合并后的表单formMainOid
     * @author: wuxx
     * @Date: 2021/8/2 9:30
     **/
    public FormMainVo mergeFormByFormMainOids(String formMainOids,String objectOid,Integer saveDataType,String formConfigJson,String formMainOid,Boolean includeChild) {
        if(StrUtil.isBlank(formMainOids)){
            throw new ResultInfoException("参数formMainOids不能为空！");
        }
        String[] splitFormOidss = formMainOids.split(",");
        //只有一个可直接使用表单
        if(splitFormOidss.length > 0){
            //多个需要判断是否存在
            //第一个表单设计
            FormMain firstFormMain = null;
            //合并的表单对象
            List<FormMain> formMainList  = new ArrayList<>();
            for (int i=0;i<splitFormOidss.length;i++){
                FormMain main = this.getFormMainByFormMainOid(splitFormOidss[i]);
                if(null==main){
                    throw new ResultInfoException("参数"+splitFormOidss[i]+",表单设计对象不存在！");
                }
                if(2 != main.getFormStatus()){
                    throw new ResultInfoException("参数"+splitFormOidss[i]+",表单设计对象处于未发布状态！");
                }
                if(i == 0){
                    firstFormMain = main;
                }
                formMainList.add(main);

                FormObject formObject = formObjectManager.getFormObjectByObjectOid(main.getObjectOid());
                if(null==formObject){
                    throw new ResultInfoException("参数"+splitFormOidss[i]+",存储对象不存在！");
                }
            }
            if(null!=firstFormMain){
                FormObject newFormObject = formObjectManager.getFormObjectByObjectOid(objectOid);
                if(null == saveDataType && StrUtil.isNotEmpty(newFormObject.getDatasourceOid())){
                    saveDataType = 2;
                }else if(null == saveDataType){
                    saveDataType = 0;
                }
                FormMainVo formMainVo = this.createMergeFormMain(formMainList, newFormObject, null == saveDataType ? 0 : saveDataType, formConfigJson,formMainOid, includeChild);
                return formMainVo;
            }else {
                throw new ResultInfoException("参数"+formMainOids+"合并表单对象异常！");
            }
        }
        return null;
    }

    /**
     * @description: 合并创建表单
     * @param formMainList 需要合并的集合
     * @param formObject 存储对象
     * @param saveDataType 数据库存储类型  0本地  1API  2数据库
     * @param formConfigJson 合并规则json
     * @author: wuxx
     * @Date: 2021/8/2 13:25
     **/
    private FormMainVo createMergeFormMain(List<FormMain> formMainList,FormObject formObject,int saveDataType,String formConfigJson,String formMainOidOrCode,Boolean includeChild){
        List<FormDesign> formDesignList = new ArrayList<>();
        for(FormMain formMain:formMainList){
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(formMain.getFormMainOid());
            if(null!=design){
               formDesignList.add(design);
            }
        }
        List<JSONObject> configJsonList = new ArrayList<>();
        Map<String, String> replaceMap = new HashMap<>();
        for(FormDesign formDesign : formDesignList){
            String formConfig = formDesign.getFormConfig();
            if(StrUtil.isNotEmpty(formConfig)){
                JSONObject jsonObject = null;
                try {
                    //去除正则
                    formConfig = FormUtil.subJsonStr(formConfig, "new RegExp", "),", replaceMap);
                    //去除方法
                    formConfig = FormUtil.subJsonStr(formConfig, "function", "},", replaceMap);
                    /*for(Map.Entry<String, String> entry: jsonMap.entrySet()){
                        System.out.printf(entry.getKey() +"======="+entry.getValue());
                    }*/
                    jsonObject = JSONUtil.parseObj(formConfig,true,true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(null == jsonObject){
                    continue;
                }
                JSONObject formDescJSONObject = jsonObject.getJSONObject("formDesc");
                configJsonList.add(formDescJSONObject);
            }
        }
        String formConfigStr = null;
        if(null==includeChild || false == includeChild.booleanValue()){
            formConfigStr = FormUtil.createMergeFormConfig(configJsonList, formConfigJson, replaceMap);
        }else {
            formConfigStr = FormUtil.createMergeFormConfigByChildForms(configJsonList, formConfigJson, replaceMap);
        }

        if(null == formConfigStr){
            throw new ResultInfoException("参数formConfigJson不符合规范！");
        }
        //可能存在修改的情况
        FormMain formMain = null;
        if(StrUtil.isNotEmpty(formMainOidOrCode)){
            formMain = this.getFormMainByFormMainOid(formMainOidOrCode);
            if(null == formMain){
                formMain = this.getFormMainByFormMainCode(formMainOidOrCode);
            }
        }
        formConfigStr = formConfigStr.replaceAll("\"undefined\"","undefined");
        //System.err.println(formConfigStr);
        if(null==formMain){
            FormMain newFormMain = new FormMain();
            newFormMain.setAuthorizeKey(formObject.getAuthorizeKey());
            newFormMain.setModuleOid(formObject.getModuleOid());
            String data = DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
            newFormMain.setFormCode("FORM" + data);
            newFormMain.setFormStatus(0);
            newFormMain.setFormName("合"+ data);
            newFormMain.setObjectOid(formObject.getObjectOid());
            newFormMain.setSaveDataType(saveDataType);
            this.saveFormMain(newFormMain);
            FormDesign newFormDesign = new FormDesign();
            newFormDesign.setFormMainOid(newFormMain.getFormMainOid());
            newFormDesign.setModuleOid(formObject.getModuleOid());
            newFormDesign.setSaveDataType(saveDataType);
            newFormDesign.setFormConfig(formConfigStr);
            newFormDesign.setDesignName("合"+ data);
            formDesignManager.saveFormDesign(newFormDesign);
            FormMainVo formMainVo = new FormMainVo();
            BeanUtils.copyProperties(newFormMain,formMainVo);
            formMainVo.setDesignOid(newFormDesign.getDesignOid());
            return formMainVo;
        }else {
            FormDesign design = formDesignManager.getFormDesignByFormMainOid(formMain.getFormMainOid());
            design.setFormConfig(formConfigStr);
            formDesignManager.updateFormDesign(design);
            FormMainVo formMainVo = new FormMainVo();
            BeanUtils.copyProperties(formMain,formMainVo);
            formMainVo.setDesignOid(design.getDesignOid());
            return formMainVo;
        }
        //return null;
    }

    /**
     * @description: 创建存储对象
     * @param columnKeyMap 存储对象的字段
     * @param moduleOid 模块id
     * @param authorizeKey 授权ke
     * @author: wuxx
     * @Date: 2021/8/2 13:04
     **/
    private FormObject createFormObjectByMap(Map<String,String> columnKeyMap,String moduleOid,String authorizeKey){
        FormObject formObject = new FormObject();
        String data = DateUtil.format(new Date(), "yyyyMMdd") + FormUtil.generateShortUuid();
        formObject.setObjectName("合并"+data);
        formObject.setModuleOid(moduleOid);
        formObject.setAuthorizeKey(authorizeKey);
        //编码更改为自动生成
        String code = "OBJECT" + data;
        formObject.setObjectCode(code);
        formObjectManager.saveFormObject(formObject);
        for(Map.Entry<String, String> entry: columnKeyMap.entrySet()){
            FormColumn column = new FormColumn();
            column.setObjectProperty(entry.getKey());
            column.setDemo(entry.getValue());
            column.setObjectOid(formObject.getObjectOid());
            formColumnManager.saveFormColumn(column);
        }
        return formObject;
    }
}
