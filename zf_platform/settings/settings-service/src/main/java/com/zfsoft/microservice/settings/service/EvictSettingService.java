package com.zfsoft.microservice.settings.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName EvictSettingService
 * @Description 数据字典和参数配置清除缓存的服务
 * @Author wuxx
 * @Date 2020-09-16 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/cache")
public interface EvictSettingService {

    /**
     * @description:  新增或者修改操作，清除缓存--参数配置和父类oid的数据字典列表
     * @param oid oid
     * @param code code
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/16 10:23
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/evictSysConfig"},method = {RequestMethod.DELETE})
    ApiResultSet<Void> evictSysConfig(Object oid, String code, String parentOid);

    /**
     * @description:  新增或者修改操作，清除缓存--当前数据字典和父类oid的数据字典列表，不需要实现方法
     * @param oid oid
     * @param code code
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/9/16 10:23
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/evictSysDict"},method = {RequestMethod.DELETE})
    ApiResultSet<Void> evictSysDict(Object oid, String code, String parentOid);

}
