package com.zfsoft.platform.utils.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName LogHandlerInterceptorConfig
 * @Description
 * @Author
 * @Date2020-09-16 14:52
 * @Version V1.0
 **/

@Configuration
@ConditionalOnProperty(name = "log.extend.fields",havingValue ="true")
public class LogHandlerInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new LogHandlerInterceptor());
        interceptor.addPathPatterns("/**");
    }
}
