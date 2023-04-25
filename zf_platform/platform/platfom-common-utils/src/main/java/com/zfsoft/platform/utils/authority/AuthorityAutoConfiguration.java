package com.zfsoft.platform.utils.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * @author: kkfan
 * @create: 2021-11-04 18:59:21
 * @description: 始化用用户信息传递有关Bean
 */
@ComponentScan(basePackages = "com.zfsoft.platform.utils.authority")
@ConditionalOnClass(HttpServletRequest.class)
public class AuthorityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.additionalInterceptors(requestInterceptor());
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestInterceptor requestInterceptor() {
        return (request, body, execution) -> {
            CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            if(currentLoginUser!=null){
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(currentLoginUser);
                request.getHeaders().add("CUSTOM-REQUEST-HEADER", URLEncoder.encode(json, "UTF-8"));
            }
            return execution.execute(request, body);
        };
    }



}
