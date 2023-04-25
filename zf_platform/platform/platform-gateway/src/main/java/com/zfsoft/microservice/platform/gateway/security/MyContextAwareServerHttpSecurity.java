package com.zfsoft.microservice.platform.gateway.security;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/23 17:53
 */

public class MyContextAwareServerHttpSecurity extends ServerHttpSecurity implements
        ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        super.setApplicationContext(applicationContext);
    }
}
