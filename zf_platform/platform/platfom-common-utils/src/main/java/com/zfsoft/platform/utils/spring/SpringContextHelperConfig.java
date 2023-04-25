package com.zfsoft.platform.utils.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入SpringContextHelper bean
 */
@Configuration
public class SpringContextHelperConfig{
    @Bean
    public SpringContextHelper springContextHelper(){
        return new SpringContextHelper();
    }
}
