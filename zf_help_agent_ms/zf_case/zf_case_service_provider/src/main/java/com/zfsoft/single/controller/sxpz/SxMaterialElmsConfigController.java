package com.zfsoft.single.controller.sxpz;


import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.manager.sxpz.SxMaterialElmsConfigManager;
import com.zfsoft.single.service.sxpz.SxMaterialElmsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: liangxm
 * @create: 2020-11-7
 * @description: 证照目录关联控制层
 */
@Slf4j
@RestController
public class SxMaterialElmsConfigController implements SxMaterialElmsConfigService {
    @Resource
    private SxMaterialElmsConfigManager sxMaterialElmsConfigManager;


    @Override
    public ApiResultSet initSxMaterialElmsConfig(String id,String serviceOid) {
        SxMaterialElmsConfig sxMaterialElmsConfig=this.sxMaterialElmsConfigManager.getSxMaterialElmsConfigByMaterOid(id,serviceOid);
        log.info("详情获取成功：{}", JSON.toJSONString(sxMaterialElmsConfig));
        return new ApiResultSet(sxMaterialElmsConfig);

    }

    @Override
    public ApiResultSet<SxMaterialElmsConfig> saveOrupdateSxMaterialElmsConfig(SxMaterialElmsConfig sxMaterialElmsConfig) {
        this.sxMaterialElmsConfigManager.saveOrUpdate(sxMaterialElmsConfig);
        return new ApiResultSet(sxMaterialElmsConfig);
    }

    @Override
    public ApiResultSet<SxMaterialElmsConfig> getSxMaterialElmsConfigByoId(String oid) {
        return null;
    }

    @Override
    public ApiResultSet<SxMaterialElmsConfig> getElecConfigByMaterialOid(String materialOid) {
       SxMaterialElmsConfig config= sxMaterialElmsConfigManager.getElecConfigByMaterialOid(materialOid);
        return new ApiResultSet(config);
    }

    @Override
    public ApiResultSet<List<SxMaterialElmsConfig>> getElecConfigByMaterialOidList(List<String> materialOids) {
       List<SxMaterialElmsConfig> list= sxMaterialElmsConfigManager.getElecConfigByMaterialOids(materialOids);
        return new ApiResultSet<>(list);
    }
}
