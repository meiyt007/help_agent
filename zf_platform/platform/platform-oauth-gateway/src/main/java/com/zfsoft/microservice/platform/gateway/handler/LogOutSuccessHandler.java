package com.zfsoft.microservice.platform.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.zfsoft.microservice.platform.gateway.data.LogOutInfo;
import com.zfsoft.microservice.platform.gateway.response.MessageCode;
import com.zfsoft.microservice.platform.gateway.response.WsResponse;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author zhanglei
 * @Date 10:52 2019-12-11
 **/
@Component
public class LogOutSuccessHandler implements ServerLogoutSuccessHandler {
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {


        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        WsResponse wsResponse = WsResponse.success();
        byte[] dataBytes = {};
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(authentication.getPrincipal() instanceof AuthUserDetails){
                AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();
                LogOutInfo logOutInfo = new LogOutInfo();
                logOutInfo.setUserName(user.getUsername());
                logOutInfo.setMessage("退出成功！");
                wsResponse.setResult(logOutInfo);
                dataBytes = mapper.writeValueAsBytes(wsResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject result = new JsonObject();
            result.addProperty("status", MessageCode.COMMON_FAILURE.getCode());
            result.addProperty("message", "授权异常");
            dataBytes = result.toString().getBytes();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
