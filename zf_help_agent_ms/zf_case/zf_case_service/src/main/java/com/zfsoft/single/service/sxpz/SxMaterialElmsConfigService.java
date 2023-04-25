package com.zfsoft.single.service.sxpz;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName SxMaterialElmsConfigService
 * @Description 证照目录关联材料接口
 * @Author liangxm
 * @Date 2020-11-7
 * @Version V1.0
 **/
@RequestMapping("/single/SxMaterialElmsConfig")
public interface SxMaterialElmsConfigService {


    /**
     * @description:  初始化套餐主题的信息
     * @param materialOid
     * @author: liangxm
     * @Date: 2020/11/7
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxMaterialElmsConfig(@RequestParam(value = "materialOid", required = false) String materialOid,
                                          @RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 保存/更新套餐主题
     * @param sxMaterialElmsConfig
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<SxMaterialElmsConfig> saveOrupdateSxMaterialElmsConfig(@RequestBody SxMaterialElmsConfig sxMaterialElmsConfig);

    /**
     * 根据主键查询套餐信息
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxMaterialElmsConfig>  getSxMaterialElmsConfigByoId(@PathVariable(value = "oid", required = false) String oid);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getElecConfigByMaterialOid"},method = {RequestMethod.GET})
    ApiResultSet<SxMaterialElmsConfig> getElecConfigByMaterialOid(@RequestParam(value = "materialOid") String materialOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getElecConfigByMaterialOidList"},method = {RequestMethod.GET})
    ApiResultSet<List<SxMaterialElmsConfig>> getElecConfigByMaterialOidList(@RequestParam(value = "materialOids") List<String> materialOids);
  }
