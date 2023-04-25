package com.zfsoft.microservice.platform.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 */
@Data
@Configuration
@RefreshScope
public class ZhuofanConfig {
    /**
     * 默认开启跨站点请求伪造
     */
    @Value("${com.zfsoft.refererCheck:#{true}}")
    private boolean refererCheck;

    /**
     * 允许站点
     */
    @Value("${com.zfsoft.allowSites:#{null}}")
    private String allowSites;

    /**
     * 图片验证码 默认不开启
     */
    @Value("${com.zfsoft.login.validCode:#{false}}")
    private boolean validCode;

    /**
     * 访问的限制字符 如 "*,&"
     */
    @Value("${com.zfsoft.limitChars:#{null}}")
    private String limitChars;

    /**
     * 权限不拦截列表
     */
    @Value("${com.zfsoft.security.access:#{null}}")
    private String access;
}
