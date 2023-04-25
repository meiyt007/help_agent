package com.zfsoft.microservice.form.util.dataSourceUtil;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.vo.FormTableDto;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName MySqlFormTableDtoUtil
 * @Description: 数据库字段判断工具类 -- mysql
 * @Author wuxx
 * @Date 2021/8/6
 **/
public class MySqlFormTableDtoUtil implements OperationTable{

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
        List<String> indexList = new ArrayList<>();
        //String dropSql = "DROP TABLE "+tableName+";";
        //createList.add(dropSql);
        String sql = "CREATE TABLE " + tableName + "("
                + "ID BIGINT NOT NULL COMMENT '主键', "
                + "MODIFY_DATE DATETIME COMMENT '修改时间', ";
        if(StrUtil.isNotBlank(idIsVarchar) && BaseStaticParameter.STR_ONE.equals(idIsVarchar)){
            sql = "CREATE TABLE " + tableName + "("
                    + "ID VARCHAR(64) NOT NULL COMMENT '主键', "
                    + "MODIFY_DATE DATETIME COMMENT '修改时间', ";
        }
        for (FormTableDto dto : dtoList) {
            if ("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())) {
                continue;
            }
            String isNotNull = " NULL ";
            if(null!=dto.getIsNotNull() && dto.getIsNotNull()){
                isNotNull = " NOT NULL ";
            }
            //['BLOB','TEXT','DATE','TIME','TIMESTAMP','DATETIME'];
            List<String> list = new ArrayList<>();
            list.add("BLOB");
            list.add("TEXT");
            list.add("DATE");
            list.add("TIMESTAMP");
            list.add("DATETIME");
            if (!list.contains(dto.getColumnType().toUpperCase())) {
                sql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") "+isNotNull+"COMMENT '" + dto.getDemo() + "', ";
           } else {
                sql += dto.getColumnName() + " " + dto.getColumnType() + isNotNull+" COMMENT '" + dto.getDemo() + "', ";
            }
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String indexSql = "ALTER TABLE "+tableName+" ADD INDEX INDEX_"+dto.getColumnName()+" ("+dto.getColumnName()+");";
                indexList.add(indexSql);
            }
        }
        sql += " PRIMARY KEY (ID));";
        createList.add(sql);
        if(indexList.size()>0){
            createList.addAll(indexList);
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
        List<String> indexList = new ArrayList<>();
        // 同时出现在dtoList集合和dataSourceList集合中人的ColumnName集合,ColumnName相同即视为存在
        List<String> commonList = dtoList.stream()
                .map(t -> dataSourceList.stream().filter(s -> Objects.nonNull(t.getColumnName().toUpperCase())
                        && Objects.nonNull(s.getColumnName()) && Objects.equals(t.getColumnName().toUpperCase(), s.getColumnName().toUpperCase())).findAny().orElse(null))
                .filter(Objects::nonNull)
                .map(r -> r.getColumnName().toUpperCase())
                .collect(Collectors.toList());
        //alter  table t_interface modify ID BIGINT(19) COMMENT '主键测试';   -- 修改
        //alter table t_interface add column position VARCHAR(200) COMMENT '这是注释';  -- 新增
        //alter table t_interface drop column REMARK ;  -- 删除
        //需要删除的字段
        List<String> columnNameList = new ArrayList<>();
        //修改和新增表字段
        for(FormTableDto dto : dtoList){
            if("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())){
                //不操作ID和MODIFY_DATE
                continue;
            }
            if(null!=dto.getDelFlag() && dto.getDelFlag()){
               //删除的字段不做处理
                columnNameList.add(dto.getColumnName());
                continue;
            }
            String updateOrDelSql = "";
            //修改字段
            String isNotNull = " NULL ";
            if(StrUtil.isNotEmpty(dto.getColumnName())
                    && commonList.contains(dto.getColumnName().toUpperCase())){
                updateOrDelSql = "ALTER TABLE "+tableName+" MODIFY ";
            }else if(StrUtil.isNotEmpty(dto.getColumnName())){
                if(StrUtil.isNotEmpty(dto.getPreColumnName())){
                    //alter  table form_ui change ceshi ceshi2 varchar(100) COMMENT '注释';
                    //修改字段名
                    updateOrDelSql = "ALTER TABLE "+tableName+" CHANGE "+ dto.getPreColumnName() + " ";
                }else{
                    //新增字段
                    updateOrDelSql = "ALTER TABLE "+tableName+" ADD ";
                }
            }
            if(null!=dto.getIsNotNull() && dto.getIsNotNull()){
                isNotNull = " NOT NULL ";
            }
            List<String> list = new ArrayList<>();
            list.add("BLOB");
            list.add("TEXT");
            list.add("DATE");
            list.add("TIMESTAMP");
            list.add("DATETIME");
            if (!list.contains(dto.getColumnType().toUpperCase())) {
                updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType() +" (" + dto.getMaxLenth() + ") "+ isNotNull+"COMMENT '" + dto.getDemo() + "'; ";
            } else {
                updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType() + isNotNull+ " COMMENT '" + dto.getDemo() + "'; ";
            }

            updateOrDelSqlList.add(updateOrDelSql);
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String dropIndexSql = "DROP INDEX INDEX_"+dto.getColumnName()+" ON "+tableName;
                indexList.add(dropIndexSql);
                String indexSql = "ALTER TABLE "+tableName+" ADD INDEX INDEX_"+dto.getColumnName()+" ("+dto.getColumnName()+");";
                indexList.add(indexSql);
            }else {
                String dropIndexSql = "DROP INDEX INDEX_"+dto.getColumnName()+" ON "+tableName;
                indexList.add(dropIndexSql);
            }
        }
        //删除字段
        List<String> list = dataSourceList.stream().map(FormTableDto::getColumnName).collect(Collectors.toList());
        if(columnNameList.size()>0){
            for (String columnName : columnNameList){
                if(!list.isEmpty() && list.contains(columnName)){
                    String delSql = "ALTER TABLE "+tableName+" DROP COLUMN "+columnName;
                    updateOrDelSqlList.add(delSql);
                }
            }
        }
        /*for(FormTableDto dto : dataSourceList){
            if(StrUtil.isNotEmpty(dto.getColumnName())
                    && !commonList.contains(dto.getColumnName().toUpperCase())){
                if("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())){
                    //不删除ID和MODIFY_DATE
                    continue;
                }
                String delSql = "ALTER TABLE "+tableName+" DROP COLUMN "+dto.getColumnName();
                updateOrDelSqlList.add(delSql);
            }
        }*/
        if(indexList.size()>0){
            updateOrDelSqlList.addAll(indexList);
        }
        return updateOrDelSqlList;
    }

}
