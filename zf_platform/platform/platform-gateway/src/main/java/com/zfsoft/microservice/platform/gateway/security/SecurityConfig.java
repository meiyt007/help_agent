package com.zfsoft.microservice.platform.gateway.security;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Sets;
import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import com.zfsoft.microservice.platform.gateway.security.handler.AuthenticationFaillHandler;
import com.zfsoft.microservice.platform.gateway.security.handler.AuthenticationSuccessHandler;
import com.zfsoft.microservice.platform.gateway.security.handler.CustomHttpBasicServerAuthenticationEntryPoint;
import com.zfsoft.microservice.platform.gateway.security.handler.LogOutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/20 11:43
 */
@Configuration
@EnableWebFluxSecurity()
@Slf4j
public class SecurityConfig {
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFaillHandler authenticationFaillHandler;
    @Autowired
    private CustomHttpBasicServerAuthenticationEntryPoint customHttpBasicServerAuthenticationEntryPoint;

    @Autowired
    private LogOutSuccessHandler logOutSuccessHandler;

    @Autowired
    private CaptchaServerFormLoginAuthenticationConverter authenticationConverter;

    @Autowired
    private ZhuofanConfig zhuofanConfig;

    @Autowired
    private ReactiveAuthorizationManager authenticationManager;

    //security的鉴权排除的url列表
    private static final String[] excludedAuthPages = {
            "/**/zfsoft/job/**",
            "/captcha-image",
            "/checkSMSVerification",
            "/genSMSVerification",
            "/getLoginCodeFlag",
            "/rsa-key",
            "/auth/login",
            "/auth/logout",
            //漏洞屏蔽
            "/health",
            /*"/actuator/**",*/
            "/favicon.ico",
            "/register/**",
            "/platform/security/atta/nologin/**",
            "/checkAccountAndCode",
            "/platform/security/findPassword/**",
            "/encrypt",
            "/platform/security/atta/uploadRegisterFile",
            "/platform/unauthentication/**",
            //表单系统
            "/form/manager/**",
            //接口请求过滤权限
            "/interface/api/**",
            "/platform/security/common/**",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**",
            "/**/v2/api-docs/**",
            /*"/cash-web-service-provider-dev/**",
            "/cash-web-service-provider-test/**",
            "/cash-web-service-provider-cbc/**",
            "/cash-web-service-provider-wzy/**",
            "/cash-web-service-provider-lsb/**",
            "/cash-web-service-provider-lx/**",
            "/cash-web-service-provider-zyq/**",
            "/cash-web-service-provider-kk/**",*/
            "/**/rest/api/**", // 后端对外提供的统一接口过滤规范
            "/**/cashWeb/**" // web端接口过滤规范
    };

    @Bean
    @RefreshScope
    public SecurityWebFilterChain webFluxSecurityFilterChain(MyContextAwareServerHttpSecurity http) throws Exception {
        Set<String> permitAlls = Sets.newConcurrentHashSet();
        permitAlls.addAll(Arrays.asList(excludedAuthPages));
        if(StringUtils.isNotEmpty(zhuofanConfig.getAccess())) {
            permitAlls.addAll(Arrays.asList(zhuofanConfig.getAccess().split(",")));
        }
        log.info(MessageFormat.format("忽略权限列表为：{0}", JSONArray.toJSONString(permitAlls)));
        http
            .authorizeExchange()
            .pathMatchers(permitAlls.toArray(new String[permitAlls.size()])).permitAll()  //无需进行权限过滤的请求路径
            .pathMatchers(HttpMethod.OPTIONS).permitAll() //option 请求默认放行
//            .pathMatchers("/**").access(authenticationManager)   //提交
            .pathMatchers("/resources/**").authenticated()
            .anyExchange().authenticated()
//                .and()
//                .httpBasic()
            .and()
            .requestCache().disable()
            .formLogin().loginPage("/auth/login")
            .authenticationConverter(authenticationConverter)
            .authenticationSuccessHandler(authenticationSuccessHandler) //认证成功
            .authenticationFailureHandler(authenticationFaillHandler) //登陆验证失败

                .authenticationManager(myReactiveAuthenticationManager)

                .and().exceptionHandling().authenticationEntryPoint(customHttpBasicServerAuthenticationEntryPoint)  //基于http的接口请求鉴权失败
            .and().csrf().disable()//必须支持跨域
            .logout()
            .logoutUrl("/auth/logout")
            .logoutSuccessHandler(logOutSuccessHandler);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
       // return new BCryptPasswordEncoder(4);
    }

    @Autowired(required = false)
    private ReactiveAuthenticationManager myReactiveAuthenticationManager;

    @Autowired(required = false)
    private ReactiveUserDetailsService reactiveUserDetailsService;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private ReactiveUserDetailsPasswordService userDetailsPasswordService;

    @Autowired(required = false)
    private BeanFactory beanFactory;

    @Bean
    @Scope("prototype")
    public MyContextAwareServerHttpSecurity httpSecurity() {
        MyContextAwareServerHttpSecurity http = new MyContextAwareServerHttpSecurity();
        http
                .authenticationManager(authenticationManager())
                .headers().and()
                .logout().and();
        return http;
    }

    private ReactiveAuthenticationManager authenticationManager() {
        if (this.myReactiveAuthenticationManager != null) {
            return this.myReactiveAuthenticationManager;
//            return new MyReactiveAuthenticationManager();
        }
        if (this.reactiveUserDetailsService != null) {
            UserDetailsRepositoryReactiveAuthenticationManager manager =
                    new UserDetailsRepositoryReactiveAuthenticationManager(this.reactiveUserDetailsService);
            if (this.passwordEncoder != null) {
                manager.setPasswordEncoder(this.passwordEncoder);
            }
            manager.setUserDetailsPasswordService(this.userDetailsPasswordService);
            return manager;
        }
        return null;
    }
}
