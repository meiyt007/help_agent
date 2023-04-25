package com.zfsoft.platform.utils.validate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamValidConfig {
    @Bean
    @ConditionalOnProperty(name = "api.param.validate",havingValue ="true")
    @ConditionalOnMissingBean(ParamValidAdvice.class)
    public ParamValidAdvice paramValidAdvice(){
        return new ParamValidAdvice();
    }
}
