package com.zfsoft.microservice.platform.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import com.zfsoft.microservice.platform.gateway.log.CacheServerHttpRequestDecorator;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.MessageFormat;
import java.util.*;

/**
 * @ClassName UrlParamFilter
 * @Description: 处理请求参数不合法的问题
 * @Author wuxx
 * @Date 2021/3/23
 **/
@Component
@RefreshScope
@Log4j2
public class UrlParamFilter  implements GlobalFilter, Ordered {

    @Autowired
    private ZhuofanConfig zhuofanConfig;

    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * httpheader，traceId的key名称
     */
    private static final String REQUESTID = "traceId";

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String GATEWAY_ROUTE_BEAN = "org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();
        if (requestUri.getPath().contains("../") || Optional.ofNullable(requestUri.getQuery()).orElse("").contains("../")) {
            return Mono.error(new ResultInfoException("请求中出现限制字符../，请检查！"));
        }
        String uriQuery = requestUri.getQuery();
        String limitChars = zhuofanConfig.getLimitChars();
        if(StrUtil.isNotBlank(limitChars)){
            String[] limitCharStrs = limitChars.split(",");
            for(String chars : limitCharStrs){
                if(null!=uriQuery && uriQuery.contains(chars) ){
                    return Mono.error(new ResultInfoException(MessageFormat.format("请求参数中出现限制字符{0}，请重新编辑！", chars)));
                }
            }
        }else{
            if(null!=uriQuery && (uriQuery.contains("[")||uriQuery.contains("]")) ){
                return Mono.error(new ResultInfoException(MessageFormat.format("请求参数中出现限制字符{0}，请重新编辑！", "[" + "或" + "]")));
            }
        }

        String contentType = request.getHeaders().getFirst(CONTENT_TYPE);
        String method = request.getMethodValue();


        //判断是否为POST请求
        if (null != contentType && HttpMethod.POST.name().equalsIgnoreCase(method) && contentType.contains(CONTENT_TYPE_JSON)) {
            ServerRequest serverRequest = new DefaultServerRequest(exchange);
            List<String> list = new ArrayList<>();
            // 读取请求体
            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                    .flatMap(body -> {
                        String[] limitCharStrs = Optional.ofNullable(limitChars).orElse("").split(",");
                        for(String chars : limitCharStrs){
                            if(StringUtils.isNotEmpty(chars) && null != body && body.contains(chars) ){
                                return Mono.error(new ResultInfoException(MessageFormat.format("请求参数中出现限制字符{0}，请重新编辑！", chars)));
                            }
                        }
                        final String nId = saveRequestOperLog(exchange, body);
                        list.add(nId);
                        return Mono.just(body);
                    });

            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            // 20201105 modify by kkfan 请求体中参数特殊字符限制
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public HttpHeaders getHeaders() {
                                long contentLength = headers.getContentLength();
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.putAll(super.getHeaders());
                                // httpHeaders.put(REQUESTID, list);
                                if (contentLength > 0) {
                                    httpHeaders.setContentLength(contentLength);
                                } else {
                                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                                }
                                return httpHeaders;
                            }

                            @Override
                            public Flux<DataBuffer> getBody() {
                                return outputMessage.getBody();
                            }
                        };
                        return chain.filter(exchange.mutate().request(decorator).build());
                    }));
        }

        CacheServerHttpRequestDecorator cacheServerHttpRequestDecorator = new CacheServerHttpRequestDecorator(exchange.getRequest());
        return chain.filter(exchange.mutate().request(cacheServerHttpRequestDecorator).build());
    }

    /**
     * 记录请求体信息
     *
     * @param exchange
     * @param requestParameters
     * @author kkfan
     * @return
     */
    private String saveRequestOperLog(ServerWebExchange exchange, String requestParameters) {
        ServerHttpRequest request = exchange.getRequest();
        String ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();

        Map logMap = Maps.newHashMap();
        logMap.put("ip", ip);
        logMap.put("reqUrl", request.getURI().toString());
        logMap.put("reqMethod", request.getMethodValue());
        logMap.put("requestParameters", requestParameters);
        Route route = exchange.getAttribute(GATEWAY_ROUTE_BEAN);
        //是否配置路由
        if (route != null) {
            logMap.put("subsystem", route.getId());
        }
        log.debug("请求信息为：{}", logMap);
        return JSONObject.toJSONString(logMap);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
