package com.zfsoft.microservice.form.util.dataSourceUtil;

import com.zfsoft.microservice.form.data.vo.FormTableDto;

import java.io.InputStream;
import java.util.List;

/**
 * @ClassName OperationTable
 * @Description: 接口操作table
 * @Author wuxx
 * @Date 2021/8/25
 **/
public interface OperationTable {

    /**
     * @description: 根据字段集合创建数据库表的sql
     * @param tableName 表名称
     * @param formTableDtoList 字段集合
     * @author: wuxx
     * @Date: 2021/8/25 14:43
     **/
    List<String> createFormTableSql(String tableName, List<FormTableDto> formTableDtoList,String idIsVarchar);

    /**
     * @description: 根据字段集合创建数据库表的更新或者删除sql
     * @param tableName 表名称
     * @param formTableDtoList 字段集合
     * @param dataSourceList 数据库已有字段集合
     * @author: wuxx
     * @Date: 2021/8/25 14:43
     **/
    List<String> updateOrDelFormTableSql(String tableName, List<FormTableDto> formTableDtoList, List<FormTableDto> dataSourceList);
}
