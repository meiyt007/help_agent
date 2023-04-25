package com.zfsoft.platform.utils.validate;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/11 14:01
 * @modify kkfan <br>
 *      新增默认分组 {@link ValidGroups.INSERT} {@link ValidGroups.UPDATA} <br>
 */
@Target({ElementType.PARAMETER,ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidGroups {

    @AliasFor("value")
    Class<?>[]  groups() default {};

    @AliasFor("groups")
    Class<?>[]  value() default {};

    /**
     * INSERT 分组
     */
    interface INSERT {};

    /**
     * UPDATA 分组
     */
    interface UPDATA {};



}
