package com.zfsoft.microservice.platform.gateway.filter;

import com.zfsoft.microservice.platform.gateway.constant.OrderedConstant;
import com.zfsoft.microservice.platform.gateway.log.CacheServerHttpRequestDecorator;
import com.zfsoft.microservice.platform.gateway.utils.CheckHostUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName NoHostFilter
 * @Description: 用于防止跨站点请求伪造
 * @Author wuxx
 * @Date 2020/11/4
 **/
@Component
public class NoHostFilter implements GlobalFilter, Ordered {

    @Autowired
    private CheckHostUtil checkHostUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String refererPath = request.getHeaders().getFirst("Referer");
        String host = request.getURI().getHost();
        // 验证站点请求来源，用于跨站点请求伪造
        boolean refererResult = checkHostUtil.check(refererPath, host);
        if (!refererResult) {
            // 验证站点请求来源，用于跨站点请求伪造
            throw new ResultInfoException("请求地址不正确！");
        }
        CacheServerHttpRequestDecorator cacheServerHttpRequestDecorator = new CacheServerHttpRequestDecorator(exchange.getRequest());
        return chain.filter(exchange.mutate().request(cacheServerHttpRequestDecorator).build());
    }

    @Override
    public int getOrder() {
        return OrderedConstant.HOST_FILTER;
    }
}
