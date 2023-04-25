package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormMainService
 * @Description 表单设计主表组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/main")
public interface FormMainService {


    /**
     * @description:  查询表单设计对象的信息列表
     * @param formName 对象名称
     * @param formStatus 表单状态 0修订 1发布 2变更
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormMainWithPage(@RequestParam(value = "authorizeKey") String authorizeKey,
                                       @RequestParam(value = "formName", required = false) String formName,
                                       @RequestParam(value = "formCode", required = false) String formCode,
                                       @RequestParam(value = "formStatus", required = false) Integer formStatus,
                                       @RequestParam(value = "objectOid", required = false) String objectOid,
                                       @RequestParam(value = "isAble", required = false,defaultValue = "0") Integer isAble,
                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  表单设计对象的新增或者修改
     * @param formMain 表单设计对象实体类
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormMain> saveFormMain(@RequestBody FormMain formMain);

    /**
     * @description:  初始化表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormMain(@RequestParam(value = "id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormMain> getFormMainById(@PathVariable("id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMainByFormMainOid/{formMainOid}",method = {RequestMethod.GET})
    ApiResultSet<FormMain> getFormMainByFormMainOid(@PathVariable("formMainOid") String formMainOid);

    /**
     * 删除指定Id的表单设计对象信息
     * @param id 表单设计对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormMainById(@PathVariable("id") Long id);

    /**
     * @description:  表单设计对象的信息的启禁用
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormMainById(@PathVariable("id") Long id);

    /**
     * @description:  表单的发布
     * @param formMainOid 表单的业务主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/deploy/{formMainOid}",method = {RequestMethod.POST})
    ApiResultSet<Integer> deployFormMain(@PathVariable("formMainOid") String formMainOid);

    /**
     * @description:  表单的发布
     * @param formCode 表单的业务主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/deployByFormCode",method = {RequestMethod.POST})
    ApiResultSet<Integer> deployByFormCode(@RequestParam("formCode") String formCode);

    /**
     * @description:  表单的复制
     * @param formMainOid 表单的业务主键
     * @author: wuxx
     * @Date: 2021/05/6 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/copy/{formMainOid}",method = {RequestMethod.POST})
    ApiResultSet<Integer> copyFormMain(@PathVariable("formMainOid") String formMainOid);

    /**
     * @description:  根据授权key查询表单设计对象只查询发布的表单
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormMainList",method = {RequestMethod.GET})
    ApiResultSet<List<FormMain>> queryFormMainList(@RequestParam("authorizeKey") String authorizeKey,
                                                   @RequestParam(value = "formStatus", required = false) Integer formStatus);

}
