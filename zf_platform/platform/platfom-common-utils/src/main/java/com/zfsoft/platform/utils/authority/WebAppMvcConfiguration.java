package com.zfsoft.platform.utils.authority;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: kkfan
 * @create: 2021-11-05 09:16:01
 * @description: WebMvc配置
 */
@Configuration
@ConditionalOnClass(HttpServletRequest.class)
public class WebAppMvcConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public WrapApiResultInterceptor wrapApiResultInterceptor() {
        return new WrapApiResultInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new WrapApiResultInterceptor());
        interceptor.addPathPatterns("/**");
    }
}
