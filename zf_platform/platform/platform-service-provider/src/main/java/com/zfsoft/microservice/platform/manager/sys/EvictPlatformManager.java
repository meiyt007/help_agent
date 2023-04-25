package com.zfsoft.microservice.platform.manager.sys;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * @ClassName EvictSettingServiceImpl
 * @Description: 基础配置清除缓存的服务 实现类
 * @Author wuxx
 * @Date 2020/9/16
 **/
@Service
public class EvictPlatformManager{

    @Caching(evict={
            @CacheEvict(cacheNames = "sys:district", key = "'getSysDistrictById:'+#districtOid"),
            @CacheEvict(cacheNames = "sys:district", key = "'getSysDistrictByDistrictOid:'+#districtOid"),
            @CacheEvict(cacheNames = "sys:district", key = "'queryDistrictSimpleTreeList:'+#districtOid"),
            @CacheEvict(cacheNames = "sys:district", key = "'queryDistrictSimpleTreeList:null'"),
            @CacheEvict(cacheNames = "sys:district", key = "'queryLowerLevelDistrictByOid:'+#districtOid")})
    public void evictSysDistrict(Object districtOid) {
        //新增或者修改操作，清除缓存--区划信息
        //不执行方法
    }

    @Caching(evict={
            @CacheEvict(cacheNames = "sys:organ", key = "'getSysOrganById:'+#oid"),
            @CacheEvict(cacheNames = "sys:organ", key = "'queryOrganTreeList:'+#districtOid"),
            @CacheEvict(cacheNames = "sys:organ", key = "'queryOrganTreeList:null'")})
    public void evictSysOrgan(Object oid, Object districtOid) {
        //新增或者修改操作，清除缓存--组织机构
        //不执行方法
    }
}
