package com.zfsoft.microservice.platform.gateway.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 异常处理 {@link AuthenticationException } 不同细化异常处理
 */
@Slf4j
@Component
//@AllArgsConstructor
public class CommenceAuthExceptionEntryPoint implements ServerAuthenticationEntryPoint {

//	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		System.out.println("出错===================================");
		return null;
	}

}
