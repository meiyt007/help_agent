package com.zfsoft.platform.auth.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 资源服务器对外直接暴露URL,如果设置contex-path 要特殊处理
 * 此配置类主要为了忽略系统内部调用时的权限url 加了 @Inner
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.oauth2.client.ignore-urls}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2.client")
public class PermitAllUrlProperties {

	// 路径传参匹配表达式  这里主要是为了吧路径传参替换成 *
	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	@Getter
	@Setter
	private List<String> ignoreUrls = new ArrayList<>();

}
