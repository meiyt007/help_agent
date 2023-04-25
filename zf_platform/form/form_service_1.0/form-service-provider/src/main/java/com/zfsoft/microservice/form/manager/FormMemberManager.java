package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormMember;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormMemberMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormMember;
import com.zfsoft.microservice.form.dbaccess.data.DbFormMemberExample;
import com.zfsoft.microservice.form.feign.SysUserFeignService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
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
 * @ClassName FormMemberManager
 * @Description: 成员管理接口实现类
 * @Author wuxx
 * @Date 2020/04/8
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:member")
public class FormMemberManager {

    @Resource
    private DbFormMemberMapper dbFormMemberMapper;

    @Resource
    private SysUserFeignService sysUserFeignService;

    /**
     * @description:  保存成员信息
     * @param formMember 成员对象
     * @author: wuxx
     * @Date: 2021/4/8
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormMember(@ValidGroups(groups = {FormMember.INSERT_GROUP.class})FormMember formMember) {
        if(null == formMember){
            throw new ResultInfoException("成员对象不能为空！");
        }
        if (null == formMember.getId()) {
            formMember.setId(null);
            formMember.setMemberOid(IdUtil.simpleUUID());
            formMember.setCreateDate(new Date());
        } else {
            // oid不为空
            FormMember cur = this.getFormMemberById(formMember.getId());
            if (cur == null) {
                throw new ResultInfoException("成员对象未查询到相应的信息!");
            }
        }
        // 设置区划信息的状态
        if (null == formMember.getIsDelete()) {
            formMember.setIsDelete(BaseStaticParameter.N);
        }
        DbFormMember dbFormMember = new DbFormMember();
        BeanUtils.copyProperties(formMember,dbFormMember);
        //处理用户
        if(null!=formMember.getUserOids()){
            for (String userOid : formMember.getUserOids()){
                DbFormMember newFormMember = dbFormMember;
                newFormMember.setId(null);
                FormMember queryMember = new FormMember();
                queryMember.setAuthorizeKey(formMember.getAuthorizeKey());
                queryMember.setUserOid(userOid);
                List<FormMember> formMembers = this.queryFormMemberList(queryMember);
                if(null!=formMembers && formMembers.size()>0){
                    continue;
                }
                //获取用户
                SysUser sysUser = sysUserFeignService.getSysUserByUserOid(userOid).getData();
                newFormMember.setUserName(null!=sysUser?sysUser.getName():"");
                newFormMember.setUserOid(userOid);
                newFormMember.setMemberOid(IdUtil.simpleUUID());
                //插入新的数据
                dbFormMemberMapper.insert(newFormMember);
            }
        }
        return 1;
    }

    /**
     * @description:  删除成员信息
     * @param id 成员对象主键
     * @author: wuxx
     * @Date: 2021/3/11 9:18
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteFormMemberById(Long id) {
        DbFormMember dbFormMember = dbFormMemberMapper.selectByPrimaryKey(id);
        if(dbFormMember == null)
            throw new ResultInfoException("成员对象信息为空！");
        int index = dbFormMemberMapper.deleteByMemberOid(dbFormMember.getMemberOid());
        if(index==0){
            throw new ResultInfoException("成员对象删除失败，请稍后再试！");
        }
        return index;
    }


    @Cacheable(key = "'getFormMemberById:'+#id", unless = "#result == null")
    public FormMember getFormMemberById(Long id) {
        DbFormMember dbFormMember = dbFormMemberMapper.selectByPrimaryKey(id);
        if(dbFormMember == null)
            return null;
        FormMember formMember = new FormMember();
        BeanUtils.copyProperties(dbFormMember,formMember);
        return formMember;
    }

    @Cacheable(key = "'getFormMemberByMemberOid:'+#memberOid", unless = "#result == null")
    public FormMember getFormMemberByMemberOid(String memberOid) {
        DbFormMember dbFormMember = dbFormMemberMapper.selectByMemberOid(memberOid);
        if(dbFormMember == null)
            return null;
        FormMember formMember = new FormMember();
        BeanUtils.copyProperties(dbFormMember,formMember);
        return formMember;
    }


    public PageResult<FormMember> queryFormMemberWithPage(FormMember formMember, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormMemberExample dbFormMemberExample = new DbFormMemberExample();
        DbFormMemberExample.Criteria criteria = dbFormMemberExample.createCriteria();
        dbFormMemberExample.setOrderByClause(" ID DESC ");
        if(null!=formMember){
            if(StrUtil.isNotEmpty(formMember.getUserName())){
                String value = formMember.getUserName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andUserNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMember.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMember.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMember.getUserOid())){
                criteria.andUserOidEqualTo(formMember.getUserOid());
            }
            if(StrUtil.isNotEmpty(formMember.getMemberOid())){
                criteria.andMemberOidEqualTo(formMember.getMemberOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbFormMember> dbFormMembers = (Page<DbFormMember>)dbFormMemberMapper.selectByExample(dbFormMemberExample);
        PageResult<FormMember> pageResult = new PageResult<>(dbFormMembers.getPageNum(),dbFormMembers.getPageSize(),dbFormMembers.getTotal());
        List<FormMember> formMemberList = dbFormMembers.stream().map(dbFormMember -> {
            FormMember dict = new FormMember();
            BeanUtils.copyProperties(dbFormMember,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(formMemberList);
        return pageResult;
    }

    /**
     * @description: 根据成员管理对象查询集合
     * @param formMember 成员管理对象
     * @author: wuxx
     * @Date: 2021/3/11 10:57
     **/
    public List<FormMember> queryFormMemberList(FormMember formMember) {
        DbFormMemberExample dbFormMemberExample = new DbFormMemberExample();
        DbFormMemberExample.Criteria criteria = dbFormMemberExample.createCriteria();
        dbFormMemberExample.setOrderByClause(" ID DESC ");
        if(null!=formMember){
            if(StrUtil.isNotEmpty(formMember.getUserName())){
                String value = formMember.getUserName().trim();
                value = value.replaceAll(Matcher.quoteReplacement("%"), Matcher.quoteReplacement("\\%"))
                        .replaceAll(Matcher.quoteReplacement("_"), Matcher.quoteReplacement("\\_"));
                criteria.andUserNameLike("%"+value+"%");
            }
            if(StrUtil.isNotEmpty(formMember.getAuthorizeKey())){
                criteria.andAuthorizeKeyEqualTo(formMember.getAuthorizeKey());
            }
            if(StrUtil.isNotEmpty(formMember.getUserOid())){
                criteria.andUserOidEqualTo(formMember.getUserOid());
            }
            if(StrUtil.isNotEmpty(formMember.getMemberOid())){
                criteria.andMemberOidEqualTo(formMember.getMemberOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormMember> dbFormMemberList = dbFormMemberMapper.selectByExample(dbFormMemberExample);
        List<FormMember> formMemberList = dbFormMemberList.stream().map(dbFormMember -> {
            FormMember object = new FormMember();
            BeanUtils.copyProperties(dbFormMember,object);
            return object;
        }).collect(Collectors.toList());
        return formMemberList;
    }


    /**
     * @description: 根据用户oid获取授权可以的集合
     * @param userOid 用户oid
     * @author: wuxx
     * @Date: 2021/4/9 13:24
     **/
    public List<String> queryAuthorizeKeyListByUserOid(String userOid) {
        DbFormMemberExample dbFormMemberExample = new DbFormMemberExample();
        DbFormMemberExample.Criteria criteria = dbFormMemberExample.createCriteria();
        dbFormMemberExample.setOrderByClause(" ID DESC ");
        if(StrUtil.isNotBlank(userOid)){
            criteria.andUserOidEqualTo(userOid);
        }else {
            return null;
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbFormMember> dbFormMemberList = dbFormMemberMapper.selectByExample(dbFormMemberExample);
        List<String> authorizeKeyList = dbFormMemberList.stream().map(DbFormMember::getAuthorizeKey).collect(Collectors.toList());
        return authorizeKeyList;
    }
}
