package com.zfsoft.microservice.platform;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.zfsoft.*")
@EnableEncryptableProperties
public class PlatformServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformServiceProviderApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功啦   ლ(´ڡ`ლ)ﾞ哈哈哈哈哈哈！ ");

    }

}
