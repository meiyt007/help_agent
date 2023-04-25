package com.zfsoft.cases.service;

import com.zfsoft.cases.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: SysDictService
 * @description: 字典信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/sysDictService")
public interface SysDictService {
    
    /**
     * 保存字典信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param sysDict
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.SysDict>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSysDict",method = {RequestMethod.POST})
    ApiResultSet<String> saveSysDict(@RequestBody SysDict sysDict);

    /**
     * 获取证件类型信息
     *
     * @author wangwg
     * @date 2020/11/17
     * @param type
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSelectCertificateType",method = {RequestMethod.GET})
    ApiResultSet<List<com.zfsoft.microservice.settings.data.SysDict>> getSelectCertificateType(@RequestParam("type") int type);


    /**
     * 获取证件名称
     *
     * @author wangwg
     * @date 2020/12/01
     * @param dictOid
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysDictByDictOid",method = {RequestMethod.GET})
    ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> getSysDictByDictOid(@RequestParam("dictOid") String dictOid);
}
