package com.zfsoft.es.service.annotation;

import com.zfsoft.es.service.enums.OperationTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义事务注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnabledToEsServer {

    /** 是否开启 数据存入es 默认true */
    boolean enabledToEs() default true;

    /** 操作类型 必填项 详见 OperationTypeEnum */
    OperationTypeEnum operationType();

    /** 索引名（只在第一次创建有效） */
    String indexName() default "";

    /** 索引类型（只在第一次创建有效） */
    String indexType() default "";

    /** 主分片数（只在第一次创建有效） */
    int replicasNum() default 3;

    /** 从分片数（只在第一次创建有效） */
    int shardsNum() default 3;
}
