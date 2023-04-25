package com.zfsoft.microservice.platform.gateway.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @ClassName BadCaptchaException
 * @Description
 * @Author
 * @Date2020-08-31 1:30
 * @Version V1.0
 **/
public class BadCaptchaException extends AuthenticationException {
    public BadCaptchaException(String msg) {
        super(msg);
    }

    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }
}
