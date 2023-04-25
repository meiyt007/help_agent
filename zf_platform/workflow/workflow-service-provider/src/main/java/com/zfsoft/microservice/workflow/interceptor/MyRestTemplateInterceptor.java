package com.zfsoft.microservice.workflow.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

/**
 * @ClassName MyRestTemplateInterceptor
 * @Description
 * @Author
 * @Date2020-09-12 14:07
 * @Version V1.0
 **/

@Configuration
public class MyRestTemplateInterceptor {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.additionalInterceptors(requestInterceptor());
        return builder.build();
    }

    @Bean
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
