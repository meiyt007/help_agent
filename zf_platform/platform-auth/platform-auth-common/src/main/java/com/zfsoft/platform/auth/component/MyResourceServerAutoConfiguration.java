package com.zfsoft.platform.auth.component;

import com.zfsoft.platform.auth.handler.RestResponseErrorHandler;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Map;

@ComponentScan("com.zfsoft.platform.auth")
public class MyResourceServerAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate lbRestTemplate() {
		// 获取上下文配置的 ClientHttpRequestInterceptor 实现
		// 实现中包含对请求头的拦截修改 TenantRequestInterceptor、AccpetRequestInterceptor 对分布式事务的支持 TransactionHttpRequestInterceptor
		Map<String, ClientHttpRequestInterceptor> beansOfType = applicationContext
				.getBeansOfType(ClientHttpRequestInterceptor.class);
		RestTemplate restTemplate = new RestTemplate();
		// 设置拦截器
		restTemplate.setInterceptors(new ArrayList<>(beansOfType.values()));
		// 设置错误处理器
		restTemplate.setErrorHandler(new RestResponseErrorHandler());
		return restTemplate;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
