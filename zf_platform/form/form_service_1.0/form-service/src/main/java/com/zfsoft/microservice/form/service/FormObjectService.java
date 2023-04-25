package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormObjectService
 * @Description 表单存储对象组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/object")
public interface FormObjectService {

    /**
     * @param id 存储对象实体类主键
     * @description: 初始化存储对象的信息
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"}, method = {RequestMethod.GET})
    ApiResultSet initFormObject(@RequestParam(value = "id") Long id);

    /**
     * 删除指定Id的存储对象信息
     *
     * @param id 存储对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteFormObjectById(@PathVariable("id") Long id);

    /**
     * @param formObject 存储对象实体类
     * @description: 存储对象的新增或者修改
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<FormObject> saveFormObject(@RequestBody FormObject formObject);

    /**
     * @param formObject 存储对象实体类--针对组件集成方法并自动生成表单
     * @description: 存储对象的新增或者修改
     * @author: wuxx
     * @Date: 2021/8/3 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveObject", method = {RequestMethod.POST})
    ApiResultSet<FormMain> saveObjectFormObject(@RequestBody FormObject formObject);

    /**
     * @param id 存储对象实体类主键
     * @description: 获取存储对象的信息
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<FormObject> getFormObjectById(@PathVariable("id") Long id);

    /**
     * @param objectOid 存储对象业务oid
     * @description: 获取存储对象的信息
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFormObjectByObjectOid/{objectOid}", method = {RequestMethod.GET})
    ApiResultSet<FormObject> getFormObjectByObjectOid(@PathVariable("objectOid") String objectOid);

    /**
     * @param objectName   对象名称
     * @param authorizeKey 存储对象授权key
     * @description: 查询存储对象的信息列表
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet queryFormObjectWithPage(@RequestParam(value = "authorizeKey") String authorizeKey,
                                         @RequestParam(value = "objectName", required = false) String objectName,
                                         @RequestParam(value = "objectCode", required = false) String objectCode,
                                         @RequestParam(value = "saveType", required = false) String saveType,
                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @param id 存储对象实体类主键
     * @description: 存储对象的信息的启禁用
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/able/{id}", method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormObjectById(@PathVariable("id") Long id);

    /**
     * @param authorizeKey 授权key
     * @description: 根据授权key查询存储对象下级列表
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFormObjectList", method = {RequestMethod.GET})
    ApiResultSet<List<FormObject>> queryFormObjectList(@RequestParam("authorizeKey") String authorizeKey,
                                                       @RequestParam(value = "moduleOid", required = false) String moduleOid);

    /**
     * 获取数据源的存储对象列表
     *
     * @param authorizeKey 授权key
     * @param moduleOid    模块主键
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryDataSourceFormObjectList", method = {RequestMethod.GET})
    ApiResultSet<List<FormObject>> queryDataSourceFormObjectList(@RequestParam("authorizeKey") String authorizeKey,
                                                                 @RequestParam(value = "moduleOid", required = false) String moduleOid,
                                                                 @RequestParam(value = "saveType", required = false) String saveType);

    /**
     * @param objectOid 存储对象业务oid
     * @description: 根据objectOid查询是否存在填报的数据
     * @author: wuxx
     * @Date: 2021/11/23 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkReportDataExist/{objectOid}", method = {RequestMethod.GET})
    ApiResultSet<Boolean> checkReportDataExist(@PathVariable("objectOid") String objectOid);


    /**
     * @description: 传formCode集合，返回所有的存储对象
     * @param formCodes 表单编码,多个用逗号隔开
     * @author: wuxx
     * @Date: 2022/1/12 11:27
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFormObjectByFormCodes", method = {RequestMethod.GET})
    ApiResultSet<List<FormObject>> getFormObjectByFormCodes(@RequestParam("formCodes") String formCodes);

}
