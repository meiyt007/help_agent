package com.zfsoft.microservice.platform.gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zfsoft.platform.utils.fileUtil.FileUploadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {FileUploadConfig.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableEncryptableProperties
public class PlatformOauthGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformOauthGatewayApplication.class, args);
    }
}
