package com.zfsoft.microservice.form.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName DbSqlUtil
 * @Description: 对象转sql的工具类
 * @Author wuxx
 * @Date 2021/4/22
 **/
public class DbSqlUtil {

    /**
     * 生成插入语句
     *
     * @param tablename 表名
     * @param map       与数据库中字段一一对应的map
     */
    public static String getInsertSql(String tablename, Map<String, Object> map) {
        //insert into table_name (column_name1,column_name2, ...) values (value1,value2, ...)
        String sql = "";
        //Field[] fields = ReflectUtil.getFieldsDirectly(clazz, false);
        Set<String> stringSet = map.keySet();
        StringBuffer topHalf = new StringBuffer("insert into " + tablename + "(");
        StringBuffer afterAalf = new StringBuffer("values (");
        for (String field : stringSet) {
            topHalf.append(field + ",");
            if (map.get(field) instanceof String) {
                String fieldStr = (String) map.get(field);
                if (fieldStr.startsWith("DATE:")) {
                    fieldStr = fieldStr.replace("DATE:", "");
                    afterAalf.append(fieldStr + ",");
                } else {
                    afterAalf.append(StringUtils.isEmpty(fieldStr) ? (null + ",") : "'" + fieldStr + "',");
                }
            } else if(map.get(field) == null){
                afterAalf.append(" " + map.get(field) + ",");
            } else {
                afterAalf.append("'" + map.get(field) + "',");
            }
        }
        topHalf = new StringBuffer(StrUtil.removeSuffix(topHalf.toString(), ","));
        afterAalf = new StringBuffer(StrUtil.removeSuffix(afterAalf.toString(), ","));
        topHalf.append(") ");
        afterAalf.append(") ");
        sql = topHalf.toString() + afterAalf.toString();
        return sql;
    }

    /**
     * 生成更新语句
     * 必须含有id
     * 数据实体中 null 与 空字段不参与更新
     *
     * @param tablename 数据库中的表明
     * @param map       与数据库中字段一一对应的类
     */
    public static <T> String getUpdateSql(String tablename, Map<String, Object> map) {
        //UPDATE table_name SET column_name1 = value1, column_name2 = value2, ... where ID=xxx
        //or
        //UPDATE table_name SET column_name1 = value1, column_name2 = value2, ... where id=xxx
        String sql = "";
        String id = ""; //保存id名：ID or id
        //Field[] fields = ReflectUtil.getFieldsDirectly(clazz, false);
        Set<String> stringSet = map.keySet();
        sql = "update " + tablename + " set ";
        for (String field : stringSet) {
            StringBuffer tmp = new StringBuffer();
            if ("ID".equals(field) || "id".equals(field)) {
                id = field;
                continue;//更新的时候无需set id=xxx
            }
            if (map.get(field) != null && map.get(field) != "") {
                tmp.append(field + "=");
                if (map.get(field) instanceof String) {
                    String fieldStr = (String) map.get(field);
                    if (fieldStr.startsWith("DATE:")) {
                        fieldStr = fieldStr.replace("DATE:", "");
                        tmp.append(fieldStr + ",");
                    } else {
                        tmp.append(StringUtils.isEmpty(fieldStr) ? (null + ",") : ("'" + fieldStr + "',"));
                    }
                    //tmp.append( "'" + map.get(field) + "',");
//                } else if (map.get(field) instanceof JSONArray) {
//                    tmp.append("'" + ((List<Object>) map.get(field)).stream().map(String::valueOf).collect(Collectors.joining(",")) + "',");
                } else if (map.get(field) == null){
                    tmp.append(" " + map.get(field) + ",");
                } else {
                    tmp.append("'" + map.get(field) + "',");
                }
                sql += tmp;
            } else if(map.get(field) == null) {
                tmp.append(field + "=" + " NULL " + ",");
                sql += tmp;
            }
        }
        sql = StrUtil.removeSuffix(sql, ",") + " where " + id + "='" + map.get(id) + "'";
        return sql;
    }

    /**
     * 生成删除语句
     * 根据 user 中第一个不为空的字段删除,应该尽量使用 id,提供至少一个非空属性
     *
     * @param tablename 表明
     * @param map       有数据的实体map
     * @param <T>       数据实体类型 如 User
     */
    public static <T> String getDeleteSql(String tablename, Map<String, Object> map) throws IllegalArgumentException {
        //delete from table_name where column_name = value
        return getSelectOrDeleteSql(tablename, map, "delete");
    }

    /**
     * 生成查询语句
     * 根据 user 中第一个不为空的字段查询
     *
     * @param tablename 表名
     * @param map       有数据的实体map
     * @param <T>       数据实体类型 如 User
     */
    public static <T> String getSelectSql(String tablename, Map<String, Object> map) throws IllegalArgumentException {
        //delete from table_name where column_name = value
        return getSelectOrDeleteSql(tablename, map, "select *");
    }

    /**
     * 根据 operation 生成一个如：operation from table_name where column_name = value 的sql语句
     *
     * @param tablename
     * @param operation "select *"  or "delete"
     * @return
     * @throws IllegalArgumentException
     */
    private static <T> String getSelectOrDeleteSql(String tablename, Map<String, Object> map, String operation) throws IllegalArgumentException {
        //operation from table_name where column_name = value
        boolean flag = false;
        String sql = "";
        Set<String> strings = map.keySet();
        StringBuffer topHalf = new StringBuffer(operation + " from " + tablename + " where ");
        for (String field : strings) {
            if ("ID".equals(field) || "id".equals(field)) {
                if (map.get(field) != null) {
                    //id 不为空
                    topHalf.append(field + " = '" + map.get(field) + "'");
                    flag = true;
                    break;
                }
            } else {
                if (map.get(field) != null && (String) map.get(field) != "") {
                    topHalf.append(field + " = '" + map.get(field) + "'");
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            throw new ResultInfoException("数据查询失败，"+tablename+"无主键！");
        }
        sql = topHalf.toString();
        return sql;
    }
}
