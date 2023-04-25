package com.zfsoft.microservice.settings;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.zfsoft.platform.utils.fileUtil.FileUploadConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {FileUploadConfig.class})
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableEncryptableProperties
public class SettingsServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettingsServiceProviderApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功啦   ლ(´ڡ`ლ)ﾞ哈哈哈哈哈哈！ ");
    }

}
