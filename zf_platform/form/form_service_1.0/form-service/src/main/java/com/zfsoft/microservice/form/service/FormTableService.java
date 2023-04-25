package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormTable;
import com.zfsoft.microservice.form.data.vo.CreateFormTableVo;
import com.zfsoft.microservice.form.data.vo.FormPhysicalDataModel;
import com.zfsoft.microservice.form.data.vo.FormTableDtoParams;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormTableService
 * @Description 表服务定义接口
 * @Author wuxx
 * @Date 2021-08-3 11:33
 * @Version V1.0
 **/
@RequestMapping("/manager/security/table")
public interface FormTableService {

    /**
     * @param formTableDtoJson 数据库字段集合配置
     * @param datasourceOid    数据源id
     * @param tableName        表名
     * @description: 根据数据库字段集合配置创建表或者更新
     * @author: wuxx
     * @Date: 2021/8/6 14:03
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/createTableByFormTableDto", method = {RequestMethod.POST})
    ApiResultSet createTableByFormTableDto(@RequestBody FormTableDtoParams formTableDtoParams);

    /**
     * @param sql           创建sql
     * @param datasourceOid 数据库连接业务主键
     * @description: 根据sql创建表
     * @author: wuxx
     * @Date: 2021/8/4 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/createTable", method = {RequestMethod.POST})
    ApiResultSet createTable(@RequestParam("sql") String sql,
                             @RequestParam("datasourceOid") String datasourceOid,
                             @RequestParam("tableName") String tableName);

    /**
     * @param sql           更新sql
     * @param datasourceOid 数据库连接业务主键
     * @description: 根据sql更新表
     * @author: wuxx
     * @Date: 2021/8/4 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateTable", method = {RequestMethod.POST})
    ApiResultSet updateTable(@RequestParam("sql") String sql,
                             @RequestParam("datasourceOid") String datasourceOid);

    /**
     * @param sql           sql
     * @param datasourceOid 数据库连接业务主键
     * @description: 根据sql查询表
     * @author: wuxx
     * @Date: 2021/8/4 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/selectTable", method = {RequestMethod.GET})
    ApiResultSet selectTable(@RequestParam("sql") String sql,
                             @RequestParam("datasourceOid") String datasourceOid,
                             @RequestParam(value = "objectOid", required = false) String objectOid);

    /**
     * @param sql           删除sql
     * @param datasourceOid 数据库连接业务主键
     * @description: 根据sql删除表
     * @author: wuxx
     * @Date: 2021/8/4 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteTable", method = {RequestMethod.POST})
    ApiResultSet deleteTable(@RequestParam("sql") String sql,
                             @RequestParam("datasourceOid") String datasourceOid);

    /**
     * @param datasourceOid 数据源oid
     * @description: 获取数据库类型
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDataSourceTypeList", method = {RequestMethod.GET})
    ApiResultSet<List<String>> getDataSourceTypeList(@RequestParam("datasourceOid") String datasourceOid);

    /**
     * @param datasourceTypeName 数据库类型名 （比如 mysql）
     * @description: 获取数据库类型
     * @author: zje
     * @Date:
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDataSourceTypeByNameList", method = {RequestMethod.GET})
    ApiResultSet<List<String>> getDataSourceTypeByNameList(@RequestParam("datasourceTypeName") String datasourceTypeName);

    /**
     * @param formTable 表结构实体类
     * @description: 表结构的新增或者修改
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<FormTable> saveFormTable(@RequestBody FormTable formTable);

    /**
     * @param createFormTableVo 创建表结构实体类
     * @description: 表结构的新增或者修改
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveFormTableVo", method = {RequestMethod.POST})
    ApiResultSet saveFormTableVo(@RequestBody CreateFormTableVo createFormTableVo);


    /**
     * @param id 表结构实体类主键
     * @description: 获取表结构的信息
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<FormTable> getFormTableById(@PathVariable("id") Long id);

    /**
     * @param tableOid 表结构业务oid
     * @description: 获取表结构的信息
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFormTableByTableOid/{tableOid}", method = {RequestMethod.GET})
    ApiResultSet<FormTable> getFormTableByTableOid(@PathVariable("tableOid") String tableOid);

    /**
     * @param tableName 表名
     * @description: 根据对象主键查询表结构列表
     * @author: wuxx
     * @Date: 2021/9/9 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFormTableList", method = {RequestMethod.GET})
    ApiResultSet<List<FormTable>> queryFormTableList(@RequestParam("tableName") String tableName,
                                                     @RequestParam(value = "datasourceOid")
                                                             String datasourceOid);

    /**
     * 根据物理数据模型创建表、存储结构、表单
     * @param formPhysicalDataModel
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/createFormTableByFormPhysicalDataModel", method = {RequestMethod.POST})
    ApiResultSet createFormTableByFormPhysicalDataModel(@RequestBody FormPhysicalDataModel formPhysicalDataModel);

    /**
     * 根据表单编码查询物理数据模型
     * @param formCode
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFormPhysicalDataModelByFormCode", method = {RequestMethod.GET})
    ApiResultSet getFormPhysicalDataModelByFormCode(@RequestParam String formCode);
}
