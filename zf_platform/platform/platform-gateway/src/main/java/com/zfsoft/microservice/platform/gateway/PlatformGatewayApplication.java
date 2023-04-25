package com.zfsoft.microservice.platform.gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zfsoft.platform.common.swagger.annotation.EnablePlatformSwagger;
import com.zfsoft.platform.utils.fileUtil.FileUploadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@EnablePlatformSwagger
@SpringBootApplication(exclude = {FileUploadConfig.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableEncryptableProperties
public class PlatformGatewayApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(PlatformGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功啦   ლ(´ڡ`ლ)ﾞ哈哈哈哈哈哈！ ");
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

}
