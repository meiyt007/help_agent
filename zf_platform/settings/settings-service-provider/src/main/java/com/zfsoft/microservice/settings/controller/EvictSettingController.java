package com.zfsoft.microservice.settings.controller;

import com.zfsoft.microservice.settings.manager.EvictSettingManager;
import com.zfsoft.microservice.settings.service.EvictSettingService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName EvictSettingController
 * @Description  缓存清除类
 * @Author lijun
 * @Date2020-10-20 9:51
 * @Version V1.0
 **/
@RestController
@Slf4j
public class EvictSettingController implements EvictSettingService {

    @Autowired
    private EvictSettingManager evictSettingManager;

    @Override
    public ApiResultSet<Void> evictSysConfig(Object oid, String code, String parentOid) {
          evictSettingManager.evictSysConfig(oid,code,parentOid);
          return new ApiResultSet();
    }

    @Override
    public ApiResultSet<Void> evictSysDict(Object oid, String code, String parentOid) {
        evictSettingManager.evictSysDict(oid,code,parentOid);
        return new ApiResultSet();
    }
}
