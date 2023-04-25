package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormAuthorize;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormAuthorizeMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormAuthorize;
import com.zfsoft.microservice.form.dbaccess.data.DbFormAuthorizeExample;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @ClassName FormAuthorizeServiceImpl
 * @Description: 接入系统授权接口实现类
 * @Author wuxx
 * @Date 2021/4/5
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:authorize")
public class FormAuthorizeManager {

    @Resource
    private DbFormAuthorizeMapper dbFormAuthorizeMapper;
    /**
     * @description:  保存授权信息
     * @param formAuthorize 授权对象
     * @author: wuxx
     * @Date: 2021/3/26
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormAuthorize(@ValidGroups(groups = {FormAuthorize.INSERT_GROUP.class})FormAuthorize formAuthorize) {
        if(null == formAuthorize){
            throw new ResultInfoException("授权对象不能为空！");
        }
        if (null == formAuthorize.getId()) {
            formAuthorize.setId(null);
            formAuthorize.setAuthorizeKey(IdUtil.simpleUUID());
            formAuthorize.setCreateDate(new Date());
        } else {
            // 字典oid不为空
            FormAuthorize curDict = this.getFormAuthorizeById(formAuthorize.getId());
            if (curDict == null) {
                throw new ResultInfoException("授权对象未查询到相应的信息!");
            }
        }
        // 设置区划信息的状态
        if (null == formAuthorize.getIsDelete()) {
            formAuthorize.setIsDelete(BaseStaticParameter.N);
        }
        if (null == formAuthorize.getIsAble()) {
            formAuthorize.setIsAble(BaseStaticParameter.Y);
        }
        DbFormAuthorize dbFormAuthorize = new DbFormAuthorize();
        BeanUtils.copyProperties(formAuthorize,dbFormAuthorize);
        int index=0;
        if (null == formAuthorize.getId()) {
            index = dbFormAuthorizeMapper.insert(dbFormAuthorize);
        }else {
            index = dbFormAuthorizeMapper.updateByPrimaryKeySelective(dbFormAuthorize);
        }
        formAuthorize.setId(dbFormAuthorize.getId());
        return index;
    }

    /**
     * @description:  删除授权信息
     * @param id 授权对象主键
     * @author: wuxx
     * @Date: 2021/3/11 9:18
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormAuthorizeById(Long id) {
        DbFormAuthorize dbFormAuthorize = dbFormAuthorizeMapper.selectByPrimaryKey(id);
        if(dbFormAuthorize == null)
            throw new ResultInfoException("授权对象信息为空！");
        int index = dbFormAuthorizeMapper.deleteByAuthorizeKey(dbFormAuthorize.getAuthorizeKey());
        if(index==0){
            throw new ResultInfoException("授权对象删除失败，请稍后再试！");
        }
        return index;
    }


    @Transactional
    @CacheEvict(allEntries = true)
    public int ableFormAuthorizeById(Long id) {
        DbFormAuthorize dbFormAuthorize = dbFormAuthorizeMapper.selectByPrimaryKey(id);
        if(dbFormAuthorize == null)
            throw new ResultInfoException("授权对象信息为空！");
        Integer isAble = dbFormAuthorize.getIsAble();
        if(BaseStaticParameter.N == isAble){
            dbFormAuthorize.setIsAble(BaseStaticParameter.Y);
        }else {
            dbFormAuthorize.setIsAble(BaseStaticParameter.N);
        }
        int index = dbFormAuthorizeMapper.updateByPrimaryKeySelective(dbFormAuthorize);
        if(index==0){
            throw new ResultInfoException("授权对象启禁用失败，请稍后再试！");
        }
        return index;
    }

    @Cacheable(key = "'getFormAuthorizeById:'+#id", unless = "#result == null")
    public FormAuthorize getFormAuthorizeById(Long id) {
        DbFormAuthorize dbFormAuthorize = dbFormAuthorizeMapper.selectByPrimaryKey(id);
        if(dbFormAuthorize == null)
            return null;
        FormAuthorize formAuthorize = new FormAuthorize();
        BeanUtils.copyProperties(dbFormAuthorize,formAuthorize);
        return formAuthorize;
    }

    @Cacheable(key = "'getFormAuthorizeByAuthorizeKey:'+#authorizeKey", unless = "#result == null")
    public FormAuthorize getFormAuthorizeByAuthorizeKey(String authorizeKey) {
        DbFormAuthorize dbFormAuthorize = dbFormAuthorizeMapper.selectByAuthorizeKey(authorizeKey);
        if(dbFormAuthorize == null)
            return null;
        FormAuthorize formAuthorize = new FormAuthorize();
        BeanUtils.copyProperties(dbFormAuthorize,formAuthorize);
        return formAuthorize;
    }


    public PageResult<FormAuthorize> queryFormAuthorizeWithPage(FormAuthorize formAuthorize, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormAuthorizeExample dbFormAuthorizeExample = new DbFormAuthorizeExample();
        DbFormAuthorizeExample.Criteria criteria = dbFormAuthorizeExample.createCriteria();
        if(null!=formAuthorize){
            if(StrUtil.isNotEmpty(formAuthorize.getSystemName())){
                String value =formAuthorize.getSystemName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                    .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andSystemNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formAuthorize.getAuthorizeKey())){
                //不为空 则为管理员 查看所有
            }else {
                //非管理员只可以查询已经授权的
                List<String> authorizeKeyList = formAuthorize.getAuthorizeKeyList();
                if(null!= authorizeKeyList&& authorizeKeyList.size()>0){
                    criteria.andAuthorizeKeyIn(authorizeKeyList);
                }else {
                    return null;
                }
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormAuthorize> dbFormAuthorizes = (Page<DbFormAuthorize>)dbFormAuthorizeMapper.selectByExample(dbFormAuthorizeExample);
        PageResult<FormAuthorize> pageResult = new PageResult<>(dbFormAuthorizes.getPageNum(),dbFormAuthorizes.getPageSize(),dbFormAuthorizes.getTotal());
        List<FormAuthorize> formAuthorizeList = dbFormAuthorizes.stream().map(dbFormAuthorize -> {
            FormAuthorize dict = new FormAuthorize();
            BeanUtils.copyProperties(dbFormAuthorize,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(formAuthorizeList);
        return pageResult;
    }

    /**
     * @description: 根据接入系统授权对象查询集合
     * @param formAuthorize 接入系统授权对象
     * @author: wuxx
     * @Date: 2021/3/11 10:57
     **/
    public List<FormAuthorize> queryFormAuthorizeList(FormAuthorize formAuthorize) {
        DbFormAuthorizeExample dbFormAuthorizeExample = new DbFormAuthorizeExample();
        DbFormAuthorizeExample.Criteria criteria = dbFormAuthorizeExample.createCriteria();
        if(null!=formAuthorize){
            if(StrUtil.isNotEmpty(formAuthorize.getSystemName())){
                criteria.andSystemNameLike("%"+formAuthorize.getSystemName().trim()+"%");
            }

            if(StrUtil.isNotEmpty(formAuthorize.getAuthorizeKey())){
                //不为空 则为管理员 查看所有
            }else {
                //非管理员只可以查询已经授权的
                List<String> authorizeKeyList = formAuthorize.getAuthorizeKeyList();
                if(null!= authorizeKeyList&& authorizeKeyList.size()>0){
                    criteria.andAuthorizeKeyIn(authorizeKeyList);
                }else {
                    return null;
                }
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbFormAuthorize> dbFormAuthorizeList = dbFormAuthorizeMapper.selectByExample(dbFormAuthorizeExample);
        List<FormAuthorize> formAuthorizeList = dbFormAuthorizeList.stream().map(dbFormAuthorize -> {
            FormAuthorize object = new FormAuthorize();
            BeanUtils.copyProperties(dbFormAuthorize,object);
            return object;
        }).collect(Collectors.toList());
        return formAuthorizeList;
    }

}
