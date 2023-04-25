package com.zfsoft.microservice.platform.gateway.security.data;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @ClassName UsernamePasswordCaptchAuthenticationToken
 * @Description
 * @Author
 * @Date2020-08-29 0:18
 * @Version V1.0
 **/
public class UsernamePasswordCaptchaAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 520L;
    private final Object principal;
    private Object credentials;
    private Object captcha;

    public UsernamePasswordCaptchaAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials, Object captch) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.captcha = captch;
    }

    public UsernamePasswordCaptchaAuthenticationToken(Object principal, Object credentials, Object captch) {
        super((Collection)null);
        this.principal = principal;
        this.credentials = credentials;
        this.captcha = captch;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public Object getCaptcha() {
        return captcha;
    }
}
