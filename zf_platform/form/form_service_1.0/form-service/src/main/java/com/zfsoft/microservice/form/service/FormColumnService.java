package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormColumn;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FormColumnService
 * @Description 表单表结构组件服务定义接口
 * @Author wuxx
 * @Date 2021-4-13 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/column")
public interface FormColumnService {

    /**
     * @description:  初始化表结构的信息
     * @param id 表结构实体类主键
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormColumn(@RequestParam(value = "id") Long id);

    /**
     * @description:  表结构的新增或者修改
     * @param dataSourceConfig 表结构实体类
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormColumn> saveFormColumn(@RequestBody FormColumn dataSourceConfig);

    /**
     * @description:  获取表结构的信息
     * @param id 表结构实体类主键
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormColumn> getFormColumnById(@PathVariable("id") Long id);

    /**
     * @description:  获取表结构的信息
     * @param columnOid 表结构业务oid
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormColumnByColumnOid/{columnOid}",method = {RequestMethod.GET})
    ApiResultSet<FormColumn> getFormColumnByColumnOid(@PathVariable("columnOid") String columnOid);

    /**
     * @description:  根据对象主键查询表结构列表
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormColumnListByObjectOid",method = {RequestMethod.GET})
    ApiResultSet<List<FormColumn>> queryFormColumnListByObjectOid(@RequestParam("objectOid") String objectOid,@RequestParam(value = "datasourceOid",required = false) String datasourceOid);

    /**
     * @description:  根据对象主键查询表结构字符串包括关联
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/12/3 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormColumnStrByObjectOid",method = {RequestMethod.GET})
    ApiResultSet<List<String>> queryFormColumnStrByObjectOid(@RequestParam("objectOid") String objectOid);

    /**
     * @description:  获取表结构的信息
     * @param datasourceOid 数据库业务oid
     * @param isNotChangeFiledName 是否数据库字段名和表单字段名 是否不用转换  1不用转换  其他需要转换  默认需要转换
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDatasourceColumn",method = {RequestMethod.GET})
    ApiResultSet<List<FormColumn>> getDatasourceColumn(@RequestParam("datasourceOid") String datasourceOid,
                                                 @RequestParam("objectForm") String objectForm,
                                                 @RequestParam(value = "isNotChangeFiledName",required = false) Integer isNotChangeFiledName);

    /**
     * @description: 获取存储对象的字段 数据存储类型
     * @author: zje
     * @Date:
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getObjectFieldSaveTypeList", method = {RequestMethod.GET})
    ApiResultSet<List<Map<String, Object>>> getObjectFieldSaveTypeList();

}
