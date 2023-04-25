package com.zfsoft.platform.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @description: 未注册异常
 * @author: wuxx
 * @Date: 2020/11/3 13:58
 **/
public class UnRegisteredException extends AuthenticationException {
    public UnRegisteredException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnRegisteredException(String msg) {
        super(msg);
    }
}
