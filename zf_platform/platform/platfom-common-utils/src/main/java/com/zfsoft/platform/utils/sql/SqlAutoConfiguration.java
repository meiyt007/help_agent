package com.zfsoft.platform.utils.sql;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: kkfan
 * @create: 2021-09-27 09:59:21
 * @description: 注册特殊字符转义拦截器
 *     1、com.zfsoft.query.special.characters-escape 为拦截器是否生效配置 默认为true
 *     2、com.zfsoft.query.special.characters 为拦截器需转义的字符配置 默认为 %,_
 *     3、注意拦截器须在PageHelper拦截器后注册
 */
@AutoConfigureAfter({MybatisAutoConfiguration.class})
@AutoConfigureBefore(PageHelperAutoConfiguration.class)
@ConditionalOnProperty(name = "com.zfsoft.query.special.characters-escape", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Interceptor.class})
public class SqlAutoConfiguration {

    @Autowired(required = false)
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addMyInterceptor() {
        if (sqlSessionFactoryList == null) {
            return;
        }
        QueryInterceptor myInterceptor = new QueryInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(myInterceptor);
        }
    }

}
