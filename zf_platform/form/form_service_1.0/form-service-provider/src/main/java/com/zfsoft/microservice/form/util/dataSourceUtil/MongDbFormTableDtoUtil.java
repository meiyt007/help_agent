package com.zfsoft.microservice.form.util.dataSourceUtil;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.vo.FormTableDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName MongDbFormTableDtoUtil
 * @Description: 数据库字段判断工具类 -- MongDb
 * @Author wuxx
 * @Date 2021/8/6
 **/
public class MongDbFormTableDtoUtil implements OperationTable{

    /**
     * @description: 根据字段集合创建数据库表的sql
     * @param tableName 表名称
     * @param dtoList 字段集合
     * @author: wuxx
     * @Date: 2021/8/25 14:43
     **/
    @Override
    public List<String> createFormTableSql(String tableName, List<FormTableDto> dtoList,String idIsVarchar){
        List<String> createList = new ArrayList<>();
        createList.add("ID");
        createList.add("MODIFY_DATE");
        for (FormTableDto dto : dtoList) {
            if ("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())) {
                continue;
            }
            createList.add(dto.getColumnName());
        }
        return createList;
    }

    /**
     * @description: 根据字段集合创建数据库表的更新或者删除sql
     * @param tableName 表名称
     * @param dtoList 字段集合
     * @param dataSourceList 数据库已有字段集合
     * @author: wuxx
     * @Date: 2021/8/25 14:43
     **/
    @Override
    public List<String> updateOrDelFormTableSql(String tableName, List<FormTableDto> dtoList, List<FormTableDto> dataSourceList){
        List<String> updateOrDelSqlList = new ArrayList<>();
        updateOrDelSqlList.add("ID");
        updateOrDelSqlList.add("MODIFY_DATE");
        for (FormTableDto dto : dtoList) {
            if ("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())) {
                continue;
            }
            updateOrDelSqlList.add(dto.getColumnName());
        }
        return updateOrDelSqlList;
    }

}
