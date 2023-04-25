package com.zfsoft.platform.utils.feign;

import org.springframework.util.StringUtils;

/**
 * @ClassName FeignCalledException
 * @Description 定义feign调用返回的ApiResultSet的code不是SUCCESS的时候，抛出的异常。
 * @Author
 * @Date2020-09-16 9:48
 * @Version V1.0
 **/
public class FeignCalledException extends RuntimeException {

    public FeignCalledException(String message){
        super(message);
    }
}
