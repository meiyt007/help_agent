package com.zfsoft.cases.manager;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * @ClassName EvictSettingServiceImpl
 * @Description: //新增或者修改操作，清除缓存--当前数字字典和父类oid的数字字典列表
 * @Author wuxx
 * @Date 2020/9/16
 **/
@Service
public class EvictSettingManager {

    @Caching(evict={
            @CacheEvict(cacheNames = "settings:dict",key = "'getSysDictById:'+#oid"),
            @CacheEvict(cacheNames = "settings:dict",key = "'getSysDictByCode:'+#code"),
            @CacheEvict(cacheNames = "settings:dict",key = "'querySysDictListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:dict",key = "'querySysDictChildrenListByParentOid:'+#parentOid"),
            @CacheEvict(cacheNames = "settings:dict", key = "'querySysDictChildrenListByParentOid:null'")})
    public void evictSysDict(Long oid, String code, Long parentOid) {
        //新增或者修改操作，清除缓存--当前数字字典和父类oid的数字字典列表
        //不执行方法
    }

    @Caching(evict={
            @CacheEvict(cacheNames = "manage:pbpj",key = "'getPbpjManageById:'+#id")})
    public void evictpbpjManage(Long id) {
        //新增或者修改操作，清除缓存--当前数字字典和父类oid的数字字典列表
        //不执行方法
    }

    @Caching(evict={
            @CacheEvict(cacheNames = "pbpj:user",key = "'getPbpjUserById:'+#id")})
    public void evictpbpjUser(Long id) {
        //新增或者修改操作，清除缓存--当前数字字典和父类oid的数字字典列表
        //不执行方法
    }

    @Caching(evict={
            @CacheEvict(cacheNames = "manage:information",key = "'getShowInformationById:'+#id")})
    public void evictInformation(Long id) {
        //新增或者修改操作，清除缓存--当前数字字典和父类oid的数字字典列表
        //不执行方法
    }
}
