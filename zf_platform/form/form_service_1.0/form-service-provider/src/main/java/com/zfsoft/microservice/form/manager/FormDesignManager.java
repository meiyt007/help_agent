package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.data.FormReportLog;
import com.zfsoft.microservice.form.data.FormStaticParameter;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormDesignMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormDesign;
import com.zfsoft.microservice.form.dbaccess.data.DbFormDesignExample;
import com.zfsoft.microservice.form.util.FormUtil;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FormDesignManager
 * @Description: 表单历史记录接口实现类
 * @Author wuxx
 * @Date 2021/4/16 10:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:design")
public class FormDesignManager {

    @Resource
    private DbFormDesignMapper dbFormDesignMapper;

    @Resource
    @Lazy
    private FormMainManager formMainManager;

    @Resource
    @Lazy
    private FormReportLogManager formReportLogManager;

    /**
     * @description: 分页查询表单历史记录的列表
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public PageResult<FormDesign> queryFormDesignWithPage(FormDesign formDesign, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormDesignExample dbFormDesignExample = new DbFormDesignExample();
        DbFormDesignExample.Criteria criteria = dbFormDesignExample.createCriteria();
        dbFormDesignExample.setOrderByClause("VERSION DESC ");
        if(null!=formDesign){
            if(StrUtil.isNotEmpty(formDesign.getDesignOid())){
                criteria.andDesignOidEqualTo(formDesign.getDesignOid().trim());
            }
            if(null != formDesign.getReleaseStatus()){
                criteria.andReleaseStatusEqualTo(formDesign.getReleaseStatus());
            }
            if(StrUtil.isNotEmpty(formDesign.getModuleOid())){
                criteria.andModuleOidEqualTo(formDesign.getModuleOid());
            }
            if(StrUtil.isNotEmpty(formDesign.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formDesign.getFormMainOid());
            }

        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        //只展示发布的
        criteria.andReleaseStatusEqualTo(2);
        Page<DbFormDesign> dbFormDesigns = (Page<DbFormDesign>)dbFormDesignMapper.selectByExample(dbFormDesignExample);
        PageResult<FormDesign> pageResult = new PageResult<>(dbFormDesigns.getPageNum(),dbFormDesigns.getPageSize(),dbFormDesigns.getTotal());
        List<FormDesign> formDesignList = dbFormDesigns.stream().map(dbFormDesign -> {
            FormDesign object = new FormDesign();
            BeanUtils.copyProperties(dbFormDesign,object);
            if(StrUtil.isNotBlank(object.getFormMainOid())){
                FormMain main = formMainManager.getFormMainByFormMainOid(object.getFormMainOid());
                object.setDesignName(null!=main?main.getFormName():null);
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formDesignList);
        return pageResult;
    }

    /**
     * @description:  保存表单历史记录信息
     * @param formDesign 表单历史记录对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormDesign(@ValidGroups(groups = {FormDesign.INSERT_GROUP.class}) FormDesign formDesign) {
        String formMainOid = formDesign.getFormMainOid();
        if(null == formDesign || StrUtil.isBlank(formMainOid)){
            throw new ResultInfoException("表单历史记录对象不能为空！");
        }
        if(StrUtil.isNotBlank(formDesign.getDesignOid())){
            FormDesign design = this.getFormDesignByDesignOid(formDesign.getDesignOid());
            if(null == design || null == design.getReleaseStatus() || FormStaticParameter.DATA_STATUS_2 == design.getReleaseStatus() ){
                formDesign.setId(null);
            }
        }
        if(null == formDesign.getReleaseStatus()
                || FormStaticParameter.DATA_STATUS_2 == formDesign.getReleaseStatus() ){
            formDesign.setId(null);
        }
        if (null == formDesign.getId()) {
            formDesign.setId(null);
            formDesign.setDesignOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormDesign curDict = this.getFormDesignById(formDesign.getId());
            if (curDict == null) {
                throw new ResultInfoException("表单历史记录对象未查询到相应的信息!");
            }
        }
        // 设置信息的状态
        formDesign.setCreateDate(new Date());
        formDesign.setIsDelete(BaseStaticParameter.N);
        //获取当前版本
        FormMain main = formMainManager.getFormMainByFormMainOid(formMainOid);
        int version = 1;
        if(null!=main){
            if(FormStaticParameter.DATA_STATUS_2 == main.getFormStatus()){
                version = null== main.getVersion()?1: main.getVersion()+1;
            }else {
                version = null== main.getVersion()?1: main.getVersion();
            }
        }
        formDesign.setSaveDataType(main.getSaveDataType());
        formDesign.setApiUrl(main.getApiUrl());
        formDesign.setVersion(version);
        formDesign.setReleaseStatus(FormStaticParameter.DATA_STATUS_1);
        DbFormDesign dbFormDesign = new DbFormDesign();
        BeanUtils.copyProperties(formDesign,dbFormDesign);
        int index=0;
        if (null == formDesign.getId()) {
            index = dbFormDesignMapper.insert(dbFormDesign);
        }else {
            index = dbFormDesignMapper.updateByPrimaryKeySelective(dbFormDesign);
        }
        formDesign.setId(dbFormDesign.getId());
        //修改主表的版本和表状态
        main.setFormStatus(FormStaticParameter.DATA_STATUS_1);
        main.setVersion(version);
        main.setModuleOid(formDesign.getModuleOid());
        main.setObjectOid(formDesign.getObjectOid());
        formMainManager.updateFormMain(main);
        return index;
    }

    /**
     * @description:  更新表单历史记录信息
     * @param formDesign 表单历史记录对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int updateFormDesign(FormDesign formDesign) {
        DbFormDesign dbFormDesign = new DbFormDesign();
        BeanUtils.copyProperties(formDesign,dbFormDesign);
        int index = dbFormDesignMapper.updateByPrimaryKeySelective(dbFormDesign);
        return index;
    }

    /**
     * @description:  复制保存表单历史记录信息
     * @param formDesign 表单历史记录对象
     * @author: wuxx
     * @Date: 2021/5/6 10:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int copySaveFormDesign(FormDesign formDesign) {
        DbFormDesign dbFormDesign = new DbFormDesign();
        BeanUtils.copyProperties(formDesign,dbFormDesign);
        int index = dbFormDesignMapper.insert(dbFormDesign);
        return index;
    }
    /**
     * @description:  删除表单历史记录信息
     * @param id 表单历史记录对象主键
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormDesignById(Long id) {
        DbFormDesign dbFormDesign = dbFormDesignMapper.selectByPrimaryKey(id);
        if(dbFormDesign == null)
            throw new ResultInfoException("表单历史记录信息为空！");
        FormReportLog formReport = new FormReportLog();
        formReport.setDesignOid(dbFormDesign.getDesignOid());
        List<FormReportLog> formReportList = formReportLogManager.queryFormReportLogList(formReport);
        if(null!=formReportList && formReportList.size()>0){
            throw new ResultInfoException("删除失败，表单设计信息存在填报数据！");
        }
        int index = dbFormDesignMapper.deleteByForeignKey(dbFormDesign.getDesignOid());
        if(index==0){
            throw new ResultInfoException("删除失败，未查询到记录！");
        }
        return index;
    }

    /**
     * @description:  删除表单历史记录信息
     * @param formMainOid 表单设计对象业务主键
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormDesignByFormMainOid(String formMainOid) {
        int index = dbFormDesignMapper.deleteAllByFormMainOid(formMainOid);
        return index;
    }

    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Cacheable(key = "'getFormDesignById:'+#id", unless = "#result == null")
    public FormDesign getFormDesignById(Long id) {
        DbFormDesign dbFormDesign = dbFormDesignMapper.selectByPrimaryKey(id);
        if(dbFormDesign == null)
            return null;
        FormDesign formDesign = new FormDesign();
        BeanUtils.copyProperties(dbFormDesign,formDesign);
        return formDesign;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param designOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    @Cacheable(key = "'getFormDesignByDesignOid:'+#designOid", unless = "#result == null")
    public FormDesign getFormDesignByDesignOid(String designOid) {
        DbFormDesign dbFormDesign = dbFormDesignMapper.selectByForeignKey(designOid);
        if(dbFormDesign == null)
            return null;
        FormDesign formDesign = new FormDesign();
        BeanUtils.copyProperties(dbFormDesign,formDesign);
        return formDesign;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param formMainOid 表单设计主表主键id
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public FormDesign getFormDesignByFormMainOid(String formMainOid) {
        DbFormDesignExample dbFormDesignExample = new DbFormDesignExample();
        dbFormDesignExample.setOrderByClause(" CREATE_DATE DESC");
        DbFormDesignExample.Criteria criteria = dbFormDesignExample.createCriteria();
        criteria.andFormMainOidEqualTo(formMainOid);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        DbFormDesign dbFormDesign = dbFormDesignMapper.selectTopByExampleWithBLOBs(dbFormDesignExample);
        if(null!=dbFormDesign){
            FormDesign formDesign = new FormDesign();
            BeanUtils.copyProperties(dbFormDesign,formDesign);
            return formDesign;
        }
        /*List<DbFormDesign> dbFormDesignList = dbFormDesignMapper.selectByExampleWithBLOBs(dbFormDesignExample);
        if(null!=dbFormDesignList && dbFormDesignList.size()>0){
            DbFormDesign dbFormDesign = dbFormDesignList.get(0);
            FormDesign formDesign = new FormDesign();
            BeanUtils.copyProperties(dbFormDesign,formDesign);
            return formDesign;
        }*/
        return null;
    }

    /**
     * @description:  获取表单设计对象的组件配置信息
     * @param formMainOid 表单主键oid
     * @param designOid 表单设计对象业务oid
     * @param components 设计的组件name 多个用逗号,隔开
     * @author: wuxx
     * @Date: 2021/5/6 13:07
     **/
    public String getFormDesignConfigList(String formMainOid, String designOid, String components) {
        if(StrUtil.isBlank(formMainOid) && StrUtil.isBlank(designOid)){
            throw new ResultInfoException("设计对象信息不能为空!");
        }
        FormDesign design = null;
        if(StrUtil.isNotBlank(designOid)){
            design = this.getFormDesignByDesignOid(designOid);
        }else {
            design = this.getFormDesignByFormMainOid(formMainOid);
        }
        if(null == design){
            throw new ResultInfoException("设计对象信息不能为空!");
        }
        String formConfig = design.getFormConfig();
        String configJson = FormUtil.getApiFormConfig(formConfig, components);
        return configJson;
    }

    /**
     * @description: 根据表单历史记录对象查询集合
     * @param formDesign 表单历史记录对象
     * @author: wuxx
     * @Date: 2021/4/16 10:00
     **/
    public List<FormDesign> queryFormDesignList(FormDesign formDesign) {
        DbFormDesignExample dbFormDesignExample = new DbFormDesignExample();
        DbFormDesignExample.Criteria criteria = dbFormDesignExample.createCriteria();
        if(null!=formDesign){
            if(StrUtil.isNotEmpty(formDesign.getDesignOid())){
                criteria.andDesignOidEqualTo(formDesign.getDesignOid().trim());
            }
            if(StrUtil.isNotEmpty(formDesign.getObjectOid())){
                criteria.andObjectOidEqualTo(formDesign.getObjectOid().trim());
            }
            if(null != formDesign.getReleaseStatus()){
                criteria.andReleaseStatusEqualTo(formDesign.getReleaseStatus());
            }
            if(StrUtil.isNotEmpty(formDesign.getModuleOid())){
                criteria.andModuleOidEqualTo(formDesign.getModuleOid());
            }
            if(StrUtil.isNotEmpty(formDesign.getFormMainOid())){
                criteria.andFormMainOidEqualTo(formDesign.getFormMainOid());
            }
            if(null != formDesign.getIsDelete()){
                criteria.andIsDeleteEqualTo(formDesign.getIsDelete());
            }
        }
        //criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormDesign> dbFormDesignList = dbFormDesignMapper.selectByExample(dbFormDesignExample);
        List<FormDesign> formDesignList = dbFormDesignList.stream().map(dbFormDesign -> {
            FormDesign object = new FormDesign();
            BeanUtils.copyProperties(dbFormDesign,object);
            return object;
        }).collect(Collectors.toList());
        return formDesignList;
    }

    /**
     * @description: 根据编码获取表单设计信息
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/30 13:29
     **/
    public JSONObject getDesignInfoByCode(String code,FormDesign formDesign) {
        if (formDesign == null) {
            throw new ResultInfoException("表单设计未查询到相应的信息!");
        }
        String formConfig = formDesign.getFormConfig();
        formConfig = FormUtil.changeToRightJsonStr(formConfig);
        try {
            if(StrUtil.isNotEmpty(formConfig)){
                JSONObject jsonObject = JSONUtil.parseObj(formConfig);
                JSONObject formDescJSONObject = jsonObject.getJSONObject("formDesc");
                JSONObject endJSONObject = FormUtil.getObjByCode(formDescJSONObject, code);
                //JSONObject valueJSONObject = this.getObjByKey(formDescJSONObject, code);
                //JSONObject valueInnerJSONObject = this.getInnerObjByKey(formDescJSONObject, code);
                //JSONObject endJSONObject = null!=valueJSONObject?valueJSONObject:valueInnerJSONObject;
                if(null!=endJSONObject){
                    JSONObject attrsObject = endJSONObject.getJSONObject("attrs");
                    Object multiple = null!=attrsObject?attrsObject.get("multiple"):null;
                    if(null!=multiple){
                        endJSONObject.set("multiple",multiple);
                    }else {
                        endJSONObject.set("multiple",false);
                    }
                    Object optionsObject = endJSONObject.get("options");
                    if(null==optionsObject ||  "undefined".equals(optionsObject.toString())){
                        endJSONObject.remove("options");
                    }
                    int aDefault = null!=endJSONObject.getStr("default")?endJSONObject.getStr("default").length():0;
                    if(0 == aDefault){
                        endJSONObject.remove("default");
                    }
                    endJSONObject.remove("layoutComponent");
                    endJSONObject.remove("attrs");
                    endJSONObject.remove("formItemList");
                    endJSONObject.remove("isOptions");
                    endJSONObject.remove("formConfig");
                    endJSONObject.remove("optionsLinkageFields");
                    endJSONObject.remove("parentsKey");
                    endJSONObject.remove("prop");
                }
                return endJSONObject;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("表单设计查询异常!");
        }
        return null;
    }

    /**
     * @description: 根据编码获取设计对象的信息
     * @param jSONObject formDescJSON对象
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/30 14:19
     **/
    public JSONObject getObjByKey(JSONObject jSONObject, String code){
        Set<String> keySets = jSONObject.keySet();
        for(String key : keySets){
            if(!key.equals(code)){
                JSONObject formItemList = jSONObject.getJSONObject(key).getJSONObject("formItemList");
                if(null!=formItemList && formItemList.size()>0){
                    this.getObjByKey(formItemList,code);
                }
            }else{
                return jSONObject.getJSONObject(key);
            }
        }
        return null;
    }

    /**
     * @description: 根据编码获取设计对象的信息--多层镶嵌
     * @param jSONObject formDescJSON对象
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/30 14:19
     **/
    public JSONObject getInnerObjByKey(JSONObject jSONObject, String code){
        Set<String> keySets = jSONObject.keySet();
        for(String key : keySets){
            if(!key.equals(code)){
                JSONObject formItemList = jSONObject.getJSONObject(key).getJSONObject("formItemList");
                if(null!=formItemList && formItemList.size()>0){
                   return this.getInnerObjByKey(formItemList,code);
                }
            }else{
                return jSONObject.getJSONObject(key);
            }
        }
        return null;
    }

}
