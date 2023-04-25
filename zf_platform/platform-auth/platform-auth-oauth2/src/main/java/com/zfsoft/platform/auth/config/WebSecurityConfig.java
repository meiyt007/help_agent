package com.zfsoft.platform.auth.config;

import com.zfsoft.platform.auth.encoder.Md5PasswordEncoder;
import com.zfsoft.platform.auth.manager.SecurityUserDetailsManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SecurityUserDetailsManage securityUserDetailsManage;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //默认的认证操作
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //加密器
//        return new BCryptPasswordEncoder();
        return new Md5PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许匿名访问所有接口 主要是 oauth 接口
        http.authorizeRequests()
                .antMatchers("/**").permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsManage).passwordEncoder(passwordEncoder());
    }

}
