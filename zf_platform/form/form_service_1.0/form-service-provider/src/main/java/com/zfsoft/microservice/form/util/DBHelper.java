package com.zfsoft.microservice.form.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mongodb.client.MongoCollection;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * @ClassName DBHelper
 * @Description:
 * @Author wuxx
 * @Date 2021/4/1
 **/
@Log4j2
public class DBHelper {
    //private Connection conn = null;
    //数据源类型
    private String type = null;

    //数据源连接url
    private String url = null;

    //数据源驱动
    private String driver = null;
    //ip
    private String username = null;
    //端口
    private String host = null;

    //用户名
    private String port = null;

    //密码
    private String password = null;

    //表名
    private String datasourceName = null;

    /**
     * @description: 根据数据库对象配置初始化
     * @param dataSource 数据库对象
     * @author: wuxx
     * @Date: 2021/4/14 14:46
     **/
    public DBHelper(FormDataSource dataSource){
        this.type = dataSource.getType();
        this.username = dataSource.getUsername();
        this.password = dataSource.getPassword();
        this.datasourceName = dataSource.getDatasourceName();
        this.host = dataSource.getHost();
        this.port = dataSource.getPort();
        String serviceName = dataSource.getServiceName();
        serviceName = StrUtil.isNotBlank(serviceName)?serviceName:"orcl11g";
        if(StrUtil.isNotBlank(type)){
            switch (type.toLowerCase()){
                case "mysql":
                    //url = "jdbc:mysql://172.168.250.1:3306/zfsoft_form_v1.0";
                    url = "jdbc:mysql://"+host+":"+port+"/"+datasourceName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                    driver = "com.mysql.cj.jdbc.Driver";
                    break;
                case "oracle":
                    //url = "jdbc:oracle:thin:@127.0.0.1:1521/orcl";
                    url = "jdbc:oracle:thin:@"+host+":"+port+"/"+serviceName+ "?allowPublicKeyRetrieval=true";
                    driver = "oracle.jdbc.driver.OracleDriver";
                    break;
                case "sqlserver":
                    //url = "jdbc:sqlserver://localhost:1433;DatabaseName=java_conn_test";
                    url = "jdbc:sqlserver://"+host+":"+port+";DatabaseName="+datasourceName;
                    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                    break;
                case "dm":
                    //url ="jdbc:dm://172.168.250.89:6236/ZFSOFT";
                    url = "jdbc:dm://"+host+":"+port+"/"+datasourceName;
                    driver = "dm.jdbc.driver.DmDriver";
                    break;
                case "postgresql":
                    //url ="jdbc:postgresql://172.168.250.89:5432/platform";
                    url = "jdbc:postgresql://"+host+":"+port+"/"+datasourceName;
                    driver = "org.postgresql.Driver";
                    break;
            }
        }
    }

    /**
     * @description: 检查数据源配置连接状态是否正常
     * @author: wuxx
     * @Date: 2021/4/1 16:04
     **/
    public Map<String,String> checkConnection(){
        Map<String,String> map = new HashMap<>();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                MongoDBUtil mongoDBUtil = new MongoDBUtil(host,port,datasourceName,username,password);
                Map<String, String> stringMap = mongoDBUtil.checkMongoDbConnnect();
                return stringMap;
            }else {
                Class.forName(driver);
                Connection conn = DriverManager.getConnection(url, username, password);
                if(null==conn){
                    map.put("success","false");
                    map.put("message","连接失败！");
                    return map;
                }else {
                    conn.close();
                    map.put("success","true");
                    return map;
                }
            }

        }catch (Exception e){
            //e.printStackTrace();
            map.put("success","false");
            map.put("message",e.getMessage());
            return map;
        }

    }

    /**
     * @description: 根据数据源配置获取连接
     * @author: wuxx
     * @Date: 2021/4/1 16:04
     **/
    public Connection getConnection(){
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("数据库连接失败，请稍后再试");
        }
    }

    /**
     * @description: 关闭数据源连接
     * @author: wuxx
     * @Date: 2021/4/1 16:04
     **/
    public void closeConnection(Connection conn) {
        try {
            if(conn!=null) {
               conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @description: 获取数据源下的所有表名
     * @author: wuxx
     * @Date: 2021/4/2 16:07
     **/
    public List<String> getTableNames(Connection conn) {
        List<String> tableList = new ArrayList<>();
        try {
            if(null!=conn){
                DatabaseMetaData metaData = conn.getMetaData();
                String connSchema = datasourceName;
                ResultSet resultSet = metaData.getTables(datasourceName, connSchema, "%",  new String[] { "TABLE" });
                while (resultSet.next()) {
                    //表名
                    String tableName = resultSet.getString(3);
                    tableList.add(tableName.toLowerCase());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return tableList;
        }
        return tableList;
    }

    /**
     * @description: 获取数据源下的所有表名-Sqlserver
     * @author: wuxx
     * @Date: 2021/4/2 16:07
     **/
    public List<String> getTableNamesSqlserver(Connection conn) {
        List<String> tableList = new ArrayList<>();
        try {
            if(null!=conn){
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT name FROM "+datasourceName+"..sysobjects Where xtype='U' ORDER BY name");
                while(resultSet.next()){
                    //表名
                    String tableName = resultSet.getString("name");
                    tableList.add(tableName.toLowerCase());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return tableList;
        }
        return tableList;
    }

    /**
     * @description: 获取数据源下的所有表名Oracle
     * @author: wuxx
     * @Date: 2021/4/2 16:07
     **/
    public List<String> getTableNamesOracle(Connection conn) {
        List<String> tableList = new ArrayList<>();
        try {
            if(null!=conn){
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER='"+datasourceName.toUpperCase()+"'");
                while(resultSet.next()){
                    //表名
                    String tableName = resultSet.getString("TABLE_NAME");
                    tableList.add(tableName.toLowerCase());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return tableList;
        }
        return tableList;
    }

    /**
     * @description: 获取数据源下的所有表名postgresql
     * @author: wuxx
     * @Date: 2021/4/2 16:07
     **/
    public List<String> getTableNamesPg(Connection conn) {
        List<String> tableList = new ArrayList<>();
        try {
            if(null!=conn){
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select relname as name from pg_class c \n" +
                        "where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname\n");
                while(resultSet.next()){
                    //表名
                    String tableName = resultSet.getString("name");
                    tableList.add(tableName.toLowerCase());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return tableList;
        }
        return tableList;
    }

    /**
     * @description: 根据表名获取数据源下的字段（关系型数据库）
     * @param conn 连接
     * @param tableName 表名
     * @author: wuxx
     * @Date: 2021/4/2 16:28
     **/
    private List<Map<String, Object>> getRelationColumnsByTableName(Connection conn,String tableName,Boolean tipFlag) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            List<String> tableNames = null;
            if(type.toLowerCase().equals("sqlserver")){
                tableNames = getTableNamesSqlserver(conn);
            }else if(type.toLowerCase().equals("postgresql")){
                tableNames = getTableNamesPg(conn);
            }else if(type.toLowerCase().equals("oracle")){
                tableNames = getTableNamesOracle(conn);
            }else {
                tableNames = getTableNames(conn);
            }
            if(!tableNames.contains(tableName.toLowerCase())){
                if(!tipFlag){
                    return null;
                }
                throw new ResultInfoException("当前数据库表名不存在！");
            }
            for (String name : tableNames){
                if(tableName.toLowerCase().equals(name.toLowerCase())){
                    //oracle需要特殊处理
                    if(type.toLowerCase().equals("oracle")){
                        String sql= "select  comments as \"Name\",    a.column_name \"Code\",        " +
                                        " a.DATA_TYPE as \"DataType\", a.DATA_LENGTH as \"DataLength\", a.NULLABLE as \"nullAble\",     " +
                                        "a.DATA_SCALE as \"DECIMAL_DIGITS\", " +
                                        "  b.comments as \"Comment\",  b.owner  from  all_tab_columns a, all_col_comments b   where   " +
                                        "   a.Table_Name=b.table_Name      and a.column_name=b.column_name    " +
                                        " and a.Table_Name='"+tableName.toUpperCase()+"'  and a.owner=b.owner  " +
                                        " and b.owner='"+datasourceName.toUpperCase()+"' order by a.COLUMN_ID ";
                        ResultSet rs1 = conn.createStatement().executeQuery(sql);
                        while (rs1.next()){
                            Map<String, Object> map = new HashMap<>();
                            // 获得指定列的列名
                            String columnName = rs1.getString("Code");
                            map.put("columnName",columnName);
                            // 获得指定列的数据类型名
                            String columnTypeName = rs1.getString("DataType");
                            map.put("columnType",columnTypeName.toLowerCase());
                            // 在数据源中类型的最大字符个数
                            String columnDisplaySize = rs1.getString("DataLength");
                            if (StringUtils.equalsIgnoreCase(columnTypeName, "NVARCHAR2")) {
                                columnDisplaySize = String.valueOf(Integer.valueOf(columnDisplaySize) / 2);
                            }
                            String decimalDigits = rs1.getString(ResultSetColumnKeys.DECIMAL_DIGITS.name());
                            map.put("maxLenth", StringUtils.isNotEmpty(decimalDigits) ? columnDisplaySize + "," + decimalDigits : columnDisplaySize);
                            if("TIMESTAMP(0)".equals(columnTypeName)){
                                map.put("maxLenth",0);
                                map.put("columnType","TIMESTAMP");
                            }
                            // 是否为空
                            String isNullable = rs1.getString("nullAble");
                            map.put("notNull", "Y".equals(isNullable)?0:1);
                            //获取注释
                            String remarks = rs1.getString("Comment");
                            map.put("demo",remarks);
                            mapList.add(map);
                        }
                    }else if(type.toLowerCase().equals("sqlserver")){
                        String sql= "SELECT  a.name columnName,b.name columnType,\n" +
                                "COLUMNPROPERTY(a.id,a.name,'PRECISION') as maxLenth,(case when a.isnullable=1 then 'Y'else '' end) notNull,\n" +
                                "isnull(g.[value], ' ') AS demo\n" +
                                "FROM syscolumns a\n" +
                                "left join systypes b on a.xtype=b.xusertype\n" +
                                "inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties'\n" +
                                "left join syscomments e on a.cdefault=e.id\n" +
                                "left join sys.extended_properties g on a.id=g.major_id AND a.colid=g.minor_id\n" +
                                "left join sys.extended_properties f on d.id=f.class and f.minor_id=0\n" +
                                "where b.name is not null\n" +
                                "AND d.name='"+tableName+"' \n" +
                                "order by a.id,a.colordeR;";
                        ResultSet rs1 = conn.createStatement().executeQuery(sql);
                        while (rs1.next()){
                            Map<String, Object> map = new HashMap<>();
                            // 获得指定列的列名
                            String columnName = rs1.getString("columnName");
                            map.put("columnName",columnName);
                            // 获得指定列的数据类型名
                            String columnTypeName = rs1.getString("columnType");
                            map.put("columnType",columnTypeName.toLowerCase());
                            // 在数据源中类型的最大字符个数
                            String columnDisplaySize = rs1.getString("maxLenth");
                            map.put("maxLenth",columnDisplaySize);
                            // 是否为空
                            String isNullable = rs1.getString("notNull");
                            map.put("notNull", "Y".equals(isNullable)?0:1);
                            //获取注释
                            String remarks = rs1.getString("demo");
                            map.put("demo",remarks);
                            mapList.add(map);
                        }
                    }else if(type.toLowerCase().equals("postgresql")){//postgreSQL
                        String sql= "SELECT\n" +
                                " col_description ( a.attrelid, a.attnum ) AS demo,\n" +
                                " format_type ( a.atttypid, a.atttypmod ) AS columnType,\n" +
                                " a.attname AS columnName,\n" +
                                " a.attnotnull AS notNull, \n" +
                                " a.attcollation AS maxLenth \n" +
                                " FROM\n" +
                                " pg_class AS c,\n" +
                                " pg_attribute AS a \n" +
                                " WHERE\n" +
                                " c.relname = '"+tableName.toLowerCase()+"' \n" +
                                " AND a.attrelid = c.oid \n" +
                                " AND a.attnum >0";
                        ResultSet rs1 = conn.createStatement().executeQuery(sql);
                        while (rs1.next()){
                            Map<String, Object> map = new HashMap<>();
                            // 获得指定列的列名
                            String columnName = rs1.getString("columnName");
                            if(StrUtil.isNotEmpty(columnName) && columnName.contains("pg.dropped")){
                                continue;
                            }
                            map.put("columnName",columnName);
                            // 获得指定列的数据类型名
                            String columnTypeName = rs1.getString("columnType");
                            map.put("columnType",columnTypeName);
                            // 在数据源中类型的最大字符个数
                            String columnDisplaySize = rs1.getString("maxLenth");
                            map.put("maxLenth",columnDisplaySize);
                            // 是否为空
                            String isNullable = rs1.getString("notNull");
                            map.put("notNull", "Y".equals(isNullable)?0:1);
                            //获取注释
                            String remarks = rs1.getString("demo");
                            map.put("demo",remarks);
                            mapList.add(map);
                        }
                    }else{
                        DatabaseMetaData metaData = conn.getMetaData();
                        ResultSet pkRSet = metaData.getPrimaryKeys(datasourceName, null, tableName);
                        String pk = null;
                        while( pkRSet.next() ) {
                            if ("1".equals(pkRSet.getObject(5).toString())) {
                                pk = pkRSet.getObject(4).toString();
                            }
                        }
                        pkRSet.close();
                        ResultSet rs = metaData.getColumns(conn.getCatalog(), "%", tableName, "%");
                        while (rs.next()) {
                            Map<String, Object> map = new HashMap<>();
                            // 获得指定列的列名
                            String columnName = rs.getString(ResultSetColumnKeys.COLUMN_NAME.name());
                            map.put("columnName",columnName);
                            //判断主键
                            if(null!=pk && pk.equals(columnName)){
                                map.put("columnPk",1);
                                // 是否自动递增
                                //String isAutoInctement = rs.getString(ResultSetColumnKeys.IS_AUTOINCREMENT.name());
                                //map.put("isAutoInctement",isAutoInctement);
                            }else {
                                map.put("columnPk",0);
                            }
                            // 获得指定列的数据类型名
                            String columnTypeName = rs.getString(ResultSetColumnKeys.TYPE_NAME.name());
                            map.put("columnType",columnTypeName.toLowerCase());
                            // 在数据源中类型的最大字符个数
                            String columnDisplaySize = rs.getString(ResultSetColumnKeys.COLUMN_SIZE.name());
                            String decimalDigits = rs.getString(ResultSetColumnKeys.DECIMAL_DIGITS.name());
                            map.put("maxLenth", columnDisplaySize + (StringUtils.isNotEmpty(decimalDigits) ? "," + decimalDigits : ""));
                            // 是否为空
                            String isNullable = rs.getString(ResultSetColumnKeys.IS_NULLABLE.name());
                            map.put("notNull", "YES".equals(isNullable)?0:1);
                            //获取注释
                            String remarks = rs.getString(ResultSetColumnKeys.REMARKS.name());
                            map.put("demo",remarks);
                            mapList.add(map);
                        /*logger.info("列名称：\t{}", rs.getString(ResultSetColumnKeys.COLUMN_NAME.name()));
                        logger.info("java.sql.Types：\t{}", rs.getString(ResultSetColumnKeys.DATA_TYPE.name()));
                        logger.info("字段类型：\t{}", rs.getString(ResultSetColumnKeys.TYPE_NAME.name()));
                        logger.info("列的大小：\t{}", rs.getString(ResultSetColumnKeys.COLUMN_SIZE.name()));
                        logger.info("小数部分的位数：\t{}", rs.getString(ResultSetColumnKeys.DECIMAL_DIGITS.name()));
                        logger.info("基数：\t{}", rs.getString(ResultSetColumnKeys.NUM_PREC_RADIX.name()));
                        logger.info("描述列的注释：\t{}", rs.getString(ResultSetColumnKeys.REMARKS.name()));
                        logger.info("该列的默认值：\t{}", rs.getString(ResultSetColumnKeys.COLUMN_DEF.name()));
                        logger.info("列中的最大字节数：\t{}", rs.getString(ResultSetColumnKeys.CHAR_OCTET_LENGTH.name()));
                        logger.info("列的索引：\t{}", rs.getString(ResultSetColumnKeys.ORDINAL_POSITION.name()));
                        logger.info("是否允许使用 NULL：\t{}", rs.getString(ResultSetColumnKeys.IS_NULLABLE.name()));
                        logger.info("指示此列是否自动增加：\t{}", rs.getString(ResultSetColumnKeys.IS_AUTOINCREMENT.name()));*/
                        }
                        rs.close();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException(e.getMessage());
            //return mapList;
        }
        return mapList;
    }


    /**
     * @description: 根据表获取属性
     * @param objectForm 表
     * @param tipFlag 是否提示报错原因
     * @author: wuxx
     * @Date: 2021/4/15 17:06
     **/
    public List<Map<String, Object>> getColumnsByTableName(String objectForm,Boolean tipFlag) {
        //mongdb
        if(type.toLowerCase().equals("mongodb")){
            MongoDBUtil mongoDBUtil = new MongoDBUtil(host,port,datasourceName,username,password);
            MongoCollection<Document> collection = mongoDBUtil.getCollection(objectForm);
            Document document = collection.find().first();
            if(null!=document){
                List<Map<String, Object>> mapList = new ArrayList<>();
                Map<String, Object> hashMap = MongoDBUtil.toHashMap(document);
                for(Map.Entry<String, Object> entry : hashMap.entrySet()){
                    String mapKey = entry.getKey();
                    if("_class".equals(mapKey)){
                        continue;
                    }
                    Map<String, Object> map = new HashMap<>();
                    if("_id".equals(mapKey)){
                        map.put("notNull", 1);
                        map.put("columnPk",1);
                        map.put("demo","主键");
                    }
                    map.put("columnName",mapKey);
                    mapList.add(map);
                }
                return mapList;
            }
        }else {
            //mysql postgresql
            Connection connection = this.getConnection();
            return this.getRelationColumnsByTableName(connection, objectForm,tipFlag);
        }
        return null;
    }

    /**
     * @description:  数据库插入操作
     * @param sql sql
     * @author: wuxx
     * @Date: 2021/4/22 18:23
     **/
    public String insertDbSql(String sql,String tablename, Map<String, Object> dataMap){
        PreparedStatement pstmt = null;
        Connection connection = null;
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                MongoDBUtil mongoDBUtil = new MongoDBUtil(host,port,datasourceName,username,password);
                /*IdWorker idWorker=new IdWorker(0,0);
                long nextId = idWorker.nextId();*/
                String uuid = IdUtil.simpleUUID();
                boolean flag = mongoDBUtil.saveMongoDBObject(dataMap, uuid, new Date(), tablename);
                if(flag){
                    return uuid;
                }
                return null;
            }else {
                connection = getConnection();
                pstmt = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
                int addIndex = pstmt.executeUpdate();
                return dataMap.get("id")+"";
                //ps.executeBatch();
                // 检索由于执行此 Statement 对象而创建的所有自动生成的键
                /*ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Object id = rs.getObject(1);
                    if(addIndex>0){
                        return id.toString()+"";
                    }
                    //System.out.println("数据主键：" + id);
                }else {
                    return dataMap.get("id")+"";
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description:  数据库查询操作
     * @param sql sql
     * @author: wuxx
     * @Date: 2021/4/22 18:23
     **/
    public Map<String, Object> selectDbSql(String sql,String tablename, Map<String, Object> dataMap,String objectForm){
        PreparedStatement pstmt = null;
        Connection connection = null;
        Map<String, Object> resultMap=null;
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                MongoDBUtil mongoDBUtil = new MongoDBUtil(host,port,datasourceName,username,password);
                Object id = null == dataMap.get("_id")?dataMap.get("id"):dataMap.get("_id");
                if(null==id){
                    throw new ResultInfoException("主键id不能为空！");
                }
                Document document = mongoDBUtil.getMongoDBInfoByOid(objectForm, id + "");
                Map toHashMap = mongoDBUtil.toHashMap(document);
                resultMap=new HashMap<>();
                resultMap.putAll(toHashMap);
                resultMap.put("_id",id);
                return resultMap;
            }else {
                connection = getConnection();
                pstmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = pstmt.executeQuery();
                resultSet.beforeFirst();
                Set<String> stringSet = dataMap.keySet();
                while (resultSet.next()) {
                    resultMap=new HashMap<>();
                    for (String property : stringSet){
                        resultMap.put(property,resultSet.getObject(property));
                    }
                   // System.out.println(resultSet.getString(""));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return resultMap;
    }

    public List<Map<String, Object>> selectListDbSql(String sql,String tablename,Map<String, Object> dataMap,String objectForm){
        PreparedStatement pstmt = null;
        Connection connection = null;
        List<Map<String, Object>> list=null;
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = pstmt.executeQuery();
                Set<String> stringSet = dataMap.keySet();
                list=new ArrayList();
                while (resultSet.next()) {
                    Map<String, Object> data=new HashMap<>();
                    for (String property : stringSet){
                        data.put(property,resultSet.getObject(property));
                    }
                    list.add(data);
                }
                if(list.size()==0){
                    list=null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * @description: 根据sql创建表记录
     * @param sql
     * @author: wuxx
     * @Date: 2021/8/4 10:03
     **/
    public Map<String, Object> createTableByDbSql(String sql){
        Statement  pstmt = null;
        Connection connection = null;
        Map<String, Object> map = new HashMap<>();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.createStatement();
                pstmt.executeUpdate(sql);
                map.put("success",true);
                return map;
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("message",e.getMessage());
            return map;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * @description: 根据sql修改表记录、删除表记录,执行其他sql
     * @param sql
     * @author: wuxx
     * @Date: 2021/8/4 10:03
     **/
    public Map<String, Object> executeByDbSql(String sql){
        Statement  pstmt = null;
        Connection connection = null;
        Map<String, Object> map = new HashMap<>();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.createStatement();
                if(pstmt.executeUpdate(sql)>0){
                    map.put("success",true);
                }else {
                    map.put("success",false);
                    map.put("message","执行记录为0条");
                }
                return map;
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("message",e.getMessage());
            return map;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 根据sql修改表记录、删除表记录,执行其他sql
     * @param sqlList
     * @author: wuxx
     * @Date: 2021/8/4 10:03
     **/
    public Map<String, Object> executeBatchBySql(List<String> sqlList){
        Statement  pstmt = null;
        Statement statement =null;
        Connection connection = null;
        Map<String, Object> map = new HashMap<>();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.createStatement();
                for(String sql : sqlList){
                    //Drop 可能不存在的情况报错，忽略
                    if(sql.toUpperCase().contains("DROP") || sql.toUpperCase().contains("ALTER")|| sql.toUpperCase().contains("CREATE")){
                        try {
                            statement = connection.createStatement();
                            statement.executeUpdate(sql);
                        }catch (Exception e){
                            if(!StringUtils.startsWithIgnoreCase(sql.replaceAll(" +", ""), "DROPINDEX")) {
                                // modify by kkfan 异常处理
                                log.error("sql执行出错：" + sql, e);
                                throw new ResultInfoException("sql执行出错：" + e.toString());
                            }
                           // e.printStackTrace();
                        }
                    }else {
                        pstmt.addBatch(sql);
                    }
                }
                pstmt.executeBatch();
                map.put("success",true);
                return map;
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("message",e.getMessage());
            return map;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=statement){
                    statement.close();
                }
                if(null!=connection){
                    connection.setAutoCommit(true);
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * @description: 根据sql查看单个数据的表
     * @param sql
     * @author: wuxx
     * @Date: 2021/8/4 10:03
     **/
    public List<Map<String, Object>> selectDbSql(String sql,Map<String, Object> dataMap){
        PreparedStatement pstmt = null;
        Connection connection = null;
        List<Map<String, Object>> list=new ArrayList();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = pstmt.executeQuery();
                if(dataMap.size()>0){
                    Set<String> stringSet = dataMap.keySet();
                    while (resultSet.next()) {
                        Map<String, Object> data=new HashMap<>();
                        for (String property : stringSet){
                            data.put(property,resultSet.getObject(property));
                        }
                        list.add(data);
                    }
                }else {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    while (resultSet.next()) {
                        Map<String, Object> data=new HashMap<>();
                        for(int i=1;i<columnCount ;i++){
                            String columnName = resultSet.getMetaData().getColumnName(i);
                            Object value = resultSet.getObject(i);
                            data.put(columnName,value);
                        }
                        list.add(data);
                    }
                }

                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 根据sql查看数据库数据得多少
     * @param sql
     * @author: wuxx
     * @Date: 2021/11/23 10:03
     **/
    public int selectDbSqlCount(String sql){
        PreparedStatement pstmt = null;
        Connection connection = null;
        List<Map<String, Object>> list=new ArrayList();
        try {
            //mongdb
            if(type.toLowerCase().equals("mongodb")){
                throw new ResultInfoException("暂不支持！");
            }else {
                connection = getConnection();
                pstmt = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = pstmt.executeQuery();
                //resultSet.last();
                if (resultSet.next()) {
                    Object value = resultSet.getObject(1);
                    return Integer.parseInt(value.toString());
                }
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }finally {
            try {
                if(null!=pstmt){
                    pstmt.close();
                }
                if(null!=connection){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * @description:  map转对象
     * @param map
     * @param beanClass
     * @author: wuxx
     * @Date: 2021/4/14 15:51
     **/
    public Object mapToObject(Map<String, Object> map, Class<?> beanClass){
        try {
            if (map == null)
                return null;
            Object obj = beanClass.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description:  驼峰命名转换
     * @param field 字段名称
     * @author: wuxx
     * @Date: 2021/4/14 15:55
     **/
    public String changeToJavaFiled(String field) {
        if (!field.contains("_")){
            return field.toLowerCase();
        }
        String[] fields = field.toLowerCase().split("_");
        StringBuilder sbuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sbuilder.append(String.valueOf(cs));
        }
        return sbuilder.toString();
    }
}
