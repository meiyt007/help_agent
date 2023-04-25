package com.zfsoft.microservice.platform.gateway.web.context;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/27 10:28
 */
public class ReactiveExchangeContextHolder {
    static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    /**
     * 从上下文中获取Mono<ServerWebExchange>
     * @return
     */
    public static Mono<ServerWebExchange> getExchange(){
        return Mono.subscriberContext().map(ctx->ctx.get(CONTEXT_KEY));
    }
}
