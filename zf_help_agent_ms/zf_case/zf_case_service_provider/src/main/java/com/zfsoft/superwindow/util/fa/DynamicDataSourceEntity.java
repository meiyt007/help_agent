package com.zfsoft.superwindow.util.fa;

import java.io.Serializable;

/**
 *
 * @author HHUA
 *
 */
public class DynamicDataSourceEntity implements Serializable {

    public final static String DB_TYPE_SQLSERVER = "sqlserver";
    public final static String DB_TYPE_MYSQL = "mysql";
    public final static String DB_TYPE_ORACLE = "oracle";
    public final static String DB_TYPE_DM = "dm";
    public final static String DB_TYPE_KINGBASE = "kingbase";

    private static final long serialVersionUID = 3513594060380235098L;
    private static DynamicDataSourceEntity dynamicDataSourceEntity = null;

    // 用于将code,name放入到缓存中
    /** id */
    private String id;
    /** dbKey */
    private String dbKey;
    /** description */
    private String description;
    /** driverClass */
    private String driverClass;
    /** url */
    private String url;
    /** dbUser */
    private String dbUser;
    /** dbPassword */
    private String dbPassword;
    /** dbType */
    private String dbType;

    private DynamicDataSourceEntity() {

    }

    public synchronized static DynamicDataSourceEntity createNewInstance() {
        if (dynamicDataSourceEntity == null) {
            dynamicDataSourceEntity = new DynamicDataSourceEntity();
            PropertiesUtil pu = new PropertiesUtil("datasource.properties");
            dynamicDataSourceEntity.setDriverClass(pu.readProperty("jdbc.driverClassName"));
            dynamicDataSourceEntity.setUrl(pu.readProperty("jdbc.url"));
            dynamicDataSourceEntity.setDbUser(pu.readProperty("jdbc.user"));
            dynamicDataSourceEntity.setDbPassword(pu.readProperty("jdbc.pass"));
            String hibernateDialect = pu.readProperty("hibernate.dialect");
            dynamicDataSourceEntity.setDbType(DB_TYPE_MYSQL);
            String mySql = "MySQL";
            String oracle = "Oracle";
            String sqlServer = "SQLServer";
            String dm = "Dm";
            String kingbase = "Kingbase";
            if (hibernateDialect.contains(mySql)) {
                dynamicDataSourceEntity.setDbType(DB_TYPE_MYSQL);
            } else if (hibernateDialect.contains(oracle)) {
                dynamicDataSourceEntity.setDbType(DB_TYPE_ORACLE);
            } else if (hibernateDialect.contains(sqlServer)) {
                dynamicDataSourceEntity.setDbType(DB_TYPE_SQLSERVER);
            }else if (hibernateDialect.contains(dm)) {
                dynamicDataSourceEntity.setDbType(DB_TYPE_DM);
            }else if (hibernateDialect.contains(kingbase)) {
                dynamicDataSourceEntity.setDbType(DB_TYPE_KINGBASE);
            }
        }
        return dynamicDataSourceEntity;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String id
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String dbKey
     */
    public String getDbKey() {
        return this.dbKey;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             dbKey
     */
    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String driverClass
     */
    public String getDriverClass() {
        return this.driverClass;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             driverClass
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String dbUser
     */
    public String getDbUser() {
        return this.dbUser;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             dbUser
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String dbPassword
     */
    public String getDbPassword() {
        return this.dbPassword;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             dbPassword
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String dbType
     */
    public String getDbType() {
        return this.dbType;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     *             dbType
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

}
