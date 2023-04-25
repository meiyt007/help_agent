package com.zfsoft.microservice.settings.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.microservice.settings.dbaccess.dao.DbSysDictMapper;
import com.zfsoft.microservice.settings.dbaccess.data.DbSysDict;
import com.zfsoft.microservice.settings.dbaccess.data.DbSysDictExample;
import com.zfsoft.microservice.settings.service.EvictSettingService;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.util.Assert;
import com.zfsoft.platform.utils.util.OptionalUtils;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SysDictServiceImpl
 * @Description: 数据字典接口实现类
 * @Author wuxx
 * @Date 2020/9/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "settings:dict")
public class SysDictManager {

    @Resource
    private DbSysDictMapper dbSysDictMapper;

    @Resource
    private EvictSettingService evictSettingService;


    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public int saveSysDict(@ValidGroups(groups = {SysDict.INSERT_GROUP.class})SysDict sysDict) {
        if (sysDict == null) {
            throw new ResultInfoException("数据字典信息不正确");
        }
        DbSysDict topDict = dbSysDictMapper.selectTopDbSysDict();
        if (("").equals(sysDict.getParentOid()) && null !=topDict && null==sysDict.getId()) {
            throw new ResultInfoException("一级数据字典只能有一个");
        }
        String message = this.checkIsExistesDictCode(sysDict.getId(), sysDict.getCode());
        if(StrUtil.isNotEmpty(message)){
            throw new ResultInfoException(message);
        }
        if (null == sysDict.getId()) {
            sysDict.setId(null);
            sysDict.setDictOid(IdUtil.simpleUUID());
            //当前登录人信息
            sysDict.setCreateDate(new Date());
        } else {
            // 字典oid不为空
            SysDict curDict = getSysDictById(sysDict.getId());
            if (curDict == null) {
                throw new ResultInfoException("字典信息编号未查询到相应的字典信息!");
            }
            sysDict.setCreateDate(curDict.getCreateDate());
            sysDict.setCreateUserOid(curDict.getCreateUserOid());
        }
        if(null!=sysDict.getParentOid()){
            SysDict parentDict = this.getSysDictByDictOid(sysDict.getParentOid());
            // 查询上一级字典
            if (parentDict == null) {
                sysDict.setPath("-1.");
                sysDict.setParentOid(null);
            } else {
                if(parentDict.getIsAble() == BaseStaticParameter.N) {
                    throw new ResultInfoException("上级字典处于禁用状态，暂不能操作！");
                }
                String path = parentDict.getPath()+ parentDict.getDictOid()+ ".";
                sysDict.setPath(path);
            }
        }else {
            sysDict.setPath("-1.");
            sysDict.setParentOid(null);
        }

        // 设置区划信息的状态
        if (sysDict.getIsDelete() == null) {
            sysDict.setIsDelete(BaseStaticParameter.N);
        }
        if (sysDict.getIsAble() == null) {
            sysDict.setIsAble(BaseStaticParameter.Y);
        }
        DbSysDict dbSysDict = new DbSysDict();
        BeanUtils.copyProperties(sysDict,dbSysDict);
        dbSysDict.setModifyDate(new Date());
        int index=0;
        if (null == sysDict.getId()) {
            index = dbSysDictMapper.insert(dbSysDict);
        }else {
            index = dbSysDictMapper.updateByPrimaryKeySelective(dbSysDict);
        }
        sysDict.setId(dbSysDict.getId());
        if(index==0){
            throw new ResultInfoException("数据字典删除失败，请稍后再试！");
        }
        //清除缓存
        evictSettingService.evictSysDict(dbSysDict.getId(),dbSysDict.getCode(),dbSysDict.getParentOid());
        evictSettingService.evictSysDict(dbSysDict.getDictOid(),dbSysDict.getCode(),dbSysDict.getParentOid());
        return index;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public int deleteSysDictById(Long id,String dictOid) {
        List<SysDict> sysDictList = this.querySysDictListByParentOid(dictOid);
        if(null!=sysDictList && sysDictList.size()>0){
            throw new ResultInfoException("当前数据字典存在下级目录，无法删除！");
        }
        int index = dbSysDictMapper.deleteByOid(id);
        if(index==0){
            throw new ResultInfoException("数据字典删除失败，请稍后再试！");
        }
        DbSysDict dbSysDict = dbSysDictMapper.selectByPrimaryKey(id);
        //清除缓存
        evictSettingService.evictSysDict(dbSysDict.getId(),dbSysDict.getCode(),dbSysDict.getParentOid());
        evictSettingService.evictSysDict(dbSysDict.getDictOid(),dbSysDict.getCode(),dbSysDict.getParentOid());
        return index;
    }

    /**
     * @description: 检测父类是否存在禁用的状态(true 存在、fasle不存在)
     * @param oid 对象
     * @author: wuxx
     * @Date: 2021/3/22 15:45
     **/
    private boolean checkParentIsAble(String oid){
        if(StrUtil.isBlank(oid)){
            return false;
        }
        SysDict sysDict = this.getSysDictByDictOid(oid);
        if(null==sysDict){
            return false;
        }
        if(null!=sysDict && BaseStaticParameter.N == sysDict.getIsAble()){
            return true;
        }
        this.checkParentIsAble(sysDict.getParentOid());
        return false;
    }

    @Transactional
    public int ableSysDictById(Long id) {
        DbSysDict dbSysDict = dbSysDictMapper.selectByPrimaryKey(id);
        if(dbSysDict == null)
            throw new ResultInfoException("数据字典信息为空！");
        Integer isAble = dbSysDict.getIsAble();
        if(BaseStaticParameter.N == isAble){
            if(this.checkParentIsAble(dbSysDict.getParentOid())){
                throw new ResultInfoException("启用失败，当前区划存在禁用的上级数据字典信息！");
            }
            dbSysDict.setIsAble(BaseStaticParameter.Y);
        }else {
            List<SysDict> sysDictList = this.querySysDictListByParentOid(dbSysDict.getDictOid());
            if(null!=sysDictList && sysDictList.size()>0){
                throw new ResultInfoException("当前数据字典存在下级目录，无法禁用！");
            }
            dbSysDict.setIsAble(BaseStaticParameter.N);
        }
        int index = dbSysDictMapper.updateByPrimaryKeySelective(dbSysDict);
        if(index==0){
            throw new ResultInfoException("数据字典启禁用失败，请稍后再试！");
        }
        //清除缓存
        evictSettingService.evictSysDict(dbSysDict.getId(),dbSysDict.getCode(),dbSysDict.getParentOid());
        evictSettingService.evictSysDict(dbSysDict.getDictOid(),dbSysDict.getCode(),dbSysDict.getParentOid());
        return index;
    }


    @Cacheable(key = "'getSysDictById:'+#id", unless = "#result == null")
    public SysDict getSysDictById(Long id) {
        DbSysDict dbSysDict = dbSysDictMapper.selectByPrimaryKey(id);
        if(dbSysDict == null)
            return null;
        SysDict sysDict = new SysDict();
        DbSysDict parentDbSysDict = dbSysDictMapper.getSysDictByDictOid(dbSysDict.getParentOid());
        if(parentDbSysDict != null){
            sysDict.setParentName(parentDbSysDict.getName());
        }
        BeanUtils.copyProperties(dbSysDict,sysDict);
        return sysDict;
    }

    @Cacheable(key = "'getSysDictByDictOid:'+#dictOid", unless = "#result == null")
    public SysDict getSysDictByDictOid(String dictOid) {
        DbSysDict dbSysDict = dbSysDictMapper.getSysDictByDictOid(dictOid);
        if(dbSysDict == null)
            return null;
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(dbSysDict,sysDict);
        return sysDict;
    }

    @Cacheable(key = "'getSysDictByCode:'+#code", unless = "#result == null")
    public SysDict getSysDictByCode(String code) {
        if(StrUtil.isEmpty(code)){
            return null;
        }
        DbSysDict dbSysDict = dbSysDictMapper.selectByDbSysDictByCode(code);
        if(dbSysDict == null)
            return null;
            //throw new ResultInfoException("数据字典信息为空！");
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(dbSysDict,sysDict);
        return sysDict;
    }


    public PageResult<SysDict> querySysDictWithPage(SysDict sysDict, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysDictExample dbSysDictExample = new DbSysDictExample();
        DbSysDictExample.Criteria criteria = dbSysDictExample.createCriteria();
        if(null!=sysDict){
            if(StrUtil.isNotEmpty(sysDict.getCode())){
                criteria.andCodeLike("%"+sysDict.getCode().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysDict.getName())){
                criteria.andNameLike("%"+sysDict.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysDict.getParentOid())){
                criteria.andParentOidEqualTo(sysDict.getParentOid().toString());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysDictExample.setOrderByClause("sort asc");
        Page<DbSysDict> dbSysDicts = (Page<DbSysDict>)dbSysDictMapper.selectByExample(dbSysDictExample);
        PageResult<SysDict> pageResult = new PageResult<>(dbSysDicts.getPageNum(),dbSysDicts.getPageSize(),dbSysDicts.getTotal());
        List<SysDict> sysDictList = dbSysDicts.stream().map(dbSysDict -> {
            SysDict dict = new SysDict();
            BeanUtils.copyProperties(dbSysDict,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(sysDictList);
        return pageResult;
    }

    @Cacheable(key = "'querySysDictListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysDict> querySysDictListByParentOid(String parentOid) {
        DbSysDictExample dbSysDictExample = new DbSysDictExample();
        DbSysDictExample.Criteria criteria = dbSysDictExample.createCriteria();
        if(null!= parentOid){
            criteria.andParentOidEqualTo(parentOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        dbSysDictExample.setOrderByClause("sort asc");
        List<DbSysDict> dbSysDicts = dbSysDictMapper.selectByExample(dbSysDictExample);
        List<SysDict> sysDicts = dbSysDicts.stream().map(sysDict -> {
            SysDict dict = new SysDict();
            BeanUtils.copyProperties(sysDict,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysDicts;
    }

    @Cacheable(key = "'querySysDictChildrenListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysDict> querySysDictChildrenListByParentOid(String parentOid) {
        DbSysDictExample dbSysDictExample = new DbSysDictExample();
        DbSysDictExample.Criteria criteria = dbSysDictExample.createCriteria();
        if(!("").equals(parentOid)){
            DbSysDict dbSysDict = dbSysDictMapper.getSysDictByDictOid(parentOid);
            criteria.andPathLike("%"+dbSysDict.getDictOid()+"%");
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        dbSysDictExample.setOrderByClause("sort asc");
        List<DbSysDict> dbSysDicts = dbSysDictMapper.selectByExample(dbSysDictExample);
        List<SysDict> sysDictList = dbSysDicts.stream().map(sysDict -> {
            SysDict dict = new SysDict();
            BeanUtils.copyProperties(sysDict,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysDictList;
    }

    @Cacheable(key = "'querySysDictListByDictOids:'+#dictOids", unless = "#result == null")
    public List<SysDict> querySysDictListByDictOids(List<String> dictOids) {
        DbSysDictExample dbSysDictExample = new DbSysDictExample();
        DbSysDictExample.Criteria criteria = dbSysDictExample.createCriteria();
        if(null!= dictOids){
            criteria.andDictOidIn(dictOids);
        }
        dbSysDictExample.setOrderByClause("sort asc");
        List<DbSysDict> dbSysDicts = dbSysDictMapper.selectByExample(dbSysDictExample);
        List<SysDict> sysDictList = dbSysDicts.stream().map(sysDict -> {
            SysDict dict = new SysDict();
            BeanUtils.copyProperties(sysDict,dict);
            return dict;
        }).collect(Collectors.toList());
        return sysDictList;
    }

    /**
     * 检查数据字典的code是否存在
     *
     * @return
     * @throws Exception
     */

    public String checkIsExistesDictCode(Long id, String code) {
        if(StrUtil.isEmpty(code)){
            throw new ResultInfoException("当前字典代码不能为空！");
        }
        DbSysDict sysDict = dbSysDictMapper.selectByDbSysDictByCode(code.trim());
        if (sysDict != null && (null==id ||sysDict.getId().longValue()!=id.longValue())) {
            throw new ResultInfoException("当前字典代码已经存在！");
        }
        return null;
    }

    public Map<String, Object> queryDictListByParentDictText(String[] parentDictCode) {
        return OptionalUtils.resolve(() -> Arrays.asList(parentDictCode)).orElseGet(Lists::newArrayList)
                .stream().map(dictCode -> {
                    List<SysDict> sysDictList = this.queryDictListByParentDictText(dictCode);
                    return new HashMap(){{put("key", dictCode);put("value", sysDictList.stream().map(item -> new HashMap(){{put("text", item.getName());put("code", item.getCode());}}));}};
                }).collect(Collectors.toMap(item -> item.get("key").toString(), item-> item.get("value")));
    }

    @Cacheable(key = "'queryDictListByParentDictText:'+#parentDictCode", unless = "#result == null")
    public List<SysDict> queryDictListByParentDictText(String parentDictCode) {
        Assert.hasLength(parentDictCode, "父级字典code不能为空！");
        DbSysDict dbSysDict = this.dbSysDictMapper.selectByDbSysDictByCode(parentDictCode);
        DbSysDictExample example = new DbSysDictExample();
        example.setOrderByClause("sort asc");
        example.createCriteria().andParentOidEqualTo(dbSysDict.getDictOid())
                .andIsDeleteEqualTo(BaseStaticParameter.N)
                .andIsAbleEqualTo(BaseStaticParameter.Y);
        return com.zfsoft.platform.utils.bean.BeanUtils.copyListProperties(this.dbSysDictMapper.selectByExample(example), SysDict::new);
    }
}
