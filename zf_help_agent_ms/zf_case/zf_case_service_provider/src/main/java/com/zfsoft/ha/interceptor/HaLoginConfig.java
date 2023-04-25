package com.zfsoft.ha.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/25 下午2:09
 */
@Configuration
public class HaLoginConfig implements WebMvcConfigurer {
    /**
     * 注入bean
     *
     * @return
     */
    @Bean
    public HaUserLoginInterceptor userLoginInterceptor() {
        return new HaUserLoginInterceptor();
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
        InterceptorRegistration registration = registry.addInterceptor(userLoginInterceptor());
        registration.addPathPatterns("/ha/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/ha/login/getLoginCheckCode",//获取验证码
                "/ha/login/loginByAccount",   //登录接口
                "/ha/scan/helpInfo",
                "/ha/takeNum/helpInfo",            //扫码填写帮代办信息接口
                "/ha/takeNum/queryAppointmentInfo", //查询预约信息
                "/ha/outer/**"     , //接收旗舰店的预约数据
                "/ha/docking/**"     , //接收旗舰店的预约数据
                //wangyh
                "/ha/banner/list",  //获取banner列表详情
                "/ha/login/checkCodeFlag",//获取验证码参数
                "/ha/login/gatewayPath",//获取网关配置
                "/ha/web/uploadCaseMaterialFile",//上传办件材料附件
                "/ha/knowledge/init",//初始化知识库
                "/ha/knowledge/**",
                "/ha/Matter/**"
        );
    }

}
