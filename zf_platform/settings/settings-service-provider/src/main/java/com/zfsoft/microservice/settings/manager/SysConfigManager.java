package com.zfsoft.microservice.settings.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.dbaccess.dao.DbSysConfigMapper;
import com.zfsoft.microservice.settings.dbaccess.data.DbSysConfig;
import com.zfsoft.microservice.settings.dbaccess.data.DbSysConfigExample;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysConfigServiceImpl
 * @Description: 参数配置接口实现类
 * @Author wuxx
 * @Date 2020/9/1
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "settings:config")
public class SysConfigManager {

    @Resource
    private DbSysConfigMapper dbSysConfigMapper;

    @Resource
    private EvictSettingManager evictSettingManager;


    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysConfig(@ValidGroups(groups = {SysConfig.INSERT_GROUP.class})SysConfig sysConfig) {
        DbSysConfig topSysConfig = dbSysConfigMapper.selectTopDbSysConfig();
        if (null == sysConfig.getParentOid() && null!=topSysConfig && null==sysConfig.getId()) {
            throw new ResultInfoException("一级系统参数配置只能有一个");
        }
        String message = this.checkIsExistesConfigCode(sysConfig.getId(), sysConfig.getCode());
        if(StrUtil.isNotEmpty(message)){
            throw new ResultInfoException(message);
        }
        if (null == sysConfig.getId()) {
            sysConfig.setId(null);
            sysConfig.setConfigOid(IdUtil.simpleUUID());
        } else {
            // 字典oid不为空
            SysConfig curDict = getSysConfigById(sysConfig.getId());
            if (curDict == null) {
                throw new ResultInfoException("字典信息编号未查询到相应的字典信息!");
            }
        }
        if(null!=sysConfig.getParentOid()){
            SysConfig parentDict = getSysConfigByConfigOid(sysConfig.getParentOid());
            // 查询上一级字典
            if (parentDict == null) {
                sysConfig.setPath("-1.");
                sysConfig.setParentOid(null);
            } else {
                if(parentDict.getIsAble() == BaseStaticParameter.N) {
                    throw new ResultInfoException("上级配置处于禁用状态，暂不能操作！");
                }
                String path = parentDict.getPath()+ parentDict.getConfigOid() + ".";
                sysConfig.setPath(path);
            }
        }else {
            sysConfig.setPath("-1.");
            sysConfig.setParentOid(null);
        }

        // 设置区划信息的状态
        if (null == sysConfig.getIsDelete()) {
            sysConfig.setIsDelete(BaseStaticParameter.N);
        }
        if (null == sysConfig.getIsAble()) {
            sysConfig.setIsAble(BaseStaticParameter.Y);
        }
        DbSysConfig dbSysConfig = new DbSysConfig();
        BeanUtils.copyProperties(sysConfig,dbSysConfig);
        int index=0;
        if (null == sysConfig.getId()) {
            index = dbSysConfigMapper.insert(dbSysConfig);
        }else {
            index = dbSysConfigMapper.updateByPrimaryKeySelective(dbSysConfig);
        }
        sysConfig.setId(dbSysConfig.getId());
        //清除缓存
        evictSettingManager.evictSysConfig(dbSysConfig.getId(),dbSysConfig.getCode(),dbSysConfig.getParentOid());
        evictSettingManager.evictSysConfig(dbSysConfig.getConfigOid(),dbSysConfig.getCode(),dbSysConfig.getParentOid());
        return index;
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysConfigById(Long id) {
        DbSysConfig dbSysConfig = dbSysConfigMapper.selectByPrimaryKey(id);
        if(dbSysConfig == null)
            throw new ResultInfoException("参数配置信息为空！");
        List<SysConfig> sysConfigList = this.querySysConfigListByParentOid(dbSysConfig.getConfigOid());
        if(null!=sysConfigList && sysConfigList.size()>0){
            throw new ResultInfoException("当前参数存在下级目录，无法删除！");
        }
        int index = dbSysConfigMapper.deleteByOid(id);
        if(index==0){
            throw new ResultInfoException("参数删除失败，请稍后再试！");
        }
        //清除缓存
        evictSettingManager.evictSysConfig(dbSysConfig.getId(),dbSysConfig.getCode(),dbSysConfig.getParentOid());
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
        SysConfig sysConfig = this.getSysConfigByConfigOid(oid);
        if(null==sysConfig){
            return false;
        }
        if(null!=sysConfig && BaseStaticParameter.N == sysConfig.getIsAble()){
            return true;
        }
        this.checkParentIsAble(sysConfig.getParentOid());
        return false;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public int ableSysConfigById(Long id) {
        DbSysConfig dbSysConfig = dbSysConfigMapper.selectByPrimaryKey(id);
        if(dbSysConfig == null)
            throw new ResultInfoException("参数配置信息为空！");
        Integer isAble = dbSysConfig.getIsAble();
        if(BaseStaticParameter.N == isAble){
            if(this.checkParentIsAble(dbSysConfig.getParentOid())){
                throw new ResultInfoException("启用失败，当前区划存在禁用的上级参数配置信息！");
            }
            dbSysConfig.setIsAble(BaseStaticParameter.Y);
        }else {
            List<SysConfig> sysConfigList = this.querySysConfigListByParentOid(dbSysConfig.getConfigOid());
            if(null!=sysConfigList && sysConfigList.size()>0){
                throw new ResultInfoException("当前参数存在下级目录，无法禁用！");
            }
            dbSysConfig.setIsAble(BaseStaticParameter.N);
        }
        int index = dbSysConfigMapper.updateByPrimaryKeySelective(dbSysConfig);
        if(index==0){
            throw new ResultInfoException("参数启禁用失败，请稍后再试！");
        }
        //清除缓存
        evictSettingManager.evictSysConfig(dbSysConfig.getId(),dbSysConfig.getCode(),dbSysConfig.getParentOid());
        evictSettingManager.evictSysConfig(dbSysConfig.getConfigOid(),dbSysConfig.getCode(),dbSysConfig.getParentOid());
        return index;
    }

    @Cacheable(key = "'getSysConfigById:'+#id", unless = "#result == null")
    public SysConfig getSysConfigById(Long id) {
        DbSysConfig dbSysConfig = dbSysConfigMapper.selectByPrimaryKey(id);
        if(dbSysConfig == null)
            return null;
        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyProperties(dbSysConfig,sysConfig);
        return sysConfig;
    }

    @Cacheable(key = "'getSysConfigByConfigOId:'+#configOid", unless = "#result == null")
    public SysConfig getSysConfigByConfigOid(String configOid) {
        DbSysConfig dbSysConfig = dbSysConfigMapper.getSysConfigByConfigOid(configOid);
        if(dbSysConfig == null)
            throw new ResultInfoException("参数配置信息为空！");
        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyProperties(dbSysConfig,sysConfig);
        return sysConfig;
    }

    @Cacheable(key = "'getSysConfigByCode:'+#code", unless = "#result == null")
    public SysConfig getSysConfigByCode(String code) {
        if(StrUtil.isEmpty(code)){
            return null;
        }
        DbSysConfig dbSysConfig = dbSysConfigMapper.selectByDbSysConfigByCode(code);
        if(dbSysConfig == null)
            throw new ResultInfoException("参数配置信息为空！");
        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyProperties(dbSysConfig,sysConfig);
        return sysConfig;
    }


    public PageResult<SysConfig> querySysConfigWithPage(SysConfig sysConfig, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysConfigExample dbSysConfigExample = new DbSysConfigExample();
        DbSysConfigExample.Criteria criteria = dbSysConfigExample.createCriteria();
        if(null!=sysConfig){
            if(StrUtil.isNotEmpty(sysConfig.getCode())){
                criteria.andCodeLike("%"+sysConfig.getCode().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysConfig.getName())){
                criteria.andNameLike("%"+sysConfig.getName().trim()+"%");
            }
            if(StrUtil.isNotEmpty(sysConfig.getParentOid())){
                criteria.andParentOidEqualTo(sysConfig.getParentOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        Page<DbSysConfig> dbSysConfigs = (Page<DbSysConfig>)dbSysConfigMapper.selectByExample(dbSysConfigExample);
        PageResult<SysConfig> pageResult = new PageResult<>(dbSysConfigs.getPageNum(),dbSysConfigs.getPageSize(),dbSysConfigs.getTotal());
        List<SysConfig> sysConfigList = dbSysConfigs.stream().map(dbSysConfig -> {
            SysConfig dict = new SysConfig();
            BeanUtils.copyProperties(dbSysConfig,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(sysConfigList);
        return pageResult;
    }


    @Cacheable(key = "'querySysConfigListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysConfig> querySysConfigListByParentOid(String parentOid) {
        DbSysConfigExample dbSysConfigExample = new DbSysConfigExample();
        DbSysConfigExample.Criteria criteria = dbSysConfigExample.createCriteria();
        if(null!= parentOid){
            criteria.andParentOidEqualTo(parentOid);
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysConfig> dbSysConfigs = dbSysConfigMapper.selectByExample(dbSysConfigExample);
        List<SysConfig> sysConfigList = dbSysConfigs.stream().map(sysConfig -> {
            SysConfig config = new SysConfig();
            BeanUtils.copyProperties(sysConfig,config);
            return config;
        }).collect(Collectors.toList());
        return sysConfigList;
    }


    @Cacheable(key = "'querySysConfigChildrenListByParentOid:'+#parentOid", unless = "#result == null")
    public List<SysConfig> querySysConfigChildrenListByParentOid(String parentOid) {
        DbSysConfigExample dbSysConfigExample = new DbSysConfigExample();
        DbSysConfigExample.Criteria criteria = dbSysConfigExample.createCriteria();
        if(StrUtil.isNotEmpty(parentOid)){
            DbSysConfig dbSysConfig = dbSysConfigMapper.selectByPrimaryKey(Long.parseLong(parentOid));
            criteria.andPathLike("%"+dbSysConfig.getId()+"-"+dbSysConfig.getCode()+"%");
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysConfig> dbSysConfigs = dbSysConfigMapper.selectByExample(dbSysConfigExample);
        List<SysConfig> sysConfigList = dbSysConfigs.stream().map(sysConfig -> {
            SysConfig config = new SysConfig();
            BeanUtils.copyProperties(sysConfig,config);
            return config;
        }).collect(Collectors.toList());
        return sysConfigList;
    }

    /**
     * 检查参数配置的code是否存在
     *
     * @return
     * @throws Exception
     */

    public String checkIsExistesConfigCode(Long id, String code) {
        DbSysConfig sysConfig = dbSysConfigMapper.selectByDbSysConfigByCode(code);
        if (sysConfig != null && (null==id || sysConfig.getId().longValue()!=id.longValue())) {
            throw new ResultInfoException("当前配置项代码已经存在！");
        }
        return null;
    }

}
