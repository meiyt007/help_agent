package com.zfsoft.ha.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 *
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/27 下午2:09
 */
@Configuration
public class HaDockingConfig implements WebMvcConfigurer {
    /**
     * 注入bean
     *
     * @return
     */
    @Bean
    public HaDockingInterceptor dockingInterceptor() {
        return new HaDockingInterceptor();
    }

    /**
     * @description: 注册拦截器方法
     * @return: void
     * @author: kangax
     * @date: 2022-07-27 14:18
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration registration = registry.addInterceptor(dockingInterceptor());
        registration.addPathPatterns("/ha/docking/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/ha/docking/getToken"//获取验证码
        );
    }

}
