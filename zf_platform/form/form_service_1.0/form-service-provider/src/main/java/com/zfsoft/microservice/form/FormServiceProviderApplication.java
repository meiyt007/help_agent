package com.zfsoft.microservice.form;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableTransactionManagement
@ServletComponentScan  //注册过滤器注解
@EnableFeignClients(basePackages="com.zfsoft")
public class FormServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormServiceProviderApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功啦   ლ(´ڡ`ლ)ﾞ哈哈哈哈哈哈！ ");
    }

}
