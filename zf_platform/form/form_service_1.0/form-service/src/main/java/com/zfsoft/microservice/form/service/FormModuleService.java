package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormModule;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormModuleService
 * @Description 模块组件服务定义接口
 * @Author wuxx
 * @Date 2021-04-2 10:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/module")
public interface FormModuleService {

    /**
     * @description:  查询模块的信息列表
     * @param moduleName 模块名称
     * @param authorizeKey 模块授权key
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormModuleWithPage(@RequestParam(value = "authorizeKey", required = false) String authorizeKey,
                                         @RequestParam(value = "moduleName", required = false) String moduleName,
                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  初始化模块的信息
     * @param id 模块实体类主键
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormModule(@RequestParam(value = "id") Long id);

    /**
     * 删除指定Id的模块信息
     * @param id 模块id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormModuleById(@PathVariable("id") Long id);

    /**
     * @description:  模块的新增或者修改
     * @param formModule 模块实体类
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormModule> saveFormModule(@RequestBody FormModule formModule);

    /**
     * @description:  获取模块的信息
     * @param id 模块实体类主键
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormModule> getFormModuleById(@PathVariable("id") Long id);

    /**
     * @description:  获取模块的信息
     * @param moduleOid 模块实体类
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormModuleByModuleOid/{moduleOid}",method = {RequestMethod.GET})
    ApiResultSet<FormModule> getFormModuleByModuleOid(@PathVariable("moduleOid") String moduleOid);

    /**
     * @description:  模块的信息的启禁用
     * @param id 模块实体类主键
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormModuleById(@PathVariable("id") Long id);

    /**
     * @description:  根据授权key查询模块下级列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormModuleList",method = {RequestMethod.GET})
    ApiResultSet<List<FormModule>> queryFormModuleList(@RequestParam("authorizeKey") String authorizeKey);

}
