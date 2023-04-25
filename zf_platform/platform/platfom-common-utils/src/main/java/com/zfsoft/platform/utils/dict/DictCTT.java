package com.zfsoft.platform.utils.dict;

import com.zfsoft.platform.utils.dict.enums.TypeEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: kkfan
 * @create: 2022-03-23 15:07:24
 * @description: dictionary code to text
 */
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DictCTT {

    /**
     * 字典类型枚举 默认{@link TypeEnums#DICT}
     * @see TypeEnums
     */
    TypeEnums typeEnum() default TypeEnums.DICT;

    /**
     * 当前字段是否为<b>字典表</b>业务主键<br>
     * isDictOid -> true 根据业务主键（dictOid）查询字典名称  <br>
         * isDictOid -> false 根据字典编码（code）查询字典名称
     * @return String
     */
    boolean isDictOid() default false;

    /**
     * 字典值对应的字段名称<br>
     * 默认取字典code字段名 - code + Text
     * @return String
     */
    String dictTextField() default "";

    /**
     * 字典名称为空时的默认显示<br>
     * @return
     */
    String emptyText() default "";
}
