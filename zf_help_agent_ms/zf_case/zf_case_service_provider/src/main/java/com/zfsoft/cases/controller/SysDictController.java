package com.zfsoft.cases.controller;

import com.zfsoft.cases.data.SysDict;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.cases.manager.SysDictManager;
import com.zfsoft.cases.service.SysDictService;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: SysDictController
 * @description: 字典信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SysDictController implements SysDictService {

    @Resource
    private SysDictManager sysDictManager;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Override
    public ApiResultSet<String> saveSysDict(SysDict sysDict) {
        String attaOid=sysDictManager.saveSysDict(sysDict);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(attaOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<com.zfsoft.microservice.settings.data.SysDict>> getSelectCertificateType(int type) {
        ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> dictResult =null;
        ApiResultSet<List<com.zfsoft.microservice.settings.data.SysDict>> listApiResultSet=null;
        if(type==1){
            dictResult = sysDictFeignService.getSysDictByCode(BaseStaticParameter.ZZLX_GR_CODE);
        }else{
            dictResult = sysDictFeignService.getSysDictByCode(BaseStaticParameter.ZZLX_FR_CODE);
        }
        if(dictResult !=null){
            com.zfsoft.microservice.settings.data.SysDict sysDict = dictResult.getData();
            listApiResultSet = sysDictFeignService.querySysDictListByParentOid(sysDict.getDictOid());
        }
        ApiResultSet<List<com.zfsoft.microservice.settings.data.SysDict>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(listApiResultSet.getData());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> getSysDictByDictOid(String dictOid) {
        ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> dictResult = sysDictFeignService.getSysDictByDictOid(dictOid);
        ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dictResult.getData());
        return apiResultSet;
    }
}
