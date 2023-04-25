package com.zfsoft.microservice.form.util.dataSourceUtil;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.vo.FormTableDto;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName PostgreSqlFormTableDtoUtil
 * @Description: 数据库字段判断工具类 -- PostgreSql
 * @Author wuxx
 * @Date 2021/8/6
 **/
public class PostgreSqlFormTableDtoUtil implements OperationTable{

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
        List<String> commentSqlList = new ArrayList<>();
        String dropSql = "DROP TABLE "+tableName+" ";
        createList.add(dropSql);
        String sql = "CREATE TABLE " + tableName + "("
                + "ID INT8 NOT NULL PRIMARY KEY, "
                + "MODIFY_DATE DATE, ";
        if(StrUtil.isNotBlank(idIsVarchar) && BaseStaticParameter.STR_ONE.equals(idIsVarchar)){
            sql = "CREATE TABLE " + tableName + "("
                    + "ID VARCHAR(64) NOT NULL PRIMARY KEY, "
                    + "MODIFY_DATE DATE, ";
        }
        for (FormTableDto dto : dtoList) {
            if ("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())) {
                continue;
            }
            String isNotNull = " NULL ";
            if(null!=dto.getIsNotNull() && dto.getIsNotNull()){
                isNotNull = " NOT NULL ";
            }
            List<String> list = new ArrayList<>();
            list.add("BLOB");
            list.add("TEXT");
            list.add("DATE");
            list.add("TIMESTAMP");
            list.add("DATETIME");
            list.add("FLOAT8");
            list.add("INTEGER");
            if (!list.contains(dto.getColumnType().toUpperCase())) {
                sql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") "+isNotNull+",";
            } else {
                sql += dto.getColumnName() + " " + dto.getColumnType() + " "+isNotNull+",";
            }
            String commentSql = "COMMENT ON COLUMN PUBLIC."+tableName+"."+dto.getColumnName()+" IS '" + dto.getDemo() + "' ";
            commentSqlList.add(commentSql);
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String indexSql = "CREATE INDEX INDEX_"+tableName+"_"+dto.getColumnName().toUpperCase()+" ON "+tableName+" ("+dto.getColumnName()+")";
                indexList.add(indexSql);
            }
        }
        sql = sql.substring(0,sql.length()-1);
        sql += " ) ";
        createList.add(sql);
        String commentSql = "COMMENT ON COLUMN PUBLIC."+tableName+".ID IS '主键' ";
        commentSqlList.add(commentSql.toUpperCase());
        String commentDateSql = "COMMENT ON COLUMN PUBLIC."+tableName+".MODIFY_DATE IS '修改时间' ";
        commentSqlList.add(commentDateSql.toUpperCase());
        if(commentSqlList.size()>0){
            createList.addAll(commentSqlList);
        }
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
        List<String> commentSqlList = new ArrayList<>();
        List<String> nullSqlList = new ArrayList<>();
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
            String isNotNull = null;
            List<String> list = new ArrayList<>();
            list.add("BLOB");
            list.add("TEXT");
            list.add("DATE");
            list.add("TIMESTAMP");
            list.add("DATETIME");
            list.add("FLOAT8");
            list.add("INTEGER");
            if(StrUtil.isNotEmpty(dto.getColumnName())
                    && commonList.contains(dto.getColumnName().toUpperCase())){
                updateOrDelSql = "ALTER TABLE "+tableName+" ALTER COLUMN ";

                if (!list.contains(dto.getColumnType().toUpperCase())) {
                    updateOrDelSql += dto.getColumnName() + " TYPE " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") ";
                } else {
                    updateOrDelSql += dto.getColumnName() + " TYPE " + dto.getColumnType();
                }
            }else if(StrUtil.isNotEmpty(dto.getColumnName())){
                if(StrUtil.isNotEmpty(dto.getPreColumnName())){
                    //：alter table tableName rename column oldCName to newCName;
                    //修改字段名
                    updateOrDelSql = "ALTER TABLE "+tableName+" RENAME "+ dto.getPreColumnName() + " TO "+dto.getColumnName();
                    updateOrDelSqlList.add(updateOrDelSql);
                    continue;
                }else{
                    //新增字段
                    updateOrDelSql = "ALTER TABLE "+tableName+" ADD ";
                    if (!list.contains(dto.getColumnType().toUpperCase())) {
                        updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") ";
                    } else {
                        updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType();
                    }
                    if(null!=dto.getIsNotNull() && dto.getIsNotNull()){
                        isNotNull = "NOT NULL";
                    }
                }

            }
            if(StrUtil.isNotEmpty(isNotNull)){
                nullSqlList.add("ALTER TABLE "+tableName+" ALTER  COLUMN "+dto.getColumnName()+" SET NOT NULL");
            }
            if(null!=dto.getIsNotNull() && !dto.getIsNotNull()){
                nullSqlList.add("ALTER TABLE "+tableName+" ALTER  COLUMN "+dto.getColumnName()+" DROP NOT NULL");
            }

            String commentSql = "COMMENT ON COLUMN PUBLIC."+tableName+"."+dto.getColumnName()+" IS '" + dto.getDemo() + "' ";
            commentSqlList.add(commentSql);
            updateOrDelSqlList.add(updateOrDelSql);
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String dropIndexSql = "DROP INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ";
                indexList.add(dropIndexSql);
                String indexSql = "CREATE INDEX INDEX_"+tableName+"_"+dto.getColumnName().toUpperCase()+" ON "+tableName+" ("+dto.getColumnName()+")";
                indexList.add(indexSql);
            }else{
                String dropIndexSql = "DROP INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ";
                indexList.add(dropIndexSql);
            }
        }
        if(commentSqlList.size()>0){
            updateOrDelSqlList.addAll(commentSqlList);
        }

        if(nullSqlList.size()>0){
            updateOrDelSqlList.addAll(nullSqlList);
        }
        //删除字段
        /*for(FormTableDto dto : dataSourceList){
            if(StrUtil.isNotEmpty(dto.getColumnName())
                    && !commonList.contains(dto.getColumnName().toUpperCase())){
                if("ID".equals(dto.getColumnName().toUpperCase()) || "MODIFY_DATE".equals(dto.getColumnName().toUpperCase())){
                    //不删除ID和MODIFY_DATE
                    continue;
                }
                String delSql = "ALTER TABLE "+tableName+" DROP COLUMN IF EXISTS "+dto.getColumnName().toUpperCase();
                updateOrDelSqlList.add(delSql);
            }
        }*/
        List<String> list = dataSourceList.stream().map(FormTableDto::getColumnName).collect(Collectors.toList());
        if(columnNameList.size()>0){
            for (String columnName : columnNameList){
                if(!list.isEmpty() && list.contains(columnName)){
                    String delSql = "ALTER TABLE "+tableName+" DROP COLUMN IF EXISTS "+columnName.toUpperCase();
                    updateOrDelSqlList.add(delSql);
                }
            }
        }
        return updateOrDelSqlList;
    }

}
