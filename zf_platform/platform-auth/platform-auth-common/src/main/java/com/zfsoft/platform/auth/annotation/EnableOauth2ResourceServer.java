package com.zfsoft.platform.auth.annotation;

import com.zfsoft.platform.auth.component.MyResourceServerAutoConfiguration;
import com.zfsoft.platform.auth.component.MySecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * 资源服务注解
 * 此注解主要为了 实现权限管理及负载均衡
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ MyResourceServerAutoConfiguration.class, MySecurityBeanDefinitionRegistrar.class })
public @interface EnableOauth2ResourceServer {

}
