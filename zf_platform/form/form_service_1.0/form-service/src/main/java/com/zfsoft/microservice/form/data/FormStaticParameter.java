package com.zfsoft.microservice.form.data;

import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 电子表单的静态资源类
 * @Author wuxx
 * @Date 2021/4/9
 **/
public class FormStaticParameter extends BaseStaticParameter {

    /**
     * 默认的管理员授权key
     */
    public static final String DEFAULT_AUTHORIZEKEY = "00000000000000000000000000000000";

    /**
     * 未设计
     */
    public static final Integer DATA_STATUS_0 = 0;
    /**
     * 草稿
     */
    public static final Integer DATA_STATUS_1 = 1;
    /**
     *  发布
     */
    public static final Integer DATA_STATUS_2 = 2;

    /**
     *  变更（作废）
     */
    public static final Integer DATA_STATUS_3 = 3;

    /**
     * 表单系统
     */
    public static final Integer SAVE_DATA_TYPE_0 = 0;
    /**
     * API
     */
    public static final Integer SAVE_DATA_TYPE_1 = 1;
    /**
     *  数据库
     */
    public static final Integer SAVE_DATA_TYPE_2 = 2;

    /**
     *  存储对象 属性 - 数据存储类型-0 字符串 1数组 2对象 3文件类 4Boolean   默认字符串
     */
    public static final Integer DATA_OBJECT_TYPE_0 = 0;

    /**
     *  存储对象 属性 - 数据存储类型-0 字符串 1数组 2对象 3文件类 4Boolean   默认字符串
     */
    public static final Integer DATA_OBJECT_TYPE_1 = 1;

    /**
     *  存储对象 属性 - 数据存储类型-0 字符串 1数组 2对象 3文件类 4Boolean   默认字符串
     */
    public static final Integer DATA_OBJECT_TYPE_2 = 2;

    /**
     *  存储对象 属性 - 数据存储类型-0 字符串 1数组 2对象 3文件类 4Boolean   默认字符串
     */
    public static final Integer DATA_OBJECT_TYPE_3 = 3;

    /**
     *  存储对象 属性 - 数据存储类型-0 字符串 1数组 2对象 3文件类 4Boolean   默认字符串
     */
    public static final Integer DATA_OBJECT_TYPE_4 = 4;

    /**
     *  数据存储类型map
     */
    public static final Map<Integer, String> SAVE_DATA_TYPE_MAP = new HashMap();

    /**
     * 数据库类型
     **/
    public static Map<String, List<String>> DATA_SOURCE_TYPE_MAP = new HashMap();

    /**
     * 存储对象 属性 - 数据存储类型
     **/
    public static List<Map<String, Object>> OBJECT_FIELD_SAVE_TYPE_LIST = new ArrayList();

    /**
     * 数据库字段类型-MYSQL
     **/
    public static List<String> MYSQL_TYPE_LIST = new ArrayList<>();

    /**
     * 数据库字段类型-oracle
     **/
    public static List<String> ORACLE_TYPE_LIST = new ArrayList<>();

    /**
     * 数据库字段类型-sqlserver
     **/
    public static List<String> SQLSERVER_TYPE_LIST = new ArrayList<>();

    /**
     * 数据库字段类型-postgreSQL
     **/
    public static List<String> POSTGRESQL_TYPE_LIST = new ArrayList<>();

    /**
     * 数据库字段类型-dm
     **/
    public static List<String> DM_TYPE_LIST = new ArrayList<>();

    /**
     * 数据库字段类型-mongoDB
     **/
    public static List<String> MONGODB_TYPE_LIST = new ArrayList<>();

    static {
        //MYSQL
        MYSQL_TYPE_LIST.add("CHAR");
        MYSQL_TYPE_LIST.add("VARCHAR");
        //MYSQL_TYPE_LIST.add("BLOB");
        MYSQL_TYPE_LIST.add("TEXT");
        MYSQL_TYPE_LIST.add("DATE");
        MYSQL_TYPE_LIST.add("DATETIME");
        MYSQL_TYPE_LIST.add("TIME");
        MYSQL_TYPE_LIST.add("TIMESTAMP");
        MYSQL_TYPE_LIST.add("INT");
        MYSQL_TYPE_LIST.add("BIGINT");
        MYSQL_TYPE_LIST.add("FLOAT");
        MYSQL_TYPE_LIST.add("DOUBLE");

        //ORACLE_TYPE_LIST.add("CHAR");
        ORACLE_TYPE_LIST.add("VARCHAR2");
        //ORACLE_TYPE_LIST.add("NCHAR");
        ORACLE_TYPE_LIST.add("NVARCHAR2");
        ORACLE_TYPE_LIST.add("DATE");
        ORACLE_TYPE_LIST.add("LONG");
        //ORACLE_TYPE_LIST.add("BLOB");
        ORACLE_TYPE_LIST.add("NUMBER");
        ORACLE_TYPE_LIST.add("FLOAT");
        ORACLE_TYPE_LIST.add("TIMESTAMP");

        SQLSERVER_TYPE_LIST.add("CHAR");
        SQLSERVER_TYPE_LIST.add("VARCHAR");
        SQLSERVER_TYPE_LIST.add("INT");
        SQLSERVER_TYPE_LIST.add("NUMERIC");
        SQLSERVER_TYPE_LIST.add("FLOAT");
        SQLSERVER_TYPE_LIST.add("DATETIME");
        SQLSERVER_TYPE_LIST.add("TEXT");
        //SQLSERVER_TYPE_LIST.add("BINARY");


        POSTGRESQL_TYPE_LIST.add("VARCHAR");
        POSTGRESQL_TYPE_LIST.add("CHARACTER");
        POSTGRESQL_TYPE_LIST.add("DATE");
        POSTGRESQL_TYPE_LIST.add("DATETIME");
        POSTGRESQL_TYPE_LIST.add("NUMERIC");
        POSTGRESQL_TYPE_LIST.add("FLOAT8");
        POSTGRESQL_TYPE_LIST.add("INTEGER");
        POSTGRESQL_TYPE_LIST.add("TEXT");
        POSTGRESQL_TYPE_LIST.add("TIMESTAMP");

        DM_TYPE_LIST.add("CHAR");
        DM_TYPE_LIST.add("VARCHAR");
        DM_TYPE_LIST.add("INT");
        DM_TYPE_LIST.add("BIGINT");
        //DM_TYPE_LIST.add("BYTE");
        DM_TYPE_LIST.add("FLOAT");
        DM_TYPE_LIST.add("DATE");
        DM_TYPE_LIST.add("DATETIME");
        DM_TYPE_LIST.add("TIMESTAMP");
        DM_TYPE_LIST.add("TEXT");
        //DM_TYPE_LIST.add("BLOB");

        DATA_SOURCE_TYPE_MAP.put("mysql",MYSQL_TYPE_LIST);
        DATA_SOURCE_TYPE_MAP.put("oracle",ORACLE_TYPE_LIST);
        DATA_SOURCE_TYPE_MAP.put("sqlserver",SQLSERVER_TYPE_LIST);
        DATA_SOURCE_TYPE_MAP.put("postgreSQL",POSTGRESQL_TYPE_LIST);
        DATA_SOURCE_TYPE_MAP.put("dm",DM_TYPE_LIST);
        DATA_SOURCE_TYPE_MAP.put("mongoDB",MONGODB_TYPE_LIST);


        SAVE_DATA_TYPE_MAP.put(SAVE_DATA_TYPE_0,"表单系统");
        SAVE_DATA_TYPE_MAP.put(SAVE_DATA_TYPE_1,"API");
        SAVE_DATA_TYPE_MAP.put(SAVE_DATA_TYPE_2,"数据库");

        //0 字符串 1数组 2对象 3文件类 4Boolean
        Map<Integer, String> OBJECT_FIELD_SAVE_TYPE_MAP = new HashMap();
        OBJECT_FIELD_SAVE_TYPE_MAP.put(DATA_OBJECT_TYPE_0,"字符串");
        OBJECT_FIELD_SAVE_TYPE_MAP.put(DATA_OBJECT_TYPE_1,"数组");
        OBJECT_FIELD_SAVE_TYPE_MAP.put(DATA_OBJECT_TYPE_2,"对象");
        OBJECT_FIELD_SAVE_TYPE_MAP.put(DATA_OBJECT_TYPE_3,"文件类");
        OBJECT_FIELD_SAVE_TYPE_MAP.put(DATA_OBJECT_TYPE_4,"Boolean");

        for (int i = 0;i<5;i++){
            Map<String, Object> OBJECT_FIELD_SAVE_TYPE_MAP_TEMP = new HashMap();
            OBJECT_FIELD_SAVE_TYPE_MAP_TEMP.put("id",i);
            OBJECT_FIELD_SAVE_TYPE_MAP_TEMP.put("value",OBJECT_FIELD_SAVE_TYPE_MAP.get(i));

            OBJECT_FIELD_SAVE_TYPE_LIST.add(OBJECT_FIELD_SAVE_TYPE_MAP_TEMP);
        }

    }

}
