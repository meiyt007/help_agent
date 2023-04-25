package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormDesignService
 * @Description 表单设计版本服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/design")
public interface FormDesignService {


    /**
     * @description:  查询表单设计对象的信息列表
     * @param formMainOid 表单主表业务主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormDesignWithPage(@RequestParam(value = "formMainOid") String formMainOid,
                                       @RequestParam(value = "moduleOid", required = false) String moduleOid,
                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  初始化表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormDesign(@RequestParam(value = "id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param id 表单设计对象实体类主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesignById(@PathVariable("id") Long id);

    /**
     * @description:  获取表单设计对象的信息
     * @param designOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesignByDesignOid/{designOid}",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesignByDesignOid(@PathVariable("designOid") String designOid);

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单主设计对象业务oid
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesignByFormMainOid/{formMainOid}",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesignByFormMainOid(@PathVariable("formMainOid") String formMainOid);

    /**
     * 删除指定Id的表单设计对象信息
     * @param id 表单设计对象id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormDesignById(@PathVariable("id") Long id);

    /**
     * @description:  根据授权key查询表单设计对象列表
     * @param formMainOid 表单主键
     * @author: wuxx
     * @Date: 2021/04/16 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormDesignList",method = {RequestMethod.GET})
    ApiResultSet<List<FormDesign>> queryFormDesignList(@RequestParam("formMainOid") String formMainOid);

}
