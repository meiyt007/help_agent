package com.zfsoft.ocr.data.pojo.annotation;

import java.lang.annotation.*;

/**
 * 目录元素名称 自定义注解
 *
 * @Auther dusd
 * @Date 2019/4/8 13:39
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CataWordBind {

    String word() default "";

}
