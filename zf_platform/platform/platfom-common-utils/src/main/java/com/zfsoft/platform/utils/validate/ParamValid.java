package com.zfsoft.platform.utils.validate;

import java.lang.annotation.*;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/11 10:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface ParamValid {

    Class<?>[]  groups() default {};

    /**
     * 预留主键字段标识 <br>
     *      todo 若无 {@link javax.persistence.Id} 标识且主键不为 id 时标注主键字段
     * @author kkfan
     * @modify 2022-04-23
     */
//    Class<Fn<? extends String, Object>> id() d;
}
