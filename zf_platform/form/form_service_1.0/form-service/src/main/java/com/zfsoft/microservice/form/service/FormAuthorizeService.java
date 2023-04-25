package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormAuthorize;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.platform.utils.web.TreeSelect;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormAuthorizeService
 * @Description 访问授权组件服务定义接口
 * @Author wuxx
 * @Date 2021-03-10 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/authorize")
public interface FormAuthorizeService {

    /**
     * @description:  查询访问授权的信息列表
     * @param systemName 访问授权名称
     * @param authorizeKey 访问授权授权key
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormAuthorizeWithPage(@RequestParam(value = "authorizeKey",required = false) String authorizeKey,
                                            @RequestParam(value = "systemName",required = false) String systemName,
                                            @RequestParam(value = "pageNum",required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize",required = false) Integer pageSize);

    /**
     * @description:  初始化访问授权的信息
     * @param id 访问授权实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormAuthorize(@RequestParam(value = "id") Long id);

    /**
     * 删除指定Id的访问授权信息
     * @param id 访问授权id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormAuthorizeById(@PathVariable("id") Long id);

    /**
     * @description:  访问授权的新增或者修改
     * @param formAuthorize 访问授权实体类
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormAuthorize> saveFormAuthorize(@RequestBody FormAuthorize formAuthorize);

    /**
     * @description:  获取访问授权的信息
     * @param id 访问授权实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormAuthorize> getFormAuthorizeById(@PathVariable("id") Long id);

    /**
     * @description:  获取访问授权的信息
     * @param authorizeKey 访问授权实体类授权key
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormAuthorizeByAuthorizeKey/{authorizeKey}",method = {RequestMethod.GET})
    ApiResultSet<FormAuthorize> getFormAuthorizeByAuthorizeKey(@PathVariable("authorizeKey") String authorizeKey);

    /**
     * @description:  访问授权的信息的启禁用
     * @param id 访问授权实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormAuthorizeById(@PathVariable("id") Long id);

    /**
     * @description:  根据授权key查询访问授权下级列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormAuthorizeList",method = {RequestMethod.GET})
    ApiResultSet<List<FormAuthorize>> queryFormAuthorizeList(@RequestParam("authorizeKey") String authorizeKey);

    /**
     * @description: 根据授权key查询表单接入管理的tree
     * @author: wuxx
     * @Date: 2021/4/7 13:16
     **/
    @RequestMapping( value = "/queryAuthorizeTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAuthorizeTree(@RequestParam(value = "userOid",required = false) String userOid);

}
