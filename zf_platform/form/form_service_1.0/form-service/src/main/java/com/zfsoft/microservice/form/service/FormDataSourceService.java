package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormDataSourceService
 * @Description 表单数据源配置组件服务定义接口
 * @Author wuxx
 * @Date 2021-03-10 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/datasource")
public interface FormDataSourceService {

    /**
     * @description:  初始化数据源配置的信息
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormDataSource(@RequestParam(value = "id") Long id);

    /**
     * 删除指定Id的数据源配置信息
     * @param id 数据源配置id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormDataSourceById(@PathVariable("id") Long id);

    /**
     * @description:  数据源配置的新增或者修改
     * @param dataSourceConfig 数据源配置实体类
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormDataSource> saveFormDataSource(@RequestBody FormDataSource dataSourceConfig);

    /**
     * @description:  获取数据源配置的信息
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormDataSource> getFormDataSourceById(@PathVariable("id") Long id);

    /**
     * @description:  获取数据源配置的信息
     * @param dataSourceOid 数据源配置业务oid
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDataSourceByDataSourceOid/{dataSourceOid}",method = {RequestMethod.GET})
    ApiResultSet<FormDataSource> getFormDataSourceByDataSourceOid(@PathVariable("dataSourceOid") String dataSourceOid);

    /**
     * @description:  查询数据源配置的信息列表
     * @param connectionName 连接名称
     * @param authorizeKey 数据源配置授权key
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormDataSourceWithPage(@RequestParam(value = "authorizeKey") String authorizeKey,
                                            @RequestParam(value = "connectionName", required = false) String connectionName,
                                             @RequestParam(value = "moduleOid", required = false) String moduleOid,
                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  数据源配置的信息的启禁用
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableFormDataSourceById(@PathVariable("id") Long id);

    /**
     * @description:  根据授权key查询数据源配置下级列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormDataSourceList",method = {RequestMethod.GET})
    ApiResultSet<List<FormDataSource>> queryFormDataSourceList(@RequestParam("authorizeKey") String authorizeKey,
                                                               @RequestParam(value = "moduleOid") String moduleOid);

    /**
     * @description: 根据数据字典编码获取数字字典列表
     * @param code 数据字典编码
     * @author: wuxx
     * @Date: 2021/03/10 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDataSourceDictList/{code}",method = {RequestMethod.GET})
    ApiResultSet getDataSourceDictList(@PathVariable("code") String code);

}
