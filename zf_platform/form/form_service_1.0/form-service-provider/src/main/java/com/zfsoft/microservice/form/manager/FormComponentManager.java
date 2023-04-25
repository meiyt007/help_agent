package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormComponent;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormComponentMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormComponent;
import com.zfsoft.microservice.form.dbaccess.data.DbFormComponentExample;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FormComponentManager
 * @Description: 组件管理接口实现类
 * @Author wuxx
 * @Date 2021/4/12
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:component")
public class FormComponentManager {

    @Resource
    private DbFormComponentMapper dbFormComponentMapper;

    /**
     * @description:  修改子组件
     * @author: wuxx
     * @Date: 2021/4/12 14:57
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormComponent(@ValidGroups(groups = {FormComponent.INSERT_GROUP.class})FormComponent formComponent) {
        if(null==formComponent || StrUtil.isBlank(formComponent.getAuthorizeKey())){
            throw new ResultInfoException("授权KEY不能为空！");
        }
        //先删除所有
        DbFormComponentExample dbFormComponentExample = new DbFormComponentExample();
        DbFormComponentExample.Criteria criteria = dbFormComponentExample.createCriteria();
        criteria.andAuthorizeKeyEqualTo(formComponent.getAuthorizeKey());
        criteria.andComponentCodeEqualTo(formComponent.getComponentCode());
        dbFormComponentMapper.deleteByExample(dbFormComponentExample);
        if(StrUtil.isNotBlank(formComponent.getComponentConfig())){
            if (null == formComponent.getId()) {
                formComponent.setId(null);
                formComponent.setComponentOid(IdUtil.simpleUUID());
                formComponent.setCreateDate(new Date());
            }
            DbFormComponent dbFormComponent = new DbFormComponent();
            BeanUtils.copyProperties(formComponent,dbFormComponent);
            dbFormComponentMapper.insert(dbFormComponent);
        }
        return 1;
    }

    /**
     * @description:  删除授权信息
     * @param id 组件信息主键
     * @author: wuxx
     * @Date: 2021/3/11 9:18
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormComponentById(Long id) {
        int index = dbFormComponentMapper.deleteByPrimaryKey(id);
        return index;
    }


    @Cacheable(key = "'getFormComponentById:'+#id", unless = "#result == null")
    public FormComponent getFormComponentById(Long id) {
        DbFormComponent dbFormComponent = dbFormComponentMapper.selectByPrimaryKey(id);
        if(dbFormComponent == null)
            return null;
        FormComponent formComponent = new FormComponent();
        BeanUtils.copyProperties(dbFormComponent,formComponent);
        return formComponent;
    }

    @Cacheable(key = "'getFormComponentByComponentOid:'+#componentOid", unless = "#result == null")
    public FormComponent getFormComponentByComponentOid(String componentOid) {
        DbFormComponent dbFormComponent = dbFormComponentMapper.selectByComponentOid(componentOid);
        if(dbFormComponent == null)
            return null;
        FormComponent formComponent = new FormComponent();
        BeanUtils.copyProperties(dbFormComponent,formComponent);
        return formComponent;
    }
    /**
     * @description: 根据授权key获取组件列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/4/26 15:07
     **/
    @Cacheable(key = "'getFormComponentListByAuthorizeKey:'+#authorizeKey", unless = "#result == null")
    public List<FormComponent> getFormComponentListByAuthorizeKey(String authorizeKey) {
        DbFormComponentExample dbFormComponentExample = new DbFormComponentExample();
        DbFormComponentExample.Criteria criteria = dbFormComponentExample.createCriteria();
        dbFormComponentExample.setOrderByClause("ID DESC");
        criteria.andAuthorizeKeyEqualTo(authorizeKey);
        List<DbFormComponent> dbFormComponentList = dbFormComponentMapper.selectByExampleWithBLOBs(dbFormComponentExample);
        List<FormComponent> formComponentList = dbFormComponentList.stream().map(dbFormComponent -> {
            FormComponent object = new FormComponent();
            BeanUtils.copyProperties(dbFormComponent,object);
            return object;
        }).collect(Collectors.toList());
        return formComponentList;
    }


    /**
     * @description:  根据授权key查询设计器中的授权组件
     * @param authorizeKey 授权key
     * @param componentCode 组件类型
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    public FormComponent getFormComponent(String authorizeKey,String componentCode) {
        DbFormComponentExample dbFormComponentExample = new DbFormComponentExample();
        DbFormComponentExample.Criteria criteria = dbFormComponentExample.createCriteria();
        dbFormComponentExample.setOrderByClause("ID DESC");
        criteria.andAuthorizeKeyEqualTo(authorizeKey);
        criteria.andComponentCodeEqualTo(componentCode);
        List<DbFormComponent> dbFormComponentList = dbFormComponentMapper.selectByExampleWithBLOBs(dbFormComponentExample);
        if(null!=dbFormComponentList && dbFormComponentList.size()>0){
            FormComponent formComponent = new FormComponent();
            BeanUtils.copyProperties(dbFormComponentList.get(0),formComponent);
            return formComponent;
        }
        return null;
    }
}
