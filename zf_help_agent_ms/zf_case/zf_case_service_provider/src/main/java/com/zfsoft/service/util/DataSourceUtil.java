package com.zfsoft.service.util;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.beans.PropertyEditorSupport;

/**
 * 数据库连接工具类
 *
 * @author yuy
 * @date 2021-8-3
 */
public class DataSourceUtil extends PropertyEditorSupport {

    public static void main(String[] strs) throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory(
                "jdbc:mysql://127.0.0.1:3306/test_0803", "root", "root");
        SqlSession sqlsession = sqlSessionFactory.openSession();
        // 调用数据库操作方法
        sqlsession.close();
    }

    /**
     * 获取SqlSessionFactory
     *
     * @author yuy
     * @date 2021-8-3
     * @param url
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public static SqlSessionFactory getSqlSessionFactory(String url, String userName, String password) throws Exception {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        // mapper路径
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:com/zfsoft/dbaccess/mapper/formConfig/DbSxFormInfoMapper.xml");
        // MybatisSqlSessionFactoryBean对象
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

}
