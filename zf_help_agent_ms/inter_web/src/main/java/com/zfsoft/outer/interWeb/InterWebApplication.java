package com.zfsoft.outer.interWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication()
@ConfigurationPropertiesScan
@CrossOrigin
@EnableAsync
@EnableScheduling
public class InterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterWebApplication.class, args);
    }

}
