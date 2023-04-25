package com.zfsoft.microservice.platform.gateway.security.handler;


import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.zfsoft.microservice.platform.gateway.feign.SysUserFeignService;
import com.zfsoft.microservice.platform.gateway.security.response.MessageCode;
import com.zfsoft.microservice.platform.gateway.security.response.WsResponse;
import com.zfsoft.microservice.platform.gateway.utils.CheckHostUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.AuthUserDetails;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CheckHostUtil checkHostUtil;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String refererPath = request.getHeaders().getFirst("Referer");
        String host = request.getURI().getHost();
        // 验证站点请求来源，用于跨站点请求伪造
        boolean refererResult = checkHostUtil.check(refererPath, host);
        if (!refererResult) {
            // 验证站点请求来源，用于跨站点请求伪造
            throw new ResultInfoException("请求地址不正确！");
        }

        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //登录成功后，清空失败次数
        //测试性能需要注释
        MultiValueMap<String, String> formData = exchange.getFormData().block();
        String username = formData.getFirst("username");
        SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
        sysUserFeignService.emptyFailNum(username);

        //设置body
        WsResponse wsResponse = WsResponse.success();
        byte[] dataBytes = {};
        ObjectMapper mapper = new ObjectMapper();
        try {
            AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();
            try {
                //登录成功后给activiti授权
                Map<String, String> map = new HashMap<>();
                map.put("username",user.getUsername());
                map.put("password",user.getPassword());
                map.put("account",user.getAccount());
                map.put("userOid",user.getUserOid());
                redisTemplate.opsForValue().set(BaseStaticParameter.WORKFLOW_ACTIVITIUSER + user.getUserOid(), JSONUtil.toJsonStr(map),3600, TimeUnit.SECONDS);
            }catch (Exception e){
                //异常了不做处理
            }
            wsResponse.setResult(user);
            dataBytes = mapper.writeValueAsBytes(wsResponse);
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


    private AuthUserDetails buildUser(User user) {
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword().substring(user.getPassword().lastIndexOf("}") + 1, user.getPassword().length()));
        Collection<String> roles = new ArrayList<>();
        Collection<GrantedAuthority> userAuthorities = user.getAuthorities();
        if(userAuthorities!=null){
            for(GrantedAuthority r : userAuthorities) {
                roles.add(r.getAuthority());
            }
        }
        userDetails.setRoles(roles);
        return userDetails;
    }
}
