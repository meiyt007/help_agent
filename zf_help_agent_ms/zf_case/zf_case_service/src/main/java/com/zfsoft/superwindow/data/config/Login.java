package com.zfsoft.superwindow.data.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 权限注解
 * @author wangyg
 * @date 2022/6/8
 * @return
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login {
    PermissionType[] value();

    /**
     * 角色类型
     */
    enum PermissionType {
        VALID_TOKEN // 验证token
    }
}
