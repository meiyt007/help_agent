package com.zfsoft.microservice.platform.gateway.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import com.google.code.kaptcha.Constants;
import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import com.zfsoft.microservice.platform.gateway.constant.Oauth2Constant;
import com.zfsoft.microservice.platform.gateway.exception.BadCaptchaException;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: kkfan
 * @create: 2021-02-10 09:19:09
 * @description: 密码解密 filter 参考 ModifyRequestBodyGatewayFilterFactory 实现
 */
@Slf4j
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {

	private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

	private static final String PASSWORD = "password";

	@Autowired
	private ZhuofanConfig zhuofanConfig;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), Oauth2Constant.OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			// 刷新token，直接向下执行
			String grantType = request.getQueryParams().getFirst("grant_type");
			if (StrUtil.equals(Oauth2Constant.REFRESH_TOKEN, grantType)) {
				return chain.filter(exchange);
			}

			Class inClass = String.class;
			Class outClass = String.class;
			ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

			// 解密生成新的报文
			Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(
                    s -> {
                        // 获取请求密码并解密
                        Map<String, String> inParamsMap = HttpUtil.decodeParamMap((String) s, CharsetUtil.CHARSET_UTF_8);

						Mono<Map<String, String>> mapMono = exchange.getSession().flatMap(session -> {
							if(zhuofanConfig.isValidCode()){
								Date lastDate = (Date) session.getAttribute(Constants.KAPTCHA_SESSION_DATE);
								if (lastDate == null) {
									//return Mono.error(new BadCaptchaException(this.messages.getMessage("CaptchaServerFormLoginAuthenticationConverter.captcha_empty", "Captcha is empty!")));
									return Mono.error(new BadCaptchaException("验证码失效，请刷新页面重新操作！"));
								}
								Date nowDate = new Date();
								long costTime = nowDate.getTime() - lastDate.getTime();
								if (costTime > 120000) {
									//return Mono.error(new BadCaptchaException(this.messages.getMessage("CaptchaServerFormLoginAuthenticationConverter.captcha_expired", "Captcha is timeOut!")));
									return Mono.error(new BadCaptchaException("验证码已经过期！"));
								}
								String captcha = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
								if(null==captcha){
									captcha="";
								}
								String captcha2 = inParamsMap.get("captcha");
								if (!captcha.equals(captcha2)) {
									throw new BadCaptchaException("验证码不正确!");
								}
							}
							Map<String, String> rsaKey = session.getAttribute(BaseStaticParameter.RSA_KEY);
							if (rsaKey == null) {
								rsaKey = new HashMap<>();
							}
							return Mono.just(rsaKey);
						});
						return mapMono.flatMap(rsaKey -> {
							if (rsaKey != null) {
								String loginEncrypt = (String) rsaKey.get(BaseStaticParameter.CONFIG_LOGIN_ENCRYPT);
								String privateKey = (String) rsaKey.get(BaseStaticParameter.PRIVATE_KEY);
								//加密传输，进行解密
								if ("1".equals(loginEncrypt) && inParamsMap.containsKey(PASSWORD)) {
									String passwordTemp = inParamsMap.get(PASSWORD);
									passwordTemp = passwordTemp.replaceAll(" ", "+");
									RSA rsa = SecureUtil.rsa(privateKey, null);
									passwordTemp = rsa.decryptStr(passwordTemp, KeyType.PrivateKey);
									// 返回修改后报文字符
									inParamsMap.put(PASSWORD, passwordTemp.trim());
								}

							}
							inParamsMap.put("grant_type", "password");
							inParamsMap.put("client_id", "gateway-client");
							inParamsMap.put("client_secret", "123456");
							return Mono.just(HttpUtil.toParams(inParamsMap));

						});

                    }
            );

			BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
			HttpHeaders headers = new HttpHeaders();
			headers.putAll(exchange.getRequest().getHeaders());
			headers.remove(HttpHeaders.CONTENT_LENGTH);

			headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
			CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
			return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
				ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
				return chain.filter(exchange.mutate().request(decorator).build());
			}));
		};
	}

	/**
	 * 报文转换
	 * @return
	 */
	private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
			CachedBodyOutputMessage outputMessage) {
		return new ServerHttpRequestDecorator(exchange.getRequest()) {
			@Override
			public HttpHeaders getHeaders() {
				long contentLength = headers.getContentLength();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(super.getHeaders());
				if (contentLength > 0) {
					httpHeaders.setContentLength(contentLength);
				}
				else {
					httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
				}
				return httpHeaders;
			}

			@Override
			public Flux<DataBuffer> getBody() {
				return outputMessage.getBody();
			}
		};
	}

}
