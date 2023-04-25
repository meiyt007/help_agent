package com.zfsoft.microservice.platform.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.microservice.platform.gateway.exception.InvalidTokenException;
import com.zfsoft.microservice.platform.gateway.exception.UnRegisteredException;
import com.zfsoft.microservice.platform.gateway.feign.SysUserFeignService;
import com.zfsoft.microservice.platform.gateway.response.MessageCode;
import com.zfsoft.microservice.platform.gateway.response.WsResponse;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFaillHandler  implements ServerAuthenticationFailureHandler {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.EXPECTATION_FAILED);
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        WsResponse<String> wsResponse = WsResponse.failure(MessageCode.COMMON_AUTHORIZED_FAILURE);
        if (e instanceof LockedException) {
            wsResponse.setMessage("用户被锁定！");
        } else if (e instanceof BadCredentialsException) {
            wsResponse.setMessage( "用户名或密码错误！");
            //登录失败锁定 -- 只有用户密码错误才更新
            MultiValueMap<String, String> formData = exchange.getFormData().block();
            String username = formData.getFirst("username");
            this.lockSysLogin(username);
        } else if (e instanceof DisabledException) {
            wsResponse.setMessage( "用户被禁用！");
        } else if (e instanceof InvalidTokenException) {
            wsResponse.setStatus(MessageCode.COMMON_INVALID_TOKEN);
            wsResponse.setMessage("用户登录过期！");
            // 为了前端跳转到登陆
            response.setStatusCode(HttpStatus.OK);
        } else if (e instanceof CredentialsExpiredException) {
            wsResponse.setMessage("用户名和密码过期！");
        }else if (e instanceof UnRegisteredException) {
            wsResponse.setMessage(e.getMessage());
        }else {
            wsResponse.setMessage(e.getMessage());
        }

        byte[]   dataBytes={};
        try {
            ObjectMapper mapper = new ObjectMapper();
            dataBytes=mapper.writeValueAsBytes(wsResponse);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    private void lockSysLogin(String username) {
        SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
        sysUserFeignService.lockSysLogin(username);
    }
}
