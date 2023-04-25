package com.zfsoft.microservice.workflow;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zfsoft.platform.utils.fileUtil.FileUploadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {FileUploadConfig.class,SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableEncryptableProperties
@EnableFeignClients(basePackages = "com.zfsoft.*")
public class WorkflowServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceProviderApplication.class, args);
    }

}
