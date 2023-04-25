package com.zfsoft.platform.utils.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FeignCalledConfig
 * @Description
 * @Author
 * @Date2020-09-16 10:41
 * @Version V1.0
 **/
@Configuration
public class FeignCalledConfig {
    @Bean
    @ConditionalOnProperty(name = "client.feign.global.error.handler",havingValue ="true")
    public FeignCalledAdvice feignCalledAdvice(){
        return new FeignCalledAdvice();
    }
}
