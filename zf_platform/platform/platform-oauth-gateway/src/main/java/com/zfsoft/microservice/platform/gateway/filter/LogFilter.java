package com.zfsoft.microservice.platform.gateway.filter;

/**
 * @ClassName LogFilter
 * @Description
 * @Author
 * @Date2020-09-29 19:04
 * @Version V1.0
 **/
import com.zfsoft.microservice.platform.gateway.constant.OrderedConstant;
import com.zfsoft.microservice.platform.gateway.log.CacheServerHttpRequestDecorator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: Li Fengdi
 * @date: 2020-03-17 18:17
 */
@Component
public class LogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        CacheServerHttpRequestDecorator cacheServerHttpRequestDecorator = new CacheServerHttpRequestDecorator(exchange.getRequest());

        return chain.filter(exchange.mutate().request(cacheServerHttpRequestDecorator).build());
    }

    @Override
    public int getOrder() {
        return OrderedConstant.LOGGING_FILTER;
    }
}
