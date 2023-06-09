package com.zfsoft.microservice.platform.gateway.filter;

import com.zfsoft.microservice.platform.gateway.constant.HeaderConstant;
import com.zfsoft.microservice.platform.gateway.constant.OrderedConstant;
import com.zfsoft.microservice.platform.gateway.log.Log;
import com.zfsoft.microservice.platform.gateway.log.LogHelper;
import com.zfsoft.microservice.platform.gateway.utils.IpUtils;
import com.zfsoft.microservice.platform.gateway.utils.UrlUtils;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求响应日志打印
 */
@Component
@Slf4j
public class ResponseLogFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return OrderedConstant.LOGGING_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            ServerRequest serverRequest = ServerRequest.create(exchange,
                    HandlerStrategies.withDefaults().messageReaders());
            URI requestUri = request.getURI();
            String uriQuery = requestUri.getQuery();
            HttpHeaders headers = request.getHeaders();
            MediaType mediaType = headers.getContentType();
            String schema = requestUri.getScheme();
            String method = request.getMethodValue().toUpperCase();

            // 只记录http、https请求
            if ((!"http".equals(schema) && !"https".equals(schema))) {
                return chain.filter(exchange);
            }

            // 这里约定 getway route 配置时 id uri 和path中使用得字符串一致
            String routeId = ((Route)exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR)).getId();

            final AtomicReference<String> requestBody = new AtomicReference<>();// 原始请求体
            // 排除流文件类型,比如上传的文件contentType.contains("multipart/form-data")
            if (Objects.nonNull(mediaType) && LogHelper.isUploadFile(mediaType)) {
                requestBody.set("上传文件");
                return chain.filter(exchange);
            } else {
                if (method.equals("GET")) {
                    if (StringUtils.isNotBlank(uriQuery)) {
                        String baseUrl = UrlUtils.getParam(requestUri.toString(), "base_url");
                        String accessToken = UrlUtils.getParam(requestUri.toString(), "access_token");
                        request = request.mutate().header("base_url", baseUrl)
                                .header("access_token", accessToken)
                                .header("route_id", routeId)
                                .build();
                        requestBody.set(uriQuery);
                    }
                } else if (headers.getContentLength() > 0){
                    return serverRequest.bodyToMono(String.class).flatMap(reqBody -> {
                        requestBody.set(reqBody);
                        // 重写原始请求
                        ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                            @Override
                            public HttpHeaders getHeaders() {
                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.putAll(super.getHeaders());
                                return httpHeaders;
                            }

                            @Override
                            public Flux<DataBuffer> getBody() {
                                NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                                DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(reqBody.getBytes());
                                return Flux.just(bodyDataBuffer);
//                                return Flux.just(reqBody).map(bx -> exchange.getRequest().bufferFactory().wrap(bx.getBytes()));
                            }
                        };
                        String baseUrl = UrlUtils.getParam(requestUri.toString(), "base_url");
                        String accessToken = UrlUtils.getParam(requestUri.toString(), "access_token");
                        requestDecorator.getHeaders().set("base_url", baseUrl);
                        requestDecorator.getHeaders().set("access_token", accessToken);
                        requestDecorator.getHeaders().set("route_id", routeId);
                        ServerHttpResponseDecorator responseDecorator = getServerHttpResponseDecorator(exchange,
                                requestBody);
                        return chain.filter(exchange.mutate()
                                .request(requestDecorator)
                                .response(responseDecorator)
                                .build());
                    });
                }
                ServerHttpResponseDecorator decoratedResponse = getServerHttpResponseDecorator(exchange,
                        requestBody);
                return chain.filter(exchange.mutate()
                        .request(request)
                        .response(decoratedResponse)
                        .build());
            }

        } catch (Exception e) {
            //log.error("请求响应日志打印出现异常", e);
            return chain.filter(exchange);
        }

    }

    private ServerHttpResponseDecorator getServerHttpResponseDecorator(ServerWebExchange exchange,
                                                                       AtomicReference<String> requestBody) {
        // 获取response的返回数据
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        HttpStatus httpStatus = originalResponse.getStatusCode();
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();
        String uriQuery = requestUri.getQuery();
        String url = requestUri.getPath() + (StringUtils.isNotBlank(uriQuery) ? "?" + uriQuery : "");
        HttpHeaders headers = request.getHeaders();
        String method = request.getMethodValue().toUpperCase();
        String requestId = headers.getFirst(HeaderConstant.REQUEST_ID);

        // 封装返回体
        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DataBuffer join = bufferFactory.join(dataBuffers);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);
                        Charset charset = LogHelper.getMediaTypeCharset(originalResponse.getHeaders().getContentType());
                        String responseBody = new String(content, charset);

                        long handleTime = LogHelper.getHandleTime(headers);
                        Log logDTO = new Log(Log.TYPE.RESPONSE);
                        logDTO.setLevel(Log.LEVEL.INFO);
                        logDTO.setRequestUrl(url);
                        logDTO.setRequestBody(requestBody.get());
                        //logDTO.setResponseBody(responseBody);
                        logDTO.setRequestMethod(method);
                        if (Objects.nonNull(httpStatus)) {
                            logDTO.setStatus(httpStatus.value());
                        }
                        logDTO.setHandleTime(handleTime);
                        logDTO.setRequestId(requestId);
                        logDTO.setIp(IpUtils.getClientIp(request));
                        exchange.getSession().subscribe(webSession -> {
                            logDTO.setSessionId(webSession.getId());
                        });

                        //log.info("url:{},method:{},请求内容:{},响应内容:{},status:{},handleTime:{},requestId:{}",
                         //       url, method, requestBody.get(), responseBody, httpStatus,
                        //        handleTime, requestId);
                        //log.info(LogHelper.toJsonString(logDTO));
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

}
