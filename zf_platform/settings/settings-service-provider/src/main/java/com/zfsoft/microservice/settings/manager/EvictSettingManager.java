package com.zfsoft.microservice.settings.manager;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * @ClassName EvictSettingServiceImpl
 * @Description: //新增或者修改操作，清除缓存--当前数据字典和父类oid的数据字典列表
 * @Author wuxx
 * @Date 2020/9/16
 **/
@Service
public class EvictSettingManager {

    @Caching(evict={
            @CacheEvict(cacheNames = "settings:config",key = "'getSysConfigById:'+#oid"),
            @CacheEvict(cacheNames = "settings:config",key = "'getSysConfigByConfigOId:'+#oid"),
            @CacheEvict(cacheNames = "settings:config",key = "'getSysConfigByCode:'+#code"),
            @CacheEvict(cacheNames = "settings:config",key = "'querySysConfigListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:config",key = "'querySysConfigChildrenListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:config", key = "'querySysConfigChildrenListByParentOid:null'"),
            @CacheEvict(cacheNames = "settings:config", key = "'querySysConfigChildrenListByParentOid:'")})
    public void evictSysConfig(Object oid, String code, String parentOid) {
        //新增或者修改操作，清除缓存--当前数据字典和父类oid的数据字典列表
        //不执行方法
    }

    @Caching(evict={
            @CacheEvict(cacheNames = "settings:dict",key = "'getSysDictById:'+#oid"),
            @CacheEvict(cacheNames = "settings:dict",key = "'getSysDictByDictOid:'+#oid"),
            @CacheEvict(cacheNames = "settings:dict",key = "'getSysDictByCode:'+#code"),
            @CacheEvict(cacheNames = "settings:dict",key = "'querySysDictListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:dict",key = "'querySysDictChildrenListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:dict", key = "'querySysDictChildrenListByParentOid:null'"),
            @CacheEvict(cacheNames = "settings:dict", key = "'querySysDictChildrenListByParentOid:'")})
    public void evictSysDict(Object oid, String code, String parentOid) {
        //新增或者修改操作，清除缓存--当前数据字典和父类oid的数据字典列表
        //不执行方法
    }
}
