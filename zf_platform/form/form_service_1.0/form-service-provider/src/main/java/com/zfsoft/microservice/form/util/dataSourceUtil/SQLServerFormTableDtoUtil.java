package com.zfsoft.microservice.form.util.dataSourceUtil;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.vo.FormTableDto;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName SQLServerFormTableDtoUtil
 * @Description: 数据库字段判断工具类 -- SQLServer
 * @Author wuxx
 * @Date 2021/8/6
 **/
public class SQLServerFormTableDtoUtil implements OperationTable{

    /**
     * @description: 根据字段集合创建数据库表的sql
     * @param tableName 表名称
     * @param dtoList 字段集合
     * @author: wuxx
     * @Date: 2021/8/25 14:43
     **/
    @Override
    public List<String> createFormTableSql(String tableName, List<FormTableDto> dtoList,String idIsVarchar){
        List<String> allSqlList = new ArrayList<>();
        List<String> indexList = new ArrayList<>();
        List<String> commentList = new ArrayList<>();
        String dropSql = "IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].["+tableName+"]') AND type IN ('U'))\n" +
                "\tDROP TABLE [dbo].["+tableName+"]";
        allSqlList.add(dropSql);
        String sql = "CREATE TABLE " + tableName + "("
                + "ID BIGINT NOT NULL, "
                + "MODIFY_DATE DATE, ";
        if(StrUtil.isNotBlank(idIsVarchar) && BaseStaticParameter.STR_ONE.equals(idIsVarchar)){
            sql = "CREATE TABLE " + tableName + "("
                    + "ID VARCHAR(64) NOT NULL, "
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
            list.add("INT");
            if (!list.contains(dto.getColumnType().toUpperCase())) {
                sql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") "+isNotNull+",";
            } else {
                sql += dto.getColumnName() + " " + dto.getColumnType() + " "+isNotNull+",";
            }
            String commentSql = "EXEC sp_addextendedproperty 'MS_Description', N'"+dto.getDemo()+"','SCHEMA', N'dbo','TABLE', N'"+tableName+"','COLUMN', N'"+dto.getColumnName()+"' ";
            commentList.add(commentSql);
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String indexSql = "CREATE INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ON "+tableName+" ("+dto.getColumnName()+")";
                indexList.add(indexSql);
            }
        }
        sql = sql.substring(0,sql.length()-1);
        sql += " ) ";
        allSqlList.add(sql);
        String keySql = "ALTER TABLE [dbo].["+tableName+ "] ADD CONSTRAINT [PK__"+tableName+"__"+ RandomUtil.randomString(10)+"] PRIMARY KEY (ID)";
        allSqlList.add(keySql);
        String commentIdSql = "EXEC sp_addextendedproperty 'MS_Description', N'主键','SCHEMA', N'dbo','TABLE', N'"+tableName+"','COLUMN', N'ID' ";
        commentList.add(commentIdSql);
        String commentDateSql = "EXEC sp_addextendedproperty 'MS_Description', N'修改时间','SCHEMA', N'dbo','TABLE', N'"+tableName+"','COLUMN', N'MODIFY_DATE' ";
        commentList.add(commentDateSql);
        if(commentList.size()>0){
            allSqlList.addAll(commentList);
        }
        if(indexList.size()>0){
            allSqlList.addAll(indexList);
        }
        return allSqlList;
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
        List<String> commentList = new ArrayList<>();
        List<String> indexList = new ArrayList<>();
        // 同时出现在dtoList集合和dataSourceList集合中人的ColumnName集合,ColumnName相同即视为存在
        List<String> commonList = dtoList.stream()
                .map(t -> dataSourceList.stream().filter(s -> Objects.nonNull(t.getColumnName().toUpperCase())
                        && Objects.nonNull(s.getColumnName()) && Objects.equals(t.getColumnName().toUpperCase(), s.getColumnName().toUpperCase())).findAny().orElse(null))
                .filter(Objects::nonNull)
                .map(r -> r.getColumnName().toUpperCase())
                .collect(Collectors.toList());
//        增加一列：
//        alter table t_interface add test varchar2(10);
//        修改一列：
//        alter table t_interface modify test varchar2(20);
//        删除一列：
//        alter table t_interface drop column test;
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
            String isNotNull = "";
            if(StrUtil.isNotEmpty(dto.getColumnName())
                    && commonList.contains(dto.getColumnName().toUpperCase())){
                updateOrDelSql = "ALTER TABLE "+tableName+" ALTER COLUMN ";
                String commentSql = "EXEC sp_updateextendedproperty 'MS_Description', N'"+dto.getDemo()+"','SCHEMA', N'dbo','TABLE', N'"+tableName+"','COLUMN', N'"+dto.getColumnName()+"' ";
                commentList.add(commentSql);
            }else if(StrUtil.isNotEmpty(dto.getColumnName())){
                if(StrUtil.isNotEmpty(dto.getPreColumnName())){
                    //execute sp_rename '表名.字段名','新字段名'
                    //修改字段名
                    String updateSql = "execute sp_rename '"+tableName+"."+dto.getPreColumnName()+"','"+dto.getColumnName()+"'";
                    commentList.add(updateSql);
                }else{
                    //新增字段
                    updateOrDelSql = "ALTER TABLE "+tableName+" ADD ";
                    if(null!=dto.getIsNotNull() && dto.getIsNotNull()){
                        isNotNull = " NOT NULL ";
                    }else {
                        isNotNull = " NULL ";
                    }
                    String commentSql = "EXEC sp_addextendedproperty 'MS_Description', N'"+dto.getDemo()+"','SCHEMA', N'dbo','TABLE', N'"+tableName+"','COLUMN', N'"+dto.getColumnName()+"' ";
                    commentList.add(commentSql);
                }

            }
            if(null==dto.getIndexFlag() || !dto.getIndexFlag()){
                String dropIndexSql = "if exists(select * from sysindexes where name='INDEX_"+tableName+"_"+dto.getColumnName()+"') drop index INDEX_"+tableName+"_"+dto.getColumnName()+" on "+tableName;
                updateOrDelSqlList.add(dropIndexSql);
            }
            List<String> list = new ArrayList<>();
            list.add("BLOB");
            list.add("TEXT");
            list.add("DATE");
            list.add("TIMESTAMP");
            list.add("DATETIME");
            list.add("INT");
            if (!list.contains(dto.getColumnType().toUpperCase())) {
                updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType() + "(" + dto.getMaxLenth() + ") "+isNotNull;
            } else {
                updateOrDelSql += dto.getColumnName() + " " + dto.getColumnType() + " "+isNotNull;
            }
            updateOrDelSqlList.add(updateOrDelSql);
            if(null!=dto.getIndexFlag() && dto.getIndexFlag()){
                String dropIndexSql = "DROP INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ";
                indexList.add(dropIndexSql);
                String indexSql = "CREATE INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ON "+tableName+" ("+dto.getColumnName()+")";
                indexList.add(indexSql);
            }else {
                String dropIndexSql = "DROP INDEX INDEX_"+tableName+"_"+dto.getColumnName()+" ";
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
        if(commentList.size()>0){
            updateOrDelSqlList.addAll(commentList);
        }
        if(indexList.size()>0){
            updateOrDelSqlList.addAll(indexList);
        }
        return updateOrDelSqlList;
    }
}
